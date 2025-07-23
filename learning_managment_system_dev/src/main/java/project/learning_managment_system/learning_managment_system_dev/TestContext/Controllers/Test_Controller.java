package project.learning_managment_system.learning_managment_system_dev.TestContext.Controllers;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Dto.Test_Dto;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Entities.Test;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Services.ServiceTest;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Test")
public class Test_Controller {
    public ServiceTest serviceTest;
    public Test_Controller(ServiceTest test){
        this.serviceTest=test;
    }
    @GetMapping("/{courseId}/course")
    @PreAuthorize("hasAnyRole('teacher','admin','student')")
    public ResponseEntity<Test_Dto> getTestByCourseId(@PathVariable int courseId){
        Optional<Test_Dto> testDto = serviceTest.getTestByCourseId(courseId);
        return testDto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('teacher','admin','student')")
    public ResponseEntity<Test_Dto> getTestById(@PathVariable int id){
        return ResponseEntity.ok(this.serviceTest.getTestById(id));
    }
    @PostMapping("/{id}/student")
    @PreAuthorize("hasAnyRole('teacher','admin','student')")
    public ResponseEntity<Test_Dto> startTest(@PathVariable int id){
        return ResponseEntity.ok(this.serviceTest.startTest(id));
    }
    @PostMapping("/{courseId}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<Test_Dto> createTest(@RequestBody Test_Dto testDto,@PathVariable int courseId){
        return ResponseEntity.status(201).body(this.serviceTest.createTest(testDto,courseId));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<Test_Dto> updateTest(@RequestBody Test_Dto testDto,@PathVariable int id){
        return ResponseEntity.ok(this.serviceTest.updateTest(testDto, id));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity deleteTest(@PathVariable int id){
        this.serviceTest.deleteTest(id);
        return ResponseEntity.status(204).build();
    }


}
