package project.learning_managment_system.learning_managment_system_dev.course_managment.Mappers.Impl;

import project.learning_managment_system.learning_managment_system_dev.course_managment.Dto.Enrollment_Dto;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Enrollements;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Mappers.Mapper_Interface;
import project.learning_managment_system.learning_managment_system_dev.user_managment.mappers.Implementation.Student_Mapper;

public class Mapper_Enrollment implements Mapper_Interface<Enrollment_Dto, Enrollements> {
    public Mapper_Course mapperCourse=new Mapper_Course();
    public Student_Mapper studentMapper=new Student_Mapper();
    @Override
    public Enrollment_Dto toDto(Enrollements entity) {
        return Enrollment_Dto.builder()
                .enrollmentDate(entity.getEnrollmentDate())
                .student(this.studentMapper.toDto(entity.getStudent()))
                .course(this.mapperCourse.toDto(entity.getCourse()))
                .enrollemnt_id(entity.getEnrollemnt_id())
                .build();
    }

    @Override
    public Enrollements toEntity(Enrollment_Dto dto) {
        return Enrollements.builder()
                .enrollmentDate(dto.getEnrollmentDate())
                .build();
    }

    @Override
    public void updateFields(Enrollment_Dto dto, Enrollements entity) {
         entity.setEnrollmentDate(dto.getEnrollmentDate()!=null? dto.enrollmentDate:entity.getEnrollmentDate());
    }
}
