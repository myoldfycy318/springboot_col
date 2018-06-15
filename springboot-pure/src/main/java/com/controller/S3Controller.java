package com.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.utils.AwsS3Util;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Created by shanmin.zhang
 * Date: 18/6/12
 * Time: 下午6:25
 */
@RestController
@RequestMapping("s3")
public class S3Controller {

    private static final int _1M = 1 * 1024 * 1024;//1M

    @Value("${dirPath}")
    private String dirPath;


    @Consumes(MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping("picUpload")
    public String upload(HttpServletRequest request) {
        FileItem item = null;
        String s3key = "";
        try {
            AmazonS3 amazonS3 = AwsS3Util.getS3Instance("ap");
            DiskFileItemFactory factory = new DiskFileItemFactory();
            request.setCharacterEncoding("utf-8");
            //设置上传到服务器上文件的临时存放目录 ,防止存放到系统盘造成系统盘空间不足,默认值：System.getProperty("java.io.tmpdir")指定的目录
            factory.setRepository(new File(dirPath));
            factory.setSizeThreshold(_1M);
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(10 * _1M);
            //调用 parseRequest（request）方法  获得上传文件 FileItem 的集合list 可实现多文件上传。
            List<FileItem> list = (List<FileItem>) upload.parseRequest(request);
            item = list.get(0);
            if (item == null) {
                return "失败";
            }
            //如果获取的表单信息是普通的文本信息
            if (item.isFormField()) {
                return "普通的文本信息";
            }
            //如果传入的是非简单字符串，而是图片，音频，视频等二进制文件。
            // String fileName = item.getName().substring(item.getName().lastIndexOf(File.separator) + 1);
            //fileName = fileName.replaceAll("[^\\w-.]*", "");//替换掉特殊字符，除字母、数字减号、下划线、小数点外
            s3key = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
            AwsS3Util.put2S3(AwsS3Util.getS3Instance("ap"), AwsS3Util.S3_LCM_BUCKET_NAME, s3key, item);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return s3key;
    }

}
