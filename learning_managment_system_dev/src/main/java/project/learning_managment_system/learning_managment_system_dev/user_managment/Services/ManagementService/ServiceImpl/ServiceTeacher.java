package project.learning_managment_system.learning_managment_system_dev.user_managment.Services.ManagementService.ServiceImpl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import project.learning_managment_system.learning_managment_system_dev.Config.JwtExtractor;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.Teacher_Dto;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Dto.UserCreation;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Entities.Teacher;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Exceptions.CustomesException.UserNotFound;
import project.learning_managment_system.learning_managment_system_dev.user_managment.KafkaConfig.Producer;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Repositories.Teacher_Repo;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Services.ManagementService.ServiceUser;
import project.learning_managment_system.learning_managment_system_dev.user_managment.mappers.Implementation.Teacher_Mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceTeacher implements ServiceUser<Teacher_Dto> {
    @Autowired
    public Teacher_Repo teacherRepo;
    @Autowired
    public Teacher_Mapper teacherMapper;
    @Autowired
    public Producer producer;
    public BCryptPasswordEncoder bCrypt=new BCryptPasswordEncoder(12);

    @Override
    public List<Teacher_Dto> getUsers() {
         return this.teacherRepo.findAll()
                .stream().map(teacherMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Teacher_Dto getSingleUser(int id) {
        return this.teacherRepo.findById(id)
                .map(teacherMapper::toDto)
                .orElseThrow(()->new UserNotFound("USER NOT FOUND"));
    }

    @Override
    public Teacher_Dto updateUser(Teacher_Dto user, int id) {
        Teacher teacher =this.teacherRepo.findById(id).orElseThrow(()->new UserNotFound("TEACHER NOT FOUND"));
        this.teacherMapper.updateEntityFromDto(user,teacher);
        this.producer.syncData(this.mapTo(user));
        return this.teacherMapper.toDto(this.teacherRepo.save(teacher));
    }

    @Override
    public void deleteUser(int id) {
        Optional<Teacher> teacher=this.teacherRepo.findById(id);
        if(teacher.isPresent()){
            this.teacherRepo.deleteById(id);
            this.producer.syncData(UserCreation.builder()
                    .mail(teacher.get().getMail())
                    .operation("DELETE")
                    .build());
        }
        else {
            throw  new UserNotFound("USER NOT FOUND FOR ID"+id);
        }
    }
    @Override
    public Teacher_Dto getMyProfile(Jwt token) {
        JwtExtractor jwtExtractor = new JwtExtractor();
        return this.teacherRepo.findByMail(jwtExtractor.extractClaim(token, "preferred_username"))
                .map(teacherMapper::toDto).orElseThrow(() -> new UserNotFound("TEACHER NOT FOUND"));
    }

    @Override
    public UserCreation mapTo(Teacher_Dto user) {
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
        this.teacherRepo.deleteByMail(mail);
        this.producer.syncData(UserCreation.builder()
                .operation("DELETE")
                .mail(mail)
                .build());
    }

    @Override
    public void updatePassword(UserCreation userCreation,Jwt jwt) {
        JwtExtractor jwtExtractor = new JwtExtractor();

        System.out.println(userCreation.getPassword());
        System.out.println(jwtExtractor.extractClaim(jwt, "preferred_username"));
        this.teacherRepo.findByMail(jwtExtractor.extractClaim(jwt, "preferred_username"))
                .ifPresent(user->{
                    user.setPassword(bCrypt.encode(userCreation.getPassword()));
                    this.teacherRepo.save(user);
                    System.out.println("password updated");

                });
        this.producer.syncData(userCreation);
    }
}
