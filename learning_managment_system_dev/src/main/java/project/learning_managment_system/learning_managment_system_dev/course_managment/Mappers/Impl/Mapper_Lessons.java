package project.learning_managment_system.learning_managment_system_dev.course_managment.Mappers.Impl;

import project.learning_managment_system.learning_managment_system_dev.course_managment.Dto.Lessons_Dto;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Lessons;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Mappers.Mapper_Interface;

public class Mapper_Lessons implements Mapper_Interface<Lessons_Dto,Lessons> {
    @Override
    public Lessons_Dto toDto(Lessons entity) {
        return Lessons_Dto.builder()
                .description(entity.getDescription())
                .id(entity.getLessons_id())
                .module_id(entity.getModule().getId())
                .build();
    }

    @Override
    public Lessons toEntity(Lessons_Dto dto) {
        return Lessons.builder()
                .description(dto.getDescription())
                .build();
    }

    @Override
    public void updateFields(Lessons_Dto dto, Lessons entity) {
        entity.setDescription(dto.getDescription()!=null? dto.getDescription() : entity.getDescription());

    }
}
