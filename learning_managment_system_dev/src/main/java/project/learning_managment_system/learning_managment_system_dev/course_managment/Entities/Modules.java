package project.learning_managment_system.learning_managment_system_dev.course_managment.Entities;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Modules {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "module_id")
    public int id;
    public String title;
    @ManyToOne
    @JoinColumn(name = "course_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Course course;
    @OneToMany(mappedBy = "module")
    public List<Lessons> lessons;




}
