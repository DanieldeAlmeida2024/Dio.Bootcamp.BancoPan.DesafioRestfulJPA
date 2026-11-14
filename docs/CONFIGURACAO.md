# Configuração por ambiente — Academia Digital JPA

Versão 1.1.0 · 14/11/2026

## Desenvolvimento

As configurações são lidas de `dio-spring-jpa/src/main/resources/application.yml`:

| Variável | Padrão | Função |
| --- | --- | --- |
| `SERVER_PORT` | `8081` | Porta HTTP |
| `DB_URL` | `jdbc:postgresql://localhost:5432/academia` | JDBC do PostgreSQL |
| `DB_USERNAME` | `academia` | Usuário do banco |
| `DB_PASSWORD` | *vazio* | Senha fornecida fora do Git |
| `JPA_DDL_AUTO` | `validate` | Validação do esquema, sem migrações implícitas |

No PowerShell, use por exemplo `$env:DB_PASSWORD = (Read-Host -AsSecureString "Senha")` **somente se o processo de aplicação for capaz de consumir o valor como texto**; não salve credenciais no arquivo versionado. Para executar normalmente, configure a variável com o gerenciador de segredos do ambiente e inicie `mvn -f dio-spring-jpa/pom.xml spring-boot:run`.

O `application.properties` antigo, com cerca de 124 KB de documentação de propriedades do Spring incluída no runtime, foi removido. Logs de SQL e parâmetros Hibernate estão desligados por padrão.

## Teste

`mvn -B -ntp -f dio-spring-jpa/pom.xml test` usa `src/test/resources/application-test.yml`, H2 em memória e `ddl-auto=create-drop`, **apenas no perfil test**. Não chama nem altera o PostgreSQL local/externo.

## Riscos restantes

O projeto usa Spring Boot 2.6.1 (linha sem suporte), JPA `javax.persistence` e contém endpoints/serviços didáticos ainda incompletos. A segurança de uma aplicação publicada requer migração do framework, tratamento de autenticação/autorização, testes API adicionais e revisão da exposição de dados de alunos.
