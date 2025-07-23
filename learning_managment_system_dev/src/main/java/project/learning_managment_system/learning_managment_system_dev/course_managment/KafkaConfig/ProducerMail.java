package project.learning_managment_system.learning_managment_system_dev.course_managment.KafkaConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Dto.Enrollment_Dto;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Enrollements;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.UserCreation;

@Service
@RequiredArgsConstructor
public class ProducerMail {
    private final KafkaTemplate<String,Enrollment_Dto> kafkaTemplate;

    public void sendMail(Enrollment_Dto enrollements){
        Message<Enrollment_Dto> message= MessageBuilder
                .withPayload(enrollements)
                .setHeader(KafkaHeaders.TOPIC,"mail")
                .build();
        kafkaTemplate.send(message);
    }
}
