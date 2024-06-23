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
    private boolean remPressed;

    public EditDialog(Frame owner, Jogador jogador) {
        super(owner, "Edit Jogador", true);
        this.jogador = jogador;

        // Setup dialog layout
        setLayout(new GridBagLayout());
        setSize(400, 300);
        setLocationRelativeTo(owner);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add some padding around components

        idField = new JTextField(Integer.toString(jogador.getId()), 20);
        idadeField = new JTextField(Integer.toString(jogador.getIdade()), 20);
        nomeJogadorField = new JTextField(jogador.getNomeJogador(), 20);
        nacionalidadeField = new JTextField(jogador.getNacionalidade(), 20);
        nomeClubeField = new JTextField(jogador.getNomeClube(), 20);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2; // Span 2 columns
        add(idField, gbc);

        // Idade
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Reset to 1 column span
        add(new JLabel("Idade:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2; // Span 2 columns
        add(idadeField, gbc);

        // Nome Jogador
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1; // Reset to 1 column span
        add(new JLabel("Nome Jogador:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2; // Span 2 columns
        add(nomeJogadorField, gbc);

        // Nacionalidade
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1; // Reset to 1 column span
        add(new JLabel("Nacionalidade:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2; // Span 2 columns
        add(nacionalidadeField, gbc);

        // Nome Clube
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1; // Reset to 1 column span
        add(new JLabel("Nome Clube:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2; // Span 2 columns
        add(nomeClubeField, gbc);

        // Buttons
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancelar");
        JButton removeButton = new JButton("Remover");

        removeButton.addActionListener(e -> {
            remPressed = true;
            okPressed = false;
            setVisible(false);
        });

        okButton.addActionListener(e -> {
            if (clicouOk()) {
                okPressed = true;
                remPressed = false;
                setVisible(false);
            }
        });

        cancelButton.addActionListener(e -> {
            okPressed = false;
            remPressed = false;
            setVisible(false);
        });

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        add(okButton, gbc);

        gbc.gridx = 1;
        add(cancelButton, gbc);

        gbc.gridx = 2;
        add(removeButton, gbc);

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
                jogador.setNomeJogador(nomeJogadorField.getText().trim().toUpperCase());
            }
            if (!nacionalidadeField.getText().trim().isEmpty()) {
                jogador.setNacionalidade(nacionalidadeField.getText().trim().toUpperCase());
            }
            if (!nomeClubeField.getText().trim().isEmpty()) {
                jogador.setNomeClube(nomeClubeField.getText().trim().toUpperCase());
            }
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "O valor do ID e da Idade do jogador devem ser do tipo int", "ERRO", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean isRemPressed() {
        return remPressed;
    }

    public boolean isOkPressed() {
        return okPressed;
    }

    public Jogador getJogador() {
        return jogador;
    }
}
