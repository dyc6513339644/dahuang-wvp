package com.ruoyi.framework.security.filter;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sign.ApiKeySignUtil;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.system.domain.WvpUserApiKey;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.IWvpUserApiKeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * API Key 认证过滤器
 *
 * 处理携带 AK/SK 签名的 API 请求，验证签名后自动以对应用户身份登录。
 * 认证失败时直接返回 401 JSON 响应，不穿透到 JWT 过滤器（避免混淆报错）。
 *
 * 请求头规范：
 *   X-Api-Access-Key: <AccessKey>
 *   X-Api-Timestamp:  <Unix毫秒时间戳>
 *   X-Api-Nonce:      <随机字符串(16-32位)>
 *   X-Api-Signature:  <HMAC-SHA256签名, Base64编码>
 *
 * @author ruoyi
 */
@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ApiKeyAuthenticationFilter.class);

    private static final String HEADER_ACCESS_KEY = "X-Api-Access-Key";
    private static final String HEADER_TIMESTAMP = "X-Api-Timestamp";
    private static final String HEADER_NONCE = "X-Api-Nonce";
    private static final String HEADER_SIGNATURE = "X-Api-Signature";

    @Autowired
    private IWvpUserApiKeyService apiKeyService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    /**
     * 只过滤携带 AK 头的请求，其他请求完全透明交给 JWT 过滤器处理
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return StringUtils.isEmpty(request.getHeader(HEADER_ACCESS_KEY));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String accessKey = request.getHeader(HEADER_ACCESS_KEY);

        // 1. 校验必填参数
        String timestampStr = request.getHeader(HEADER_TIMESTAMP);
        String nonce = request.getHeader(HEADER_NONCE);
        String signature = request.getHeader(HEADER_SIGNATURE);
        if (StringUtils.isEmpty(timestampStr) || StringUtils.isEmpty(nonce)
                || StringUtils.isEmpty(signature)) {
            respondUnauthorized(response, "缺少签名参数 (X-Api-Timestamp / X-Api-Nonce / X-Api-Signature)");
            return;
        }

        // 2. 解析时间戳
        long timestamp;
        try {
            timestamp = Long.parseLong(timestampStr);
        } catch (NumberFormatException e) {
            respondUnauthorized(response, "时间戳格式错误，请传入 Unix 毫秒时间戳");
            return;
        }

        // 3. 查找密钥
        WvpUserApiKey apiKey;
        try {
            apiKey = apiKeyService.getByAccessKey(accessKey);
        } catch (Exception e) {
            log.error("查询API Key异常 - AK: {}", accessKey, e);
            respondUnauthorized(response, "系统内部错误");
            return;
        }

        if (apiKey == null) {
            respondUnauthorized(response, "无效的 Access Key");
            return;
        }

        if (apiKey.getEnable() == null || !apiKey.getEnable()) {
            respondUnauthorized(response, "该 API Key 已被停用");
            return;
        }

        // 4. 构建请求路径
        String path = request.getRequestURI();
        String queryString = request.getQueryString();
        if (StringUtils.isNotEmpty(queryString)) {
            path = path + "?" + queryString;
        }

        // 5. 验证签名
        String secretKey = apiKey.getSecretKey();
        if (!ApiKeySignUtil.verify(secretKey, request.getMethod(), path,
                timestamp, nonce, signature)) {
            long now = System.currentTimeMillis();
            long diff = Math.abs(now - timestamp);
            if (diff > ApiKeySignUtil.MAX_TIME_DEVIATION_MS) {
                respondUnauthorized(response,
                        "签名时间戳已过期（偏差 " + (diff / 1000) + " 秒，最大允许 "
                                + (ApiKeySignUtil.MAX_TIME_DEVIATION_MS / 1000) + " 秒）");
            } else {
                respondUnauthorized(response, "签名验证失败");
            }
            return;
        }

        // 6. 加载关联用户
        Long userId = apiKey.getUserId();
        SysUser sysUser = userService.selectUserById(userId);
        if (sysUser == null) {
            log.warn("API Key 关联用户不存在 - userId: {}, AK: {}", userId, accessKey);
            respondUnauthorized(response, "关联用户不存在");
            return;
        }

        // 7. 获取用户完整权限并注入 SecurityContext
        Set<String> permissions = permissionService.getMenuPermission(sysUser);
        LoginUser loginUser = new LoginUser(sysUser.getUserId(), sysUser.getDeptId(),
                sysUser, permissions);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginUser, null,
                        loginUser.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        log.info("API Key 认证成功 - AK: {}, user: {} (id={}), path: {}",
                accessKey, sysUser.getUserName(), userId, path);

        chain.doFilter(request, response);
    }

    /**
     * 返回 401 JSON 响应（不穿透到后续过滤器）
     */
    private void respondUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JSON.toJSONString(AjaxResult.error(message)));
        }
    }
}
