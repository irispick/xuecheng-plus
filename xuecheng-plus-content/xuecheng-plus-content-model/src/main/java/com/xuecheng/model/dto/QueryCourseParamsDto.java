package com.xuecheng.model.dto;

import lombok.Data;

/**
 * @author Iris
 * @version 1.0
 * @description TODO
 * @date 2023/2/7 18:44
 */
@Data
public class QueryCourseParamsDto {
    // 课程名称
    private String courseName;

    // 审核状态
    private String auditStatus;

    // 发布状态
    private String publishStatus;
}
