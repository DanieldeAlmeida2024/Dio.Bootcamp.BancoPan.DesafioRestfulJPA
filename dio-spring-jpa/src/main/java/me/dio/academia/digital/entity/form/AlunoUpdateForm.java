package me.dio.academia.digital.entity.form;

import java.time.LocalDate;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class AlunoUpdateForm {
    @Size(min = 3, max = 50)
    private String nome;

    @Size(min = 3, max = 50)
    private String bairro;

    @Past
    private LocalDate dataDeNascimento;
}
