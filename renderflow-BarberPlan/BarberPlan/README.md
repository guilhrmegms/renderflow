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
- `Usuario.java` - dados do usuário e validação de login

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

Ao iniciar, o programa exibe opções de login separadas para cliente,
administrador e barbeiro, além do cadastro de clientes:

- 1. Login do cliente
- 2. Login do administrador
- 3. Login do barbeiro
- 4. Cadastrar cliente
- 0. Sair

Clientes podem criar uma conta e usar o login do cliente para fazer
agendamentos, consultar seus próprios agendamentos e ver os serviços.
O administrador pode cadastrar agendamentos, consultar a agenda, ver os serviços
e cadastrar barbeiros com login e senha. Barbeiros podem somente consultar a
agenda e cancelar um agendamento pelo ID.

Contas iniciais para acesso:

- Barbeiro: login `barbeiro`, senha `barbeiro123`
- Administrador: login `administrador`, senha `administrador123`

Os usuários e agendamentos são armazenados apenas em memória e são perdidos ao
encerrar o programa. As senhas padrão servem somente para este projeto acadêmico.

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
