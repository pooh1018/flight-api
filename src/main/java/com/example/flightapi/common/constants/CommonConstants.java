package com.example.flightapi.common.constants;

/**
 * 通用常量定义
 */
public final class CommonConstants {

    public static final String DATE_FORMAT_NO_SPLASH = "yyyyMMddHHmmss";

    /**
     * request.setAttribute和request.getAttribute的key定义
     */
    public static final class REQUEST_KEY {
        // 登录用户保存Key(request.setAttribute)
        public static final String REQUEST_KEY_LOGIN_USER = "_mineralmart_login_user:";
        public static final String REQUEST_KEY_PRIVILEGE = "_mineralmart_privilege:";
        public static final String CAPTCHA_CODE_KEY_PREFIX = "_mineralmart_captcha_code:";
    }

}
