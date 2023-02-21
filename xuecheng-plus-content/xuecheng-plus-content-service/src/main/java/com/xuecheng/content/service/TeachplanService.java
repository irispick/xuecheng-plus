package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;

import java.util.List;

/**
 * @author Iris
 * @version 1.0
 * @description 课程基本信息管理业务接口
 * @date 2023/2/20 13:33
 */
public interface TeachplanService {

    /**
    * @description 查询课程计划树形结构
    * @param courseId 课程id
    * @return List<TeachplanDto>
    * @author Iris
    * @date 2023/2/20 13:35
    */
    List<TeachplanDto> findTeachplanTree(long courseId);

    /**
    * @description 创建或修改课程计划
    * @param teachplanDto 课程计划信息
    * @return void
    * @author Iris
    * @date 2023/2/20 15:54
    */
    void saveTeachplan(SaveTeachplanDto teachplanDto);
}
