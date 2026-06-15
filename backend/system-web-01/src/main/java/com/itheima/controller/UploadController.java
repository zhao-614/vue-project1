package com.itheima.controller;

import com.itheima.pojo.Result;
import com.itheima.utils.AliyunOSSOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequestMapping("/upload")
@RestController
public class UploadController {
    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;
    /**
     * 文件上传到阿里云
     * @param file 文件
     * @return 阿里云文件访问路径
     */
//    @PostMapping
//    public Result upload(MultipartFile file) throws Exception {
//        log.info("文件上传：{}",file.getOriginalFilename());
//        // 上传文件
//        String url = aliyunOSSOperator.upload(file.getBytes(), file.getOriginalFilename());
//        log.info("文件上传完成，结果：{}",url);
//        return Result.success(url);
//    }
    /**
     * 文件上传在本地
     */

    private static final String UPLOAD_DIR = "D:/images/";
    /**
     * 上传文件 - 参数名file
     */
    @PostMapping
    public Result upload( MultipartFile file) throws Exception {
        log.info("上传文件：{}", file.getOriginalFilename());
        String uniqueFileName = null;
        if (!file.isEmpty()) {
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
            uniqueFileName = UUID.randomUUID().toString().replace("-", "") + extName;
            // 拼接完整的文件路径
            File targetFile = new File(UPLOAD_DIR + uniqueFileName);

            // 如果目标目录不存在，则创建它
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            // 保存文件
            file.transferTo(targetFile);
        }
        return Result.success(UPLOAD_DIR + uniqueFileName);
    }

}
