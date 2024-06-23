# Relatório POO – SSC0103: Trabalho Final

## 1. Introdução

### Objetivo do projeto
<p align="justify">
O objetivo do projeto é desenvolver um programa desktop em Java com interface gráfica (GUI) que permita a manipulação de registros dos arquivos de jogadores da FIFA 2017 a FIFA 2023. O sistema deve permitir a busca, visualização, alteração e remoção de dados dos jogadores, além de se conectar a um servidor para interação com um programa em C desenvolvido previamente na disciplina de arquivos.
</p>

### Descrição geral do sistema
<p align="justify">
O sistema consiste em uma GUI desenvolvida em Java que se conecta a um servidor socket escrito em Python. O servidor, por sua vez, comunica-se com um programa em C para executar operações nos arquivos de jogadores. A GUI permite carregar arquivos de diferentes anos, realizar buscas, visualizar resultados, alterar dados e gerar listagens de jogadores.
</p>

### Integrantes do grupo
- Bruno Basckeira Chinaglia
- Giordano Santorum Lorenzetto
- Victor Moreli dos Santos
</p>

## 2. Estrutura do Projeto

### Organização dos diretórios e arquivos do projeto
- **Diretório principal**
  - `FootballManager/`
    - **Diretório de código fonte Java**
      - `EditDialog.java`
      - `FootballManagerMain.java`
      - `Jogador.java`
    - **Servidor socket em Python**
      - `socket_server.py`
    - **Diretório de código fonte C**
      - `ArquivosC/`
        - `create.c`
        - `create.h`
        - `delete.c`
        - `delete.h`
        - `indice.c`
        - `indice.h`
        - `insert.c`
        - `insert.h`
        - `Makefile`
        - `op_bin.c`
        - `op_bin.h`
        - `op_csv.c`
        - `op_csv.h`
        - `programaTrab.c`
        - `reg.h`
        - `select.c`
        - `select.h`
        - `util.c`
        - `util.h`

### Dependências e bibliotecas utilizadas
- Java SE Development Kit (JDK) 11+
- Biblioteca Java Swing para a interface gráfica
- Biblioteca Java Socket para comunicação via socket
- Python 3.8+
- Biblioteca Python `socket`
- Programa em C previamente desenvolvido

## 3. Descrição da Interface Gráfica (GUI)

#### Estrutura Geral da GUI
- **JFrame (FootballManagerMain):**
  - A classe `FootballManagerMain` estende `JFrame`, que é a janela principal da aplicação.
  - Configurações principais são feitas no construtor:
    - **Título e Dimensões**: Define o título da janela (`setTitle`) e o tamanho (`setSize`).
    - **Layout**: Usa um layout de borda (`BorderLayout`) para organizar os componentes.

#### Componentes Principais:
- Painel de Entrada (`inputPanel`):
  - Contém `JTextField` para inserir dados como ID, Idade, Nome do Jogador, Nacionalidade, Nome do Clube.
  - Organizado em um layout de grade (`GridLayout`).
- Painel Central (`scrollPane`):
  - Contém JList (`jogadoresListView`) para exibir uma lista de jogadores.
  - Utiliza um modelo (`listModel`) para gerenciar os dados exibidos na lista.
  - Incorporado em um JScrollPane para suportar rolagem.
- Painel de Botões (`buttonPanel`):
  - Inclui um `JButton` (`procurarButton`) para iniciar uma busca de jogadores.
- Menu (`JMenuBar`):
  - Criado com opções de arquivo (`File`) para abrir, fechar e sair do aplicativo.

#### Inicialização e Conectividade:
- Socket (`socket_server.py`):
  - Estabelece uma conexão de socket TCP com um servidor (`initializeSocket`).
  - Usa `PrintWriter` e `BufferedReader` para comunicação de entrada e saída com o servidor.

#### Funcionalidades Implementadas:
- Busca de Jogadores (`clicouProcurar`):
  - Constrói um comando baseado nos dados inseridos nos campos de texto.
  - Envia o comando para o servidor via socket e exibe os resultados na `JList`.
- Abrir Arquivo (`clicouAbrir`):
  - Permite ao usuário selecionar um arquivo CSV.
  - Envia comandos para o servidor para processar o arquivo e atualizar a interface com os jogadores carregados.
- Editar e Remover Jogadores (`clicouJogador`):
  - Abre um `EditDialog` para editar as informações de um jogador selecionado na `JList`.
  - Envia comandos para o servidor para atualizar informações ou remover jogadores.
- Encerramento e Saída (`clicouFechar`, `clicouSair`):
  - Exibe mensagens de confirmação ao fechar ou sair do aplicativo, limpando dados conforme necessário.

#### Classe `EditDialog`:
- Modal para edição de detalhes de jogador.
- Usa `GridBagLayout` para organizar campos de texto e botões.

