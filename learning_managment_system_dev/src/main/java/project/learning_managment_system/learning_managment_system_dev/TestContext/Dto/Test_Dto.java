package project.learning_managment_system.learning_managment_system_dev.TestContext.Dto;

import lombok.*;
import project.learning_managment_system.learning_managment_system_dev.TestContext.Entities.Questions;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Test_Dto {
    public int id;
    public String title;
    public List<Questions_Dto> questions;
}
