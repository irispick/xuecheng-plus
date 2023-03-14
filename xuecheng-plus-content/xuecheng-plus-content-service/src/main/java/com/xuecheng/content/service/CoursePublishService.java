package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CoursePreviewDto;

/**
 * @author Iris
 * @version 1.0
 * @description 课程预览、发布接口
 * @date 2023/3/10 0:03
 */
public interface CoursePublishService {

    /**
    * @description 获取课程预览信息
    * @param courseId 课程id
    * @return com.xuecheng.content.model.dto.CoursePreviewDto
    * @author Iris
    * @date 2023/3/10 0:05
    */
    CoursePreviewDto getCoursePreviewInfo(Long courseId);

    /**
    * @description 提交审核
    * @param companyId 机构id
    * @param courseId 课程id
    * @return void
    * @author Iris
    * @date 2023/3/11 18:04
    */
    void commitAudit(Long companyId, Long courseId);
}
