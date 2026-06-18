package com.ai.doctor.controller;


import cn.hutool.core.io.FileUtil;
import com.ai.doctor.utils.Results;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("files")
@Slf4j
public class FileController {

    //上传位置
    private static final String ROOT_PATH = System.getProperty("user.dir") + "/files";

    /**
     * 文件上传接口
     * @return
     */
    @PostMapping("upload")
    public Results upload(@RequestParam("file") MultipartFile file) throws Exception{
        //上传的原始的文件名字
        String originalFilename = file.getOriginalFilename();
        log.info("上传的原始的文件名是:"+originalFilename);
        //生成一个新的文件名
        String fileName =UUID.randomUUID().toString()+"_"+originalFilename;
        File finalFile = new File(ROOT_PATH + "/" + fileName);  // 最终存到磁盘的文件对象
        if (!finalFile.getParentFile().exists()) {  // 如果父级目录不存在 就得创建
            finalFile.getParentFile().mkdirs();
        }
        //文件上传
        file.transferTo(finalFile);
        // 返回文件的url
        String url = "http://127.0.0.1:9090/files/download/"+ fileName;
        return Results.success(url);
    }

    /**
     * 文件下载
     * @param fileName
     * @param response
     */
    @GetMapping("/download/{fileName}")
    public void downLoad(@PathVariable("fileName") String fileName, HttpServletResponse response) throws Exception{
        File finalFile = new File(ROOT_PATH + "/" + fileName);  // 要下载的图片
        //字节输出流
        ServletOutputStream os = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentType("application/octet-stream");
        // os.write(FileUtil.readBytes(file));
        FileUtil.writeToStream(finalFile, os);
        os.flush();
        os.close();
    }


}
