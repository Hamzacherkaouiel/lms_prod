package project.learning_managment_system.learning_managment_system_dev.TestContext.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Dto.Test_Dto;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Entities.Test;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Exceptions.Test_Exception;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Mappers.Imp.Mapper_Questions;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Mappers.Imp.Mapper_Test;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Repository.Test_Repo;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Course;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Exceptions.CustomesException.Course_Exception;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Repositories.Course_Repo;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Entities.Student;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Exceptions.CustomesException.UserNotFound;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Repositories.Student_Repo;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceTest {
    public Test_Repo testRepo;
    public Mapper_Test mapperTest;
    public Mapper_Questions mapperQuestion;
    public ServiceTestAttempt serviceTestAttempt;
    public ServiceTest(Test_Repo testRepo,ServiceTestAttempt testAttempt,Course_Repo course_repo){
        this.testRepo=testRepo;
        this.mapperTest=new Mapper_Test();
        this.serviceTestAttempt=testAttempt;
        this.mapperQuestion=new Mapper_Questions();
    }
    public Test_Dto getTestById(int id){
        return this.mapperTest.toDto(this.testRepo.findById(id).orElseThrow(()->new Test_Exception("Test not found")));
    }
    public Optional<Test_Dto> getTestByCourseId(int id){
        return this.testRepo.findByCourse_Id(id).map(mapperTest::toDto);
    }

    public Test_Dto createTest(Test_Dto test,int id){
        Test entity=this.mapperTest.toEntity(test);
        entity.setCourse(Course.builder()
                        .id(id)
                .build());
        return this.mapperTest.toDto(this.testRepo.save(entity));
    }
    public Test_Dto startTest(int id){
        Test test=this.testRepo.findById(id).orElseThrow(()-> new Test_Exception("Test not found"));
        Test_Dto testDto=this.mapperTest.toDto(test);
        testDto.setQuestions(test.getQuestions().stream()
                .map(mapperQuestion::toDto).collect(Collectors.toList())
        );
        return testDto;
    }
    public Test_Dto updateTest(Test_Dto testDto,int id) {
        Test test=this.testRepo.findById(id).orElseThrow(()->new Test_Exception("Test not found"));
        this.mapperTest.updateEntity(testDto,test);
        return this.mapperTest.toDto(this.testRepo.save(test));
    }
    public void deleteTest(int id) {
        Test test=this.testRepo.findById(id).orElseThrow(()->new Test_Exception("Test not found"));
        Course course = test.getCourse();
        if (course != null) {
            course.setTest(null);
        }
        testRepo.delete(test);
    }

}
