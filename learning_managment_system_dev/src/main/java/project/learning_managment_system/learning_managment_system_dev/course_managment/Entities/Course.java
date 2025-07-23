package project.learning_managment_system.learning_managment_system_dev.course_managment.Entities;

import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Entities.Test;

import project.learning_managment_system.learning_managment_system_dev.user_managment.Entities.Teacher;

import java.util.List;

@Entity
@Table(name = "course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    public int id;
    public String title;
    public String description;
    public int capacity;
    @OneToMany(mappedBy = "course")
    public List<Modules> modulesList;
    @ManyToOne
    @JoinColumn(name = "id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Teacher teacher;
    @OneToMany(mappedBy = "course")
    public List<Enrollements> enrollementsCourses;
    @OneToOne(mappedBy = "course")
    public Test test;

    public boolean isFull(){
        return capacity==0;
    }


}
