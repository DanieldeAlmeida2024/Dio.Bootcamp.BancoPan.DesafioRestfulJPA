# Matriz de testes — Academia Digital — alunos, matrículas e avaliações físicas

v1.0.0 (11/11/2026), base `b365a931a65b00519d0297ddf1d2581ad07b4dbd`. Inventário de código; casos de teste são critérios futuros, não resultados PASS.

| ID | Cenário | Resultado esperado | Situação |
|---|---|---|---|
| JPA-01 | POST /alunos com formulário válido | Aluno criado com ID e data correta | Pendente de execução específica |
| JPA-02 | CPF duplicado | Violação de UNIQUE/erro de domínio controlado | Pendente de execução específica |
| JPA-03 | CPF de formato inválido | Política explícita de validação | Pendente de execução específica |
| JPA-04 | GET /alunos | Lista alunos sem carga indevida de avaliações LAZY | Pendente de execução específica |
| JPA-05 | GET /alunos?dataDeNascimento=... | Filtra pelo contrato e parse de datas | Pendente de execução específica |
| JPA-06 | GET /alunos/avaliacoes/{id} | Só avaliações do aluno informado | Pendente de execução específica |
| JPA-07 | Cadastrar matrícula válida | Associação vinculada ao aluno | Pendente de execução específica |
| JPA-08 | Cadastrar avaliação física | Valores aceitos segundo as validações DTO | Pendente de execução específica |
| JPA-09 | Excluir aluno com avaliações | Confirmar cascade REMOVE e impacto | Pendente de execução específica |
| JPA-10 | Payload com data inválida | Resposta controlada do Jackson | Pendente de execução específica |
| JPA-11 | Aluno inexistente | Erro sem null dereference | Pendente de execução específica |
| JPA-12 | Dados de CPF em logs/API | Sem exposição desnecessária | Pendente de execução específica |

## Estratégia
Testes JUnit para métodos puros e service mocks; Spring Boot Test/MockMvc para status/DTO; JPA em banco de teste e casos de falha para integrações. 

```bash
cd dio-spring-jpa && ./mvnw test
```

O banco PostgreSQL deve ser preparado em ambiente isolado e as propriedades Spring revisadas; evitar testes destrutivos na instância Oracle/produção.

Cada execução deverá registrar JDK, versão Maven/Spring, commit, fixtures, status, logs sem segredos e defeitos. A existência de contexto Spring testado não equivale a conformidade de todos os IDs.
