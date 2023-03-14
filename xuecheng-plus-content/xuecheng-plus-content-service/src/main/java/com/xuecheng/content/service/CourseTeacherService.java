package com.xuecheng.content.service;

import com.xuecheng.content.model.po.CourseTeacher;

import java.util.List;

/**
 * @author Iris
 * @version 1.0
 * @description 师资管理接口
 * @date 2023/3/11 19:08
 */
public interface CourseTeacherService {

    /**
    * @description 查询某课程的授课教师
    * @param courseId 课程id
    * @return java.util.List<com.xuecheng.content.model.po.CourseTeacher>
    * @author Iris
    * @date 2023/3/11 19:16
    */
    List<CourseTeacher> getCourseTeacherList(Long courseId);

    /**
    * @description 保存或修改教师信息
    * @param courseTeacher 教师信息
    * @return com.xuecheng.content.model.po.CourseTeacher
    * @author Iris
    * @date 2023/3/11 20:04
    */
    CourseTeacher saveOrUpdateCourseTeacher(Long companyId, CourseTeacher courseTeacher);

    /**
    * @description TODO 删除教师信息
    * @param courseId 课程id
    * @param teacherId 教师id
    * @return void
    * @author Iris
    * @date 2023/3/11 20:07
    */
    void deleteCourseTeacher(Long companyId, Long courseId, Long teacherId);
}
