package project.learning_managment_system.learning_managment_system_dev.TestContext.Services;

import org.springframework.stereotype.Service;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Dto.Test_Attempt_Dto;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Dto.Test_Dto;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Entities.AnswerOption;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Entities.Questions;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Entities.Test;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Entities.TestAttempt;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Exceptions.Answer_Exception;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Exceptions.Attemption_Exception;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Exceptions.Questions_Exception;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Exceptions.Test_Exception;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Mappers.Imp.Mapper_Test_Attempt;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Repository.Answer_Repo;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Repository.Questions_Repo;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Repository.Test_Attempt_Repo;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Repository.Test_Repo;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Entities.Student;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Exceptions.CustomesException.UserNotFound;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Repositories.Student_Repo;

import java.awt.event.AWTEventListenerProxy;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ServiceTestAttempt {
    public Mapper_Test_Attempt mapperTestAttempt;
    public Test_Attempt_Repo testAttemptRepo;
    public Questions_Repo questionsRepo;
    public Answer_Repo answerRepo;
    public Test_Repo testRepo;
    public Student_Repo studentRepo;
    public ServiceTestAttempt(Test_Attempt_Repo attemptRepo,Test_Repo test_repo
    ,Questions_Repo questions_repo,Answer_Repo answer_repo,Student_Repo repo
    ){
        this.testAttemptRepo=attemptRepo;
        this.mapperTestAttempt=new Mapper_Test_Attempt();
        this.testRepo=test_repo;
        this.answerRepo=answer_repo;
        this.questionsRepo=questions_repo;
        this.studentRepo=repo;
    }
    public List<Test_Attempt_Dto> getAllAttemptions(String email){
        Student student=this.studentRepo.findByMail(email).orElseThrow(()-> new UserNotFound("Student not found"));
        return this.testAttemptRepo.findByUserId(student.getId())
                .stream().map(mapperTestAttempt::toDto)
                .collect(Collectors.toList());
    }
    public List<Test_Attempt_Dto> getAttemptionofTest(String mail,int testId){
        Student student=this.studentRepo.findByMail(mail).orElseThrow(()-> new UserNotFound("Student not found"));
        return this.testAttemptRepo.findByUserIdAndTest_Id(student.getId(),testId)
                .stream().map(mapperTestAttempt::toDto)
                .collect(Collectors.toList());
    }
    public Test_Attempt_Dto getTestAttemption(int id ){
        return this.mapperTestAttempt.toDto(this.testAttemptRepo.findById(id).orElseThrow(()->new Attemption_Exception("Attemption not found")));
    }
    public Test_Attempt_Dto createAttemption(int testId,String email ){
        int max_score=0;
        TestAttempt testAttempt=new TestAttempt();
        Student student=this.studentRepo.findByMail(email).orElseThrow(()-> new UserNotFound("Student not found"));
        Test test =this.testRepo.findById(testId).orElseThrow(()-> new Test_Exception("Test not found"));
        testAttempt.setMessage("your score for "+test.getTitle()+":");
        List<Questions> questions=test.getQuestions();
        for(int i=0;i<questions.size();i++){
            max_score+=questions.get(i).getScoreQuestion();
        }
        testAttempt.setMax_score(max_score);
        testAttempt.setTest(test);
        testAttempt.setUserId(student.getId());
        return this.mapperTestAttempt.toDto(this.testAttemptRepo.save(testAttempt));
    }
    public Test_Attempt_Dto submitTest(Map<Integer,Integer> selected_options, int id){
        int total_score=0;
        TestAttempt testAttempt=this.testAttemptRepo.findById(id).orElseThrow(()-> new Attemption_Exception("Attemption not found"));
        for(Map.Entry<Integer,Integer> entry : selected_options.entrySet()){
            int questionId=entry.getKey();
            int optionId= entry.getValue();
            Questions questions=this.questionsRepo.findById(questionId).orElseThrow(()->new Questions_Exception("Question not found "));
            AnswerOption answerOption=this.answerRepo.findById(optionId).orElseThrow(()->new Answer_Exception("Answer not found "));
            boolean isCorrect= answerOption.isIsfalse();
            int score_question=!isCorrect?questions.getScoreQuestion():0;
            total_score+=score_question;
        }
        testAttempt.setScore(total_score);
        return this.mapperTestAttempt.toDto(this.testAttemptRepo.save(testAttempt));
    }
    public void deleteAttempt(int id){
        this.testAttemptRepo.deleteById(id);
    }
}
