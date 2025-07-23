package project.learning_managment_system.learning_managment_system_dev.user_managment.Services.ManagementService.ServiceImpl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import project.learning_managment_system.learning_managment_system_dev.Config.JwtExtractor;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.Admin_Dto;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.UserCreation;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Entities.Admin;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Entities.Student;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Entities.Teacher;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Exceptions.CustomesException.UserNotFound;
import project.learning_managment_system.learning_managment_system_dev.user_managment.KafkaConfig.Producer;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Repositories.Admin_Repo;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Repositories.Student_Repo;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Repositories.Teacher_Repo;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Services.ManagementService.ServiceUser;
import project.learning_managment_system.learning_managment_system_dev.user_managment.mappers.Implementation.Admin_Mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceAdmin implements ServiceUser<Admin_Dto> {
    @Autowired
    public Admin_Repo adminRepo;
    @Autowired
    public Admin_Mapper adminMapper;
    @Autowired
    public Producer producer;
    @Autowired
    public Teacher_Repo teacherRepo;
    @Autowired
    public Student_Repo studentRepo;
    public BCryptPasswordEncoder bCrypt=new BCryptPasswordEncoder(12);
    @Override
    public List<Admin_Dto> getUsers() {
        return this.adminRepo.findAll()
                .stream().map(adminMapper::toDto)
                .collect(Collectors.toList());    }

    @Override
    public Admin_Dto getSingleUser(int id) {
        return this.adminRepo.findById(id)
                .map(adminMapper::toDto)
                .orElseThrow(()->new UserNotFound("USER NOT FOUND"));
    }

    @Override
    public Admin_Dto updateUser(Admin_Dto user, int id) {
        Admin admin =this.adminRepo.findById(id).orElseThrow(()->new UserNotFound("USER NOT FOUND"));
        this.adminMapper.updateEntityFromDto(user,admin);
        this.producer.syncData(this.mapTo(user));
        return this.adminMapper.toDto(this.adminRepo.save(admin));
    }

    @Override
    public void deleteUser(int id) {
        Optional<Admin> admin=this.adminRepo.findById(id);
        Optional<Teacher> teacher=this.teacherRepo.findById(id);
        Optional<Student> student=this.studentRepo.findById(id);
        if(admin.isPresent()){
            this.adminRepo.deleteById(id);
            this.producer.syncData(UserCreation.builder()
                    .mail(admin.get().getMail())
                    .operation("DELETE")
                    .build());
        } else if (teacher.isPresent()) {
            this.teacherRepo.deleteById(id);
            this.producer.syncData(UserCreation.builder()
                    .mail(teacher.get().getMail())
                    .operation("DELETE")
                    .build());
        } else if (student.isPresent()) {
            this.studentRepo.deleteById(id);
            this.producer.syncData(UserCreation.builder()
                    .mail(student.get().getMail())
                    .operation("DELETE")
                    .build());
        }
        else {
            throw  new UserNotFound("User not found");
        }
    }
    @Override
    public Admin_Dto getMyProfile(Jwt token) {
        JwtExtractor jwtExtractor = new JwtExtractor();
        return this.adminRepo.findByMail(jwtExtractor.extractClaim(token, "preferred_username"))
                .map(adminMapper::toDto).orElseThrow(() -> new UserNotFound("ADMIN NOT FOUND"));
    }

    @Override
    public UserCreation mapTo(Admin_Dto user) {
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
        this.adminRepo.deleteByMail(mail);
        this.producer.syncData(UserCreation.builder()
                .operation("DELETE")
                .mail(mail)
                .build());
    }

    @Override
    public void updatePassword(UserCreation userCreation, Jwt jwt) {
        JwtExtractor jwtExtractor = new JwtExtractor();
        this.adminRepo.findByMail(jwtExtractor.extractClaim(jwt, "preferred_username"))
                .ifPresent(user->{
                    user.setPassword(bCrypt.encode(userCreation.getPassword()));
                    this.adminRepo.save(user);
                });
        this.producer.syncData(userCreation);
    }
}
