package project.learning_managment_system.learning_managment_system_dev.ContentContext.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.learning_managment_system.learning_managment_system_dev.ContentContext.Config.StorageConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Map;

@Service
public class StorageService {


    @Value("${application.bucket.name}")
    private String bucketName;
    @Autowired
    public AmazonS3 s3Client;
    public Map<String,String> generatePresignedUploadUrl(String file) {
        Date expiration = new Date(System.currentTimeMillis() +15* 60 * 1000L);
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, file)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);

        URL url = s3Client.generatePresignedUrl(request);
        return Map.of(
                "uploadUrl", url.toString(),
                "objectKey", file
        );
    }
    public String deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
        return fileName + " removed ...";
    }
    public String saveUrl(String filename){
        return "https://s3." + s3Client.getRegionName() + ".amazonaws.com/" + bucketName + "/" + filename;
    }
    public String objectkey(String filename){
        return System.currentTimeMillis() + "_" + filename;
    }


}
