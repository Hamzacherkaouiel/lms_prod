package project.learning_managment_system.learning_managment_system_dev.user_managment.mappers;

import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.Admin_Dto;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.UserCreation;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Entities.Admin;

public interface Mapper_Interface<T,B> {
    public T toDto(B data);


    public B toEntity(T data);
    public B Creation(UserCreation user);
    public void updateEntityFromDto(T dto,B entity);
}
