package me.dio.academia.digital.entity.form;

import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class AvaliacaoFisicaUpdateForm {
    @Positive
    private double peso;

    @Positive
    private double altura;
}
