package com.xuecheng.media.api;

import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.service.MediaFileService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Iris
 * @version 1.0
 * @description 媒资文件管理接口
 * @date 2023/3/10 22:27
 */
@Api(value = "媒资文件管理接口", tags = "媒资文件管理接口")
@RestController
@RequestMapping("/open")
public class MediaOpenController {

    @Autowired
    private MediaFileService mediaFileService;

    @GetMapping("/preview/{mediaId}")
    public RestResponse<String> getPlayUrlByMediaId(@PathVariable String mediaId) {
        // 查询媒资信息
        MediaFiles mediaFiles = mediaFileService.getFileById(mediaId);

        if (mediaFiles == null) {
            return RestResponse.validfail("找不到视频信息");
        }
        // 取出视频播放地址
        String url = mediaFiles.getUrl();
        if (StringUtils.isEmpty(url)) {
            return RestResponse.validfail("该视频转码中");
        }
        return RestResponse.success(url);
    }
}
