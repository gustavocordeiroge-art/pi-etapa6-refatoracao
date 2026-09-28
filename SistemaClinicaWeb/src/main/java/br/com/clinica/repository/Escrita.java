package br.com.clinica.repository;

/** Interface pequena (ISP): operações que alteram dados. */
public interface Escrita<T> {
    T salvar(T entidade);
    void remover(int id);
}
