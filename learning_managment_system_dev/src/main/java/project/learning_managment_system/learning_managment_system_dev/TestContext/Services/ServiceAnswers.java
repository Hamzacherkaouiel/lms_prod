package project.learning_managment_system.learning_managment_system_dev.TestContext.Services;

import org.springframework.stereotype.Service;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Dto.Answer_Dto;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Entities.AnswerOption;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Entities.Questions;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Exceptions.Answer_Exception;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Mappers.Imp.Mapper_Answers;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Repository.Answer_Repo;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceAnswers {
    public Mapper_Answers mapperAnswers;
    public Answer_Repo answerRepo;
    public ServiceAnswers(Answer_Repo answer_repo){
        this.mapperAnswers=new Mapper_Answers();
        this.answerRepo=answer_repo;
    }
    public List<Answer_Dto> getOptionsBYQuestion(int id){
        return this.answerRepo.findByQuestions_Id(id)
                .stream().map(mapperAnswers::toDto).collect(Collectors.toList());
    }
    public Answer_Dto getSingleAnswer(int id){
        return this.mapperAnswers.toDto(this.answerRepo.findById(id).orElseThrow(()-> new Answer_Exception("Answer not found")));
    }
    public Answer_Dto createAnswer(Answer_Dto answerDto,int id){
        AnswerOption answerOption=this.mapperAnswers.toEntity(answerDto);
        answerOption.setQuestions(Questions.builder()
                        .id(id)
                .build());
        return this.mapperAnswers.toDto(this.answerRepo.save(answerOption));
    }
    public Answer_Dto updateAnswer(Answer_Dto answerDto,int id){
        AnswerOption answerOption=this.answerRepo.findById(id).orElseThrow(()->new Answer_Exception("Answer not found"));
        this.mapperAnswers.updateEntity(answerDto,answerOption);
        return this.mapperAnswers.toDto(this.answerRepo.save(answerOption));
    }
    public void deleteAnswer(int id){
        this.answerRepo.deleteById(id);
    }
}
