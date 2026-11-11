# Arquitetura

API didática para gestão de alunos, matrículas e avaliações físicas, construída com Spring Boot e Spring Data JPA.

## Componentes
- Interface de entrada / consumidor.
- Casos de uso, serviços ou eventos.
- Entidades de domínio (Aluno).
- Persistência / mensageria externa.

A implementação atual é a fonte da verdade; as camadas acima são apenas uma visão para navegação.

## Decisões
Separar domínio de integração externa. Usar testes unitários com adaptadores simulados e integração real apenas em ambiente preparado. Não migrar a tecnologia existente por preferência estética.

## Dependências
Configurar PostgreSQL conforme o projeto e conferir as propriedades reais; não executar em produção com credenciais de exemplo.
