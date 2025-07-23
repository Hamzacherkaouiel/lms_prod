package project.learning_managment_system.learning_managment_system_dev.TestContext.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import project.learning_managment_system.learning_managment_system_dev.course_managment.Entities.Course;

import java.util.List;

@Entity
@Table(name = "test")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    public int id;
    public String title;
    @OneToMany(mappedBy = "quiz")
    public List<Questions> questions;
    @OneToOne
    @JoinColumn(name = "course_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Course course;
}
