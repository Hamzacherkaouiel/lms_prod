package project.learning_managment_system.learning_managment_system_dev.ContentContext.Config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentials;

@Configuration
public class StorageConfig {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String accessSecret;
    @Value("${cloud.aws.region.static}")
    private String region;
    @Bean
    public AmazonS3 clientS3(){
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);
        return AmazonS3Client.builder()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .withPathStyleAccessEnabled(true)
                .build();

    }
}
