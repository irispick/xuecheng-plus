package com.xuecheng.content.feignclient;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Iris
 * @version 1.0
 * @description
 * @date 2023/3/17 13:49
 */
@Slf4j
@Component
public class MediaServiceClientFallbackFactory implements FallbackFactory<MediaServiceClient> {
    // 拿到了熔断的异常信息throwable
    @Override
    public MediaServiceClient create(Throwable throwable) {

        return new MediaServiceClient() {
            // 发生熔断，上游服务调用此方法执行降级逻辑
            @Override
            public String upload(MultipartFile filedata, String folder, String objectName) {

                log.debug("远程调用上传文件的接口发生熔断: {}", throwable.toString());
                return null;
            }
        };
    }
}
