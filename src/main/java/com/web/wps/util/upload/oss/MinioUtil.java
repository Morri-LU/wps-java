package com.web.wps.util.upload.oss;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.web.wps.propertie.MinioProperties;
import com.web.wps.propertie.OSSProperties;
import com.web.wps.util.file.FileType;
import com.web.wps.util.file.FileTypeJudge;
import com.web.wps.util.file.FileUtil;
import com.web.wps.util.upload.ResFileDTO;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.MinioException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MinioUtil {
  private final MinioProperties oss;
  
  private final FileTypeJudge fileTypeJudge;
  
  @Autowired
  public MinioUtil(MinioProperties minio, FileTypeJudge fileTypeJudge) {
    this.oss = minio;
    this.fileTypeJudge = fileTypeJudge;
  }
  
  public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException {}
  
  private MinioClient getOSSClient() {
    return new MinioClient(this.oss.getEndpoint(), this.oss.getAccessKey(), this.oss.getAccessSecret());
  }
  
  public void deleteFile(String key) {
    MinioClient client = getOSSClient();
    try {
      System.out.println("delete-getBucketName" + this.oss.getBucketName());
      System.out.println("delete-diskname:" + this.oss.getDiskName());
      System.out.println("delete-key:" + key);
      client.removeObject(this.oss.getBucketName(), String.valueOf(this.oss.getDiskName()) + key);
    } catch (Exception e) {
      e.printStackTrace();
    }  
  }
  
  public String uploadFile(InputStream inputStream, String fileName, long fileSize, String bucketName, String diskName, String localFileName) {
    String resultStr = null;
    try {
      MinioClient minioClient = new MinioClient(this.oss.getEndpoint(), this.oss.getAccessKey(), this.oss.getAccessSecret());
      ObjectWriteResponse objectWriteResponse = minioClient.putObject((PutObjectArgs)((PutObjectArgs.Builder)((PutObjectArgs.Builder)((PutObjectArgs.Builder)PutObjectArgs.builder()
          .bucket(bucketName))
          .stream(inputStream, inputStream.available(), -1L)
          .object(fileName))
          .bucket(bucketName))
          .build());
      resultStr = objectWriteResponse.etag();
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return resultStr;
  }
  
  public ResFileDTO uploadMultipartFile(MultipartFile file) {
    String fileType, fileName = file.getOriginalFilename();
    ResFileDTO o = new ResFileDTO();
    long fileSize = file.getSize();
    try {
      InputStream inputStream = file.getInputStream();
      FileType type = this.fileTypeJudge.getType(inputStream);
      if (type == null || "null".equals(type.toString()) || 
        "XLS_DOC".equals(type.toString()) || "XLSX_DOCX".equals(type.toString()) || 
        "WPSUSER".equals(type.toString()) || "WPS".equals(type.toString())) {
        fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
      } else {
        fileType = type.toString().toLowerCase();
      } 
    } catch (Exception e) {
      e.printStackTrace();
      fileType = "";
    } 
    try {
      o = uploadDetailInputStream(file.getInputStream(), fileName, fileType, fileSize);
    } catch (IOException e) {
      e.printStackTrace();
    } 
    return o;
  }
  
  public ResFileDTO uploadDetailInputStream(InputStream in, String fileName, String fileType, long fileSize) {
    String uuidFileName = FileUtil.getFileUUIDName(fileName, fileType);
    String fileUrl = String.valueOf(this.oss.getFileUrlPrefix()) + this.oss.getDiskName() + uuidFileName;
    String md5key = uploadFile(in, uuidFileName, fileSize, this.oss.getBucketName(), 
        "", fileName);
    ResFileDTO o = new ResFileDTO();
    if (md5key != null) {
      o.setFileType(fileType);
      o.setFileName(fileName);
      o.setCFileName(uuidFileName);
      o.setFileUrl(fileUrl);
      o.setFileSize(fileSize);
      o.setMd5key(md5key);
    } 
    return o;
  }
  
}