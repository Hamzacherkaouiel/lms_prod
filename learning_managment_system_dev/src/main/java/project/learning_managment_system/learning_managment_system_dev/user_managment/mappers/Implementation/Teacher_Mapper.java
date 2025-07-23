package project.learning_managment_system.learning_managment_system_dev.user_managment.mappers.Implementation;

import org.springframework.stereotype.Component;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.Teacher_Dto;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.UserCreation;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Entities.Teacher;
import project.learning_managment_system.learning_managment_system_dev.user_managment.mappers.Mapper_Interface;
@Component
public class Teacher_Mapper implements Mapper_Interface<Teacher_Dto, Teacher> {
    @Override
    public Teacher_Dto toDto(Teacher entity) {
        return Teacher_Dto.builder()
                .id(entity.getId())
                .mail(entity.getMail())
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .speciality(entity.getSpeciality()).build();
    }

    @Override
    public Teacher toEntity(Teacher_Dto entity) {
        return Teacher.builder()
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .mail(entity.getMail())
                .speciality(entity.getSpeciality())
                .build();
    }

    @Override
    public Teacher Creation(UserCreation user) {
        return Teacher.builder()
                .mail(user.getMail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .password(user.getPassword())
                .speciality(user.getSpeciality())
                .build();
    }

    @Override
    public void updateEntityFromDto(Teacher_Dto dto, Teacher entity) {
        entity.setSpeciality(dto.getSpeciality()!=null? dto.getSpeciality() : entity.getSpeciality());
        entity.setMail(dto.getMail()!=null? dto.getMail() : entity.getMail());
        entity.setFirstname(dto.getFirstname()!=null? dto.getFirstname() : entity.getFirstname());
        entity.setLastname(dto.getLastname()!=null? dto.getLastname() : entity.getLastname());
    }
}
