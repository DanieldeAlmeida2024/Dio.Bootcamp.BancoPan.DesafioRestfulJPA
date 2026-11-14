# Requisitos — Academia Digital

Versão 1.2.0 · 14/11/2026

## Funcionais

- **RF01:** criar, listar, buscar por ID, editar campos permitidos e excluir aluno.
- **RF02:** criar, listar, consultar, atualizar peso/altura e excluir avaliação física.
- **RF03:** matricular aluno existente, consultar, listar por bairro e cancelar matrícula.
- **RF04:** impedir duas matrículas para o mesmo aluno, retornando conflito 409.
- **RF05:** impedir que remover matrícula apague o aluno associado.
- **RF06:** retornar HTTP 400/404 para entradas inválidas/entidades ausentes e 201/204 na criação/exclusão.
- **RF07:** não serializar CPF nas respostas JSON de alunos.
- **RF08:** validar dígitos verificadores de CPF antes de persistir; recusar CPF malformado com 400.
- **RF09:** persistir CPF somente com 11 dígitos, detectar duplicatas com e sem pontuação e retornar 409. Restringir unicidade no banco para concorrência.

## Não funcionais

- **RNF01:** isolar testes no H2, sem uso do PostgreSQL de desenvolvimento/produção.
- **RNF02:** preservar integridade das referências e operações de exclusão em transações.
- **RNF03:** usar variáveis de ambiente para a conexão do PostgreSQL.
- **RNF04:** testes integrados `MockMvc` + JPA e Maven CI.
- **RNF05:** manter créditos do exercício DIO e documentar limitações reais.

## Riscos ainda abertos

Spring Boot 2.6.1 legado, falta autenticação, DTOs de saída, migração de schema, tratamento de concorrência e testes E2E PostgreSQL. Estas funções são exercícios, não serviço pronto para produção.
