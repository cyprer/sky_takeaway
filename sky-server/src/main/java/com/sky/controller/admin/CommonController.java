package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传:{}", file);
        String filePath = null;
        try {
            // 获取原始文件名
            String originFilename = file.getOriginalFilename();
            // 获取文件后缀名
            if (originFilename != null) {
                String extension = originFilename.substring(originFilename.lastIndexOf("."));

                // 生成新的文件名
                String objectName = UUID.randomUUID().toString() + extension;
                // 获取文件的请求路径
                filePath = aliOssUtil.upload(file.getBytes(), objectName);
            }
        } catch (IOException e) {
            log.error("文件上传失败:{}", e);
        }
        return Result.success(filePath);
    }
}
