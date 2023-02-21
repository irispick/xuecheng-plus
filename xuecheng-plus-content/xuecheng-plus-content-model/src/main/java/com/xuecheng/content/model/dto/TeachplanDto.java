package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import lombok.Data;

import java.util.List;

/**
 * @author Iris
 * @version 1.0
 * @description 课程计划树形结构dto
 * @date 2023/2/19 16:46
 */
@Data
public class TeachplanDto extends Teachplan {

    // 关联的媒资信息
    TeachplanMedia teachplanMedia;

    // 子目录
    List<TeachplanDto> teachPlanTreeNodes;
}
