package com.xuecheng.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.media.mapper.MediaFilesMapper;
import com.xuecheng.media.mapper.MediaProcessHistoryMapper;
import com.xuecheng.media.mapper.MediaProcessMapper;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.model.po.MediaProcess;
import com.xuecheng.media.model.po.MediaProcessHistory;
import com.xuecheng.media.service.MediaFileProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Iris
 * @version 1.0
 * @description 媒资文件处理业务方法实现
 * @date 2023/3/5 16:04
 */
@Slf4j
@Service
public class MediaFileProcessServiceImpl implements MediaFileProcessService {
    @Autowired
    private MediaProcessMapper mediaProcessMapper;

    @Autowired
    private MediaFilesMapper mediaFilesMapper;

    @Autowired
    private MediaProcessHistoryMapper mediaProcessHistoryMapper;

    @Override
    public List<MediaProcess> getMediaProcessList(int shardIndex, int shardTotal, int count) {
        List<MediaProcess> mediaProcesses = mediaProcessMapper.selectListByShardIndex(shardTotal, shardIndex, count);
        return mediaProcesses;
    }

    @Override
    public void savaProcessFinishStatus(Long taskId, String status, String fileId, String url, String errorMsg) {

        // 查询任务，如果不存在直接返回
        MediaProcess mediaProcess = mediaProcessMapper.selectById(taskId);
        if (mediaProcess == null) {
            log.debug("更新任务状态时此任务: {} 为空", taskId);
            return;
        }

        LambdaQueryWrapper<MediaProcess> queryWrapperById = new LambdaQueryWrapper<>();
        queryWrapperById.eq(MediaProcess::getId, taskId);
        if ("3".equals(status)) {
            // 任务失败
            MediaProcess mediaProcess_u = new MediaProcess();
            mediaProcess_u.setStatus("3");  //处理失败
            mediaProcess_u.setErrormsg(errorMsg);
            mediaProcessMapper.update(mediaProcess_u, queryWrapperById);
            return;
        }
        
        // 处理成功，更新url和状态
        if ("2".equals(status)) {
            MediaFiles mediaFiles = mediaFilesMapper.selectById(fileId);
            if (mediaFiles != null) {
                mediaFiles.setUrl(url);
                mediaFilesMapper.updateById(mediaFiles);
            }
            mediaProcess.setStatus("2");
            mediaProcess.setUrl(url);
            mediaProcess.setFinishDate(LocalDateTime.now());
            mediaProcessMapper.updateById(mediaProcess);
        }
        

        MediaProcessHistory mediaProcessHistory = new MediaProcessHistory();
        BeanUtils.copyProperties(mediaProcess, mediaProcessHistory);
        // 处理成功，将任务添加到历史记录表
        mediaProcessHistoryMapper.insert(mediaProcessHistory);
        // 处理成功，将待处理任务删除
        mediaProcessMapper.deleteById(taskId);

    }
}
