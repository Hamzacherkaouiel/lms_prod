package project.learning_managment_system.learning_managment_system_dev.course_managment.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course_Dto {
    public int id;
    public String title;
    public String description;
    public int capacity;
    public int teacher_id;
    public boolean isFull(){
        return capacity==0;
    }
}
