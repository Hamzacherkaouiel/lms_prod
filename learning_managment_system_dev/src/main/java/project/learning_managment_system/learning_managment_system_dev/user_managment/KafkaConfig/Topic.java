package project.learning_managment_system.learning_managment_system_dev.user_managment.KafkaConfig;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class Topic {
    @Bean
    public NewTopic keycloakTopic(){

        return TopicBuilder.name("keycloak").build();
    }
}
