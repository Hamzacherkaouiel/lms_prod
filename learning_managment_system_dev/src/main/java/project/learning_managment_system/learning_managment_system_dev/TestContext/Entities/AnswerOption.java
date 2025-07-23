package project.learning_managment_system.learning_managment_system_dev.TestContext.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "answerOption")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    public int id;
    public String answer;
    public boolean isfalse;
    @ManyToOne
    @JoinColumn(name = "questions_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Questions questions;
}
