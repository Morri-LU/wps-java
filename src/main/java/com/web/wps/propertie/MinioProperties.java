package com.web.wps.propertie;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    private String fileUrlPrefix;
    private String bucketName;
    private String diskName;
    private String regionId;
    private String endpoint;
    private String accessKey;
    private String accessSecret;

}
