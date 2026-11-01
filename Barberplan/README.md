# BarberPlan — Sistema de Agendamento para Barbearia

Projeto acadêmico de Java. O sistema é um **servidor web simples**: o Java
entrega o frontend no navegador e também responde a API de agendamentos.

## Como rodar

```bash
# 1. Compilar
javac -d out src/*.java

# 2. Executar
java -cp out BarberPlan

# 3. Abrir no navegador
#    http://localhost:8080
```

Para usar outra porta:

```bash
java -cp out BarberPlan 9090
```

Não precisa de Maven, Gradle, npm ou banco de dados. Apenas Java instalado.

## Estrutura

```
Barberplan/
├── src/                          # Backend Java (sem framework)
│   ├── BarberPlan.java           # main: sobe o servidor
│   ├── ServidorHttp.java         # servidor HTTP do JDK: API + arquivos estáticos
│   ├── GerenciadorAgendamentos.java  # regras de negócio + validações
│   ├── Agendamento.java          # modelo do agendamento
│   ├── Servico.java              # modelo do serviço (tabela de preços)
│   └── Json.java                 # leitura/escrita de JSON sem biblioteca externa
├── web/                          # Frontend (HTML, CSS e JS puro)
│   ├── index.html
│   ├── style.css
│   └── script.js
├── data/
│   └── agendamentos.json         # persistência (gerado automaticamente)
└── README.md
```

## Funcionalidades

- **Agenda** — lista os agendamentos ordenados por data e horário, com filtro
  por data, selo "HOJE" e botão de cancelar em cada card.
- **Novo agendamento** — formulário com validação no front e no back.
- **Serviços** — tabela de preços (Corte, Barba, Corte + Barba, etc.).

## Regras de negócio

- Não permite agendar em data passada.
- Não permite dois agendamentos no mesmo dia e horário.
- Campos obrigatórios não podem ficar vazios.
- Os dados ficam salvos em `data/agendamentos.json` e sobrevivem ao
  reinício do programa.

## API

| Método | Rota                              | Descrição                        |
|--------|-----------------------------------|----------------------------------|
| GET    | `/api/agendamentos`               | lista todos, ordenados           |
| GET    | `/api/agendamentos?data=2026-11-05` | filtra por data                |
| POST   | `/api/agendamentos`               | cadastra (retorna 201 ou 400)    |
| DELETE | `/api/agendamentos/{id}`          | cancela (retorna 200 ou 404)     |
| GET    | `/api/servicos`                   | lista serviços e preços          |

Exemplo de corpo para o POST:

```json
{
  "nome": "João Silva",
  "telefone": "(11) 99999-0000",
  "servico": "Corte de cabelo",
  "data": "2026-11-05",
  "horario": "10:00"
}
```

## Decisões de projeto

- **Java puro com `HttpServer`** — o servidor HTTP já vem embutido no JDK
  (`com.sun.net.httpserver`), então não há dependência externa.
- **JSON manual (`Json.java`)** — evita Maven/Gradle para um projeto simples.
- **Frontend no mesmo servidor** — como HTML/CSS/JS e API saem da mesma
  origem, não há problema de CORS.
- **Tema barbearia** — visual escuro com detalhes em dourado, sem CSS
  framework.
