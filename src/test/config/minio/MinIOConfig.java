package com.heima.common.minio;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(MinIOProperties.class)
@Import(MinIOService.class)
// 当微服务的配置文件中包含 minio ，且 enable 为 true 时才进行加载
@ConditionalOnProperty(prefix = "minio", value = "enable", havingValue = "true")
public class MinIOConfig {

    @Bean
    public MinioClient minioClient(MinIOProperties minIOProperties) throws InvalidPortException, InvalidEndpointException {
        // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minIOProperties.getEndpoint())
                .credentials(minIOProperties.getAccessKey(),minIOProperties.getSecretKey())
                .build();
        return minioClient;
    }
}