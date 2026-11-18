# BarberPlan 3.0

Sistema simples de agendamento para barbearia, desenvolvido em Java.


## Funcionalidades

- Cadastro de agendamento com nome, telefone, serviço, data e horário
- Listagem de todos os agendamentos cadastrados
- Visualização dos serviços disponíveis e seus preços
- Armazenamento em memória com array fixo
- Menu interativo no terminal

## Estrutura do projeto

- `Main.java` - ponto de entrada do sistema
- `Barbearia.java` - lógica principal da barbearia
- `Agendamento.java` - modelo de agendamento
- `Servico.java` - modelo de serviço

## Como executar

1. Abra o terminal na pasta do projeto
2. Compile os arquivos Java:

```bash
javac -d bin src/*.java
```

3. Execute o programa:

```bash
java -cp bin Main
```

## Menu do sistema

Ao iniciar, o programa exibe um menu com as opções:

- 1. Novo Agendamento
- 2. Listar Agendamentos
- 3. Ver Serviços
- 0. Sair

## Exemplo de uso

Ao escolher a opção de cadastro, o sistema solicita:

- Nome
- Telefone
- Serviço
- Data
- Horário

Depois, o agendamento é guardado e pode ser consultado pela opção de listagem.

## Observações

- O sistema usa arrays com capacidade limitada para armazenar agendamentos.
- Os serviços iniciais cadastrados são: Corte, Barba e Corte + Barba.
- O projeto é acadêmico e pode ser expandido com persistência em banco de dados, validação de campos e interface gráfica.

## DESENVOLVIDO POR

- Guilherme Gomes
- João Felipe
- Denny Franco
- Henrique
- Igor
