package br.com.clinica.repository;

import java.util.List;
import java.util.Optional;

/** Interface pequena (ISP): quem só consulta dados depende apenas disto. */
public interface Leitura<T> {
    Optional<T> buscarPorId(int id);
    List<T> listarTodos();
}
