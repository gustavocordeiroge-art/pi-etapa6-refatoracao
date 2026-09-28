import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * VERSÃO DESKTOP (ANTES DA REFATORAÇÃO) - mantida apenas como evidência.
 * Reproduz o estilo típico de um formulário gerado no NetBeans, com vários code smells:
 * God Class, Long Method, SQL e regra de negócio dentro da tela, magic strings,
 * nomes genéricos e código duplicado.
 */
public class FormPacienteLegado extends JFrame {
    private JTextField jTextField1 = new JTextField(); // nome
    private JTextField jTextField2 = new JTextField(); // cpf
    private JTextField jTextField3 = new JTextField(); // telefone
    private JButton jButton1 = new JButton("Salvar");
    private JButton jButton2 = new JButton("Agendar");

    public FormPacienteLegado() {
        jButton1.addActionListener(e -> jButton1ActionPerformed());
        jButton2.addActionListener(e -> jButton2ActionPerformed());
    }

    private void jButton1ActionPerformed() {
        String a = jTextField1.getText();
        String b = jTextField2.getText().replaceAll("\\D", "");
        String c = jTextField3.getText().replaceAll("\\D", "");
        if (a == null || a.trim().length() < 3) {
            JOptionPane.showMessageDialog(this, "Nome inválido");
            return;
        }
        if (b.length() != 11) {
            JOptionPane.showMessageDialog(this, "CPF inválido");
            return;
        }
        if (c.length() < 10 || c.length() > 11) {
            JOptionPane.showMessageDialog(this, "Telefone inválido");
            return;
        }
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/clinica", "root", "1234");
            PreparedStatement ps = con.prepareStatement("INSERT INTO paciente (nome, cpf, telefone) VALUES (?,?,?)");
            ps.setString(1, a);
            ps.setString(2, b);
            ps.setString(3, c);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Salvo!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void jButton2ActionPerformed() {
        String b = jTextField2.getText().replaceAll("\\D", ""); // duplicado do método acima
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/clinica", "root", "1234");
            PreparedStatement ps = con.prepareStatement("INSERT INTO consulta (cpf, status) VALUES (?, 'AGENDADA')");
            ps.setString(1, b);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Agendado!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
}
