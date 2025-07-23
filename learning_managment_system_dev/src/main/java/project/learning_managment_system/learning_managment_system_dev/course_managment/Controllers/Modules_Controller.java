package project.learning_managment_system.learning_managment_system_dev.course_managment.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Dto.Modules_Dto;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Services.Impl.Service_Module;

import java.util.List;

@RestController
@RequestMapping("/modules")
public class Modules_Controller {

    public Service_Module serviceModule;

    public Modules_Controller(Service_Module module) {
        this.serviceModule = module;
    }

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('teacher','admin','student')")
    public ResponseEntity<List<Modules_Dto>> getModules() {
        return ResponseEntity.ok(this.serviceModule.getAllData());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('teacher','admin','student')")
    public ResponseEntity<Modules_Dto> getSingleModule(@PathVariable int id) {
        return ResponseEntity.ok(this.serviceModule.getSingleData(id));
    }

    @GetMapping("/course/{id}")
    @PreAuthorize("hasAnyRole('teacher','admin','student')")
    public ResponseEntity<List<Modules_Dto>> getModulesByCourse(@PathVariable int id) {
        return ResponseEntity.ok(this.serviceModule.getModulesByCourse(id));
    }

    @PostMapping("/course/{id}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<Modules_Dto> createModules(@RequestBody Modules_Dto modulesDto, @PathVariable int id) {
        return ResponseEntity.status(201).body(this.serviceModule.createData(modulesDto, id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<Modules_Dto> updateModules(@RequestBody Modules_Dto modulesDto, @PathVariable int id) {
        return ResponseEntity.ok(this.serviceModule.updateData(modulesDto, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<String> deleteLessons(@PathVariable int id) {
        this.serviceModule.deleteData(id);
        return ResponseEntity.status(204).body("COURSE DELETED");
    }
}
