package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.TeachplanMedia;

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

    /**
    * @description 删除课程计划
    * @param teachplanId 课程计划id
    * @return void
    * @author Iris
    * @date 2023/2/22 14:12
    */
    void deleteTeachplan(long teachplanId);

    /**
    * @description 上移或下移课程计划
    * @param movetype 移动类型
    * @param teachplanId 课程计划id
    * @return void
    * @author Iris
    * @date 2023/2/22 16:43
    */
    void moveTeachplan(String movetype, long teachplanId);

    /**
    * @description 教学计划绑定媒资
    * @param bindTeachplanMediaDto
    * @return com.xuecheng.content.model.po.TeachplanMedia
    * @author Iris
    * @date 2023/3/8 0:32
    */
    TeachplanMedia associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto);

    /**
    * @description 教学计划解绑媒资
    * @param teachplanId 教学计划id
    * @param mediaId 媒资id
    * @return void
    * @author Iris
    * @date 2023/3/8 19:39
    */
    void unassociationMedia(Long teachplanId, String mediaId);
}
