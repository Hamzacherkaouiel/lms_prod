package project.learning_managment_system.learning_managment_system_dev.user_managment.KafkaConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.UserCreation;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.UserDTO;

@Service
@RequiredArgsConstructor
public class Producer {
    private final KafkaTemplate<String,String> kafkaTemplate;

    public void syncData(UserCreation userDTO){
        Message<UserCreation> message= MessageBuilder
                .withPayload(userDTO)
                .setHeader(KafkaHeaders.TOPIC,"keycloak")
                .build();
        kafkaTemplate.send(message);
    }


}
