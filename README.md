# BarberPlan — Sistema de Agendamento para Barbearia

Aplicação Java para administrar os agendamentos de uma barbearia diretamente
pelo terminal. O programa apresenta um menu para consultar a agenda, cadastrar
e cancelar horários e consultar os serviços e preços.

## Como rodar

Com o JDK instalado, abra o terminal na pasta do projeto e execute:

```bash
javac -d out src/*.java
java -cp out BarberPlan
```

Use os números apresentados no menu. Para encerrar, escolha `0`. Não é
necessário instalar Maven, Gradle, npm ou qualquer biblioteca externa.

## Funcionalidades

- Listar agendamentos em ordem de data e horário ou filtrar por uma data.
- Cadastrar agendamento escolhendo um serviço da tabela de preços.
- Cancelar um agendamento pelo ID.
- Consultar serviços e preços.
- Salvar os dados em `data/agendamentos.json`, preservando-os entre execuções.

## Regras de negócio

- Não permite agendar em data passada.
- Não permite dois agendamentos no mesmo dia e horário.
- Nome, telefone, serviço, data e horário são obrigatórios.
- As datas devem ser informadas no formato `dd/MM/aaaa` e os horários em
  `HH:mm`.

## Estrutura

- `src/BarberPlan.java`: menu e interação com o usuário pelo terminal.
- `src/GerenciadorAgendamentos.java`: regras de negócio e persistência.
- `src/Agendamento.java` e `src/Servico.java`: modelos do sistema.
- `src/Json.java`: leitura e gravação do arquivo JSON sem dependências.
- `data/agendamentos.json`: dados salvos, criado automaticamente.

