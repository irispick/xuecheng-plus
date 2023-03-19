package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CoursePreviewDto;

import java.io.File;

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

    /**
    * @description 课程发布接口
    * @param companyId 机构id
    * @param courseId 课程id
    * @return void
    * @author Iris
    * @date 2023/3/14 16:22
    */
    void publish(Long companyId, Long courseId);

    /**
    * @description 课程静态化
    * @param courseId 课程id
    * @return java.io.File 静态化文件
    * @author Iris
    * @date 2023/3/17 23:12
    */
    File generateCourseHtml(Long courseId);

    /**
    * @description 上传课程静态化页面
    * @param courseId 课程id
     * @param file 静态化文件
    * @return void
    * @author Iris
    * @date 2023/3/17 23:13
    */
    void uploadCourseHtml(Long courseId, File file);
}
