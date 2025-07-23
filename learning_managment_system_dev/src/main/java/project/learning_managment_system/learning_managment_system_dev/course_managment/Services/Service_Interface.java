package project.learning_managment_system.learning_managment_system_dev.course_managment.Services;

import project.learning_managment_system.learning_managment_system_dev.course_managment.Mappers.Mapper_Interface;

import java.util.List;

public interface Service_Interface<T>{
    public List<T> getAllData();
    public T getSingleData(int id);
    public T createData(T data,int id);
    public T updateData(T data,int id);
    public void deleteData(int id);
}
