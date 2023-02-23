package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Iris
 * @version 1.0
 * @description 课程基本信息管理业务接口实现类
 * @date 2023/2/20 13:36
 */
@Slf4j
@Service
public class TeachplanServiceImpl implements TeachplanService {

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public List<TeachplanDto> findTeachplanTree(long courseId) {

        return teachplanMapper.selectTreeNodes(courseId);
    }

    @Transactional
    @Override
    public void saveTeachplan(SaveTeachplanDto teachplanDto) {
        Long id = teachplanDto.getId();
        if (id != null) {
            // 修改课程计划
            Teachplan teachplan = teachplanMapper.selectById(id);
            BeanUtils.copyProperties(teachplanDto, teachplan);
            teachplanMapper.updateById(teachplan);
        } else {
            // 新增课程计划
            Teachplan teachplanNew = new Teachplan();
            BeanUtils.copyProperties(teachplanDto, teachplanNew);
            // 获取同父同级别的课程数量
            int count = this.getTeachplanCount(teachplanDto.getCourseId(), teachplanDto.getParentid());
            // 设置新课程计划的排序号
            teachplanNew.setOrderby(count + 1);
            teachplanMapper.insert(teachplanNew);
        }
    }

    @Transactional
    @Override
    public void deleteTeachplan(long teachplanId) {
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if (teachplan == null) {
            XueChengPlusException.cast("课程计划不存在");
        }
        // 课程计划级别
        Integer grade = teachplan.getGrade();
        //删除第二级别的小节的同时需要将其它关联的视频信息也删除。
        if (grade == 2) {
            // 删除关联的媒资信息
            LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TeachplanMedia::getTeachplanId, teachplanId);
            teachplanMediaMapper.delete(queryWrapper);
            // 删除课程计划
            teachplanMapper.deleteById(teachplanId);
        } else {
            //删除第一级别的章时要求章下边没有小节方可删除。
            // 查询章节下是否有小节
            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Teachplan::getParentid, teachplanId);
            Integer count = teachplanMapper.selectCount(queryWrapper);
            if (count > 0) {
                // 章节下包含小节，删除失败
                XueChengPlusException.cast("课程计划信息还有子级信息，无法操作");
            }
            teachplanMapper.deleteById(teachplanId);
        }
    }

    @Transactional
    @Override
    public void moveTeachplan(String movetype, long teachplanId) {
        // 获取当前操作的课程计划
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        // 获取当前课程计划的父id、课程id
        Long parentid = teachplan.getParentid();
        Long courseId = teachplan.getCourseId();

        // 获取当前课程计划的排序字段
        Integer orderby = teachplan.getOrderby();

        Teachplan teachplanOther;
        Integer otherOrderby;

        // 构造查询条件
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId, courseId);
        queryWrapper.eq(Teachplan::getParentid, parentid);

        if ("moveup".equals(movetype)) {
            // 上移
            // 获取当前课程计划的上一条计划，即排序号仅次于当前课程计划
            queryWrapper.lt(Teachplan::getOrderby, orderby);
            queryWrapper.orderByDesc(Teachplan::getOrderby).last("limit 1");
            teachplanOther = teachplanMapper.selectOne(queryWrapper);
            if (teachplanOther == null) {
                // 当前课程计划为第一条计划，无法上移
                XueChengPlusException.cast("无法上移");
            }
            otherOrderby = teachplanOther.getOrderby();

        } else {
            // 下移
            // 获取当前课程计划的下一条计划，即排序号仅大于当前课程计划
            queryWrapper.gt(Teachplan::getOrderby, orderby);
            queryWrapper.orderByAsc(Teachplan::getOrderby).last("limit 1");
            teachplanOther = teachplanMapper.selectOne(queryWrapper);
            if (teachplanOther == null) {
                // 当前课程计划为最后一条计划，无法下移
                XueChengPlusException.cast("无法下移");
            }
            otherOrderby = teachplanOther.getOrderby();
        }
        // 将两个课程计划的排序号互换
        teachplan.setOrderby(otherOrderby);
        teachplanOther.setOrderby(orderby);

        teachplanMapper.updateById(teachplan);
        teachplanMapper.updateById(teachplanOther);
    }

    /**
    * @description 获取最新的课程排序号
    * @param courseId 课程id
    * @param parentId 所属课程计划id
    * @return int
    * @author Iris
    * @date 2023/2/20 16:10
    */
    private int getTeachplanCount(long courseId, long parentId) {
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId, courseId);
        queryWrapper.eq(Teachplan::getParentid, parentId);
        queryWrapper.orderByDesc(Teachplan::getOrderby).last("limit 1");
        // 查询同等级中排序号最大的课程
        Teachplan teachplan = teachplanMapper.selectOne(queryWrapper);
        // 如果不存在课程，返回0，存在课程，返回最大的排序号
        return teachplan==null ? 0 : teachplan.getOrderby();
    }
}
