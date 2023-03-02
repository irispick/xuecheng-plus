package com.xuecheng.base.model;

import lombok.Data;
import lombok.ToString;

/**
 * @description 通用结果类型
 * @author Iris
 * @date 2023/2/27 19:58
 * @version 1.0
 */
@Data
@ToString
public class RestResponse<T> {
    /**
     * 响应编码，0为正常，-1为错误
     */
    private int code;

    /**
     * 响应提示信息
     */
    private String msg;

    /**
     * 响应内容
     */
    private T result;

    public RestResponse() {
        this(0, "success");
    }

    public RestResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 错误信息的封装
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> validfail(String msg) {
        RestResponse<T> response = new RestResponse<>();
        response.setCode(-1);
        response.setMsg(msg);
        return response;
    }

    /**
     * 添加正常响应数据（包含响应内容）
     * @param result
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> success(T result) {
        RestResponse<T> response = new RestResponse<>();
        response.setResult(result);
        return response;
    }

    /**
     * 添加正常响应数据（不包含响应内容）
     * @param <T>
     * @return RestResponse Rest服务封装响应数据
     */
    public static <T> RestResponse<T> success() {
        return new RestResponse<T>();
    }

    public Boolean isSuccessful() {
        return this.code == 0;
    }


}
