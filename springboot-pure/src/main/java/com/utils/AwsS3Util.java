package com.utils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * Created by shanmin.zhang
 * Date: 18/6/5
 * Time: 上午11:46
 */
public class AwsS3Util {

    private final static Logger logger = LoggerFactory.getLogger(AwsS3Util.class);

    //cn、ap 环境，S3桶key
    public static final String S3_LCM_BUCKET_NAME = "lcm-client-screenshot";

    private static AmazonS3 apAmazonS3 = null;

    private static AmazonS3 cnAmazonS3 = null;

    private static Byte[] apS3Lock = new Byte[1];
    private static Byte[] cnS3Lock = new Byte[1];


    /**
     * 构建ap环境S3实例
     *
     * @return
     */
    private static AmazonS3 buildApS3Instance() {
        if (apAmazonS3 == null) {
            synchronized (apS3Lock) {
                if (apAmazonS3 == null) {
                    apAmazonS3 = AmazonS3ClientBuilder.standard()
                            .withCredentials(new AWSStaticCredentialsProvider(new ProfileCredentialsProvider("/data/s3/aws_s3_ap_credentials", "default").getCredentials()))
                            .withRegion(Regions.AP_NORTHEAST_1.getName())
                            .build();
                }
            }
        }
        return apAmazonS3;
    }

    /**
     * 构建cn环境S3实例
     *
     * @return
     */
    private static AmazonS3 buildCnS3Instance() {
        if (cnAmazonS3 == null) {
            synchronized (cnS3Lock) {
                if (cnAmazonS3 == null) {
                    try {
                        cnAmazonS3 = AmazonS3ClientBuilder.standard()
                                .withCredentials(new ProfileCredentialsProvider())
                                .withRegion(Regions.CN_NORTH_1.getName())
                                .build();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return cnAmazonS3;
    }


    public static AmazonS3 getS3Instance(String region) {
        Assert.hasLength(region, "region is empty");
        switch (region) {
            case "cn": {
                return buildCnS3Instance();
            }
            case "ap": {
                return buildApS3Instance();
            }
        }
        return null;
    }


    /**
     * 上传文件流到S3
     *
     * @param s3
     * @param bucketName
     * @param key
     * @param fileItem
     * @throws IOException
     */
    public static void put2S3(AmazonS3 s3, String bucketName, String key, FileItem fileItem) throws IOException {
        Assert.notNull(s3, "AmazonS3 instance is null");
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(fileItem.getSize());
        objectMetadata.setContentType(fileItem.getContentType());
        s3.putObject(new PutObjectRequest(bucketName, key, fileItem.getInputStream(), objectMetadata));
    }


    /**
     * 从S3下载对象
     *
     * @param s3
     * @param bucketName
     * @param key
     * @return
     */
    public static S3Object getS3Object(AmazonS3 s3, String bucketName, String key) {
        return s3.getObject(new GetObjectRequest(bucketName, key));
    }

}
