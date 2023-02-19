package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

/**
 * @author Iris
 * @version 1.0
 * @description 课程管理service
 * @date 2023/2/9 17:00
 */
public interface CourseBaseInfoService {

    /**
     * @description 课程查询
     * @param params 分页参数
     * @param queryCourseParamsDto 查询条件
     * @return
     */
    PageResult<CourseBase> queryCourseBaseList(PageParams params, QueryCourseParamsDto queryCourseParamsDto);


    /**
     * 新增课程
     * @param companyId 培训机构id
     * @param addCourseDto 新增课程的信息
     * @return 课程信息包括基本信息、营销信息
     */
    CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);

    /**
    * @description 根据id查询课程信息
    * @param courseId 课程id
    * @return com.xuecheng.content.model.dto.CourseBaseInfoDto
    * @author Iris
    * @date 2023/2/18 19:02
    */
    CourseBaseInfoDto getCourseBaseInfo(Long courseId);

    /**
    * @description 修改课程信息
    * @param companyId 机构id，本机构只能修改本机构的课程
    * @param dto 课程信息
    * @return com.xuecheng.content.model.dto.CourseBaseInfoDto
    * @author Iris
    * @date 2023/2/18 19:07
    */
    CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto);
}
