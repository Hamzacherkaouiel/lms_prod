package project.learning_managment_system.learning_managment_system_dev.user_managment.mappers.Implementation;

import org.springframework.stereotype.Component;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.Student_Dto;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.UserCreation;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Entities.Student;
import project.learning_managment_system.learning_managment_system_dev.user_managment.mappers.Mapper_Interface;
@Component
public class Student_Mapper implements Mapper_Interface<Student_Dto, Student> {
    @Override
    public Student_Dto toDto(Student student) {
        return Student_Dto.builder()
                .id(student.getId())
                .firstname(student.getFirstname())
                .lastname(student.getLastname())
                .mail(student.getMail())
                .progress(student.getProgress())
                .build();
    }

    @Override
    public Student toEntity(Student_Dto student) {
        return Student.builder()
                .firstname(student.getFirstname())
                .lastname(student.getLastname())
                .mail(student.getMail())
                .progress(student.getProgress())
                .build();
    }

    @Override
    public Student Creation(UserCreation user) {
        return Student.builder()
                .mail(user.getMail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())                .password(user.getPassword())
                .build();
    }

    @Override
    public void updateEntityFromDto(Student_Dto dto, Student entity) {
        entity.setProgress(dto.getProgress()!=null? dto.getProgress() : entity.getProgress());
        entity.setMail(dto.getMail()!=null? dto.getMail() : entity.getMail());
        entity.setFirstname(dto.getFirstname()!=null? dto.getFirstname() : entity.getFirstname());
        entity.setLastname(dto.getLastname()!=null? dto.getLastname() : entity.getLastname());
    }
}
