import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditDialog extends JDialog {

    private JTextField idField;
    private JTextField idadeField;
    private JTextField nomeJogadorField;
    private JTextField nacionalidadeField;
    private JTextField nomeClubeField;
    private Jogador jogador;
    private boolean okPressed;

    public EditDialog(Frame owner, Jogador jogador) {
        super(owner, "Edit Jogador", true);
        this.jogador = jogador;

        // Setup dialog layout
        setLayout(new GridLayout(6, 2));
        setSize(400, 300);
        setLocationRelativeTo(owner);

        idField = new JTextField(Integer.toString(jogador.getId()));
        idadeField = new JTextField(Integer.toString(jogador.getIdade()));
        nomeJogadorField = new JTextField(jogador.getNomeJogador());
        nacionalidadeField = new JTextField(jogador.getNacionalidade());
        nomeClubeField = new JTextField(jogador.getNomeClube());

        add(new JLabel("ID:"));
        add(idField);
        add(new JLabel("Idade:"));
        add(idadeField);
        add(new JLabel("Nome Jogador:"));
        add(nomeJogadorField);
        add(new JLabel("Nacionalidade:"));
        add(nacionalidadeField);
        add(new JLabel("Nome Clube:"));
        add(nomeClubeField);

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clicouOk()) {
                    okPressed = true;
                    setVisible(false);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                okPressed = false;
                setVisible(false);
            }
        });

        add(okButton);
        add(cancelButton);

        pack();
    }

    private boolean clicouOk() {
        try {
            if (!idField.getText().trim().isEmpty()) {
                jogador.setId(Integer.parseInt(idField.getText().trim()));
            }
            if (!idadeField.getText().trim().isEmpty()) {
                jogador.setIdade(Integer.parseInt(idadeField.getText().trim()));
            }
            if (!nomeJogadorField.getText().trim().isEmpty()) {
                jogador.setNomeJogador(nomeJogadorField.getText().trim());
            }
            if (!nacionalidadeField.getText().trim().isEmpty()) {
                jogador.setNacionalidade(nacionalidadeField.getText().trim());
            }
            if (!nomeClubeField.getText().trim().isEmpty()) {
                jogador.setNomeClube(nomeClubeField.getText().trim());
            }
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for ID and Idade.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean isOkPressed() {
        return okPressed;
    }

    public Jogador getJogador() {
        return jogador;
    }
}
