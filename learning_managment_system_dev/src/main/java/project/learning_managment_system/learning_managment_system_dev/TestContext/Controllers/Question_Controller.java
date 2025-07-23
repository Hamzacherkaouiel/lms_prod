package project.learning_managment_system.learning_managment_system_dev.TestContext.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Dto.Questions_Dto;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Entities.Questions;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Services.ServiceQuestions;

import java.util.List;

@RestController
@RequestMapping("/Questions")
public class Question_Controller {
    public ServiceQuestions serviceQuestions;
    public Question_Controller(ServiceQuestions questions){
        this.serviceQuestions=questions;
    }
    @GetMapping("/{testId}/test")
    @PreAuthorize("hasAnyRole('teacher','student','admin')")
    public ResponseEntity<List<Questions_Dto>> getQuestionByTest(@PathVariable int testId){
        return ResponseEntity.ok(this.serviceQuestions.getQuestionsByTest(testId));
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('teacher','student','admin')")
    public ResponseEntity<Questions_Dto> getQuestionById(@PathVariable int id){
        return ResponseEntity.ok(this.serviceQuestions.getSingleQuestion(id));
    }
    @PostMapping("/{testId}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<Questions_Dto> createQuestion(@RequestBody Questions_Dto questionsDto,@PathVariable int testId){
        return ResponseEntity.status(201).body(this.serviceQuestions.createQuestion(questionsDto, testId));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<Questions_Dto> updateQuestion(@RequestBody Questions_Dto questionsDto,@PathVariable int id){
        return ResponseEntity.status(201).body(this.serviceQuestions.updateQuestion(questionsDto, id));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity deleteQuestion(@PathVariable int id){
        this.serviceQuestions.deleteQuestion(id);
        return ResponseEntity.status(204).build();
    }
}
