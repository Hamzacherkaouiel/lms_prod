package project.learning_managment_system.learning_managment_system_dev.course_managment.Services.Impl;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Dto.Course_Dto;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Dto.Enrollment_Dto;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Course;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Enrollements;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Exceptions.CustomesException.Course_Exception;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Exceptions.CustomesException.Enrollemnts_Exception;
import project.learning_managment_system.learning_managment_system_dev.course_managment.KafkaConfig.ProducerMail;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Mappers.Impl.Mapper_Course;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Mappers.Impl.Mapper_Enrollment;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Mappers.Mapper_Interface;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Repositories.Course_Repo;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Repositories.Enrollemnt_Repo;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Entities.Student;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Exceptions.CustomesException.UserNotFound;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Repositories.Student_Repo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Service_Enrollemnt {

    public Mapper_Course mapperCourse;
    public Enrollemnt_Repo enrollemntRepo;
    public Course_Repo courseRepo;
    public ProducerMail producerMail;
    public Student_Repo studentRepo;
    public Mapper_Enrollment mapperEnrollment;
    public Service_Enrollemnt(Enrollemnt_Repo repo,Course_Repo course_repo,ProducerMail producer,Student_Repo student_repo
    ){
        this.studentRepo=student_repo;
        this.enrollemntRepo=repo;
        this.mapperCourse=new Mapper_Course();
        this.courseRepo=course_repo;
        this.producerMail=producer;
        this.mapperEnrollment=new Mapper_Enrollment();
    }
    public List<Course_Dto> getEnrollmentCourses(int studentId) {
        return this.enrollemntRepo.findByStudent_Id(studentId)
                .stream().map(enrollements -> mapperCourse.toDto(enrollements.getCourse()))
                .collect(Collectors.toList());
    }
    public List<Course_Dto> getEnrollmentCoursesByMail(String mail) {
        Student student=this.studentRepo.findByMail(mail).orElseThrow(()->new UserNotFound("Student not found"));
        return this.enrollemntRepo.findByStudent_Id(student.getId())
                .stream().map(enrollements -> mapperCourse.toDto(enrollements.getCourse()))
                .collect(Collectors.toList());
    }
    public Enrollements createSingleEnrollement(int studentId,int courseId){
        if(this.enrollemntRepo.existsByStudent_IdAndCourse_Id(studentId,courseId)){
            throw  new Enrollemnts_Exception("Student Already enrolled to this course");
        }
        Course course=this.courseRepo.findById(courseId).orElseThrow(()->new Course_Exception("Course Not Found"));
        if(course.isFull()){
            throw new Enrollemnts_Exception("The course is already full you can't add student");
        }
        course.capacity-=1;
        Student student=this.studentRepo.findById(studentId).orElseThrow(()->new UserNotFound("Stdeunt not found"));
        Enrollements enrollements= Enrollements.builder()
                .enrollmentDate(LocalDate.now())
                .course(course)
                .student(student)
                .build();
        this.courseRepo.save(course);
        this.produceSingleMail(enrollements);
        return this.enrollemntRepo.save(enrollements);
    }
    public List<Enrollements> createMultipleEnrollemnts(int courseId, List<Integer> students){

        List<Enrollements> enrollements=new ArrayList<>();
        students.forEach(id->{
            System.out.println(id);
            Course course=this.courseRepo.findById(courseId).get();
            Student student=this.studentRepo.findById(id).orElseThrow(()->new UserNotFound("Student not found"));
            if(!this.enrollemntRepo.existsByStudent_IdAndCourse_Id(id,courseId)
            && !course.isFull()
            ) {
                course.capacity-=1;
                Enrollements element = Enrollements.builder()
                        .enrollmentDate(LocalDate.now())
                        .course(course)
                        .student(student)
                        .build();
                enrollements.add(element);
                this.courseRepo.save(course);
            }
        });
        this.produceMultipleMail(enrollements);
        return this.enrollemntRepo.saveAll(enrollements);
    }
    public void deleteEnrollemnt(int id){
        Enrollements enrollements=this.enrollemntRepo.findById(id).orElseThrow(()->new Enrollemnts_Exception("Enrollement not found"));
        Course course=this.courseRepo.findById(enrollements.getCourse().getId()).orElseThrow(()->new Course_Exception("No course for this enrollemnt"));
        course.setCapacity(course.getCapacity()+1);
        this.courseRepo.save(course);
        this.enrollemntRepo.delete(enrollements);
    }
    @Transactional
    public void deleteEnrollemntByStudentAndCourseId(int id,int courseId){
        Enrollements enrollements=this.enrollemntRepo.findByStudent_IdAndCourse_Id(id,courseId).orElseThrow(()->new Enrollemnts_Exception("Enrollment not found"));
        Course course=this.courseRepo.findById(enrollements.getCourse().getId()).orElseThrow(()->new Course_Exception("No course for this enrollemnt"));
        course.setCapacity(course.getCapacity()+1);
        this.courseRepo.save(course);
        this.enrollemntRepo.delete(enrollements);
    }
    private void produceSingleMail(Enrollements enrollements){

        this.producerMail.sendMail(this.mapperEnrollment.toDto(enrollements));
    }
    private void produceMultipleMail(List<Enrollements> enrollementsList){
        enrollementsList.forEach(enrollements -> producerMail.sendMail(this.mapperEnrollment.toDto(enrollements)));
    }




}
