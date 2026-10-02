/* Comportamento das páginas. Persistência provisória em localStorage (sem back-end). */
const DB = { get: k => JSON.parse(localStorage.getItem(k) || '[]'), set: (k, v) => localStorage.setItem(k, JSON.stringify(v)) };
const esc = s => s.replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
const dataBR = s => new Date(s).toLocaleString('pt-BR', { dateStyle: 'short', timeStyle: 'short' });

function marcar(campo, msg) {
  campo.parentElement.querySelector('.erro').textContent = msg || '';
  campo.classList.toggle('invalido', !!msg);
  return !msg;
}
function aviso(txt, ok) {
  const a = document.getElementById('aviso');
  a.textContent = txt; a.className = 'aviso ' + (ok ? 'ok' : 'falha');
}

function paginaInicio() {
  document.getElementById('totPacientes').textContent = DB.get('pacientes').length;
  document.getElementById('totConsultas').textContent = DB.get('consultas').filter(c => c.status === 'AGENDADA').length;
}

function paginaPacientes() {
  const f = document.getElementById('formPaciente'), tb = document.querySelector('#tabela tbody');
  f.cpf.addEventListener('input', () => f.cpf.value = V.mascaraCpf(f.cpf.value));
  f.telefone.addEventListener('input', () => f.telefone.value = V.mascaraTel(f.telefone.value));

  const render = () => {
    const l = DB.get('pacientes');
    tb.innerHTML = l.length ? l.map((p, i) => {
      const id = V.idade(p.nascimento), d = V.desconto(id);
      return `<tr><td>${esc(p.nome)}</td><td>${p.cpf}</td><td>${id} anos</td><td>${Math.round(d * 100)}%</td>
        <td><button class="perigo" data-i="${i}">Remover</button></td></tr>`;
    }).join('') : '<tr><td colspan="5">Nenhum paciente cadastrado.</td></tr>';
  };

  f.addEventListener('submit', e => {
    e.preventDefault();
    const lista = DB.get('pacientes');
    const ok = [
      marcar(f.nome, V.nome(f.nome.value) ? '' : 'Informe ao menos 3 caracteres.'),
      marcar(f.cpf, !V.cpf(f.cpf.value) ? 'CPF inválido.' : lista.some(p => p.cpf === f.cpf.value) ? 'CPF já cadastrado.' : ''),
      marcar(f.nascimento, V.nascimento(f.nascimento.value) ? '' : 'Data inválida ou futura.'),
      marcar(f.telefone, V.telefone(f.telefone.value) ? '' : 'Informe DDD + 8 ou 9 dígitos.')
    ].every(Boolean);
    if (!ok) return aviso('Corrija os campos destacados.', false);
    lista.push({ nome: f.nome.value.trim(), cpf: f.cpf.value, nascimento: f.nascimento.value, telefone: f.telefone.value });
    DB.set('pacientes', lista); f.reset(); render(); aviso('Paciente cadastrado com sucesso!', true);
  });
  f.addEventListener('reset', () => f.querySelectorAll('input').forEach(i => marcar(i, '')));
  tb.addEventListener('click', e => {
    if (!e.target.dataset.i) return;
    const l = DB.get('pacientes'); l.splice(e.target.dataset.i, 1); DB.set('pacientes', l); render();
  });
  render();
}

function paginaConsultas() {
  const f = document.getElementById('formConsulta'), tb = document.querySelector('#tabela tbody');
  const pacientes = DB.get('pacientes');
  f.paciente.innerHTML = '<option value="">Selecione</option>' + pacientes.map((p, i) => `<option value="${i}">${esc(p.nome)}</option>`).join('');

  const render = () => {
    const l = DB.get('consultas');
    tb.innerHTML = l.length ? l.map((c, i) => `<tr><td>${esc(c.paciente)}</td><td>${dataBR(c.dataHora)}</td><td>${esc(c.especialidade)}</td>
      <td><span class="tag ${c.status}">${c.status}</span></td>
      <td>${c.status === 'AGENDADA' ? `<button class="perigo" data-i="${i}">Cancelar</button>` : ''}</td></tr>`).join('')
      : '<tr><td colspan="5">Nenhuma consulta agendada.</td></tr>';
  };

  f.addEventListener('submit', e => {
    e.preventDefault();
    const lista = DB.get('consultas'), p = pacientes[f.paciente.value];
    const conflito = p && lista.some(c => c.paciente === p.nome && c.status === 'AGENDADA' && c.dataHora === f.dataHora.value);
    const ok = [
      marcar(f.paciente, p ? '' : 'Selecione um paciente (cadastre um antes, se necessário).'),
      marcar(f.dataHora, !V.futura(f.dataHora.value) ? 'Escolha uma data futura.' : conflito ? 'O paciente já tem consulta neste horário.' : ''),
      marcar(f.especialidade, f.especialidade.value ? '' : 'Selecione a especialidade.')
    ].every(Boolean);
    if (!ok) return aviso('Corrija os campos destacados.', false);
    lista.push({ paciente: p.nome, dataHora: f.dataHora.value, especialidade: f.especialidade.value, status: 'AGENDADA' });
    DB.set('consultas', lista); f.reset(); render(); aviso('Consulta agendada!', true);
  });
  tb.addEventListener('click', e => {
    if (!e.target.dataset.i) return;
    const l = DB.get('consultas'); l[e.target.dataset.i].status = 'CANCELADA'; DB.set('consultas', l); render();
  });
  render();
}

({ inicio: paginaInicio, pacientes: paginaPacientes, consultas: paginaConsultas })[document.body.dataset.pagina]();
