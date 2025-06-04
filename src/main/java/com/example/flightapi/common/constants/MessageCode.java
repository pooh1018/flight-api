package com.example.flightapi.common.constants;
/**
 * 消息代码定义。
 */
public final class MessageCode {

    /** 数据库异常 */
    public static final String DB_ERROR = "F100";
    /** IO异常 */
    public static final String IO_ERROR = "F200";
    /** IO异常 */
    public static final String MINIO_ERROR = "F201";
    /** 请求参数错误 */
    public static final String INVALID_PARAMETER = "E101";
    /**
     * 数据已存在
     */
    public static final String DATA_EXIST = "E301";
    /**
     * 数据不存在
     */
    public static final String DATA_NOT_EXIST = "E302";
    /**
     * 数据使用中
     */
    public static final String DATA_USED = "E303";
    /**
     * 用户名或密码错误
     */
    public static final String BAD_CREDENTIALS = "E400";
    /**
     * token无效
     */
    public static final String INVALID_TOKEN = "E401";
    /**
     * token 临期
     */
    public static final String EXPIRED = "0401";
    /**
     * 角色token权限不足
     */
    public static final String ROLE_INSUFFICIENT = "E403";
    /**
     * token无效
     */
    public static final String INVALID_REFRESH_TOKEN = "E408";
    /**
     * 过期请求
     */
    public static final String EXPIRED_IDENTITY = "E409";
    /**
     * 导入excel的数量超过限制
     */
    public static final String IMPORT_EXCEEDS_LIMIT = "E500";
    /**
     * 数据重复校验
     */
    public static final String DATA_DUPLICATION = "E501";
    /** 未知异常 */
    public static final String UNKNOWN = "E999";

    /** 上传文件超出大小限制错误 */
    public static final String MAX_UPLOAD_SIZE_EXCEEDED = "E10001";
    /** ClientId或ClientSecret验证错误 */
    public static final String BAD_CLIENT_AUTH = "E41000";
    /** 账号锁定 */
    public static final String ACCOUNT_LOCKED = "F1013";
    /** 账号无效 */
    public static final String ACCOUNT_DISABLED = "F1014";

    /**请求外部系统错误**/
    public static final String REQUEST_ERROR = "E1404";

}
