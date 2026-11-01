/* =========================================================
   BARBERPLAN - LÓGICA DO FRONTEND
   Conversa com a API do servidor Java usando fetch.
   Sem bibliotecas externas.
   ========================================================= */

// Endereço da API. Como o frontend e a API ficam no mesmo
// servidor, basta usar o caminho relativo.
const API = '/api';

// Formatos usados para mostrar as datas e horários.
const DIAS_DOMINGO = ['dom', 'seg', 'ter', 'qua', 'qui', 'sex', 'sáb'];

/* =========================================================
   ABAS
   ========================================================= */

const abas = document.querySelectorAll('.aba');
const paineis = document.querySelectorAll('.painel');

abas.forEach(aba => {

    aba.addEventListener('click', () => {

        abas.forEach(a => a.classList.remove('ativa'));
        paineis.forEach(p => p.classList.remove('ativo'));

        aba.classList.add('ativa');
        document
            .getElementById('painel-' + aba.dataset.aba)
            .classList.add('ativo');

        if (aba.dataset.aba === 'agenda') {
            carregarAgendamentos();
        }

        if (aba.dataset.aba === 'servicos') {
            carregarServicos();
        }
    });
});

/* =========================================================
   AGENDAMENTOS - LISTAR E FILTRAR
   ========================================================= */

const campoFiltro = document.getElementById('filtro-data');
const listaAgendamentos = document.getElementById('lista-agendamentos');

document.getElementById('btn-filtrar')
    .addEventListener('click', carregarAgendamentos);

document.getElementById('btn-limpar').addEventListener('click', () => {
    campoFiltro.value = '';
    carregarAgendamentos();
});

async function carregarAgendamentos() {

    listaAgendamentos.innerHTML = '<p class="carregando">Carregando...</p>';

    let url = API + '/agendamentos';

    if (campoFiltro.value) {
        url += '?data=' + campoFiltro.value;
    }

    try {

        const resposta = await fetch(url);
        const dados = await resposta.json();

        if (!resposta.ok) {
            throw new Error(dados.erro || 'Não foi possível carregar.');
        }

        mostrarAgendamentos(dados);

    } catch (erro) {

        listaAgendamentos.innerHTML =
            '<div class="aviso-erro">' + escapar(erro.message) + '</div>';
    }
}

function mostrarAgendamentos(agendamentos) {

    if (agendamentos.length === 0) {

        listaAgendamentos.innerHTML =
            '<div class="vazio">Nenhum agendamento encontrado.</div>';
        return;
    }

    const hoje = dataDeHoje();
    const html = [];

    agendamentos.forEach(agendamento => {

        const eHoje = agendamento.data === hoje;
        const etiqueta = eHoje
            ? '<span class="etiqueta-hoje">HOJE</span> '
            : '';

        html.push(`
            <div class="card">
                <div class="card-info">
                    <div class="card-nome">
                        ${etiqueta}${escapar(agendamento.nome)}
                    </div>
                    <div class="card-detalhes">
                        <strong>Serviço:</strong> ${escapar(agendamento.servico)}<br>
                        <strong>Telefone:</strong> ${escapar(agendamento.telefone)}<br>
                        <strong>Data:</strong> ${formatarData(agendamento.data)}
                        (${DIAS_DOMINGO[diaDaSemana(agendamento.data)]})
                    </div>
                </div>

                <div class="card-horario">
                    <div class="hora">${escapar(agendamento.horario)}</div>
                    <div class="data">${formatarData(agendamento.data)}</div>
                </div>

                <button class="botao botao-perigo"
                        onclick="cancelarAgendamento(${agendamento.id})">
                    Cancelar
                </button>
            </div>
        `);
    });

    listaAgendamentos.innerHTML = html.join('');
}

/* =========================================================
   AGENDAMENTO - CANCELAR
   ========================================================= */

async function cancelarAgendamento(id) {

    const confirmado = window.confirm(
        'Deseja mesmo cancelar este agendamento?'
    );

    if (!confirmado) {
        return;
    }

    try {

        const resposta = await fetch(API + '/agendamentos/' + id, {
            method: 'DELETE'
        });

        const dados = await resposta.json();

        if (!resposta.ok) {
            throw new Error(dados.erro || 'Não foi possível cancelar.');
        }

        carregarAgendamentos();

    } catch (erro) {
        window.alert(erro.message);
    }
}

/* =========================================================
   AGENDAMENTO - CADASTRAR
   ========================================================= */

const form = document.getElementById('form-agendamento');
const mensagemForm = document.getElementById('mensagem-form');

