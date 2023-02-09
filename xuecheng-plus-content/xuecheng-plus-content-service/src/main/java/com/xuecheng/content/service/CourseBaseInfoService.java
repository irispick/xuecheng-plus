package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
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
    public PageResult<CourseBase> queryCourseBaseList(PageParams params, QueryCourseParamsDto queryCourseParamsDto);
}
