package project.learning_managment_system.learning_managment_system_dev.course_managment.Services.Impl;

import org.springframework.stereotype.Service;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Dto.Modules_Dto;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Course;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Modules;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Exceptions.CustomesException.Module_Exception;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Mappers.Impl.Mapper_Module;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Repositories.Modules_Repo;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Services.Service_Interface;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Service_Module implements Service_Interface<Modules_Dto> {
    public Mapper_Module mapperModule;
    public Modules_Repo modulesRepo;
    public Service_Module(Modules_Repo repo){
        this.modulesRepo=repo;
        this.mapperModule=new Mapper_Module();
    }

    @Override
    public List<Modules_Dto> getAllData() {
        return this.modulesRepo.findAll()
                .stream().map(mapperModule::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Modules_Dto getSingleData(int id) {
        return this.modulesRepo.findById(id)
                .map(mapperModule::toDto)
                .orElseThrow(()->new Module_Exception("Module Not found"));
    }

    @Override
    public Modules_Dto createData(Modules_Dto data, int id) {
        Modules modules=this.mapperModule.toEntity(data);
        modules.setCourse(Course.builder()
                        .id(id)
                .build());
        return this.mapperModule.toDto(this.modulesRepo.save(modules));
    }

    @Override
    public Modules_Dto updateData(Modules_Dto data, int id) {
        Modules modules=this.modulesRepo.findById(id)
                .orElseThrow(()->new Module_Exception("Module not found"));
        this.mapperModule.updateFields(data,modules);
        return this.mapperModule.toDto(this.modulesRepo.save(modules));
    }

    @Override
    public void deleteData(int id) {
        this.modulesRepo.deleteById(id);
    }
    public  List<Modules_Dto> getModulesByCourse(int id ){
        return this.modulesRepo.findByCourse_Id(id)
                .stream().map(mapperModule::toDto)
                .collect(Collectors.toList());
    }
}
