# BarberPlan

Sistema de agendamento para barbearia desenvolvido em Java. A aplicação usa
menus no terminal e conceitos básicos de orientação a objetos, sem interface
gráfica ou bibliotecas externas.

## Funcionalidades

- Cadastro e login de clientes.
- Login separado para administrador e barbeiros.
- Cadastro de barbeiros pelo administrador.
- Agendamentos criados pelo cliente ou pelo administrador.
- Consulta dos próprios agendamentos pelo cliente.
- Consulta da agenda completa pelo administrador e pelo barbeiro.
- Cancelamento de agendamentos pelo barbeiro, usando o ID.
- Consulta dos serviços e preços disponíveis.
- Exportação de usuários, serviços e agendamentos para `dados.json`.

## Perfis e permissões

| Perfil | Permissões |
| --- | --- |
| Cliente | Criar agendamentos para si, consultar seus agendamentos e ver serviços. |
| Administrador | Criar agendamentos para clientes, consultar a agenda, ver serviços e cadastrar barbeiros. |
| Barbeiro | Consultar a agenda e cancelar agendamentos pelo ID. |

Clientes criam sua própria conta pela opção **Cadastrar cliente** no menu
inicial. Barbeiros são cadastrados pelo administrador; não há cadastro público
de barbeiros.

## Contas iniciais

| Perfil | Login | Senha |
| --- | --- | --- |
| Administrador | `administrador` | `administrador123` |
| Barbeiro | `barbeiro` | `barbeiro123` |

Essas credenciais são padrões de demonstração e devem ser alteradas antes de
qualquer uso real.

## Serviços iniciais

| Serviço | Preço |
| --- | ---: |
| Corte | R$ 25,00 |
| Barba | R$ 20,00 |
| Corte + Barba | R$ 45,00 |

## Como compilar e executar

Execute os comandos abaixo na pasta raiz do repositório, onde este README está.
É necessário ter o JDK instalado e os comandos `javac` e `java` disponíveis no
terminal.

```powershell
New-Item -ItemType Directory -Force bin
javac -encoding UTF-8 -d bin BarberPlan\src\Agendamento.java BarberPlan\src\Barbearia.java BarberPlan\src\Main.java BarberPlan\src\Servico.java BarberPlan\src\Usuario.java
java -cp bin Main
```

Ao iniciar, escolha o tipo de login apropriado. Para cadastrar um cliente,
escolha **Cadastrar cliente**; para criar uma conta de barbeiro, entre como
administrador e use **Cadastrar barbeiro**.

Agendamentos solicitam a data no formato `dd/MM/aaaa` (por exemplo,
`19/11/2026`) e o horário no formato de 24 horas `HH:mm` (por exemplo,
`14:30`).

## Arquivo `dados.json`

O arquivo contém listas de `usuarios`, `servicos` e `agendamentos`. Ele é
atualizado quando um usuário é cadastrado, um agendamento é criado ou um
agendamento é cancelado. Abra `dados.json` em um editor de texto ou no VS Code
para visualizar o conteúdo.

O JSON é uma exportação para consulta: o programa não carrega os dados do
arquivo ao iniciar. Usuários e agendamentos ficam em memória durante a execução,
portanto não são restaurados após encerrar e abrir o programa novamente. As
senhas não são incluídas no JSON. O arquivo é gravado na pasta de trabalho
atual; os comandos acima iniciam o programa a partir da raiz do repositório.

## Estrutura do projeto

```text
BarberPlan/
  src/
    Agendamento.java  # Dados de um agendamento
    Barbearia.java    # Serviços, usuários, agenda e exportação JSON
    Main.java         # Menus, login e interação pelo terminal
    Servico.java      # Dados de um serviço e seu preço
    Usuario.java      # Dados e autenticação dos usuários
dados.json            # Exportação visual dos dados
README.md
```

## Limitações

- Usuários e agendamentos são armazenados em arrays com capacidade para 100
  registros e não são carregados do JSON.
- A exportação JSON não inclui senhas.
- As contas iniciais e suas senhas são apenas para demonstração acadêmica; o
  sistema não implementa armazenamento seguro de senhas.


## Desenvolvido por ##
- Guilherme Gomes
- João Felipe
- Denny Franco
- José Henrique
- Igor Souza