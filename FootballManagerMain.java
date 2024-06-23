import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class FootballManagerMain extends JFrame {

    private JTextField idField;
    private JTextField idadeField;
    private JTextField nomeJogadorField;
    private JTextField nacionalidadeField;
    private JTextField nomeClubeField;
    private JList<Jogador> jogadoresListView;
    private DefaultListModel<Jogador> listModel;
    private JButton procurarButton;

    private Socket socket = null;
    private PrintWriter output = null;
    private BufferedReader input = null;

    public FootballManagerMain() {
        // Setup JFrame
        setTitle("Football Manager");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel for input fields
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        idField = new JTextField();
        idadeField = new JTextField();
        nomeJogadorField = new JTextField();
        nacionalidadeField = new JTextField();
        nomeClubeField = new JTextField();

        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Idade:"));
        inputPanel.add(idadeField);
        inputPanel.add(new JLabel("Nome Jogador:"));
        inputPanel.add(nomeJogadorField);
        inputPanel.add(new JLabel("Nacionalidade:"));
        inputPanel.add(nacionalidadeField);
        inputPanel.add(new JLabel("Nome Clube:"));
        inputPanel.add(nomeClubeField);

        add(inputPanel, BorderLayout.NORTH);

        // Center panel for list view
        listModel = new DefaultListModel<>();
        jogadoresListView = new JList<>(listModel);
        jogadoresListView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(jogadoresListView);

        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel for buttons
        JPanel buttonPanel = new JPanel();
        procurarButton = new JButton("Procurar");

        procurarButton.setEnabled(false);

        buttonPanel.add(procurarButton);

        add(buttonPanel, BorderLayout.SOUTH);

        procurarButton.addActionListener(e -> clicouProcurar());
        jogadoresListView.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                clicouJogador();
            }
        });

        createMenuBar();

        // Initialize socket connection
        initializeSocket();

        // Show the JFrame
        setVisible(true);
    }


    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem abrirItem = new JMenuItem("Abrir");
        abrirItem.addActionListener(e -> clicouAbrir());
        fileMenu.add(abrirItem);

        JMenuItem fecharItem = new JMenuItem("Fechar");
        fecharItem.addActionListener(e -> clicouFechar());
        fileMenu.add(fecharItem);

        JMenuItem sairItem = new JMenuItem("Sair");
        sairItem.addActionListener(e -> clicouSair());
        fileMenu.add(sairItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }
    private void initializeSocket() {
        try {
            socket = new Socket("127.0.0.1", 12345);
            socket.setSoTimeout(5000);
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "FALHA NA CONECcaO SOCKET", "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void clicouProcurar() {
        String comando = "";
        int numCampos = 0;

        if (!idField.getText().trim().isEmpty()) {
            numCampos++;
            comando = comando.concat(" id " + idField.getText().trim());
            try {
                Integer.parseInt(idField.getText().trim());
            } catch (Exception e) {
                idField.setText("");
                JOptionPane.showMessageDialog(this, "Valor do campo ID precisa ser um int", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (!idadeField.getText().trim().isEmpty()) {
            numCampos++;
            comando = comando.concat(" idade " + idadeField.getText().trim());
            try {
                Integer.parseInt(idadeField.getText().trim());
            } catch (Exception e) {
                idadeField.setText("");
                JOptionPane.showMessageDialog(this, "Valor do campo IDADE precisa ser um int", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (!nomeJogadorField.getText().trim().isEmpty()) {
            numCampos++;
            comando = comando.concat(" nomeJogador \"" + nomeJogadorField.getText().trim().toUpperCase() + "\"");
        }
        if (!nacionalidadeField.getText().trim().isEmpty()) {
            numCampos++;
            comando = comando.concat(" nacionalidade \"" + nacionalidadeField.getText().trim().toUpperCase() + "\"");
        }
        if (!nomeClubeField.getText().trim().isEmpty()) {
            numCampos++;
            comando = comando.concat(" nomeClube \"" + nomeClubeField.getText().trim().toUpperCase() + "\"");
        }
        comando = Integer.toString(numCampos).concat(comando.concat("\n"));
        comando = "3 binario.bin 1 \n".concat(comando);

        if (socket != null && socket.isConnected()) {
            output.println(comando);

            ArrayList<String> listaJog = new ArrayList<>();
            String s;
            try {
                s = input.readLine();
                while (!s.equals("FIM")) {
                    if (s.equals("REGISTRO INEXISTENTE")) {
                        JOptionPane.showMessageDialog(this, "Nenhum jogador foi encontrado", "Informacao", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    listaJog.add(s);
                    s = input.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            listModel.clear();
            for (String jogString : listaJog) {
                listModel.addElement(new Jogador(jogString));
            }
        }
    }

    private void clicouAbrir() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Abrir arquivo FIFA 2017 - 2023");
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("FIFA Files (*.csv)", "csv"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (socket != null && socket.isConnected()) {
                output.println("1 " + selectedFile.getPath() + " binario.bin ");
                try {
                    String s = input.readLine();
                    if(s.equals("Falha no processamento do arquivo.")){
                        JOptionPane.showMessageDialog(this, "Falha na abertura do arquivo.", "Informacao", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    System.out.println(s);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                output.println("4 binario.bin indice.bin ");
                try {
                    String s = input.readLine();
                    if(s.equals("Falha no processamento do arquivo.")){
                        JOptionPane.showMessageDialog(this, "Falha na abertura do arquivo.", "Informacao", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    System.out.println(s);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                output.println("2 binario.bin ");
                ArrayList<String> listaJog = new ArrayList<>();
                String s;
                try {
                    s = input.readLine();
                    while (!s.equals("FIM")) {
                        listaJog.add(s);
                        s = input.readLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                listModel.clear();
                for (String jogString : listaJog) {
                    listModel.addElement(new Jogador(jogString));
                }
                procurarButton.setEnabled(true);
            }
        }
    }

    private void clicouFechar() {
        int result = JOptionPane.showConfirmDialog(this, "Todas as alteracões serao perdidas!", "Deseja fechar o arquivo?", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            listModel.clear();
            procurarButton.setEnabled(false);
        }
    }

    private void clicouSair() {
        int result = JOptionPane.showConfirmDialog(this, "Todas as alteracões serao perdidas!", "Deseja sair?", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void clicouJogador() {
        Jogador jogador = jogadoresListView.getSelectedValue();
        if (jogador != null) {
            String jogOriginal = jogador.toString1();
            EditDialog editDialog = new EditDialog(this, jogador);
            editDialog.setVisible(true);

            if (editDialog.isOkPressed()) {
                Jogador jogEditado = editDialog.getJogador();

                if (socket != null && socket.isConnected()) {
                    output.println("5 binario.bin indice.bin 1 \n" + jogOriginal);

                    try {
                        String s = input.readLine();
                        System.out.println(s);
                        s = input.readLine();
                        System.out.println(s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    output.println("6 binario.bin indice.bin 1 \n" + jogEditado);

                    try {
                        String s = input.readLine();
                        System.out.println(s);
                        s = input.readLine();
                        System.out.println(s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    output.println("2 binario.bin ");
                    ArrayList<String> listaJog = new ArrayList<>();
                    String s;
                    try {
                        s = input.readLine();
                        while (!s.equals("FIM")) {
                            listaJog.add(s);
                            s = input.readLine();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    listModel.clear();
                    for (String jogString : listaJog) {
                        listModel.addElement(new Jogador(jogString));
                    }
                    procurarButton.setEnabled(true);
                }
            }
            if (editDialog.isRemPressed()) {
                if (socket != null && socket.isConnected()) {
                    output.println("5 binario.bin indice.bin 1 \n" + jogOriginal);

                    try {
                        String s = input.readLine();
                        System.out.println(s);
                        s = input.readLine();
                        System.out.println(s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    output.println("2 binario.bin ");
                    ArrayList<String> listaJog = new ArrayList<>();
                    String s;
                    try {
                        s = input.readLine();
                        while (!s.equals("FIM")) {
                            listaJog.add(s);
                            s = input.readLine();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    listModel.clear();
                    for (String jogString : listaJog) {
                        listModel.addElement(new Jogador(jogString));
                    }
                    procurarButton.setEnabled(true);
                }
            }
        }
    }

    public static void main(String[] args) {

        // Run a command line command before initializing Swing components
        try {
            // Example command to execute
            String command = "python socket_server.py";

            // Create ProcessBuilder for the command
            ProcessBuilder pb = new ProcessBuilder(command.split(" "));
            pb.redirectErrorStream(true); // Redirect error stream to output stream

            // Start the process
            Process process = pb.start();

            Thread pythonThread = new Thread(() -> {
                try {
                    int exitCode = process.waitFor();
                    System.out.println("Python process finished with exit code: " + exitCode);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            pythonThread.start();
            Thread.sleep(5000L);

        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(FootballManagerMain::new);
    }
}