package project.learning_managment_system.learning_managment_system_dev.course_managment.Mappers.Impl;

import project.learning_managment_system.learning_managment_system_dev.course_managment.Dto.Full_Lessons_Dto;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Dto.Simple_Lessons_Dto;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Lessons;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Mappers.Mapper_Interface;

public class Mapper_Full_Lessons implements Mapper_Interface<Full_Lessons_Dto, Lessons> {
    @Override
    public Full_Lessons_Dto toDto(Lessons entity) {
        return Full_Lessons_Dto.builder()
                .description(entity.getDescription())
                .id(entity.getLessons_id())
                .module_id(entity.getModule().getId())
                .s3Url(entity.getS3Url())
                .objectKey(entity.getObjectKey())
                .contentType(entity.getContentType())
                .build();
    }

    @Override
    public Lessons toEntity(Full_Lessons_Dto dto) {
        return Lessons.builder()
                .description(dto.getDescription())
                .contentType(dto.getContentType())
                .build();
    }

    @Override
    public void updateFields(Full_Lessons_Dto dto, Lessons entity) {
        entity.setDescription(dto.getDescription()!=null? dto.getDescription() : entity.getDescription());
        entity.setS3Url(dto.getS3Url()!=null? dto.getS3Url() : entity.getS3Url());
    }
}
