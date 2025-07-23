package project.learning_managment_system.learning_managment_system_dev.TestContext.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Dto.Test_Attempt_Dto;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Services.ServiceTestAttempt;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Attemption")
public class TestAttempt_Controller {
    public ServiceTestAttempt serviceTestAttempt;
    public TestAttempt_Controller(ServiceTestAttempt attempt){
        this.serviceTestAttempt=attempt;
    }
    @GetMapping("/{email}/student")
    @PreAuthorize("hasRole('student')")
    public ResponseEntity<List<Test_Attempt_Dto>> getAllAttemptions(@PathVariable String email ){
        return ResponseEntity.ok(this.serviceTestAttempt.getAllAttemptions(email));
    }
    @GetMapping("/{email}/student/{testId}/test")
    @PreAuthorize("hasRole('student')")
    public ResponseEntity<List<Test_Attempt_Dto>> getAllAttemptionsByTest(@PathVariable String email,@PathVariable int testId ){
        return ResponseEntity.ok(this.serviceTestAttempt.getAttemptionofTest(email,testId));
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('student')")
    public ResponseEntity<Test_Attempt_Dto> getSingleAttemption(@PathVariable int id ){
        return ResponseEntity.ok(this.serviceTestAttempt.getTestAttemption(id));
    }
    @PostMapping("/{testId}/student/{email}")
    @PreAuthorize("hasRole('student')")
    public ResponseEntity<Test_Attempt_Dto> startTest(@PathVariable int testId,@PathVariable String email){
        return ResponseEntity.status(201).body(this.serviceTestAttempt.createAttemption(testId, email));
    }
    @PostMapping("/{attemptionId}")
    @PreAuthorize("hasRole('student')")
    public ResponseEntity<Test_Attempt_Dto> submitTest(@PathVariable int attemptionId,@RequestBody Map<Integer,Integer> answers){
        return ResponseEntity.status(201).body(this.serviceTestAttempt.submitTest(answers,attemptionId));
    }
    @DeleteMapping("/{attemptionId}")
    @PreAuthorize("hasRole('student')")
    public ResponseEntity<Test_Attempt_Dto> deleteAttemption(@PathVariable int attemptionId){
        this.serviceTestAttempt.deleteAttempt(attemptionId);
        return ResponseEntity.status(204).build();
    }



}
