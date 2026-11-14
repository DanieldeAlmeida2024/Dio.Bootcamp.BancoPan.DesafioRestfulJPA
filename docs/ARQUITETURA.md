# Arquitetura — Academia Digital (Spring Data JPA)

Versão 1.1.0 · 14/11/2026

## Camadas existentes

- `controller/`: endpoints `/alunos`, `/avaliacoes` e `/matriculas`, com dados JSON.
- `service/` e `service/impl/`: regras de criação e listagem; partes de consulta, atualização e exclusão ainda não foram implementadas.
- `repository/`: Spring Data JPA para as entidades `Aluno`, `AvaliacaoFisica` e `Matricula`.
- `entity/` e `entity/form/`: mapeamento JPA e entrada de formulários.

Aplicação em Java 11 / Spring Boot 2.6.1 / Hibernate com importações `javax.persistence`. Trata-se de um laboratório legado, não da stack recomendada para publicação em 2026.

## Ambientes

- Configuração de runtime: `dio-spring-jpa/src/main/resources/application.yml`; variáveis `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `SERVER_PORT`, `JPA_DDL_AUTO`.
- Configuração de teste: `dio-spring-jpa/src/test/resources/application-test.yml`; H2 em memória, esquema transitório.
- O arquivo genérico `application.properties` de 124 KB foi retirado, pois sobrepunha propriedades do projeto com centenas de exemplos inadequados.
- A senha não é mais hardcoded. O log de SQL Hibernate está desabilitado e o default DDL é `validate`, sem alterações implícitas no banco.

## Riscos de arquitetura

Os controllers retornam entidades JPA diretamente, com informações pessoais (`cpf`); **não publicar** assim sem DTOs de saída e revisão LGPD. Falta autenticação e autorização, métodos CRUD completos, migração de esquema e atualização do Spring Boot para uma linha suportada. As relações JPA exigem revisão de cascatas de exclusão.

Este documento distingue o estado atual das próximas etapas; aprovação do CI de H2 não homologará a API em produção.
