package project.learning_managment_system.learning_managment_system_dev.NotificationContext.EmailNotification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Dto.Enrollment_Dto;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Enrollements;

@Service
public class EmailSenderService {
    @Value("${hamza.address}")
    public String company_mail;
    @Autowired
    private JavaMailSender mailSender;

    private void sendSimpleEmail(String toEmail,
                                String subject,
                                String body
    ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(company_mail);
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        System.out.println("Mail Send...");
    }
    public void notifying(Enrollment_Dto enrollements){
        String subject="Confirmation d'inscription au cours : "+enrollements.getCourse().getTitle();
        String message = String.format(
                "Bonjour %s,\n\n" +
                        "Nous vous confirmons que vous avez été inscrit avec succès au cours intitulé : %s.\n\n" +
                        "📅 Date d’inscription : %s\n" +
                        "🎓 Intitulé du cours : %s\n" +
                        "📘 Description : %s\n\n" +
                        "Nous vous souhaitons une excellente expérience d’apprentissage !\n\n" +
                        "Cordialement,\n" +
                        "L’équipe de gestion des cours",
                enrollements.getStudent().getFirstname(),
                enrollements.getCourse().getTitle(),
                enrollements.getEnrollmentDate(),
                enrollements.getCourse().getTitle(),
                enrollements.getCourse().getDescription()
        );

        this.sendSimpleEmail(enrollements.getStudent().getMail(),subject,message );
    }

}

