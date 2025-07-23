package project.learning_managment_system.learning_managment_system_dev.course_managment.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Dto.Full_Lessons_Dto;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Dto.Simple_Lessons_Dto;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Services.Impl.Service_Lessons;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lessons")
public class Lessons_Controller {
    public Service_Lessons serviceLessons;

    public Lessons_Controller(Service_Lessons lessons){
        this.serviceLessons = lessons;
    }

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('teacher','admin','student')")
    public ResponseEntity<List<Simple_Lessons_Dto>> getLessons(){
        return ResponseEntity.ok(this.serviceLessons.getAllData());
    }

    @GetMapping("/{id}/simple")
    @PreAuthorize("hasAnyRole('teacher','admin','student')")
    public ResponseEntity<Simple_Lessons_Dto> getSimpleLesson(@PathVariable int id){
        return ResponseEntity.ok(this.serviceLessons.getSingleData(id));
    }

    @GetMapping("/{id}/full")
    @PreAuthorize("hasAnyRole('teacher','admin','student')")
    public ResponseEntity<Full_Lessons_Dto> getFullLesson(@PathVariable int id){
        return ResponseEntity.ok(this.serviceLessons.getLessonWithVideo(id));
    }

    @GetMapping("/module/{id}")
    @PreAuthorize("hasAnyRole('teacher','admin','student')")
    public ResponseEntity<List<Simple_Lessons_Dto>> getLessonsByModules(@PathVariable int id ){
        return ResponseEntity.ok(this.serviceLessons.getLessonsByModules(id));
    }

    @PostMapping("/module/{id}/simple")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<Simple_Lessons_Dto> createSimpleLesson(@RequestBody Simple_Lessons_Dto lessonsDto, @PathVariable int id){
        return ResponseEntity.status(201)
                .body(this.serviceLessons.createData(lessonsDto, id));
    }

    @PostMapping("/module/{id}/full")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<Map<String, String>> createFullLessons(@RequestBody Full_Lessons_Dto lessonsDto, @PathVariable int id, @RequestParam(required = false) String filename){
        return ResponseEntity.status(201)
                .body(this.serviceLessons.createLessonWithVideo(lessonsDto, id, filename));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<Simple_Lessons_Dto> updateLessons(@RequestBody Simple_Lessons_Dto lessonsDto, @PathVariable int id){
        return ResponseEntity.ok(this.serviceLessons.updateData(lessonsDto, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<String> deleteLessons(@PathVariable int id){
        this.serviceLessons.deleteData(id);
        return ResponseEntity.status(204).body("LESSON DELETED");
    }
}
