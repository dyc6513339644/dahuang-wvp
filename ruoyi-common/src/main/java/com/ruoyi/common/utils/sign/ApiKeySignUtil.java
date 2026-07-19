package com.ruoyi.common.utils.sign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * API Key 签名工具类
 *
 * 签名算法: HMAC-SHA256
 * 签名串 = HTTP方法 + "\n" + 请求路径(含query) + "\n" + 时间戳 + "\n" + 随机数
 * 签名值 = Base64(HMAC-SHA256(SecretKey, 签名串))
 *
 * @author ruoyi
 */
public class ApiKeySignUtil {

    private static final Logger log = LoggerFactory.getLogger(ApiKeySignUtil.class);

    /** HMAC-SHA256 算法名 */
    private static final String HMAC_SHA256 = "HmacSHA256";

    /** 允许的时间偏差（毫秒），默认 5 分钟 */
    public static final long MAX_TIME_DEVIATION_MS = 5 * 60 * 1000L;

    /**
     * 生成签名
     *
     * @param secretKey   Secret Key (SK)
     * @param method      HTTP 方法 (GET/POST/PUT/DELETE)
     * @param path        URI 路径 (含 query string，如 /api/device/list?page=1)
     * @param timestamp   时间戳 (Unix 毫秒)
     * @param nonce       随机数 (防重放)
     * @return Base64 编码的签名
     */
    public static String sign(String secretKey, String method, String path,
                              long timestamp, String nonce) {
        String stringToSign = buildStringToSign(method, path, timestamp, nonce);
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec keySpec = new SecretKeySpec(
                    secretKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            mac.init(keySpec);
            byte[] hash = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("签名生成失败", e);
            throw new RuntimeException("签名生成失败", e);
        }
    }

    /**
     * 验证签名
     *
     * @param secretKey   Secret Key (SK)
     * @param method      HTTP 方法
     * @param path        URI 路径 (含 query string)
     * @param timestamp   时间戳 (Unix 毫秒)
     * @param nonce       随机数
     * @param signature   客户端传入的签名
     * @return true=签名有效, false=签名无效或时间过期
     */
    public static boolean verify(String secretKey, String method, String path,
                                 long timestamp, String nonce, String signature) {
        // 1. 检查时间戳是否在允许范围内（防重放攻击）
        long now = System.currentTimeMillis();
        if (Math.abs(now - timestamp) > MAX_TIME_DEVIATION_MS) {
            log.warn("API签名时间戳过期 - now: {}, timestamp: {}, diff: {}ms",
                    now, timestamp, Math.abs(now - timestamp));
            return false;
        }

        // 2. 重新计算签名并比对（恒定时间比较防时序攻击）
        String expected = sign(secretKey, method, path, timestamp, nonce);
        return constantTimeEquals(expected, signature);
    }

    /**
     * 构建待签名字符串
     */
    private static String buildStringToSign(String method, String path,
                                            long timestamp, String nonce) {
        return method.toUpperCase().trim() + "\n"
                + path + "\n"
                + timestamp + "\n"
                + nonce;
    }

    /**
     * 恒定时间字符串比较，防止时序攻击
     */
    private static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        byte[] aBytes = a.getBytes(StandardCharsets.UTF_8);
        byte[] bBytes = b.getBytes(StandardCharsets.UTF_8);
        if (aBytes.length != bBytes.length) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < aBytes.length; i++) {
            result |= aBytes[i] ^ bBytes[i];
        }
        return result == 0;
    }
}
