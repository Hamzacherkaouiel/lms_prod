package project.learning_managment_system.learning_managment_system_dev.course_managment.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Modules_Dto {
    public int id;
    public String title;
    public int course_id;
}
