# Plano de testes — API Academia Digital

Versão 1.2.0 · 14/11/2026

```bash
mvn -B -ntp -f dio-spring-jpa/pom.xml test
```

O workflow `testes-jpa.yml` usa Java 11 e H2 do perfil `test`. A API usa datas no formato brasileiro **dd/MM/yyyy**, definido no `LocalDateDeserializer` (não ISO 8601).

| ID | Cenário | Resultado esperado |
| --- | --- | --- |
| JPA-01 | Contexto Spring (incluindo pacote legado) | Inicializa com H2 |
| JPA-02 | Persistência de aluno | `saveAndFlush` gera ID, `findById` recupera |
| API-01 | GET por ID de aluno | 200 e **sem CPF no JSON** |
| API-02 | PUT de aluno com nome/bairro | Preserva ID e data de nascimento |
| API-03 | GET de aluno ausente | 404, não 500 |
| API-04 | POST de matrícula | 201, aluno associado |
| API-05 | DELETE da matrícula | 204, aluno permanece no banco |
| API-06 | POST duplicado de matrícula | 409 (conflito) |
| API-07 | POST avaliação com peso 0 | 400 |
| API-08 | DELETE de aluno matriculado | 204, matrícula associada removida |
| CPF-01 | POST com `529.982.247-25` | 201; H2 armazena `52998224725`; JSON sem CPF |
| CPF-02 | POST novamente com `52998224725` | 409; um único registro |
| CPF-03 | POST com `111.111.111-11` | 400 sem persistência |
| CPF-04 | POST sem CPF | 400 sem persistência |

## Fora do escopo testado

Não há integração com PostgreSQL real, concorrência, testes de múltiplos formatos internacionais de identificação e carga, medição de carga, autorização por perfil ou aplicação de migrações no banco. A versão do Spring Boot é antiga e deve ser atualizada em projeto de migração com testes adicionais.

Os testes HTTP são executados com `MockMvc`: isso verifica endpoints e serviço com H2, mas não substitui implantação real.

## JPA-CPF 1.3.0 — defesa em profundidade do cadastro

| ID | Camada | Cenário | Critério |
| --- | --- | --- | --- |
| CPF-05 | Serviço, sem MockMvc | CPF repetido (`11111111111` ou `00000000000`) | HTTP 400, não persiste |
| CPF-06 | Serviço, sem MockMvc | CPF com dígito verificador incorreto (`52998224726`) | HTTP 400, não persiste |
| CPF-07 | Serviço, sem MockMvc | CPF válido pontuado (`529.982.247-25`) | Normaliza e persiste 11 dígitos |

Os testes `AlunoCpfIntegrationTests` invocam diretamente `AlunoServiceImpl.create` com H2 transacional, **sem passar pelo controlador nem pela anotação `@CPF`**. Isso comprova que a regra de domínio não depende apenas de validação HTTP. Não valida CPF com serviço governamental nem identidade real.
