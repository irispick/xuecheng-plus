package com.xuecheng.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.mapper.MediaFilesMapper;
import com.xuecheng.media.mapper.MediaProcessMapper;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.model.po.MediaProcess;
import com.xuecheng.media.service.MediaFileService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Iris
 * @version 1.0
 * @description 媒资文件管理业务接口实现
 * @date 2023/2/25 16:00
 */
@Slf4j
@Service
public class MediaFileServiceImpl implements MediaFileService {
    @Autowired
    MediaFilesMapper mediaFilesMapper;

    @Autowired
    MediaProcessMapper mediaProcessMapper;

    @Autowired
    MinioClient minioClient;

    @Autowired
    MediaFileService currentProxy;

    // 普通文件存储的桶
    @Value("${minio.bucket.files}")
    private String bucket_Files;

    // 视频文件存储的桶
    @Value("${minio.bucket.videofiles}")
    private String bucket_videoFiles;


    @Override
    public PageResult<MediaFiles> queryMediaFiels(Long companyId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto) {

        //构建查询条件对象
        LambdaQueryWrapper<MediaFiles> queryWrapper = new LambdaQueryWrapper<>();

        // 根据文件名称模糊查询
        queryWrapper.like(StringUtils.isNotEmpty(queryMediaParamsDto.getFilename()), MediaFiles::getFilename, queryMediaParamsDto.getFilename());

        // 根据文件类型查询
        queryWrapper.eq(StringUtils.isNotEmpty(queryMediaParamsDto.getFileType()), MediaFiles::getFileType, queryMediaParamsDto.getFileType());

        //分页对象
        Page<MediaFiles> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        // 查询数据内容获得结果
        Page<MediaFiles> pageResult = mediaFilesMapper.selectPage(page, queryWrapper);
        // 获取数据列表
        List<MediaFiles> list = pageResult.getRecords();
        // 获取数据总数
        long total = pageResult.getTotal();
        // 构建结果集
        PageResult<MediaFiles> mediaListResult = new PageResult<>(list, total, pageParams.getPageNo(), pageParams.getPageSize());
        return mediaListResult;

    }

