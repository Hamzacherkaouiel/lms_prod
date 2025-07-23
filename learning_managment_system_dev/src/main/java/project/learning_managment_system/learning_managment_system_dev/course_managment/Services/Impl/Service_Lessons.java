package project.learning_managment_system.learning_managment_system_dev.course_managment.Services.Impl;

import org.springframework.stereotype.Service;
import project.learning_managment_system.learning_managment_system_dev.ContentContext.Service.StorageService;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Dto.Full_Lessons_Dto;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Dto.Simple_Lessons_Dto;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Lessons;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Modules;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Exceptions.CustomesException.Lessons_Exception;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Mappers.Impl.Mapper_Full_Lessons;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Mappers.Impl.Mapper_Simple_Lessons;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Repositories.Lessons_Repo;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Services.Service_Interface;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class Service_Lessons implements Service_Interface<Simple_Lessons_Dto> {
    public StorageService service;
    public Mapper_Simple_Lessons mapperLessons;
    public Mapper_Full_Lessons mapperFullLessons;
    public Lessons_Repo lessonsRepo;

    public Service_Lessons(Lessons_Repo repo, StorageService storageService) {
        this.mapperLessons = new Mapper_Simple_Lessons();
        this.mapperFullLessons = new Mapper_Full_Lessons();
        this.lessonsRepo = repo;
        this.service = storageService;
    }

    @Override
    public List<Simple_Lessons_Dto> getAllData() {
        return this.lessonsRepo.findAll()
                .stream()
                .map(mapperLessons::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Simple_Lessons_Dto getSingleData(int id) {
        return this.lessonsRepo.findById(id)
                .map(mapperLessons::toDto)
                .orElseThrow(() -> new Lessons_Exception("Lesson not found"));
    }

    @Override
    public Simple_Lessons_Dto createData(Simple_Lessons_Dto data, int id) {
        Lessons lessons = this.mapperLessons.toEntity(data);
        lessons.setModule(Modules.builder().id(id).build());
        return this.mapperLessons.toDto(this.lessonsRepo.save(lessons));
    }

    @Override
    public Simple_Lessons_Dto updateData(Simple_Lessons_Dto data, int id) {
        Lessons lessons = this.lessonsRepo.findById(id)
                .orElseThrow(() -> new Lessons_Exception("Lessons not found"));
        this.mapperLessons.updateFields(data, lessons);
        return this.mapperLessons.toDto(this.lessonsRepo.save(lessons));
    }

    @Override
    public void deleteData(int id) {
        Lessons lessons = this.lessonsRepo.findById(id)
                .orElseThrow(() -> new Lessons_Exception("Lesson not found"));
        this.lessonsRepo.deleteById(id);
        this.service.deleteFile(lessons.getObjectKey());
    }

    public List<Simple_Lessons_Dto> getLessonsByModules(int id) {
        return this.lessonsRepo.findByModule_Id(id)
                .stream()
                .map(mapperLessons::toDto)
                .collect(Collectors.toList());
    }

    public Full_Lessons_Dto getLessonWithVideo(int id) {
        return this.mapperFullLessons.toDto(
                this.lessonsRepo.findById(id)
                        .orElseThrow(() -> new Lessons_Exception("Lesson not found"))
        );
    }

    public Map<String, String> createLessonWithVideo(Full_Lessons_Dto lessonsDto, int id, String filename) {
        Lessons lessons = this.mapperFullLessons.toEntity(lessonsDto);
        lessons.setModule(Modules.builder().id(id).build());
        String objectKey = this.service.objectkey(filename);
        lessons.setObjectKey(objectKey);
        lessons.setS3Url(this.service.saveUrl(objectKey));
        this.lessonsRepo.save(lessons);
        return this.service.generatePresignedUploadUrl(objectKey);
    }
}