#### Classe `Jogador`:
- Representa um jogador com campos como ID, Idade, Nome do Jogador, Nacionalidade e Nome do Clube.
- Possui métodos para obter e definir esses atributos (`getters` e `setters`).

### Campos de texto para busca
- `ID: código identificador do jogador (inteiro, 4 bytes)`
- `Idade: idade do jogador (inteiro, 4 bytes)`
- `Nome Jogador: nome do jogador (string)`
- `Nacionalidade: nacionalidade do jogador (string)`
- `Nome Clube: nome do clube onde o jogador joga (string)`


### Botão "Procurar" e exibição da lista de jogadores
- Ao clicar no botão "Procurar", a lista de jogadores que correspondem aos critérios de busca é exibida abaixo dos campos de texto.


### Diálogo para exibição e edição de dados do jogador
- **Alteração de dados**
  - Permite alterar os dados do jogador selecionado e atualizar o arquivo indexado.
- **Remoção do jogador**
  - Permite remover o jogador selecionado do arquivo indexado.

## 4. Funcionamento Interno do Sistema

### Descrição do fluxo de operações
- **Carregamento de arquivos FIFA**
  - O usuário seleciona o arquivo desejado no menu, e o sistema carrega os dados desse arquivo.
- **Busca de jogadores com base nos campos de texto**
  - O usuário preenche os campos de busca e clica em "Procurar". O sistema envia uma solicitação ao servidor e exibe os resultados na GUI.
- **Exibição e manipulação de resultados**
  - O usuário pode selecionar um jogador da lista e abrir um diálogo para visualizar ou editar seus dados.

### Interação com o servidor socket
- **Conexão e comunicação com o servidor**
  - A GUI estabelece uma conexão via socket com o servidor Python para enviar comandos e receber respostas.
- **Envio de comandos e recepção de respostas**
  - Os comandos de busca, alteração e remoção são enviados pela GUI ao servidor Python, que os encaminha para o programa em C.

### Descrição do servidor socket em Python
- **Recepção de comandos da GUI**
  - O servidor socket em Python recebe comandos enviados pela GUI Java e os processa conforme necessário.
- **Interação com o programa em C via subprocess ou CTypes**
  - Utilizando a biblioteca `subprocess` do Python, o servidor executa o programa em C ou interage com suas funções diretamente através do módulo `CTypes`.
- **Gerenciamento de múltiplas conexões**
  - O servidor é capaz de lidar simultaneamente com várias conexões de diferentes instâncias da GUI. Cada instância tem sua própria cópia do arquivo indexado, permitindo um gerenciamento eficiente e seguro dos dados.
Esta estrutura de interação entre a GUI Java, o servidor Python e o programa em C garante uma comunicação fluida e eficaz, permitindo operações de busca, edição e remoção de dados de jogadores de futebol de maneira segura e confiável.

## 5. Integração com o Programa em C

### Estrutura e funções do programa em C
<p align="justify">
O programa em C realiza operações de leitura, escrita, busca e manipulação dos arquivos de dados dos jogadores.
</p>

### Comunicação entre Python e C

- **Exemplo de chamada de função**
  - A criação do comando para chamar as funções em C é projetado no Java e repassada para o C pelo python, que repassa novamente o resultado elaborado pelo C para o Java mostrar as informações na interface.

- **Tratamento de dados via STDIN**
  - O servidor Python envia dados para o programa em C através da entrada padrão (STDIN). Isso é feito usando subprocessos para executar o programa compilado e passar informações relevantes para processamento.

- **Implementação do servidor em Python**
  - O servidor utiliza a biblioteca `socket` para gerenciar conexões TCP/IP.
  - As conexões dos clientes são tratadas em threads separadas, garantindo a escalabilidade e o gerenciamento eficiente de múltiplas requisições simultâneas.
  - A função `handle_client` processa cada requisição recebida, executando o programa em C e enviando de volta a resposta ao cliente.

- **Execução do programa em C**
  - O programa compilado é executado através de subprocessos controlados pelo servidor Python, permitindo interações seguras e eficientes entre os dois ambientes.

## 6. Instruções de Compilação e Execução

### Requisitos do sistema (Windows e Linux)
- Java SE Development Kit (JDK) 11+
- Python 3.8+
- Compilador C (gcc)

### Passos para compilar o programa em Java
1. Navegar até o diretório `FootballManager/`.
2. Executar o comando `javac *.java`.

### Passos para configurar e iniciar o servidor em Python
1. Navegar até o diretório do servidor Python.
2. Executar o comando `python3 socket_server.py`.

### Passos para compilar e executar o programa em C
1. Ao inicializar o servidor (`socket_server.py`), os programas em C já são previamente compilados. 

