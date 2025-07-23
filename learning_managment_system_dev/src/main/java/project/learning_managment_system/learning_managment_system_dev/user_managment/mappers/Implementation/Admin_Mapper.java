package project.learning_managment_system.learning_managment_system_dev.user_managment.mappers.Implementation;

import org.springframework.stereotype.Component;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.Admin_Dto;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.UserCreation;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Entities.Admin;
import project.learning_managment_system.learning_managment_system_dev.user_managment.mappers.Mapper_Interface;
@Component
public class Admin_Mapper implements Mapper_Interface<Admin_Dto,Admin> {

    @Override
    public Admin_Dto toDto(Admin admin) {
        return Admin_Dto.builder()
                .id(admin.getId())
                .mail(admin.getMail())
                .firstname(admin.getFirstname())
                .lastname(admin.getLastname())
                .build();

    }

    @Override
    public Admin toEntity(Admin_Dto adminDto) {
        return Admin.builder()
                .mail(adminDto.getMail())
                .firstname(adminDto.getFirstname())
                .lastname(adminDto.getLastname())
                .build();
    }

    @Override
    public Admin Creation(UserCreation user) {
        return Admin.builder()
                .mail(user.getMail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .password(user.getPassword())
                .build();
    }

    @Override
    public void updateEntityFromDto(Admin_Dto dto, Admin entity) {
        entity.setMail(dto.getMail()!=null? dto.getMail() : entity.getMail());
        entity.setFirstname(dto.getFirstname()!=null? dto.getFirstname() : entity.getFirstname());
        entity.setLastname(dto.getLastname()!=null? dto.getLastname() : entity.getLastname());
    }
}
