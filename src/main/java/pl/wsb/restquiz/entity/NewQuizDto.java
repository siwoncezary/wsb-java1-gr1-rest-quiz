package pl.wsb.restquiz.entity;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NewQuizDto {
    @NotNull
    @Size(min = 10, message = "Pytanie nie może być null, ani krótsze niż 10 znaków.")
    private String question;
    @NotNull
    @NotEmpty
    private String[] options;
    @NotNull
    @NotEmpty
    private String[] answers;
}
