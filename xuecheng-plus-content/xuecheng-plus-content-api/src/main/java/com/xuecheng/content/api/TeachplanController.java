package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.service.TeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Iris
 * @version 1.0
 * @description 课程计划编辑接口
 * @date 2023/2/19 19:12
 */
@Api(value = "课程计划编辑接口", tags = "课程计划编辑接口")
@RestController
public class TeachplanController {

    @Autowired
    private TeachplanService teachplanService;

    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId) {

        return teachplanService.findTeachplanTree(courseId);
    }

    @ApiOperation("课程计划创建或修改")
    @PostMapping("/teachplan")
    public void saveTeachplan(@RequestBody SaveTeachplanDto teachplanDto) {
        teachplanService.saveTeachplan(teachplanDto);
    }

    @ApiOperation("删除课程计划")
    @DeleteMapping("/teachplan/{teachplanId}")
    public void deleteTeachplan(@PathVariable long teachplanId) {
        teachplanService.deleteTeachplan(teachplanId);
    }

    @ApiOperation("课程计划排序")
    @PostMapping("/teachplan/{movetype}/{teachplanId}")
    public void moveTeachplan(@PathVariable String movetype, @PathVariable long teachplanId) {
        teachplanService.moveTeachplan(movetype, teachplanId);
    }

    @ApiOperation(value = "课程计划和媒资信息绑定")
    @PostMapping("/teachplan/association/media")
    public void associationMedia(@RequestBody BindTeachplanMediaDto bindTeachplanMediaDto) {
        teachplanService.associationMedia(bindTeachplanMediaDto);
    }

    @ApiOperation(value = "课程计划和媒资信息解绑")
    @DeleteMapping("/teachplan/association/media/{teachPlanId}/{mediaId}")
    public void unassociationMedia(@PathVariable Long teachPlanId,
                              @PathVariable String mediaId) {
        teachplanService.unassociationMedia(teachPlanId, mediaId);
    }
}
