package project.learning_managment_system.learning_managment_system_dev.TestContext.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Questions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "questions_id")
    public int id;
    public String description;
    public int scoreQuestion;
    @ManyToOne
    @JoinColumn(name = "quiz_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Test quiz;
    @OneToMany(mappedBy = "questions")
    public List<AnswerOption> answerOptions;
}
