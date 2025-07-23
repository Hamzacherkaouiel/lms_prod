package project.learning_managment_system.learning_managment_system_dev.TestContext.Services;

import org.springframework.stereotype.Service;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Dto.Questions_Dto;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Entities.Questions;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Entities.Test;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Exceptions.Questions_Exception;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Mappers.Imp.Mapper_Questions;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Repository.Questions_Repo;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceQuestions {
    public Mapper_Questions mapperQuestions;
    public Questions_Repo questionsRepo;
    public ServiceQuestions(Questions_Repo questions_repo){
        this.mapperQuestions=new Mapper_Questions();
        this.questionsRepo=questions_repo;
    }
    public List<Questions_Dto> getQuestionsByTest(int id){
        return this.questionsRepo.findByQuiz_Id(id)
                .stream().map(mapperQuestions::toDto)
                .collect(Collectors.toList());
    }
    public Questions_Dto getSingleQuestion(int id ){
        return this.mapperQuestions.toDto(this.questionsRepo.findById(id)
                .orElseThrow(()-> new Questions_Exception("Question not found"))
        );
    }
    public Questions_Dto createQuestion(Questions_Dto questionsDto,int id){
        Questions questions=this.mapperQuestions.toEntity(questionsDto);
        questions.setQuiz(Test.builder()
                        .id(id)
                .build());
        return this.mapperQuestions.toDto(this.questionsRepo.save(questions));
    }
    public Questions_Dto updateQuestion(Questions_Dto questionsDto,int id){
        Questions questions=this.questionsRepo.findById(id).orElseThrow(()->new Questions_Exception("Question not found"));
        this.mapperQuestions.updateEntity(questionsDto,questions);
        return this.mapperQuestions.toDto(this.questionsRepo.save(questions));
    }
    public void deleteQuestion(int id){
        this.questionsRepo.deleteById(id);
    }
}
