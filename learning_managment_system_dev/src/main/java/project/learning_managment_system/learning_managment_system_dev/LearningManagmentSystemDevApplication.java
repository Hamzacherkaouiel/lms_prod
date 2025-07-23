package project.learning_managment_system.learning_managment_system_dev;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import project.learning_managment_system.learning_managment_system_dev.NotificationContext.EmailNotification.EmailSenderService;

@SpringBootApplication
public class LearningManagmentSystemDevApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningManagmentSystemDevApplication.class, args);

	}


}
