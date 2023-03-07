package com.xuecheng.media.service;

import com.xuecheng.media.model.po.MediaProcess;

import java.util.List;

/**
 * @author Iris
 * @version 1.0
 * @description 媒资文件处理业务方法
 * @date 2023/3/5 13:57
 */
public interface MediaFileProcessService {

    /**
    * @description 获取待处理任务
    * @param shardIndex 分片序号
    * @param shardTotal 分片总数
    * @param count 获取记录数
    * @return java.util.List<com.xuecheng.media.model.po.MediaProcess>
    * @author Iris
    * @date 2023/3/5 16:07
    */
    List<MediaProcess> getMediaProcessList(int shardIndex, int shardTotal, int count);

    /**
    * @description 保存任务结果
    * @param taskId 任务id
    * @param status 任务状态
    * @param fileId 文件id
    * @param url url
    * @param errorMsg 错误信息
    * @return void
    * @author Iris
    * @date 2023/3/5 16:18
    */
    void savaProcessFinishStatus(Long taskId, String status, String fileId, String url, String errorMsg);
}
