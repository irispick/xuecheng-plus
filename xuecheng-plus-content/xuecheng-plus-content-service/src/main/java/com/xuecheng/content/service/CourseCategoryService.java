package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;

import java.util.List;

/**
 * @author Iris
 * @version 1.0
 * @description 课程分类操作相关的service
 * @date 2023/2/13 20:31
 */
public interface CourseCategoryService {

    /**
    * @description 课程分类树形结构查询
    * @param id 根结点id
    * @return 根结点下面的所有结点
    * @author Iris
    * @date 2023/2/13 20:32
    */
    List<CourseCategoryTreeDto> queryTreeNodes(String id);
}
