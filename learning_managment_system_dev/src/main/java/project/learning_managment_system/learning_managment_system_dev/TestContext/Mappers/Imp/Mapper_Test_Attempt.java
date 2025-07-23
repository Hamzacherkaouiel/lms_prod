package project.learning_managment_system.learning_managment_system_dev.TestContext.Mappers.Imp;

import project.learning_managment_system.learning_managment_system_dev.TestContext.Dto.Test_Attempt_Dto;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Entities.TestAttempt;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Mappers.Mapper_Interface;

public class Mapper_Test_Attempt implements Mapper_Interface<Test_Attempt_Dto, TestAttempt> {
    @Override
    public Test_Attempt_Dto toDto(TestAttempt entity) {
        return Test_Attempt_Dto.builder()
                .id(entity.getId())
                .score(entity.getScore())
                .max_score(entity.getMax_score())
                .message(entity.getMessage())
                .build();
    }

    @Override
    public TestAttempt toEntity(Test_Attempt_Dto dto) {
        return TestAttempt.builder()
                .message(dto.getMessage())
                .build();
    }

    @Override
    public void updateEntity(Test_Attempt_Dto dto, TestAttempt entity) {
        entity.setMessage(dto.getMessage()!=null? dto.getMessage() : entity.getMessage());
    }
}
