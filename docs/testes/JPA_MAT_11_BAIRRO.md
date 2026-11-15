# JPA-MAT-11 — Consulta de matrículas por bairro

14/11/2026 · Spring Boot / JPA / H2 em testes de integração.

## Defeito potencial
A consulta antiga usava SQL nativo `SELECT * FROM tb_matriculas m INNER JOIN tb_alunos a`. Ambos os lados possuem coluna `id`; alguns mapeamentos Hibernate falham ao descobrir aliases duplicados ou interpretam incorretamente colunas da entidade.

## Correção
- Substituída por JPQL `SELECT m FROM Matricula m JOIN m.aluno a WHERE a.bairro = :bairro` com parâmetro nomeado explícito.
- O filtro trim do serviço é preservado; consulta retorna apenas matrículas do bairro, sem duplicar ou confundir IDs de aluno e matrícula.
- Integração via MockMvc cria dois alunos matriculados em bairros diferentes e verifica resultado filtrado com HTTP 200.

## Limites
O fluxo permanece um laboratório; não há autenticação de usuários, requisitos LGPD para dados reais nem teste de banco de produção. Não implantar diretamente na Internet.
