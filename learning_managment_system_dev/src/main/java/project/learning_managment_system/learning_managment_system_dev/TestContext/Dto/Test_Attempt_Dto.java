package project.learning_managment_system.learning_managment_system_dev.TestContext.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Test_Attempt_Dto {
    public int id;
    public float score;
    public float max_score;
    public String message;
}
