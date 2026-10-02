/* Regras de validação (espelham as regras RN01-RN09 do back-end Java). */
const V = {
  nome: n => n.trim().length >= 3,
  cpf(c) {
    const d = c.replace(/\D/g, '');
    if (d.length !== 11 || /^(\d)\1+$/.test(d)) return false;
    const dig = n => { let s = 0; for (let i = 0; i < n; i++) s += d[i] * (n + 1 - i); const r = (s * 10) % 11; return r === 10 ? 0 : r; };
    return dig(9) == d[9] && dig(10) == d[10];
  },
  telefone(t) { const n = t.replace(/\D/g, '').length; return n === 10 || n === 11; },
  nascimento: d => !!d && new Date(d + 'T00:00') <= new Date(),
  futura: dt => !!dt && new Date(dt) > new Date(),
  idade(d) {
    const n = new Date(d + 'T00:00'), h = new Date();
    let i = h.getFullYear() - n.getFullYear();
    if (h < new Date(h.getFullYear(), n.getMonth(), n.getDate())) i--;
    return i;
  },
  desconto: idade => idade < 12 ? 0.2 : idade >= 60 ? 0.3 : 0,
  mascaraCpf(v) {
    const d = v.replace(/\D/g, '').slice(0, 11);
    return d.replace(/(\d{3})(\d)/, '$1.$2').replace(/(\d{3})(\d)/, '$1.$2').replace(/(\d{3})(\d{1,2})$/, '$1-$2');
  },
  mascaraTel(v) {
    const d = v.replace(/\D/g, '').slice(0, 11);
    return d.length > 10 ? d.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3') : d.replace(/(\d{2})(\d{4})(\d{0,4})/, '($1) $2-$3').replace(/-$/, '');
  }
};