form.addEventListener('submit', async evento => {

    evento.preventDefault();

    const agendamento = {
        nome: document.getElementById('nome').value.trim(),
        telefone: document.getElementById('telefone').value.trim(),
        servico: document.getElementById('servico').value,
        data: document.getElementById('data').value,
        horario: document.getElementById('horario').value
    };

    // Validação rápida antes de chamar o servidor.
    if (!agendamento.nome || !agendamento.telefone
            || !agendamento.servico || !agendamento.data
            || !agendamento.horario) {

        mostrarMensagem('erro', 'Preencha todos os campos obrigatórios.');
        return;
    }

    if (agendamento.data < dataDeHoje()) {

        mostrarMensagem('erro', 'Não é possível agendar em data passada.');
        return;
    }

    try {

        const resposta = await fetch(API + '/agendamentos', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(agendamento)
        });

        const dados = await resposta.json();

        if (!resposta.ok) {
            throw new Error(dados.erro || 'Não foi possível salvar.');
        }

        mostrarMensagem(
            'sucesso',
            'Agendamento realizado com sucesso! ' +
            'Cliente: ' + dados.nome
        );

        form.reset();

        // Depois de salvar, leva o usuário para a aba Agenda
        // já com a lista atualizada.
        setTimeout(() => {
            document.querySelector('[data-aba="agenda"]').click();
            mostrarMensagemOculta();
        }, 900);

    } catch (erro) {
        mostrarMensagem('erro', erro.message);
    }
});

function mostrarMensagem(tipo, texto) {
    mensagemForm.className = 'mensagem ' + tipo;
    mensagemForm.textContent = texto;
}

function mostrarMensagemOculta() {
    mensagemForm.className = 'mensagem oculto';
    mensagemForm.textContent = '';
}

/* =========================================================
   SERVIÇOS
   ========================================================= */

async function carregarServicos() {

    const lista = document.getElementById('lista-servicos');

    lista.innerHTML = '<p class="carregando">Carregando...</p>';

    try {

        const resposta = await fetch(API + '/servicos');
        const dados = await resposta.json();

        if (!resposta.ok) {
            throw new Error(dados.erro || 'Não foi possível carregar.');
        }

        // Guarda os serviços também no select do formulário.
        montarSelectServicos(dados);

        const html = dados.map(servico => `
            <div class="card card-servico">
                <div class="card-info">
                    <div class="card-nome">${escapar(servico.nome)}</div>
                    <div class="card-detalhes">Serviço oferecido na barbearia</div>
                </div>
                <div class="card-preco">${formatarPreco(servico.preco)}</div>
            </div>
        `);

        lista.innerHTML = html.length
            ? html.join('')
            : '<div class="vazio">Nenhum serviço cadastrado.</div>';

    } catch (erro) {
        lista.innerHTML =
            '<div class="aviso-erro">' + escapar(erro.message) + '</div>';
    }
}

function montarSelectServicos(servicos) {

    const select = document.getElementById('servico');
    const escolhido = select.value;

    // Mantém a primeira opção fixa e reconstrói as demais.
    select.innerHTML = '<option value="">Selecione um serviço</option>';

    servicos.forEach(servico => {
        const opcao = document.createElement('option');
        opcao.value = servico.nome;
        opcao.textContent =
            servico.nome + ' — ' + formatarPreco(servico.preco);
        select.appendChild(opcao);
    });

    select.value = escolhido;
}

/* =========================================================
   FUNÇÕES AUXILIARES
   ========================================================= */

/**
 * Converte AAAA-MM-DD para DD/MM/AAAA,
 * que é o formato usado no Brasil.
 */
function formatarData(iso) {

    if (!iso) {
        return '';
    }

    const partes = iso.split('-');
    return partes[2] + '/' + partes[1] + '/' + partes[0];
}

/**
 * Retorna o dia da semana de uma data no formato ISO.
 * 0 = domingo, 1 = segunda, ... 6 = sábado.
 */
function diaDaSemana(iso) {

    const partes = iso.split('-');
    const data = new Date(
        Number(partes[0]),
        Number(partes[1]) - 1,
        Number(partes[2])
    );

    return data.getDay();
}

/**
 * Data de hoje no formato AAAA-MM-DD.
 */
function dataDeHoje() {

    const hoje = new Date();
    const mes = String(hoje.getMonth() + 1).padStart(2, '0');
    const dia = String(hoje.getDate()).padStart(2, '0');

    return hoje.getFullYear() + '-' + mes + '-' + dia;
}

/**
 * Formata o preço como moeda brasileira: R$ 35,00
 */
function formatarPreco(preco) {
    return Number(preco).toLocaleString('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    });
}

/**
 * Protege a página contra textos com caracteres especiais
 * digitados pelo usuário.
 */
function escapar(texto) {

    const div = document.createElement('div');
    div.textContent = texto == null ? '' : String(texto);

    return div.innerHTML;
}

/* =========================================================
   INICIALIZAÇÃO
   ========================================================= */

// Data mínima do formulário = hoje (não deixa agendar no passado).
document.getElementById('data').min = dataDeHoje();

// Carrega a lista e os serviços assim que a página abre.
carregarAgendamentos();
carregarServicos();
