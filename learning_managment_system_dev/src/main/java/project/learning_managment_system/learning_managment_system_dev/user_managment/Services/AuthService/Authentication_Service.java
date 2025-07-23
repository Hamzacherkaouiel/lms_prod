package project.learning_managment_system.learning_managment_system_dev.user_managment.Services.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.*;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Entities.Admin;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Entities.Student;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Entities.Teacher;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Exceptions.CustomesException.InvalidUser;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Exceptions.CustomesException.RoleNotFound;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Repositories.Admin_Repo;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Repositories.Student_Repo;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Repositories.Teacher_Repo;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Services.AuthService.ConfigKeycloak.KeyCloakService;
import project.learning_managment_system.learning_managment_system_dev.user_managment.mappers.Implementation.Admin_Mapper;
import project.learning_managment_system.learning_managment_system_dev.user_managment.mappers.Implementation.Student_Mapper;
import project.learning_managment_system.learning_managment_system_dev.user_managment.mappers.Implementation.Teacher_Mapper;
import project.learning_managment_system.learning_managment_system_dev.user_managment.mappers.Mapper_Interface;

@Service
public class Authentication_Service {
    @Autowired
    public Student_Repo studentRepo;
    @Autowired
    public Teacher_Repo teacherRepo;
    @Autowired
    public Admin_Repo adminRepo;
    @Autowired
    public KeyCloakService keyCloakService;
    public Mapper_Interface mapper;
    private BCryptPasswordEncoder bCrypt=new BCryptPasswordEncoder(12);

    public UserDTO orchestrator(UserCreation userCreation){
        if(userCreation.getMail()==null || userCreation.getPassword()==null
                ||userCreation.getFirstname()==null||userCreation.getLastname()==null){
            throw  new InvalidUser("INVALID USER TO CREATE");
        }
        if(userCreation.getRole().equals("student")){
            System.out.println(userCreation.getMail());
            return this.creaUser(userCreation);
        }
        return this.createPrivateUser(userCreation);
    }
    public UserDTO creaUser(UserCreation userCreation)  {
             this.mapper=new Student_Mapper();
             this.keyCloakService.createUser(userCreation);
             userCreation.setPassword(this.bCrypt.encode(userCreation.getPassword()));
             Student student=this.studentRepo.save((Student)this.mapper.Creation(userCreation));
             return (Student_Dto) this.mapper.toDto(student);
    }
    public UserDTO createPrivateUser(UserCreation userCreation){

            this.keyCloakService.createUser(userCreation);

            userCreation.setPassword(this.bCrypt.encode(userCreation.getPassword()));
        return switch (userCreation.getRole()) {
            case "admin" -> {
                this.mapper=new Admin_Mapper();
                Admin admin = this.adminRepo.save((Admin)this.mapper.Creation(userCreation));
                yield (Admin_Dto)this.mapper.toDto(admin);
            }
            case "teacher" -> {
                this.mapper=new Teacher_Mapper();
                Teacher teacher = this.teacherRepo.save((Teacher)this.mapper.Creation(userCreation));
                yield (Teacher_Dto)this.mapper.toDto(teacher);
            }
            default -> throw new RoleNotFound("ROLE NOTE FOUND");
        };

    }
}
