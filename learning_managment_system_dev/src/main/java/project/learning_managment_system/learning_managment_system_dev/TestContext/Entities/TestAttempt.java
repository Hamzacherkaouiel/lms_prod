package project.learning_managment_system.learning_managment_system_dev.TestContext.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "testAttempt")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attempt_id")
    public int id;
    public float score;
    public float max_score;
    public int userId;
    public String message;
    @ManyToOne
    @JoinColumn(name = "quiz_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Test test;
}
