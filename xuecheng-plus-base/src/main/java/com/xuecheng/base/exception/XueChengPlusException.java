package com.xuecheng.base.exception;

/**
 * @author Iris
 * @version 1.0
 * @description TODO
 * @date 2023/2/16 16:40
 */
public class XueChengPlusException extends RuntimeException {

    private String errMessage;

    public String getErrMessage() {
        return errMessage;
    }

    public XueChengPlusException() {
        super();
    }

    public XueChengPlusException(String message) {
        super(message);
        this.errMessage = message;
    }

    public static void cast(String errMessage) {
        throw new XueChengPlusException(errMessage);
    }

    public static void cast(CommonError commonError) {
        throw new XueChengPlusException(commonError.getErrMessage());
    }
}
