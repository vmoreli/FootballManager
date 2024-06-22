package com.footballmanager;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;

public class FootballManagerController {

    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private TextField id;
    @FXML
    private TextField idade;
    @FXML
    private TextField nomeJogador;
    @FXML
    private TextField nacionalidade;
    @FXML
    private TextField nomeClube;
    @FXML
    private ListView<Jogador> jogadoresListView;
    @FXML
    private Button procurar;

    private Socket socket = null;
    private PrintWriter output = null;
    private BufferedReader input = null;

    public void initialize(){
        // Inicializa a listview de jogadores
        jogadoresListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); // Só pode selecionar um jogador por vez
        jogadoresListView.setItems(JogadoresData.getInstance().getJogadores()); // Data bindin com a lista de jogadores
        jogadoresListView.setCellFactory(new JogadorCellFactory()); // CellFactory customizada
        procurar.setDisable(true); // Inicialmente o botão de procurar está desabiltado pois nenhum dataset foi escolhido
        try { // Estabelece a conecção socket
            socket = new Socket("127.0.0.1", 12345);
            socket.setSoTimeout(5000);
            output = new PrintWriter(socket.getOutputStream(), true); // Printer do output
            input = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Leitor do input
        }catch (Exception e){
            // Se a conexão não for estabelecida corretamente mostra um Alerta e fecha o programa
            Alert alert = new Alert(Alert.AlertType.NONE, "FALHA NA CONECÇÃO SOCKET", ButtonType.CLOSE);
            alert.showAndWait();
            Platform.exit();
        }
    }

    @FXML
    public void clicouProcurar(){
        String comando = ""; // Essa string será o comando resultante para o programa em C
        int numCampos = 0; // numero de campos especificados

        if(id.getText().trim().length() != 0){
            // Se tiver algo no campo id tenta converter para int e adiciona na string de comando
            numCampos++;
            comando = comando.concat(" id " + id.getText().trim());
            try{
                Integer.parseInt(id.getText().trim());
            }catch (Exception e){
                // se não der pra converter para int cancela a ação e alerta o usuário
                id.setText("");
                Alert alert = new Alert(Alert.AlertType.NONE, "Valor do campo ID precisa ser um int", ButtonType.OK);
                alert.show();
                return;
            }
        }
        if(idade.getText().trim().length() != 0){
            // Se tiver algo no campo idade tenta converter para int e adiciona na string de comando
            numCampos++;
            comando = comando.concat(" idade " + idade.getText().trim());
            try{
                Integer.parseInt(idade.getText().trim());
            }catch (Exception e){
                // se não der pra converter para int cancela a ação e alerta o usuário
                idade.setText("");
                Alert alert = new Alert(Alert.AlertType.NONE, "Valor do campo IDADE precisa ser um int", ButtonType.OK);
                alert.show();

                return;
            }
        }
        if(nomeJogador.getText().trim().length() != 0){
            // Se tiver algo no campo nomeJogador adiciona na string de comando
            numCampos++;
            comando = comando.concat(" nomeJogador \"" + nomeJogador.getText().trim().toUpperCase() + "\"");
        }
        if(nacionalidade.getText().trim().length() != 0){
            // Se tiver algo no campo nacionalidade adiciona na string de comando
            numCampos++;
            comando = comando.concat(" nacionalidade \"" + nacionalidade.getText().trim().toUpperCase() + "\"");
        }
        if(nomeClube.getText().trim().length() != 0){
            // Se tiver algo no campo nomeClube adiciona na string de comando
            numCampos++;
            comando = comando.concat(" nomeClube \"" + nomeClube.getText().trim().toUpperCase() + "\"");
        }
        comando = Integer.toString(numCampos).concat(comando.concat("\n"));
        comando = "3 binario.bin 1 \n".concat(comando); // adiciona a parte inicial do comando

        if(socket != null && socket.isConnected()){
            output.println(comando); // manda a requisição para o server

            ArrayList<String> listaJog = new ArrayList<>(); // Lista com todos os jogadores lidos da resposta do servidor
            String s; // String temporaria que é cada jogador lido da resposta do servidor
            try {
                s = input.readLine(); // Le um jogador da resposta do server
                while (!s.equals("FIM")){// enquanto a string lida não for o indicador de final da busca, continua lendo os jogadores
                    s = input.readLine(); // Le um jogador da resposta do server

                    if(s.equals("REGISTRO INEXISTENTE")){ // indica que nenhum jogador foi encontrado
                        Alert alert = new Alert(Alert.AlertType.NONE, "Nenhum jogador foi encontrado", ButtonType.OK);
                        alert.show();
                        return;
                    }
                    listaJog.add(s); // Adiciona o jogador lido na lista de strings de jogadores
                }
            }catch (IOException e){
                e.printStackTrace();
            }

            JogadoresData.getInstance().clear(); // Tira todos os jogadores da listView
            JogadoresData.getInstance().loadJogadores(listaJog); // Adiciona os jogadores que satisfazem a busca
        }
    }

    public void clicouAbrir(){
        // Cria um FileChooser
        FileChooser fileChooser = new FileChooser();

        // Define o título do diálogo
        fileChooser.setTitle("Abrir arquivo FIFA 2017 - 2023");

        // Define o diretorio inicial no fileChooser
        File initialDirectory = new File(System.getProperty("user.dir"));
        fileChooser.setInitialDirectory(initialDirectory);
        // Aceita apenas arquivos csv
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("FIFA Files(*.csv)", "*.csv"));

        // Mostra o diálogo e pega o arquivo
        File selectedFile = fileChooser.showOpenDialog(mainBorderPane.getScene().getWindow());

        // Se algum arquivo for selecionado, processa de acordo
        if (selectedFile != null) {
            if(socket != null && socket.isConnected()){
                // Requisição para criar o arquivo binário
                output.println("1 " + selectedFile.getPath() + " binario.bin ");

                try {
                    String s = input.readLine(); // descarta a resposta do server [binarioNaTela()]
                    System.out.println(s);
                }catch (IOException e){
                    e.printStackTrace();
                }

                // Requisição de busca de todos os jogadores
                output.println("2 binario.bin ");
                ArrayList<String> listaJog = new ArrayList<>(); // Lista com todos os jogadores lidos da resposta do servidor
                String s; // String temporaria que é cada jogador lido da resposta do servidor
                try {
                    s = input.readLine(); // Le um jogador da resposta do server
                    while (!s.equals("FIM")){// enquanto a string lida não for o indicador de final da busca, continua lendo os jogadores
                        s = input.readLine(); // Le um jogador da resposta do server
                        listaJog.add(s); // Adiciona o jogador lido na lista de strings de jogadores
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }

                JogadoresData.getInstance().clear(); // Tira todos os jogadores da listView
                JogadoresData.getInstance().loadJogadores(listaJog); // Adiciona os jogadores do arquivo aberto
                procurar.setDisable(false); // Habilita o botão de procurar
            }
        }
    }

    public void clicouFechar(){
        // Confirma se o usuario realmente quer fechar o arquivo
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Todas as alterações serão perdidas!", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Deseja fechar o arquivo?");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.YES) {
            // Limpa a listView e desabilita o botão de procurar
            JogadoresData.getInstance().clear();
            procurar.setDisable(true);
        }
    }

    public void clicouSair(){
        // Confirma se o usuario realmente quer sair da aplicação
        Alert alert = new Alert(Alert.AlertType.WARNING, "Todas as alterações serão perdidas!", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Deseja sair?");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.YES) {
            // fecha a aplicação
            Platform.exit();
        }
    }

    public void clicouJogador(){

        Dialog<ButtonType> editDialog = new Dialog<>();
        editDialog.initOwner(mainBorderPane.getScene().getWindow());
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("edit_dialog.fxml"));
            editDialog.getDialogPane().setContent(loader.load());
        }catch (IOException e){
            e.printStackTrace();
            return;
        }

        editDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        editDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        editDialog.showAndWait();
        if(editDialog.getResult() == ButtonType.OK) {
            JogadoresData.getInstance().clear();
            procurar.setDisable(true);
        }

    }
}