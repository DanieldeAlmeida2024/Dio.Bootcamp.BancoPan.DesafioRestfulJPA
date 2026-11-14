# Arquitetura — Academia Digital (Spring Data JPA)

Versão 1.2.0 · 14/11/2026

## Organização real

- `controller/AlunoController`: `POST /alunos`, `GET /alunos`, `GET /alunos/{id}`, `PUT /alunos/{id}`, `DELETE /alunos/{id}` e histórico de avaliações.
- `controller/AvaliacaoFisicaController`: inclusão, consulta por ID, listagem, edição de peso/altura e exclusão.
- `controller/MatriculaController`: criação, consulta por ID, listagem filtrada e exclusão.
- `service/impl/`: regras de existência e status HTTP, atualização de entidades gerenciadas, validação de medidas e prevenção de matrícula duplicada.
- `repository/`: JPA para `Aluno`, `AvaliacaoFisica` e `Matricula` (relação 1:1).
- `entity/form/`: entrada de criação e atualização parcial do aluno e edição completa de avaliação física.

## Integridade referencial

Removida a configuração `CascadeType.ALL` de `Matricula.aluno`, que podia **excluir o aluno ao remover uma matrícula**. A exclusão de aluno agora remove as matrículas associadas antes de remover a entidade principal, na mesma transação. A relação de matrícula tem restrição `unique=true` sobre `aluno_id` e o serviço devolve HTTP 409 para duplicidade.

## CPF: consistência na fronteira HTTP

O `AlunoForm` usa `@CPF` para verificar dígitos, em vez de aceitar qualquer string. O `AlunoServiceImpl` remove pontuação e persiste 11 dígitos no PostgreSQL/H2; o repositório permite `existsByCpf` para detectar duplicata antes do insert. A restrição `unique` do banco é preservada, e uma colisão de persistência retorna 409. Registros antigos com CPF não normalizado precisariam ser tratados antes de migração para produção (não houve migração do banco).

## Privacidade e escopo

`Aluno.cpf` passa a ter `@JsonProperty(access=WRITE_ONLY)` para não aparecer nas respostas JSON. **A entidade JPA ainda é usada diretamente na resposta**: precisam ser criados DTOs, autorização por papel e rastreabilidade antes de publicar. Essa correção não equivale à conformidade integral com a LGPD.

O banco de teste é H2 isolado (`application-test.yml`), runtime padrão usa PostgreSQL com variáveis `DB_URL`, `DB_USERNAME` e `DB_PASSWORD`. Spring Boot 2.6.1 / `javax.persistence` continua legado; migração para Spring suportado é um trabalho separado.

## Testabilidade

`AcademiaCrudIntegrationTests` sobe aplicação Spring, faz chamadas `MockMvc` e grava em H2. Os testes validam a persistência e comportamento HTTP sem banco externo. Não incluem cargas concorrentes, migrações de esquema PostgreSQL ou autenticação.
