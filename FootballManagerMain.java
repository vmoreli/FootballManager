import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class FootballManagerMain extends JFrame {

    // Declaração dos campos de entrada de texto
    private JTextField idField;
    private JTextField idadeField;
    private JTextField nomeJogadorField;
    private JTextField nacionalidadeField;
    private JTextField nomeClubeField;

    // Declaração do JList para exibir os jogadores e o modelo de lista
    private JList<Jogador> jogadoresListView;
    private DefaultListModel<Jogador> listModel;

    // Botão para procurar jogadores
    private JButton procurarButton;

    // Variáveis de conexão de socket
    private Socket socket = null;
    private PrintWriter output = null;
    private BufferedReader input = null;

    public FootballManagerMain() {
        // Configuração inicial do JFrame
        setTitle("Football Manager");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel superior para campos de entrada
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        idField = new JTextField();
        idadeField = new JTextField();
        nomeJogadorField = new JTextField();
        nacionalidadeField = new JTextField();
        nomeClubeField = new JTextField();

        // Adiciona os campos de entrada ao painel superior
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

        // Adiciona o painel superior ao JFrame
        add(inputPanel, BorderLayout.NORTH);

        // Painel central para exibir a lista de jogadores
        listModel = new DefaultListModel<>();
        jogadoresListView = new JList<>(listModel);
        jogadoresListView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(jogadoresListView);

        // Adiciona o painel central ao JFrame
        add(scrollPane, BorderLayout.CENTER);

        // Painel inferior para os botões
        JPanel buttonPanel = new JPanel();
        procurarButton = new JButton("Procurar");

        // Desabilita o botão de procurar inicialmente
        procurarButton.setEnabled(false);

        //Adiciona o botão ao painel inferior
        buttonPanel.add(procurarButton);

        // Adiciona o painel inferior ao JFrame
        add(buttonPanel, BorderLayout.SOUTH);

        // Event Handler do botão de procurar
        procurarButton.addActionListener(e -> clicouProcurar());
        jogadoresListView.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                clicouJogador();
            }
        });

        // Cria a barra de menu
        createMenuBar();

        // Inicializa a conexão socket
        initializeSocket();

        // Exibe o JFrame
        setVisible(true);
    }


    // Método para criar a barra de menu
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        // Cria os itens do menu e define suas ações
        JMenuItem abrirItem = new JMenuItem("Abrir");
        abrirItem.addActionListener(e -> clicouAbrir());
        fileMenu.add(abrirItem);

        JMenuItem fecharItem = new JMenuItem("Fechar");
        fecharItem.addActionListener(e -> clicouFechar());
        fileMenu.add(fecharItem);

        JMenuItem sairItem = new JMenuItem("Sair");
        sairItem.addActionListener(e -> clicouSair());
        fileMenu.add(sairItem);

        // Adiciona o menu de arquivo à barra de menu
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    // Metodo para inicializar a conexão socket
    private void initializeSocket() {
        try {
            // Estabelece a conexão de socket com o servidor
            socket = new Socket("127.0.0.1", 12345);
            socket.setSoTimeout(5000); // Define um timeout para a conexão
            output = new PrintWriter(socket.getOutputStream(), true); // Inicializa o escritor de saída
            input = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Inicializa o leitor de entrada
        } catch (Exception e) {
            // Mostra uma mensagem de erro se a conexão falhar
            JOptionPane.showMessageDialog(this, "FALHA NA CONECcaO SOCKET", "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(0); // Encerra o programa
        }
    }

    // Event Handler do botão procurar
    private void clicouProcurar() {
        String comando = "";
        int numCampos = 0;

        // Verifica e valida os campos de entrada
        if (!idField.getText().trim().isEmpty()) {
            numCampos++;
            comando = comando.concat(" id " + idField.getText().trim());
            try {
                // Verifica se o campo ID é um inteiro
                Integer.parseInt(idField.getText().trim());
            } catch (Exception e) {
                idField.setText("");
                // Mostra mensagem de erro se o valor do campo ID não for um inteiro
                JOptionPane.showMessageDialog(this, "Valor do campo ID precisa ser um int", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (!idadeField.getText().trim().isEmpty()) {
            numCampos++;
            comando = comando.concat(" idade " + idadeField.getText().trim());
            try {
                // Verifica se o campo Idade é um inteiro
                Integer.parseInt(idadeField.getText().trim());
            } catch (Exception e) {
                idadeField.setText("");
                // Mostra mensagem de erro se o valor do campo Idade não for um inteiro
                JOptionPane.showMessageDialog(this, "Valor do campo IDADE precisa ser um int", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (!nomeJogadorField.getText().trim().isEmpty()) {
            numCampos++;
            // Adiciona o campo nome do jogador ao comando, convertendo o valor para maiúsculas
            comando = comando.concat(" nomeJogador \"" + nomeJogadorField.getText().trim().toUpperCase() + "\"");
        }
        if (!nacionalidadeField.getText().trim().isEmpty()) {
            numCampos++;
            // Adiciona o campo nacionalidade ao comando, convertendo o valor para maiúsculas
            comando = comando.concat(" nacionalidade \"" + nacionalidadeField.getText().trim().toUpperCase() + "\"");
        }
        if (!nomeClubeField.getText().trim().isEmpty()) {
            numCampos++;
            // Adiciona o campo nome do clube ao comando, convertendo o valor para maiúsculas
            comando = comando.concat(" nomeClube \"" + nomeClubeField.getText().trim().toUpperCase() + "\"");
        }

        // Finaliza a construção do comando
        comando = Integer.toString(numCampos).concat(comando.concat("\n"));
        comando = "3 binario.bin 1 \n".concat(comando);

        // Envia o comando ao servidor através da conexão socket
        if (socket != null && socket.isConnected()) {
            output.println(comando);

            ArrayList<String> listaJog = new ArrayList<>();
            String s;
            try {
                // Leitura da resposta do servidor
                s = input.readLine();
                // Verifica se houve falha no processamento do arquivo
                if(s.equals("Falha no processamento do arquivo.")){
                    // Mostra uma mensagem informando a falha na abertura do arquivo
                    JOptionPane.showMessageDialog(this, "Falha na abertura do arquivo.", "Informacao", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                while (!s.equals("FIM")) {
                    if (s.equals("REGISTRO INEXISTENTE")) {
                        // Mostra mensagem se nenhum jogador for encontrado
                        JOptionPane.showMessageDialog(this, "Nenhum jogador foi encontrado", "Informacao", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    // Adiciona cada jogador encontrado à lista
                    listaJog.add(s);
                    s = input.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Após ler toda a resposta do servidor atualiza a lista de jogadores
            listModel.clear();
            for (String jogString : listaJog) {
                listModel.addElement(new Jogador(jogString));
            }
        }
    }


    // Event Handler do botão de abrir arquivo
    private void clicouAbrir() {
        // Cria um JFileChooser para abrir um arquivo
        JFileChooser fileChooser = new JFileChooser();
        // Define o título da caixa de diálogo do JFileChooser
        fileChooser.setDialogTitle("Abrir arquivo FIFA 2017 - 2023");
        // Define o diretório atual como diretório inicial do JFileChooser
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        // Define um filtro para mostrar apenas arquivos com extensão .csv
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("FIFA Files (*.csv)", "csv"));

        // Mostra a caixa de diálogo para abrir arquivo e captura o resultado
        int result = fileChooser.showOpenDialog(this);
        // Verifica se o usuário aprovou a seleção do arquivo
        if (result == JFileChooser.APPROVE_OPTION) {
            // Obtém o arquivo selecionado
            File selectedFile = fileChooser.getSelectedFile();
            // Verifica se o socket está conectado
            if (socket != null && socket.isConnected()) {
                // Envia um comando para o servidor processar o arquivo selecionado e salvar como binario.bin
                output.println("1 " + selectedFile.getPath() + " binario.bin ");
                try {
                    // Lê a resposta do servidor
                    String s = input.readLine();
                    // Verifica se houve falha no processamento do arquivo
                    if(s.equals("Falha no processamento do arquivo.")){
                        // Mostra uma mensagem informando a falha na abertura do arquivo
                        JOptionPane.showMessageDialog(this, "Falha na abertura do arquivo.", "Informacao", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Envia um comando para o servidor criar um índice a partir do binario.bin
                output.println("4 binario.bin indice.bin ");
                try {
                    // Lê a resposta do servidor
                    String s = input.readLine();
                    // Verifica se houve falha no processamento do arquivo
                    if(s.equals("Falha no processamento do arquivo.")){
                        // Mostra uma mensagem informando a falha na abertura do arquivo
                        JOptionPane.showMessageDialog(this, "Falha na abertura do arquivo.", "Informacao", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Envia um comando para o servidor ler os dados do binario.bin
                output.println("2 binario.bin ");
                // Cria uma lista para armazenar os dados dos jogadores
                ArrayList<String> listaJog = new ArrayList<>();
                String s;
                try {
                    // Lê os dados dos jogadores até encontrar "FIM"
                    s = input.readLine();
                    // Verifica se houve falha no processamento do arquivo
                    if(s.equals("Falha no processamento do arquivo.")){
                        // Mostra uma mensagem informando a falha na abertura do arquivo
                        JOptionPane.showMessageDialog(this, "Falha na abertura do arquivo.", "Informacao", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    while (!s.equals("FIM")) {
                        listaJog.add(s);
                        s = input.readLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Limpa o modelo da lista antes de adicionar novos dados
                listModel.clear();
                // Adiciona cada jogador no modelo da lista
                for (String jogString : listaJog) {
                    listModel.addElement(new Jogador(jogString));
                }
                // Habilita o botão de procurar
                procurarButton.setEnabled(true);
            }
        }
    }

    // Event Handler do botão fechar
    private void clicouFechar() {
        // Exibe uma caixa de diálogo de confirmação com a mensagem e opções Sim/Não
        int result = JOptionPane.showConfirmDialog(this, "Todas as alterações serão perdidas!", "Deseja fechar o arquivo?", JOptionPane.YES_NO_OPTION);
        // Verifica se o usuário clicou em "Sim"
        if (result == JOptionPane.YES_OPTION) {
            // Limpa o modelo da lista
            listModel.clear();
            // Desabilita o botão de procurar
            procurarButton.setEnabled(false);
        }
    }

    // Event Handler do botão sair
    private void clicouSair() {
        // Exibe uma caixa de diálogo de confirmação com a mensagem e opções Sim/Não
        int result = JOptionPane.showConfirmDialog(this, "Todas as alterações serão perdidas!", "Deseja sair?", JOptionPane.YES_NO_OPTION);
        // Verifica se o usuário clicou em "Sim"
        if (result == JOptionPane.YES_OPTION) {
            // Encerra a aplicação
            System.exit(0);
        }
    }

    // Event Handler para os jogadores da lista de jogadores
    private void clicouJogador() {
        // Obtém o jogador selecionado na lista de visualização
        Jogador jogador = jogadoresListView.getSelectedValue();
        if (jogador != null) {
            // Obtém a representação original do jogador como string
            String jogOriginal = jogador.toString1();
            // Cria e exibe o diálogo de edição do jogador
            EditDialog editDialog = new EditDialog(this, jogador);
            editDialog.setVisible(true);

            // Verifica se o botão OK foi pressionado no diálogo de edição
            if (editDialog.isOkPressed()) {
                // Obtém o jogador editado a partir do diálogo
                Jogador jogEditado = editDialog.getJogador();

                // Verifica se o socket está conectado
                if (socket != null && socket.isConnected()) {
                    // Envia comando ao servidor para remover o jogador original
                    output.println("5 binario.bin indice.bin 1 \n" + jogOriginal);

                    try {
                        // Lê a resposta do servidor e imprime no console
                        String s = input.readLine();
                        // Verifica se houve falha no processamento do arquivo
                        if(s.equals("Falha no processamento do arquivo.")){
                            // Mostra uma mensagem informando a falha na abertura do arquivo
                            JOptionPane.showMessageDialog(this, "Falha na abertura do arquivo.", "Informacao", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        s = input.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Envia comando ao servidor para adicionar o jogador editado
                    output.println("6 binario.bin indice.bin 1 \n" + jogEditado);

                    try {
                        // Lê a resposta do servidor e imprime no console
                        String s = input.readLine();
                        // Verifica se houve falha no processamento do arquivo
                        if(s.equals("Falha no processamento do arquivo.")){
                            // Mostra uma mensagem informando a falha na abertura do arquivo
                            JOptionPane.showMessageDialog(this, "Falha na abertura do arquivo.", "Informacao", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        s = input.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Envia comando ao servidor para obter a lista atualizada de jogadores
                    output.println("2 binario.bin ");
                    ArrayList<String> listaJog = new ArrayList<>();
                    String s;
                    try {
                        // Lê a lista de jogadores até encontrar "FIM"
                        s = input.readLine();
                        // Verifica se houve falha no processamento do arquivo
                        if(s.equals("Falha no processamento do arquivo.")){
                            // Mostra uma mensagem informando a falha na abertura do arquivo
                            JOptionPane.showMessageDialog(this, "Falha na abertura do arquivo.", "Informacao", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        while (!s.equals("FIM")) {

                            listaJog.add(s);
                            s = input.readLine();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Atualiza o modelo da lista com a lista de jogadores
                    listModel.clear();
                    for (String jogString : listaJog) {
                        listModel.addElement(new Jogador(jogString));
                    }
                    // Habilita o botão de procurar
                    procurarButton.setEnabled(true);
                }
            }

            // Verifica se o botão Remover foi pressionado no diálogo de edição
            if (editDialog.isRemPressed()) {
                // Verifica se o socket está conectado
                if (socket != null && socket.isConnected()) {
                    // Envia comando ao servidor para remover o jogador original
                    output.println("5 binario.bin indice.bin 1 \n" + jogOriginal);

                    try {
                        // Lê a resposta do servidor e imprime no console
                        String s = input.readLine();
                        // Verifica se houve falha no processamento do arquivo
                        if(s.equals("Falha no processamento do arquivo.")){
                            // Mostra uma mensagem informando a falha na abertura do arquivo
                            JOptionPane.showMessageDialog(this, "Falha na abertura do arquivo.", "Informacao", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        s = input.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Envia comando ao servidor para obter a lista atualizada de jogadores
                    output.println("2 binario.bin ");
                    ArrayList<String> listaJog = new ArrayList<>();
                    String s;
                    try {
                        // Lê a lista de jogadores até encontrar "FIM"
                        s = input.readLine();
                        // Verifica se houve falha no processamento do arquivo
                        if(s.equals("Falha no processamento do arquivo.")){
                            // Mostra uma mensagem informando a falha na abertura do arquivo
                            JOptionPane.showMessageDialog(this, "Falha na abertura do arquivo.", "Informacao", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        while (!s.equals("FIM")) {
                            listaJog.add(s);
                            s = input.readLine();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Atualiza o modelo da lista com a lista de jogadores
                    listModel.clear();
                    for (String jogString : listaJog) {
                        listModel.addElement(new Jogador(jogString));
                    }
                    // Habilita o botão de procurar
                    procurarButton.setEnabled(true);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Comando para iniciar o script Python
            String command = "python3 socket_server.py";

            // Cria um ProcessBuilder para executar o comando
            ProcessBuilder pb = new ProcessBuilder(command.split(" "));
            // Redireciona o fluxo de erro para o fluxo de saída padrão
            pb.redirectErrorStream(true);

            // Inicia o processo do script Python
            Process process = pb.start();

            // Cria uma nova thread para aguardar a conclusão do processo Python
            Thread pythonThread = new Thread(() -> {
                try {
                    // Espera pelo término do processo Python e obtém o código de saída
                    int exitCode = process.waitFor();
                    // Imprime o código de saída do processo Python
                    System.out.println("Python process finished with exit code: " + exitCode);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            // Inicia a thread para monitorar o processo Python
            pythonThread.start();
            // Aguarda 2 segundos para garantir que o servidor Python esteja em execução
            Thread.sleep(2000L);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Inicia a aplicação Swing invocando o construtor de FootballManagerMain
        SwingUtilities.invokeLater(FootballManagerMain::new);
    }

}