package project.learning_managment_system.learning_managment_system_dev.course_managment.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lessons {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int lessons_id;
    public String description;
    @ManyToOne
    @JoinColumn(name = "module_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Modules  module;
    public String s3Url;
    public String objectKey;
    public String contentType;


}
