package project.learning_managment_system.learning_managment_system_dev.TestContext.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Entities.Questions;

import java.util.List;

public interface Questions_Repo extends JpaRepository<Questions,Integer> {
    List<Questions> findByQuiz_Id(int id);
}
