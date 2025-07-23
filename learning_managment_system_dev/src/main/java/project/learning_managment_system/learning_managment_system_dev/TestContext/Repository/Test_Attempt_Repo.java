package project.learning_managment_system.learning_managment_system_dev.TestContext.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Entities.TestAttempt;

import java.util.List;

public interface Test_Attempt_Repo extends JpaRepository<TestAttempt,Integer> {
    List<TestAttempt> findByTest_Id(int id);
    List<TestAttempt> findByUserId(int id);
    List<TestAttempt> findByUserIdAndTest_Id(int userid,int test_id);
}
