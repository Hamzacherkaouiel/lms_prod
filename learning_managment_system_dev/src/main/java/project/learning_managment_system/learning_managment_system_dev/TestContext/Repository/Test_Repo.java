package project.learning_managment_system.learning_managment_system_dev.TestContext.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Entities.Test;

import java.util.List;
import java.util.Optional;

public interface Test_Repo extends JpaRepository<Test,Integer> {
    Optional<Test> findByCourse_Id(int id);
}
