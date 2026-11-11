# Academia Digital — alunos, matrículas e avaliações físicas

v1.0.0 (11/11/2026), base `b365a931a65b00519d0297ddf1d2581ad07b4dbd`. Inventário de código; casos de teste são critérios futuros, não resultados PASS.

## Arquitetura, contratos e regras reais
O módulo Java real está em `dio-spring-jpa/`, com controllers `AlunoController`, `MatriculaController` e `AvaliacaoFisicaController`, implementações de serviços e repositórios JPA. O domínio tem `Aluno`, `Matricula` e `AvaliacaoFisica`, não um CRUD genérico de produto.

`Aluno` é a entidade `tb_alunos` e possui ID `Long` gerado por IDENTITY, nome, CPF `@Column(unique=true)`, bairro, `LocalDate dataDeNascimento` e lista de avaliações `@OneToMany(mappedBy="aluno",cascade=REMOVE,fetch=LAZY)`. `AlunoController` mapeia `POST /alunos` com `@Valid AlunoForm`, `GET /alunos` com filtro opcional por data de nascimento e `GET /alunos/avaliacoes/{id}` para avaliações de um aluno.

Datas têm serializadores e desserializadores Jackson próprios sob `infra/jackson`. O tratamento de CPF, dados pessoais e avaliações exige atenção à privacidade; a presença de campo único no banco não equivale a validação de formato. A remoção em cascata de avaliações deve ser testada para evitar perda inadvertida de histórico. O exercício segue o projeto Academia Digital e não deve alegar cobertura funcional completa sem testes de integração.

## Comando de validação
```bash
cd dio-spring-jpa && ./mvnw test
```

## Riscos e limites
O banco PostgreSQL deve ser preparado em ambiente isolado e as propriedades Spring revisadas; evitar testes destrutivos na instância Oracle/produção.

Documentos vinculados: [UML](../uml/README.md), [plano de testes](../testes/PLANO_DE_TESTES.md) e [controle documental](../controle/VERSIONAMENTO.md).
