package com.asio.exception;

import cn.hutool.core.text.StrFormatter;

/**
 * 自定义异常同意消息处理
 */
public class BusinessException extends RuntimeException {
    private int status = 500;
    private String msg = "";

    public BusinessException(String message) {
        super(message);
        this.msg = message;
    }

    public BusinessException(String message, Object ... objArray) {
        super(StrFormatter.format(message, objArray));
        this.msg = StrFormatter.format(message, objArray);
    }

    public BusinessException(int status, String message) {
        super(message);
        this.msg = message;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