    @Override
    public UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, byte[] bytes, String folder, String objectName) {

        // 得到文件的md5值
        String fileMd5 = DigestUtils.md5Hex(bytes);

        if (StringUtils.isEmpty(folder)) {
            // 自动生成目录的路径：年/月/日
            folder = getFileFolder(new Date(), true, true, true);
        } else if (folder.indexOf("/") < 0) {   //传入的目录没有"/"
            folder += "/";
        }

        // 源文件名
        String filename = uploadFileParamsDto.getFilename();

        if (StringUtils.isEmpty(objectName)) {
            // 如果objectName为空，使用文件的md5值为objectName
            objectName = fileMd5 + filename.substring(filename.lastIndexOf("."));
        }

        objectName = folder + objectName;

        this.addMediaFilesToMinIO(bytes, bucket_Files, objectName);

        try {

            MediaFiles mediaFiles = currentProxy.addMediaFilesToDb(companyId, fileMd5, uploadFileParamsDto, bucket_Files, objectName);

            // 准备返回数据
            UploadFileResultDto uploadFileResultDto = new UploadFileResultDto();
            BeanUtils.copyProperties(mediaFiles, uploadFileResultDto);

            return uploadFileResultDto;
        } catch (Exception e) {
            log.debug("上传文件失败：{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
/*
    @Override
    public UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, byte[] bytes, String folder, String objectName) {

        // 得到文件的md5值
        String fileMd5 = DigestUtils.md5Hex(bytes);

        if (StringUtils.isEmpty(folder)) {
            // 自动生成目录的路径：年/月/日
            folder = getFileFolder(new Date(), true, true, true);
        } else if (folder.indexOf("/") < 0) {   //传入的目录没有"/"
            folder += "/";
        }

        // 源文件名
        String filename = uploadFileParamsDto.getFilename();

        if (StringUtils.isEmpty(objectName)) {
            // 如果objectName为空，使用文件的md5值为objectName
            objectName = fileMd5 + filename.substring(filename.lastIndexOf("."));
        }

        objectName = folder + objectName;

        try {
            String contentType = uploadFileParamsDto.getContentType();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucket_Files)
                    .object(objectName)
                    .stream(byteArrayInputStream, byteArrayInputStream.available(), -1)
                    .contentType(contentType)
                    .build();
            // 上传到minio
            minioClient.putObject(putObjectArgs);

            // 保存到数据库
            MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);
            if (mediaFiles == null) {
                // 插入文件表
                mediaFiles = new MediaFiles();

                // 封装数据
                BeanUtils.copyProperties(uploadFileParamsDto, mediaFiles);
                mediaFiles.setId(fileMd5);
                mediaFiles.setFileId(fileMd5);
                mediaFiles.setCompanyId(companyId);
                mediaFiles.setBucket(bucket_Files);
                mediaFiles.setFilePath(objectName);
                mediaFiles.setUrl("/" + bucket_Files + "/" + objectName);
                mediaFiles.setCreateDate(LocalDateTime.now());
                mediaFiles.setStatus("1");
                mediaFiles.setAuditStatus("002003");

                // 插入文件表
                mediaFilesMapper.insert(mediaFiles);
            }

            // 准备返回数据
            UploadFileResultDto uploadFileResultDto = new UploadFileResultDto();
            BeanUtils.copyProperties(mediaFiles, uploadFileResultDto);
            return uploadFileResultDto;
        } catch (Exception e) {
            log.debug("上传文件失败：{}", e.getMessage());
        }




        return null;
    }
*/

    // 将文件上传到分布式文件系统
    public void addMediaFilesToMinIO(String filePath, String bucket, String objectName) {
        try {
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .filename(filePath)
                    .build();
            minioClient.uploadObject(uploadObjectArgs);
            log.debug("文件上传成功: {}", filePath);
        } catch (Exception e) {
            XueChengPlusException.cast("文件上传到文件系统失败");
        }
    }

    // 将文件上传到分布式文件系统
    private void addMediaFilesToMinIO(byte[] bytes, String bucket, String objectName) {
        //  取objectName中的扩展名
        String extension = null;
        if (objectName.indexOf(".") >= 0) {
            extension = objectName.substring(objectName.lastIndexOf("."));
        }
        String contentType = getMimeTypeByextension(extension);

        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(byteArrayInputStream, byteArrayInputStream.available(), -1)
                    .contentType(contentType)
                    .build();
            // 上传到minio
            minioClient.putObject(putObjectArgs);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("上传文件到文件系统出错:{}", e.getMessage());
            XueChengPlusException.cast("上传文件出错");
        }
    }

    private String getMimeTypeByextension(String extension) {
        // 资源的媒体类型
        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;// 默认为 未知二进制流
        if (StringUtils.isNotEmpty(extension)) {
            ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
            if (extensionMatch != null) {
                contentType = extensionMatch.getMimeType();
            }
        }

        return contentType;
    }

    @Transactional
    @Override
    public MediaFiles addMediaFilesToDb(Long companyId, String fileId, UploadFileParamsDto uploadFileParamsDto, String bucket, String objectName) {
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileId);
        if (mediaFiles == null) {
            // 插入文件表
            mediaFiles = new MediaFiles();

            // 封装数据
            BeanUtils.copyProperties(uploadFileParamsDto, mediaFiles);
            mediaFiles.setId(fileId);
            mediaFiles.setFileId(fileId);
            mediaFiles.setCompanyId(companyId);
            mediaFiles.setBucket(bucket);
            mediaFiles.setFilePath(objectName);
            // 图片、MP4格式的视频可以直接设置url
            // 获取扩展名
            String filename = uploadFileParamsDto.getFilename();
            String extension = null;
            if (StringUtils.isNotEmpty(filename) && filename.indexOf(".") >= 0) {
                extension = filename.substring(filename.lastIndexOf("."));
            }
            // 得到媒体类型
            String mimeType = this.getMimeTypeByextension(extension);
            if (mimeType.contains("image") || mimeType.contains("mp4")) {
                mediaFiles.setUrl("/" + bucket + "/" + objectName);
            }
            mediaFiles.setCreateDate(LocalDateTime.now());
            mediaFiles.setStatus("1");
            mediaFiles.setAuditStatus("002003");

            // 插入文件表
            mediaFilesMapper.insert(mediaFiles);

            // 将avi视频添加到待处理任务表
            if ("video/x-msvideo".equals(mimeType)) {
                MediaProcess mediaProcess = new MediaProcess();
                BeanUtils.copyProperties(mediaFiles, mediaProcess);
                mediaProcess.setStatus("1");    // 未处理
                mediaProcessMapper.insert(mediaProcess);
            }

//            int i = 1/0;
        }

        return mediaFiles;
    }

    @Override
    public RestResponse<Boolean> checkFile(String fileMd5) {
        // 思路：在文件表存在，并且在文件系统存在，此文件才存在

        // 查询文件信息
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);
        if (mediaFiles != null) {
            // 桶
            String bucket = mediaFiles.getBucket();
            // 存储路径
            String filePath = mediaFiles.getFilePath();
            InputStream stream = null;
            try {
                stream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(bucket)
                                .object(filePath)
                                .build());
                if (stream != null) {
                    // 文件存在
                    return RestResponse.success(true);
                }
            } catch (Exception e) {
                return RestResponse.success(false);
            }
        }
        // 文件不存在
        return RestResponse.success(false);
    }

    @Override
    public RestResponse<Boolean> checkChunk(String fileMd5, int chunkIndex) {
        // 得到分块文件的目录
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        // 得到分块文件的路径
        String chunkFilePath = chunkFileFolderPath + chunkIndex;

        InputStream inputStream = null;
        try {
            inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket_videoFiles)
                            .object(chunkFilePath)
                            .build());
            if (inputStream != null) {
                // 分块文件存在
                return RestResponse.success(true);
            }
        } catch (Exception e) {
            // 分块文件不存在
            return RestResponse.success(false);
        }

        // 分块文件不存在
        return RestResponse.success(false);
    }

    @Override
    public RestResponse uploadChunk(String fileMd5, int chunk, byte[] bytes) {
        // 得到分块文件的目录
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        // 得到分块文件的上传路径
        String chunkFilePath = chunkFileFolderPath + chunk;

        try {
            this.addMediaFilesToMinIO(bytes, bucket_videoFiles, chunkFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            XueChengPlusException.cast("上传过程出错，请重试");
        }
        return RestResponse.success();
    }

    @Override
    public RestResponse mergeChunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto) {

        // 下载分块
        File[] chunkFiles = this.checkChunkStatus(fileMd5, chunkTotal);

        // 得到合并后的文件扩展名
        String filename = uploadFileParamsDto.getFilename();
        String extension = filename.substring(filename.lastIndexOf("."));//扩展名

        File tempMergeFile = null;
        try {
            // 创建一个临时文件，作为合并文件
            tempMergeFile = File.createTempFile("merge", extension);
        } catch (IOException e) {
            e.printStackTrace();
            XueChengPlusException.cast("创建临时合并文件失效");
        }

        // 合并分块
        try {

            byte[] b = new byte[1024];
            try (
                    // 创建合并文件的流对象
                    RandomAccessFile raf_write = new RandomAccessFile(tempMergeFile, "rw");
            ) {
                for (File file : chunkFiles) {
                    try (
                            // 读取分块文件的流对象
                            RandomAccessFile raf_read = new RandomAccessFile(file, "r");
                    ) {
                        int len = -1;
                        while ((len = raf_read.read(b)) != -1) {
                            // 向合并文件写数据
                            raf_write.write(b, 0, len);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                XueChengPlusException.cast("合并文件过程出错");

            }

            // 校验合并后的文件是否正确
            try {
                FileInputStream mergeFileStream = new FileInputStream(tempMergeFile);
                String mergeMd5Hex = DigestUtils.md5Hex(mergeFileStream);
                if (!fileMd5.equals(mergeMd5Hex)) {
                    log.debug("合并文件校验不通过, 文件路径: {}, 原始文件md5: {}", tempMergeFile.getAbsolutePath(), fileMd5);
                    XueChengPlusException.cast("合并文件校验不通过");
                }
            } catch (IOException e) {
                log.debug("合并文件校验出错, 文件路径: {}, 原始文件md5: {}", tempMergeFile.getAbsolutePath(), fileMd5);
                XueChengPlusException.cast("合并文件校验出错");
            }

            // 将合并后的文件上传到文件系统
            // 得到合并文件在minio的存储路径
            String mergeFilePath = this.getFilePathByMd5(fileMd5, extension);
            this.addMediaFilesToMinIO(tempMergeFile.getAbsolutePath(), bucket_videoFiles, mergeFilePath);
            // 将文件信息入库保存
            uploadFileParamsDto.setFileSize(tempMergeFile.length());//合并文件的大小
            currentProxy.addMediaFilesToDb(companyId, fileMd5, uploadFileParamsDto, bucket_videoFiles, mergeFilePath);
            return RestResponse.success();
        } finally {
            // 删除临时分块文件
            if (chunkFiles != null) {
                for (File chunkFile : chunkFiles) {
                    if (chunkFile.exists()) {
                        chunkFile.delete();
                    }
                }
            }
            // 删除合并的临时文件
            if (tempMergeFile != null)
                tempMergeFile.delete();

        }
    }

    @Override
    public MediaFiles getFileById(String id) {
        MediaFiles mediaFiles = mediaFilesMapper.selectById(id);
        if (mediaFiles == null) {
            XueChengPlusException.cast("文件不存在");
        }
        return mediaFiles;
    }

    /**
     * @param fileMd5    文件md5
     * @param chunkTotal 分块数量
     * @return java.io.File[] 分块文件数组
     * @description 下载分块
     * @author Iris
     * @date 2023/3/1 15:27
     */
    private File[] checkChunkStatus(String fileMd5, int chunkTotal) {
        // 所有分块文件列表
        File[] chunkFiles = new File[chunkTotal];
        // 分块文件所在目录
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        // 开始下载
        for (int i = 0; i < chunkTotal; i++) {
            // 分块文件路径
            String chunkFilePath = chunkFileFolderPath + i;
            File chunkFile = null;
            try {
                chunkFile = File.createTempFile("chunk", null);
            } catch (IOException e) {
                e.printStackTrace();
                XueChengPlusException.cast("创建分块临时信息出错");
            }
            // 下载分块文件
            chunkFile = this.downloadFileFromMinIO(chunkFile, bucket_videoFiles, chunkFilePath);
            chunkFiles[i] = chunkFile;
            /*GetObjectArgs getObjetArgs = GetObjectArgs.builder()
                    .bucket(bucket_videoFiles)
                    .object(chunkFilePath)
                    .build();
            try (
                    InputStream inputStream = minioClient.getObject(getObjetArgs);
                    FileOutputStream outputStream = new FileOutputStream(chunkFile);
                    ){
                IOUtils.copy(inputStream, outputStream);
                // 将下载好的分块文件加入数组
                chunkFiles[i] = chunkFile;
            } catch (Exception e) {
                e.printStackTrace();
                XueChengPlusException.cast("查询分块文件出错");
            } */
        }

        return chunkFiles;
    }


    public File downloadFileFromMinIO(File file, String bucket, String objectName) {
        GetObjectArgs getObjetArgs = GetObjectArgs.builder()
                .bucket(bucket)
                .object(objectName)
                .build();
        try (
                InputStream inputStream = minioClient.getObject(getObjetArgs);
                FileOutputStream outputStream = new FileOutputStream(file);
        ) {
            IOUtils.copy(inputStream, outputStream);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            XueChengPlusException.cast("查询分块文件出错");
        }
        return null;
    }

    // 得到分块文件的目录
    private String getChunkFileFolderPath(String fileMd5) {
        return fileMd5.substring(0, 1) + "/" + fileMd5.subSequence(1, 2)
                + "/" + fileMd5 + "chunk" + "/";
    }

    // 得到合并文件在minio的存储路径
    private String getFilePathByMd5(String fileMd5, String fileExt) {
        return fileMd5.substring(0, 1) + "/" + fileMd5.subSequence(1, 2)
                + "/" + fileMd5 + fileExt;
    }

    // 根据日期拼接目录
    private String getFileFolder(Date date, boolean year, boolean month, boolean day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 获取当前日期字符串
        String dateString = sdf.format(new Date());
        // 取出年月日
        String[] dateStringArray = dateString.split("-");
        StringBuffer folderString = new StringBuffer();
        if (year) {
            folderString.append(dateStringArray[0]);
            folderString.append("/");
        }
        if (month) {
            folderString.append(dateStringArray[1]);
            folderString.append("/");
        }
        if (year) {
            folderString.append(dateStringArray[2]);
            folderString.append("/");
        }
        return folderString.toString();

    }

    public static void main(String[] args) {
        String extension = ".jpg";
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
        if (extensionMatch != null) {
            System.out.println(extensionMatch.getMimeType());
        }
    }
}