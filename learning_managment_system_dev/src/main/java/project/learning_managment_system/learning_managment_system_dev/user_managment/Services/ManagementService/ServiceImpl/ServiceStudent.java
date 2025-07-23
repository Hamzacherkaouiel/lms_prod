package project.learning_managment_system.learning_managment_system_dev.user_managment.Services.ManagementService.ServiceImpl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import project.learning_managment_system.learning_managment_system_dev.Config.JwtExtractor;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.Student_Dto;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.UserCreation;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Entities.Student;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Exceptions.CustomesException.UserNotFound;
import project.learning_managment_system.learning_managment_system_dev.user_managment.KafkaConfig.Producer;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Repositories.Student_Repo;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Services.ManagementService.ServiceUser;
import project.learning_managment_system.learning_managment_system_dev.user_managment.mappers.Implementation.Student_Mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceStudent implements ServiceUser<Student_Dto> {
    @Autowired
    public Student_Repo studentRepo;
    @Autowired
    public Student_Mapper studentMapper;
    @Autowired
    public Producer producer;
    public BCryptPasswordEncoder bCrypt=new BCryptPasswordEncoder(12);

    @Override
    public List<Student_Dto> getUsers() {
        return this.studentRepo.findAll()
                .stream().map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Student_Dto getSingleUser(int id) {
        return this.studentRepo.findById(id)
                .map(studentMapper::toDto)
                .orElseThrow(()->new UserNotFound("USER NOT FOUND"));
    }

    @Override
    public Student_Dto updateUser(Student_Dto user, int id) {
        Student student =this.studentRepo.findById(id).orElseThrow(()->new UserNotFound("STUDENT NOT FOUND"));
        this.studentMapper.updateEntityFromDto(user,student);
        this.producer.syncData(this.mapTo(user));
        return this.studentMapper.toDto(this.studentRepo.save(student));
    }

    @Override
    public void deleteUser(int id) {
        Optional<Student> student=this.studentRepo.findById(id);
        if(student.isPresent()){
            this.studentRepo.deleteById(id);
            this.producer.syncData(UserCreation.builder()
                            .mail(student.get().getMail())
                    .operation("DELETE")
                    .build());
        }
        else {
            throw  new UserNotFound("USER NOT FOUND");
        }
    }

    @Override
    public Student_Dto getMyProfile(Jwt token) {
        JwtExtractor jwtExtractor = new JwtExtractor();
        return this.studentRepo.findByMail(jwtExtractor.extractClaim(token, "preferred_username"))
                .map(studentMapper::toDto).orElseThrow(() -> new UserNotFound("STUDENT NOT FOUND"));
    }

    @Override
    public UserCreation mapTo(Student_Dto user) {
        return UserCreation.builder()
                .mail(user.getMail())
                .lastname(user.getLastname())
                .firstname(user.getFirstname())
                .operation("UPDATE_PROFILE")
                .build();
    }

    @Override
    @Transactional
    public void deleteMyProfile(Jwt token) {
        JwtExtractor jwtExtractor=new JwtExtractor();
        String mail =jwtExtractor.extractClaim(token,"preferred_username");
        this.studentRepo.deleteByMail(mail);
        this.producer.syncData(UserCreation.builder()
                        .operation("DELETE")
                        .mail(mail)
                .build());
    }

    @Override
    public void updatePassword(UserCreation userCreation, Jwt jwt) {
        JwtExtractor jwtExtractor = new JwtExtractor();
        userCreation.setMail(jwtExtractor.extractClaim(jwt, "preferred_username"));
        this.studentRepo.findByMail(jwtExtractor.extractClaim(jwt, "preferred_username"))
                .ifPresent(user->{
                    user.setPassword(bCrypt.encode(userCreation.getPassword()));
                    this.studentRepo.save(user);
                });
        this.producer.syncData(userCreation);
    }
    public List<Student_Dto> getNotEnrolledStudents(int courseId){
        return this.studentRepo.findStudentsNotEnrolledInCourse(courseId)
                .stream().map(studentMapper::toDto)
                .collect(Collectors.toList());
    }
    public List<Student_Dto> getEnrolledStudents(int courseId){
        return this.studentRepo.findStudentsEnrolledInCourse(courseId)
                .stream().map(studentMapper::toDto)
                .collect(Collectors.toList());
    }
}
