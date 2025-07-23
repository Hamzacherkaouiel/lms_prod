package project.learning_managment_system.learning_managment_system_dev.NotificationContext.EmailNotification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Dto.Enrollment_Dto;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Enrollements;

@Service
public class MailConsummer {
    @Autowired
    public EmailSenderService senderService;
    @KafkaListener(topics = "mail",groupId = "keycloak-id")
    public void consumme(Enrollment_Dto enrollements){
        this.senderService.notifying(enrollements);
    }
}
