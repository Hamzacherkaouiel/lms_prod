package project.learning_managment_system.learning_managment_system_dev.course_managment.Services.Impl;

import org.springframework.stereotype.Service;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Entities.Test;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Repository.Test_Repo;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Dto.Course_Dto;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Course;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Exceptions.CustomesException.Course_Exception;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Mappers.Impl.Mapper_Course;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Mappers.Mapper_Interface;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Repositories.Course_Repo;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Services.Service_Interface;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Entities.Teacher;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Exceptions.CustomesException.UserNotFound;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Repositories.Teacher_Repo;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Services.ManagementService.ServiceImpl.ServiceTeacher;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Service_Course implements Service_Interface<Course_Dto> {
    public Mapper_Interface<Course_Dto,Course> mapperInterface;
    public Course_Repo courseRepo;
    public Teacher_Repo teacherRepo;
    public Test_Repo testRepo;

    public Service_Course(Course_Repo repo,Teacher_Repo service,Test_Repo test_repo){
        mapperInterface=new Mapper_Course();
        courseRepo=repo;
        this.teacherRepo=service;
        this.testRepo=test_repo;
    }
    @Override
    public List<Course_Dto> getAllData() {
        return this.courseRepo.findAll()
                .stream().map(mapperInterface::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Course_Dto getSingleData(int id) {
        return this.courseRepo.findById(id)
                .map(mapperInterface::toDto)
                .orElseThrow(()->new Course_Exception("Course Not found"));
    }

    @Override
    public Course_Dto createData(Course_Dto data,int id) {
        Course course=this.mapperInterface.toEntity(data);
        course.setTeacher(Teacher.builder()
                        .id(id)
                .build());
        return this.mapperInterface.toDto(this.courseRepo.save(course));
    }

    @Override
    public Course_Dto updateData(Course_Dto data, int id) {
        Course course=this.courseRepo.findById(id)
                .orElseThrow(()->new Course_Exception("Course not found"));
        this.mapperInterface.updateFields(data,course);
        return this.mapperInterface.toDto(this.courseRepo.save(course));
    }

    @Override
    public void deleteData(int id) {
        Course course = courseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Test test = course.getTest();
        if (test != null) {

            course.setTest(null);
            courseRepo.save(course);
            testRepo.delete(test);
        }

        courseRepo.delete(course);
    }
    public List<Course_Dto> getCourseByOwnerId(int id){
        return this.courseRepo.findByTeacher_Id(id)
                .stream().map(mapperInterface::toDto)
                .collect(Collectors.toList());
    }
    public List<Course_Dto> getCourseByOwnerMail(String mail){
        Teacher teacher=this.teacherRepo.findByMail(mail).orElseThrow(()->new UserNotFound("Teacher not found"));
        return this.courseRepo.findByTeacher_Id(teacher.getId())
                .stream().map(mapperInterface::toDto)
                .collect(Collectors.toList());
    }
    public Course_Dto createCourseByMail(Course_Dto data,String mail){
        Teacher teacher=this.teacherRepo.findByMail(mail).orElseThrow(()->new UserNotFound("Teacher not found"));
        Course course=this.mapperInterface.toEntity(data);
        course.setTeacher(teacher);
        return this.mapperInterface.toDto(this.courseRepo.save(course));
    }

}
