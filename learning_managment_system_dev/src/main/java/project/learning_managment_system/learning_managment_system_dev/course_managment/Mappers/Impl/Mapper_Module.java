package project.learning_managment_system.learning_managment_system_dev.course_managment.Mappers.Impl;

import project.learning_managment_system.learning_managment_system_dev.course_managment.Dto.Modules_Dto;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Modules;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Mappers.Mapper_Interface;

public class Mapper_Module implements Mapper_Interface<Modules_Dto, Modules> {
    @Override
    public Modules_Dto toDto(Modules entity) {
        return Modules_Dto.builder()
                .title(entity.getTitle())
                .id(entity.getId())
                .course_id(entity.getCourse().getId())
                .build();
    }

    @Override
    public Modules toEntity(Modules_Dto dto) {
        return Modules.builder()
                .title(dto.getTitle())
                .build();
    }

    @Override
    public void updateFields(Modules_Dto dto, Modules entity) {
        entity.setTitle(dto.getTitle()!=null? dto.getTitle() : entity.getTitle());

    }
}
