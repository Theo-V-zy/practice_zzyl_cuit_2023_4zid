package com.soft.controller;

import com.soft.utils.AliyunOssUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@RestController
public class FileController {

    @Autowired
    private AliyunOssUtils aliyunOssUtils;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Value("${app.upload.storage:local}")
    private String uploadStorage;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png");

    @PostMapping("/upload")
    public String fileUpload(@RequestParam("mf") MultipartFile mf) throws Exception {
        if (mf == null || mf.isEmpty()) {
            throw new IllegalArgumentException("请选择要上传的图片");
        }
        String oldName = mf.getOriginalFilename();
        int dotIndex = oldName == null ? -1 : oldName.lastIndexOf(".");
        if (dotIndex < 0) {
            throw new IllegalArgumentException("图片文件缺少扩展名");
        }
        String ext = oldName.substring(dotIndex).toLowerCase(Locale.ROOT);
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new IllegalArgumentException("仅支持 JPG、JPEG、PNG 图片");
        }
        String name = UUID.randomUUID().toString() + ext;
        byte[] bytes = mf.getBytes();

        if ("oss".equalsIgnoreCase(uploadStorage) && aliyunOssUtils.isConfigured()) {
            try {
                return aliyunOssUtils.uploadFile(name, bytes);
            } catch (Exception e) {
                System.err.println("OSS上传失败，降级到本地存储: " + e.getMessage());
            }
        }

        // 本地兜底
        Path root = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(root);
        Files.write(root.resolve(name), bytes, StandardOpenOption.CREATE_NEW);
        String localUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(name)
                .toUriString();
        System.out.println("本地存储: " + localUrl);
        return localUrl;
    }
}
