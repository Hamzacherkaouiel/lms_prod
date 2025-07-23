package project.learning_managment_system.learning_managment_system_dev.TestContext.Mappers;

public interface Mapper_Interface<T,B> {
    public T toDto(B entity);
    public B toEntity(T dto);
    public void updateEntity(T dto,B entity);
}
