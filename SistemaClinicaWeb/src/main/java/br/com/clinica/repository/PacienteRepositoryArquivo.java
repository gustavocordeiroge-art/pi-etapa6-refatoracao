package br.com.clinica.repository;

import br.com.clinica.exception.RegraNegocioException;
import br.com.clinica.model.Paciente;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Persiste os pacientes em arquivo CSV. Pode substituir a versão em memória em
 * qualquer lugar (Liskov): mesmo contrato, mesmo comportamento observável.
 */
public class PacienteRepositoryArquivo extends PacienteRepositoryMemoria {
    private final Path arquivo;

    public PacienteRepositoryArquivo(Path arquivo) {
        this.arquivo = arquivo;
        carregar();
    }

    @Override
    public Paciente salvar(Paciente p) {
        Paciente salvo = super.salvar(p);
        persistir();
        return salvo;
    }

    @Override
    public void remover(int id) {
        super.remover(id);
        persistir();
    }

    private void carregar() {
        if (!Files.exists(arquivo)) {
            return;
        }
        try {
            for (String linha : Files.readAllLines(arquivo, StandardCharsets.UTF_8)) {
                if (linha.isBlank()) {
                    continue;
                }
                String[] c = linha.split(";", -1);
                Paciente p = new Paciente(c[1], c[2], LocalDate.parse(c[3]), c[4]);
                p.setId(Integer.parseInt(c[0]));
                dados.put(p.getId(), p);
                proximoId = Math.max(proximoId, p.getId() + 1);
            }
        } catch (IOException | RuntimeException e) {
            throw new RegraNegocioException("Falha ao ler o arquivo de pacientes: " + e.getMessage());
        }
    }

    private void persistir() {
        List<String> linhas = new ArrayList<>();
        for (Paciente p : dados.values()) {
            linhas.add(String.join(";", String.valueOf(p.getId()), limpar(p.getNome()), p.getCpf(),
                    p.getDataNascimento().toString(), limpar(p.getTelefone())));
        }
        try {
            if (arquivo.getParent() != null) {
                Files.createDirectories(arquivo.getParent());
            }
            Files.write(arquivo, linhas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RegraNegocioException("Falha ao gravar o arquivo de pacientes: " + e.getMessage());
        }
    }

    private String limpar(String texto) {
        return texto.replace(';', ',');
    }
}
