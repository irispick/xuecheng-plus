package com.xuecheng.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Iris
 * @version 1.0
 * @description 添加课程dto
 * @date 2023/2/18 18:55
 */
@Data
@ApiModel(value = "EditCourseDto", description = "修改课程基本信息")
public class EditCourseDto extends AddCourseDto{

    //课程id
    @ApiModelProperty(value = "课程名称", required = true)
    private Long id;
}
