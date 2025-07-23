package project.learning_managment_system.learning_managment_system_dev.TestContext.Mappers.Imp;

import project.learning_managment_system.learning_managment_system_dev.TestContext.Dto.Answer_Dto;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Entities.AnswerOption;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Mappers.Mapper_Interface;

public class Mapper_Answers implements Mapper_Interface<Answer_Dto, AnswerOption> {

    @Override
    public Answer_Dto toDto(AnswerOption entity) {
        return Answer_Dto.builder()
                .id(entity.getId())
                .answer(entity.getAnswer())
                .isfalse(entity.isIsfalse())
                .build();
    }

    @Override
    public AnswerOption toEntity(Answer_Dto dto) {
        return AnswerOption.builder()

                .answer(dto.getAnswer())
                .isfalse(dto.isIsfalse())
                .build();
    }

    @Override
    public void updateEntity(Answer_Dto dto, AnswerOption entity) {
       entity.setAnswer(dto.getAnswer()!=null? dto.getAnswer() : entity.getAnswer());
       entity.setIsfalse(dto.isIsfalse());
    }
}
