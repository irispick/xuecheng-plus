package com.xuecheng.content;

import com.xuecheng.XuechengPlusContentServiceApplication;
import com.xuecheng.content.config.MultipartSupportConfig;
import com.xuecheng.content.feignclient.MediaServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author Iris
 * @version 1.0
 * @description 测试远程调用媒资服务
 * @date 2023/3/16 22:18
 */
@SpringBootTest(classes = {XuechengPlusContentServiceApplication.class})
public class FeignUploadTest {

    @Autowired
    MediaServiceClient mediaServiceClient;

    @Test
    public void test() {

        // 将file转成MultipartFile
        File file = new File("D:\\develop\\upload\\117.html");
        MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(file);
        String upload = mediaServiceClient.upload(multipartFile, "course/", "117.html");
        if (upload == null) {
            System.out.println("走了降级逻辑");
        }
    }
}
