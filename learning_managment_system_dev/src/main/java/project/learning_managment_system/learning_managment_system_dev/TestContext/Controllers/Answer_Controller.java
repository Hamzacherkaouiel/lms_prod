package project.learning_managment_system.learning_managment_system_dev.TestContext.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Dto.Answer_Dto;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Services.ServiceAnswers;

import java.util.List;

@RestController
@RequestMapping("/Options")
public class Answer_Controller {
    public ServiceAnswers serviceAnswers;
    public Answer_Controller(ServiceAnswers answers){
        this.serviceAnswers=answers;
    }
    @GetMapping("/{questionId}/questions")
    @PreAuthorize("hasAnyRole('teacher','student','admin')")
    public ResponseEntity<List<Answer_Dto>> getOptionsByQuestion(@PathVariable int questionId){
        return ResponseEntity.ok(this.serviceAnswers.getOptionsBYQuestion(questionId));
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('teacher','student','admin')")
    public ResponseEntity<Answer_Dto> getOptionsById(@PathVariable int id){
        return ResponseEntity.ok(this.serviceAnswers.getSingleAnswer(id));
    }
    @PostMapping("/{id}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<Answer_Dto> createAnswer(@RequestBody Answer_Dto answerDto,@PathVariable int id){
        return ResponseEntity.status(201).body(this.serviceAnswers.createAnswer(answerDto, id));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<Answer_Dto> updateAnswer(@RequestBody Answer_Dto answerDto,@PathVariable int id){
        return ResponseEntity.status(201).body(this.serviceAnswers.updateAnswer(answerDto, id));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity deleteAnswer(@PathVariable int id ){
        this.serviceAnswers.deleteAnswer(id);
        return ResponseEntity.status(204).build();
    }


}