### Instruções de execução completa do sistema
1. **Carregar arquivos**
  - Na GUI, selecionar o arquivo FIFA desejado.
2. **Realizar buscas e manipulações de dados**
  - Preencher os campos de busca, clicar em "Procurar", selecionar um jogador da lista e realizar alterações ou remoções.

## 7. Testes e Validação

### Descrição dos testes realizados
- **Testes de funcionalidade da GUI**
  - Teste de carregamento de arquivos.
  - Teste de busca de jogadores.
  - Teste de edição e remoção de dados.
- **Testes de comunicação via socket**
  - Teste de conexão com o servidor.
  - Teste de envio e recepção de comandos.
- **Testes de manipulação de dados no programa em C**
  - Teste de leitura e escrita nos arquivos.
  - Teste de busca e manipulação de registros.

### Resultados dos testes
- **Casos de sucesso**
  - Carregamento correto de arquivos.
  - Busca precisa de jogadores.
  - Alteração e remoção de dados funcionando conforme esperado.

## 8. Conclusões e Considerações Finais

### Resumo do desenvolvimento e funcionalidades implementadas
<p align="justify">
O projeto foi desenvolvido conforme especificado, com a implementação de uma GUI funcional que permite a manipulação de registros de jogadores da FIFA, conexão com um servidor via socket e interação com um programa em C para operações nos arquivos.
</p>

## 9. Referências
- URLs de tutoriais e bibliotecas utilizadas
  - [Java Swing Tutorial](https://www.javatpoint.com/java-swing)
  - [Python Socket Programming](https://realpython.com/python-sockets/)
  - [C Programming Reference](https://www.cprogramming.com/)

## Anexos
- Diagrama da classe Jogador
  ![ClasseJogador](https://github.com/vmoreli/FootballManager/assets/124844938/321ac07d-d15d-4b57-a91e-f8cc67f95021)

- Captura de tela da interface gráfica
  ![TelaInterfaceInicial](https://github.com/vmoreli/FootballManager/assets/124844938/93f79ac8-5e0c-464d-b4c1-feca0bc42053)

### Capturas de execução e resultados de testes

1. **Abrir Arquivo na Interface**
   ![InterfaceAbrirArquivo](https://github.com/vmoreli/FootballManager/assets/124844938/71090bdc-c229-495b-a225-b7321d9294a1)
   - Nesta etapa, o usuário inicia a aplicação e seleciona o arquivo que contém os dados dos jogadores a serem manipulados.

2. **Arquivo Aberto na Interface**
   ![InterfaceArquivoAberto](https://github.com/vmoreli/FootballManager/assets/124844938/345ac8dc-861e-4dbc-8d35-4a0da6eb5718)
   - A interface mostra que o arquivo foi aberto com sucesso, exibindo os dados dos jogadores disponíveis para consulta e modificação.

3. **Buscando um Jogador na Interface**
   ![InterfaceBuscando](https://github.com/vmoreli/FootballManager/assets/124844938/b7ec931b-e98f-42cb-adb6-03bfd7f4c2bc)
   - O usuário realiza uma busca por um jogador específico, fornecendo o ID ou outro critério de busca na interface.

4. **Após a Busca na Interface**
   ![InterfaceAposBusca](https://github.com/vmoreli/FootballManager/assets/124844938/7c0a3451-82d9-4050-b6e0-9691db0c5bae)
   - A interface exibe os detalhes do jogador encontrado após a busca, permitindo que o usuário visualize e verifique as informações atuais.

5. **Remoção de um Jogador na Interface**
   ![InterfaceAposRemocao](https://github.com/vmoreli/FootballManager/assets/124844938/44a9622a-6a6c-4a7d-9bac-5f358d7350e3)
   - Após confirmar a remoção de um jogador específico, a interface exibe a confirmação da operação concluída com sucesso.

6. **Alteração de um Jogador na Interface**
   ![InterfaceAlteracao](https://github.com/vmoreli/FootballManager/assets/124844938/661cb013-2cbb-467f-8065-9c2b424b271f)
   - O usuário inicia o processo de alteração dos dados de um jogador, inserindo novas informações como nome, idade, nacionalidade ou clube.

7. **Alterando um Jogador na Interface**
   ![InterfaceAlterando](https://github.com/vmoreli/FootballManager/assets/124844938/2e6921d5-a482-44fc-94fc-e447a7305bb5)
   - Durante o processo de alteração, a interface mostra o estado atualizado dos campos sendo modificados, garantindo que o usuário revise as alterações antes de confirmar.

8. **Jogador Alterado na Interface**
   ![InterfaceAlterada](https://github.com/vmoreli/FootballManager/assets/124844938/ce0ca167-4c38-4bef-8d1f-6f9999563c24)
   - Após a confirmação das alterações, a interface exibe a confirmação de que os dados do jogador foram atualizados com sucesso.
