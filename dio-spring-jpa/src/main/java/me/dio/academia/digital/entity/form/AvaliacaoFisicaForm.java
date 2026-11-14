package me.dio.academia.digital.entity.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoFisicaForm {

  @NotNull
  @Positive
  private Long alunoId;

  //@NotNull(message = "Preencha o campo corretamente.")
  //@Positive(message = "${validatedValue}' precisa ser positivo.")
  @Positive
  private double peso;

  //@NotNull(message = "Preencha o campo corretamente.")
  //@Positive(message = "${validatedValue}' precisa ser positivo.")
  //@DecimalMin(value = "150", message = "'${validatedValue}' precisa ser no mínimo {value}.")
  @Positive
  private double altura;
}
