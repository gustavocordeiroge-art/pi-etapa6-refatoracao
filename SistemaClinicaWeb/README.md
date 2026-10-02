# SistemaClinicaWeb

Projeto Integrador, Etapa 6: regras de negócio de uma clínica (pacientes e consultas) separadas de qualquer interface, prontas para reuso em uma aplicação web.

## Estrutura
- `model`: entidades (Paciente, Consulta, StatusConsulta)
- `validation`: regras de validação (padrão Strategy)
- `repository`: interfaces e implementações de persistência (memória e arquivo CSV)
- `service`: regras de negócio (PacienteService, ConsultaService)
- `Main`: composição das classes e testes

## Como executar
- **NetBeans:** File > Open Project > selecione a pasta `SistemaClinicaWeb` > Run Project.
- **Terminal:** `mvn compile exec:java`
- Requer Java 17 ou superior. A saída mostra `[OK]`/`[FALHOU]` para cada teste.

## Testes automatizados (Etapa 7)
- Testes JUnit 5 em `src/test/java` (no NetBeans aparecem em **Test Packages**).
- Classes: `CpfUtilTest`, `IdadeUtilTest`, `CalculadoraDescontoTest`, `ValidadoresTest`, `PacienteServiceTest`, `ConsultaServiceTest` (48 testes).
- **NetBeans:** botão direito no projeto > **Test** (ou Alt + F6). Os resultados aparecem em *Test Results*.
- **Terminal:** `mvn test`
- Plano de testes completo: `Plano_de_Testes.docx`.
