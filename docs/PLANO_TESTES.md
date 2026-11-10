# Plano de testes

T01: cenário principal com dados válidos.
T02: entrada inválida e tratamento de erro.
T03: conexão externa indisponível.
T04: testes documentais e validação de UML.

```bash
cd dio-spring-jpa
./mvnw test
./mvnw spring-boot:run
python -m unittest discover -s tests -p test_documentacao.py -v
```

Os testes de documentação não verificam a execução do projeto. Configurar PostgreSQL conforme o projeto e conferir as propriedades reais; não executar em produção com credenciais de exemplo.
