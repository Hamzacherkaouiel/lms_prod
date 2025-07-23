package project.learning_managment_system.learning_managment_system_dev.course_managment.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Simple_Lessons_Dto {
    public int id;
    public String description;
    public int module_id;
}
