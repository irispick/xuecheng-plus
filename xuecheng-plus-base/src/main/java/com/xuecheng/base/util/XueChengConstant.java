package com.xuecheng.base.util;

/**
 * @author Iris
 * @version 1.0
 * @description TODO
 * @date 2023/3/2 0:03
 */
public interface XueChengConstant {

    //    [{"code":"001001","desc":"图片"},{"code":"001002","desc":"视频"},{"code":"001003","desc":"其它"}]
    // 资源类型
    interface ResourceType {
        /**
         * 图片
         */
        String IMAGE = "001001";
        /**
         * 视频
         */
        String VEDIO = "001002";
        /**
         * 其他
         */
        String OTHER = "001003";
    }

    //[{"code":"002001","desc":"审核未通过"},{"code":"002002","desc":"未审核"},{"code":"002003","desc":"审核通过"}]
    interface AuditStatusOfObject {
        /**
         * 审核未通过
         */
        String FAIL = "002001";
        /**
         * 未审核
         */
        String UNAUDITED = "002002";
        /**
         * 审核通过
         */
        String PASS = "002003";
    }

    // [{"code":"003001","desc":"未通知"},{"code":"003002","desc":"成功"}]
    // 消息通知状态
    interface NotificationStatus {
        /**
         * 未通知
         */
        String NOTIFY_FAIL = "003001";

        /**
         * 成功
         */
        String NOTIFY_SUCCESS = "003002";
    }

}
