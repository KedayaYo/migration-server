package com.skysys.service.model;

import cn.hutool.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author tth
 * @description http请求响应实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> implements Serializable {
    /**
     * 成功
     */
    public static final int SUCCESS = HttpStatus.HTTP_OK;
    /**
     * 失败
     */
    public static final int FAIL = HttpStatus.HTTP_INTERNAL_ERROR;
    private static final long serialVersionUID = 1L;

    /**
     * 响应编码
     */
    private int code;

    /**
     * 响应信息
     */
    private String reason;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应时间戳
     */
    private long timestamp;

    public static <T> R<T> ok() {
        return restResult(null, SUCCESS, "操作成功");
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, SUCCESS, "操作成功");
    }

    public static <T> R<T> ok(String msg) {
        return restResult(null, SUCCESS, msg);
    }

    public static <T> R<T> ok(String msg, T data) {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> R<T> fail() {
        return restResult(null, FAIL, "操作失败");
    }

    public static <T> R<T> fail(String msg) {
        return restResult(null, FAIL, msg);
    }

    public static <T> R<T> fail(T data) {
        return restResult(data, FAIL, "操作失败");
    }

    public static <T> R<T> fail(String msg, T data) {
        return restResult(data, FAIL, msg);
    }

    public static <T> R<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }

    private static <T> R<T> restResult(T data, int code, String reason) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        r.setReason(reason);
        return r;
    }

    private static <T> R<T> restResult(T data, int code, String reason, long timestamp) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        r.setReason(reason);
        r.setTimestamp(timestamp);
        return r;
    }

    public static <T> Boolean isError(R<T> ret) {
        return !isSuccess(ret);
    }

    public static <T> Boolean isSuccess(R<T> ret) {
        return R.SUCCESS == ret.getCode();
    }
}
