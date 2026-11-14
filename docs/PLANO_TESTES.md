# Plano de testes — Academia Digital

Versão 1.1.0 · 14/11/2026

## Testes automatizados nesta revisão

Requer Java 11 e Maven:

```bash
mvn -B -ntp -f dio-spring-jpa/pom.xml test
```

| ID | Teste | Resultado esperado |
| --- | --- | --- |
| JPA-01 | Inicialização `AcademiaDigitalApplicationTests` | Contexto Spring sobe usando perfil `test` |
| JPA-02 | Teste no pacote legado `dio.dio_spring_jpa` | Classe de configuração explicitamente informada; contexto sobe |
| JPA-03 | Criar `Aluno` em H2 | `saveAndFlush` gera ID, `findById` recupera nome e data |
| CONF-01 | Configuração padrão | Não contém password fixa, desliga SQL trace |
| CONF-02 | Runtime | `ddl-auto=validate` por padrão e variáveis de ambiente |

Os casos CONF são decisões de configuração inspecionadas no repositório, **não asserts automáticos nesta versão**. A CI executa JPA-01 a JPA-03.

## Testes pendentes

- Contrato HTTP dos três controllers, status 400/404/201, validações DTO e erros de entrada.
- CRUD completo de alunos/avaliações/matrículas e cascatas de exclusão.
- Integração real PostgreSQL isolado e evolução de schema.
- Controle de acesso, serialização de CPF e exposição de dados pessoais.
- Migração e verificação de segurança do Spring Boot legado.

Não classificar o serviço como pronto apenas com a aprovação do H2.
