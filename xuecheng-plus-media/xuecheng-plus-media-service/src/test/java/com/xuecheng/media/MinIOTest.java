package com.xuecheng.media;

import io.minio.*;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;

/**
 * @author Iris
 * @version 1.0
 * @description 测试minio上传文件、删除文件、查询文件
 * @date 2023/2/24 15:54
 */
public class MinIOTest {
    static MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://192.168.101.65:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();

    // 上传文件测试
    @Test
    public void upload() {

        try {
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket("testbucket") // 桶名
                    .object("08ac3ce22c3f6212dcc29f3b90427e66a9836eafc15668ef700c3ee165ed8f42.png")   // 同一个桶内，对象名不能重复
                    .filename("E:\\125最可爱\\picturebulabula\\壁纸\\08ac3ce22c3f6212dcc29f3b90427e66a9836eafc15668ef700c3ee165ed8f42.png")  // 磁盘文件
                    .build();
            // 上传
            minioClient.uploadObject(uploadObjectArgs);
            System.out.println("上传成功");
        } catch (Exception e) {
            System.out.println("上传失败");
        }
    }

    // 指定桶内的子目录
    @Test
    public void upload2Sub() {

        try {
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket("testbucket") // 桶名
                    .object("test/53ee80e81f0d0e3b7479c6e902993dd847d0cca35b59a6202460bc30df8f4c19.png")   // 同一个桶内，对象名不能重复
                    .filename("E:\\125最可爱\\picturebulabula\\壁纸\\53ee80e81f0d0e3b7479c6e902993dd847d0cca35b59a6202460bc30df8f4c19.png")  // 磁盘文件
                    .build();
            // 上传
            minioClient.uploadObject(uploadObjectArgs);
            System.out.println("上传成功");
        } catch (Exception e) {
            System.out.println("上传失败");
        }
    }

    // 删除文件
    @Test
    public void delete() {

        try {
            RemoveObjectArgs removeObjectArgs =
                    RemoveObjectArgs
                            .builder()
                            .bucket("testbucket")
                            .object("test/53ee80e81f0d0e3b7479c6e902993dd847d0cca35b59a6202460bc30df8f4c19.png")
                            .build();
            minioClient.removeObject(removeObjectArgs);
            System.out.println("删除成功");
        } catch (Exception e) {
            System.out.println("删除失败");
        }
    }

    // 查询文件
    @Test
    public void getFile() {

        GetObjectArgs getObjectArgs =
                GetObjectArgs.builder()
                        .bucket("testbucket")
                        .object("08ac3ce22c3f6212dcc29f3b90427e66a9836eafc15668ef700c3ee165ed8f42.png")
                        .build();

        try(
                FilterInputStream inputStream = minioClient.getObject(getObjectArgs);
                FileOutputStream outputStream = new FileOutputStream(new File("D:\\develop\\upload\\1_1.png"));
                ) {


            if (inputStream != null) {
                IOUtils.copy(inputStream, outputStream);
            }
        } catch (Exception e) {
            System.out.println("查询失败");
        }
    }
}
