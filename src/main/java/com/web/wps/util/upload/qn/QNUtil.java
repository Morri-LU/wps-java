package com.web.wps.util.upload.qn;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.web.wps.propertie.QNProperties;
import com.web.wps.util.file.FileUtil;
import com.web.wps.util.upload.ResFileDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class QNUtil {

    private final QNProperties qnConfig;

    public QNUtil(QNProperties qnConfig) {
        this.qnConfig = qnConfig;
    }

    private UploadManager uploadManager;
    private String upToken;

    /**
     * 获取配置
     */
    private Configuration getConf() {
        return new Configuration(Region.huanan());
    }

    /**
     * 获取权限
     */
    private Auth getAuth() {
        return Auth.create(qnConfig.getAccessKey(), qnConfig.getSecretKey());
    }

    /**
     * 同名覆盖，不同名新建
     *
     * @param key cFilename
     */
    private void makeUploadManager(String key) {
        StringMap putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"fsize\":$(fsize)}");
        Configuration cfg = getConf();
        this.uploadManager = new UploadManager(cfg);
        Auth auth = getAuth();
        this.upToken = auth.uploadToken(qnConfig.getBucket(), key, 3600, putPolicy);
    }

    /**
     * 通过文件流上传
     */
    public ResFileDTO uploadInputStream(InputStream inputStream, String fileName) {
        try {
            String fileType = FileUtil.getFileTypeByName(fileName);
            String uuidFileName = FileUtil.makeNewFileName(fileName, fileType);
            String key = qnConfig.getDiskName() + uuidFileName;
            this.makeUploadManager(key);
            // 默认不指定key的情况下，以文件内容的hash值作为文件名
//            String key = fileName;
            Response response = uploadManager.put(inputStream, key, upToken, null, null);
            MyPutRet myPutRet = response.jsonToObject(MyPutRet.class);
            String fileUrl = qnConfig.getUrl() + myPutRet.key;
            return new ResFileDTO(fileUrl, fileName, key, fileType, myPutRet.fsize, myPutRet.hash);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过文件上传
     */
    public ResFileDTO uploadFile(File file) {
        try {
            String fileName = file.getName();
            String fileType = FileUtil.getFileTypeByName(fileName);
            String uuidFileName = FileUtil.makeNewFileName(fileName, fileType);
            String key = qnConfig.getDiskName() + uuidFileName;
            this.makeUploadManager(key);
            // 默认不指定key的情况下，以文件内容的hash值作为文件名
//            String key = file.getName();
            Response response = uploadManager.put(file, key, upToken);
            MyPutRet myPutRet = response.jsonToObject(MyPutRet.class);
            String fileUrl = qnConfig.getUrl() + myPutRet.key;
            return new ResFileDTO(fileUrl, fileName, key, fileType, myPutRet.fsize, myPutRet.hash);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过网络文件上传
     */
    public ResFileDTO uploadFilePath(String filePath) {
        try {
            String fileName = FileUtil.getFileName(filePath);
            String fileType = FileUtil.getFileTypeByPath(filePath);
            String uuidFileName = FileUtil.makeNewFileName(fileName, fileType);
            String key = qnConfig.getDiskName() + uuidFileName;
            this.makeUploadManager(key);
            // 默认不指定key的情况下，以文件内容的hash值作为文件名
//            String key = FileUtil.getFileName(filePath);
            Response response = uploadManager.put(filePath, key, upToken);
            MyPutRet myPutRet = response.jsonToObject(MyPutRet.class);
            String fileUrl = qnConfig.getUrl() + myPutRet.key;
            return new ResFileDTO(fileUrl, fileName, key, fileType, myPutRet.fsize, myPutRet.hash);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过MultipartFile上传
     */
    public ResFileDTO uploadMultipartFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String fileType = FileUtil.getFileTypeByName(fileName);
            String uuidFileName = FileUtil.makeNewFileName(fileName, fileType);
            String key = qnConfig.getDiskName() + uuidFileName;
            this.makeUploadManager(key);
            // 默认不指定key的情况下，以文件内容的hash值作为文件名
//            String key = file.getOriginalFilename();
            Response response = uploadManager.put(file.getInputStream(), key, upToken, null, null);
            MyPutRet myPutRet = response.jsonToObject(MyPutRet.class);
            String fileUrl = qnConfig.getUrl() + myPutRet.key;
            return new ResFileDTO(fileUrl, fileName, key, fileType, myPutRet.fsize, myPutRet.hash);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param key 文件key
     */
    public void deleteFile(String key) {
        Configuration cfg = getConf();
        Auth auth = getAuth();
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(qnConfig.getBucket(), key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }

}
