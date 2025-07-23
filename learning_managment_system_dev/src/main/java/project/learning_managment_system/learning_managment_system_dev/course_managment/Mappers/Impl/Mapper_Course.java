package project.learning_managment_system.learning_managment_system_dev.course_managment.Mappers.Impl;

import project.learning_managment_system.learning_managment_system_dev.course_managment.Dto.Course_Dto;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Course;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Mappers.Mapper_Interface;

public class Mapper_Course implements Mapper_Interface<Course_Dto, Course> {
    @Override
    public Course_Dto toDto(Course entity) {
        return Course_Dto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .capacity(entity.getCapacity())
                .teacher_id(entity.getTeacher().getId())
                .build();
    }

    @Override
    public Course toEntity(Course_Dto dto) {
        return Course.builder()
                .title(dto.getTitle())
                .capacity(dto.getCapacity())
                .description(dto.getDescription())
                .build();
    }

    @Override
    public void updateFields(Course_Dto dto, Course entity) {
        entity.setTitle(dto.getTitle()!=null? dto.getTitle() : entity.getTitle());
        entity.setCapacity(dto.getCapacity()!=0? dto.getCapacity() : entity.getCapacity());
        entity.setDescription(dto.getDescription()!=null? dto.getDescription() : entity.getDescription());
    }
}
