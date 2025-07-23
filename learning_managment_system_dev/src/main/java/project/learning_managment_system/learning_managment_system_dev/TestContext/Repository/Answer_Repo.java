package project.learning_managment_system.learning_managment_system_dev.TestContext.Repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Entities.AnswerOption;

import java.util.List;

public interface Answer_Repo extends JpaRepository<AnswerOption, Integer> {
    List<AnswerOption> findByQuestions_Id(int id);
}
