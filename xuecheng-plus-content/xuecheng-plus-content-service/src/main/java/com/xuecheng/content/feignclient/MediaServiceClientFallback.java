package com.xuecheng.content.feignclient;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Iris
 * @version 1.0
 * @description
 * @date 2023/3/17 13:42
 */
public class MediaServiceClientFallback implements MediaServiceClient {
    @Override
    public String upload(MultipartFile filedata, String folder, String objectName) {
        return null;
    }
}
