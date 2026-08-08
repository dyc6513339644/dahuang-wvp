package com.ruoyi.system.domain;

import java.io.Serializable;

/**
 * Logo配置DTO — 基础配置页"Logo配置"Tab的数据模型（5项）
 * <p>
 * 所有Logo URL均存储于 sys_config 表（config_key = value）。
 * 前端上传图片后获得URL，回填到对应字段，保存时写入 sys_config。
 * </p>
 *
 * @author ruoyi
 */
public class LogoConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // ========== sys_config 中的 key 常量 ==========
    public static final String KEY_SITE_LOGO_LOGIN     = "site_logo_login";      // 后台登录页Logo (270x75)
    public static final String KEY_SITE_LOGO_SQUARE    = "site_logo_square";     // 后台小Logo (180x180)
    public static final String KEY_SITE_LOGO_LEFTTOP   = "site_logo_lefttop";    // 后台大Logo (170x50)
    public static final String KEY_MOBILE_LOGIN_LOGO   = "mobile_login_logo";    // 移动端登录Logo (86x86)
    public static final String KEY_SITE_FAVICON        = "site_favicon";         // 系统ICO图标

    // ========== 字段 ==========

    /** 后台登录页Logo（建议270x75） */
    private String siteLogoLogin;

    /** 后台小Logo — 菜单缩进时显示（建议180x180） */
    private String siteLogoSquare;

    /** 后台大Logo — 菜单展开左上角显示（建议170x50） */
    private String siteLogoLefttop;

    /** 移动端登录Logo（建议86x86，PNG格式） */
    private String mobileLoginLogo;

    /** 系统ICO图标（更换后需清除浏览器缓存） */
    private String siteFavicon;

    // ========== getters / setters ==========

    public String getSiteLogoLogin() {
        return siteLogoLogin;
    }

    public void setSiteLogoLogin(String siteLogoLogin) {
        this.siteLogoLogin = siteLogoLogin;
    }

    public String getSiteLogoSquare() {
        return siteLogoSquare;
    }

    public void setSiteLogoSquare(String siteLogoSquare) {
        this.siteLogoSquare = siteLogoSquare;
    }

    public String getSiteLogoLefttop() {
        return siteLogoLefttop;
    }

    public void setSiteLogoLefttop(String siteLogoLefttop) {
        this.siteLogoLefttop = siteLogoLefttop;
    }

    public String getMobileLoginLogo() {
        return mobileLoginLogo;
    }

    public void setMobileLoginLogo(String mobileLoginLogo) {
        this.mobileLoginLogo = mobileLoginLogo;
    }

    public String getSiteFavicon() {
        return siteFavicon;
    }

    public void setSiteFavicon(String siteFavicon) {
        this.siteFavicon = siteFavicon;
    }

    @Override
    public String toString() {
        return "LogoConfigDTO{" +
                "siteLogoLogin='" + siteLogoLogin + '\'' +
                ", siteLogoSquare='" + siteLogoSquare + '\'' +
                ", siteLogoLefttop='" + siteLogoLefttop + '\'' +
                ", mobileLoginLogo='" + mobileLoginLogo + '\'' +
                ", siteFavicon='" + siteFavicon + '\'' +
                '}';
    }
}
