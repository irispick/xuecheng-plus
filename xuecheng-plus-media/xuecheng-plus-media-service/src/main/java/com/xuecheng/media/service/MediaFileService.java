package com.xuecheng.media.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Mr.M
 * @version 1.0
 * @description 媒资文件管理业务类
 * @date 2022/9/10 8:55
 */
public interface MediaFileService {

    /**
     * @param pageParams          分页参数
     * @param queryMediaParamsDto 查询条件
     * @return com.xuecheng.base.model.PageResult<com.xuecheng.media.model.po.MediaFiles>
     * @description 媒资文件查询方法
     * @author Mr.M
     * @date 2022/9/10 8:57
     */
    PageResult<MediaFiles> queryMediaFiels(Long companyId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto);

    /**
    * @description 上传文件
    * @param companyId 机构id
    * @param uploadFileParamsDto 上传文件信息
    * @param bytes 文件字节数组
    * @param folder 桶下面的子文件目录，如果不传则默认年/月/日
    * @param objectName 对象名称
    * @return com.xuecheng.media.model.dto.UploadFileResultDto 上传文件结果
    * @author Iris
    * @date 2023/2/24 18:31
    */
    UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, byte[] bytes, String folder, String objectName);


    /**
    * @description 将文件信息添加到文件表
    * @param companyId 机构id
    * @param fileId 文件md5值
    * @param uploadFileParamsDto 上传文件的信息
    * @param bucket 桶
    * @param objectName 对象名称
    * @return com.xuecheng.media.model.po.MediaFiles
    * @author Iris
    * @date 2023/2/26 16:45
    */
    MediaFiles addMediaFilesToDb(Long companyId, String fileId, UploadFileParamsDto uploadFileParamsDto, String bucket, String objectName);


}
