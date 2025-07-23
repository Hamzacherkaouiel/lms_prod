package project.learning_managment_system.learning_managment_system_dev.course_managment.KafkaConfig;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
@Configuration
public class TopicMail {
    @Bean
    public NewTopic MailTopic(){

        return TopicBuilder.name("mail").build();
    }
}
