package project.learning_managment_system.learning_managment_system_dev.TestContext.Dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Questions_Dto {
    public int id;
    public String description;
    public int scoreQuestion;
    public List<Answer_Dto> options;
}
