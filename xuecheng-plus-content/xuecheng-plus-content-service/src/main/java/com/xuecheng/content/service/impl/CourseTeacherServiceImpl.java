package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Iris
 * @version 1.0
 * @description 师资管理相关service
 * @date 2023/3/11 19:17
 */
@Service
public class CourseTeacherServiceImpl implements CourseTeacherService {

    @Autowired
    private CourseTeacherMapper courseTeacherMapper;

    @Override
    public List<CourseTeacher> getCourseTeacherList(Long courseId) {
        List<CourseTeacher> courseTeachers = courseTeacherMapper.selectList(
                new LambdaQueryWrapper<CourseTeacher>().eq(CourseTeacher::getCourseId, courseId)
        );
        return courseTeachers;
    }

    @Transactional
    @Override
    public CourseTeacher saveOrUpdateCourseTeacher(Long companyId, CourseTeacher courseTeacher) {
        Long id = courseTeacher.getId();
        if (id == null) {
            // 新增一条教师信息记录
            courseTeacher.setCreateDate(LocalDateTime.now());
            int insert = courseTeacherMapper.insert(courseTeacher);
            if (insert <= 0)
                XueChengPlusException.cast("添加教师信息失败");
        } else {
            // 更新一条教师信息记录
            int update = courseTeacherMapper.updateById(courseTeacher);
            if (update <= 0)
                XueChengPlusException.cast("修改教师信息失败");
        }
        return courseTeacherMapper.selectById(courseTeacher.getId());
    }

    @Transactional
    @Override
    public void deleteCourseTeacher(Long companyId, Long courseId, Long teacherId) {
//        CourseTeacher courseTeacher = courseTeacherMapper.selectById(teacherId);
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getId, teacherId);
        queryWrapper.eq(CourseTeacher::getCourseId, courseId);
        int delete = courseTeacherMapper.delete(queryWrapper);
        if (delete < 0) {
            XueChengPlusException.cast("删除教师信息失败");
        }
    }

}
