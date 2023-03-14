package com.xuecheng.content.api;

import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Iris
 * @version 1.0
 * @description 师资管理
 * @date 2023/3/11 19:10
 */
@Api(value = "师资管理相关接口", tags = "师资管理相关接口")
@RestController
public class CourseTeacherController {

    @Autowired
    private CourseTeacherService courseTeacherService;

    @ApiOperation("查询教师信息接口")
    @GetMapping("/courseTeacher/list/{courseId}")
    public List<CourseTeacher> getCourseTeacherList(@PathVariable("courseId") Long courseId) {
        List<CourseTeacher> courseTeacherList = courseTeacherService.getCourseTeacherList(courseId);
        /*if (courseTeacherList == null || courseTeacherList.size() == 0) {
            XueChengPlusException.cast("当前课程暂未安排老师");
        }*/
        return courseTeacherList;
    }

    @ApiOperation("添加/修改教师信息接口")
    @PostMapping("/courseTeacher")
    public CourseTeacher addCourseTeacher(@RequestBody CourseTeacher courseTeacher) {
        Long companyId = 1232141425L;

        return courseTeacherService.saveOrUpdateCourseTeacher(companyId, courseTeacher);
    }

    @ApiOperation("删除教师信息接口")
    @DeleteMapping("/courseTeacher/course/{courseId}/{teacherId}")
    public void deleteCourseTeacher(@PathVariable("courseId") Long courseId,
                                    @PathVariable("teacherId") Long teacherId) {
        Long companyId = 1232141425L;
        courseTeacherService.deleteCourseTeacher(companyId, courseId, teacherId);
    }
}
