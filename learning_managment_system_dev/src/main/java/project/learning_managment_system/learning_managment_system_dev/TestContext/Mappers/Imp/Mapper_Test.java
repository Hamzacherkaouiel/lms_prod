package project.learning_managment_system.learning_managment_system_dev.TestContext.Mappers.Imp;

import project.learning_managment_system.learning_managment_system_dev.TestContext.Dto.Test_Dto;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Entities.Test;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Mappers.Mapper_Interface;

import java.util.stream.Collectors;

public class Mapper_Test implements Mapper_Interface<Test_Dto, Test> {


    @Override
    public Test_Dto toDto(Test entity) {
        return Test_Dto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .build();
    }

    @Override
    public Test toEntity(Test_Dto dto) {
        return Test.builder()
                .title(dto.getTitle())
                .build();
    }

    @Override
    public void updateEntity(Test_Dto dto, Test entity) {
        entity.setTitle(dto.getTitle()!=null? dto.getTitle() : entity.getTitle());

    }
}
