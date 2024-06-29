# Football Manager

## Introdução

### Objetivo do Projeto
Desenvolver um programa desktop em Java com interface gráfica (GUI) para manipulação de registros de jogadores da FIFA 2017 a FIFA 2023. O sistema deve permitir a busca, visualização, alteração e remoção de dados dos jogadores, além de se conectar a um servidor para interação com um programa em C previamente desenvolvido.

### Descrição Geral do Sistema
O sistema consiste em uma GUI desenvolvida em Java que se conecta a um servidor socket em Python, que por sua vez se comunica com um programa em C para executar operações nos arquivos de jogadores. A GUI permite carregar arquivos de diferentes anos, realizar buscas, visualizar resultados, alterar dados e gerar listagens de jogadores.

### Integrantes do Grupo
- Bruno Basckeira Chinaglia
- Giordano Santorum Lorenzetto
- Victor Moreli dos Santos

## Estrutura do Projeto

### Organização dos Diretórios e Arquivos
- `FootballManager/`
  - **Código fonte Java**
    - `EditDialog.java`
    - `FootballManagerMain.java`
    - `Jogador.java`
  - **Servidor socket em Python**
    - `socket_server.py`
  - **Código fonte C**
    - `ArquivosC/`
      - `create.c`, `create.h`
      - `delete.c`, `delete.h`
      - `indice.c`, `indice.h`
      - `insert.c`, `insert.h`
      - `Makefile`
      - `op_bin.c`, `op_bin.h`
      - `op_csv.c`, `op_csv.h`
      - `programaTrab.c`
      - `reg.h`
      - `select.c`, `select.h`
      - `util.c`, `util.h`

### Dependências e Bibliotecas Utilizadas
- Java SE Development Kit (JDK) 11+
- Biblioteca Java Swing para GUI
- Biblioteca Java Socket para comunicação
- Python 3.8+
- Biblioteca Python `socket`
- Programa em C previamente desenvolvido

## Descrição da Interface Gráfica (GUI)

### Estrutura Geral da GUI
- **JFrame (FootballManagerMain)**:
  - Janela principal da aplicação com layout `BorderLayout`.
  - Painéis organizados para entrada de dados, exibição de lista e botões de controle.

### Componentes Principais
- **Painel de Entrada**: Campos de texto para inserir dados como ID, idade, nome, nacionalidade e clube do jogador.
- **Painel Central**: JList para exibir uma lista de jogadores com suporte a rolagem.
- **Painel de Botões**: Botão para iniciar busca de jogadores.
- **Menu**: Opções para abrir, fechar e sair do aplicativo.

### Funcionalidades Implementadas
- **Busca de Jogadores**: Envia comandos ao servidor e exibe resultados na JList.
- **Abrir Arquivo**: Seleção e carregamento de arquivos CSV.
- **Editar e Remover Jogadores**: Abre diálogo para editar ou remover jogadores.
- **Encerramento e Saída**: Mensagens de confirmação ao fechar ou sair do aplicativo.

### Classe `EditDialog`
- Modal para edição de detalhes de jogador usando `GridBagLayout`.

### Classe `Jogador`
- Representa um jogador com métodos `getters` e `setters`.

## Funcionamento Interno do Sistema

### Descrição do Fluxo de Operações
- **Carregamento de Arquivos FIFA**: Seleção e carregamento de dados.
- **Busca de Jogadores**: Envio de solicitação ao servidor e exibição de resultados.
- **Manipulação de Resultados**: Edição e remoção de dados.

### Interação com o Servidor Socket
- **Conexão e Comunicação**: Estabelece conexão TCP com servidor Python.
- **Envio de Comandos e Recepção de Respostas**: Comandos de busca, alteração e remoção são enviados e processados.

### Descrição do Servidor Socket em Python
- **Recepção de Comandos**: Processa comandos recebidos da GUI.
- **Interação com o Programa em C**: Executa programa em C usando subprocessos.
- **Gerenciamento de Conexões**: Lida com múltiplas conexões simultâneas.

## Integração com o Programa em C

### Estrutura e Funções do Programa em C
Realiza operações de leitura, escrita, busca e manipulação dos arquivos de dados dos jogadores.

### Comunicação entre Python e C
- **Chamada de Função**: Comandos são passados do Java para o C através do Python.
- **Tratamento de Dados via STDIN**: Envio de dados para o programa em C através da entrada padrão.
- **Execução do Programa em C**: Executado por subprocessos controlados pelo servidor Python.

## Instruções de Compilação e Execução

### Requisitos do Sistema
- Java SE Development Kit (JDK) 11+
- Python 3.8+
- Compilador C (gcc)

### Passos para Compilar o Programa em Java
1. Navegar até o diretório `FootballManager/`.
2. Executar `javac *.java`.

### Passos para Configurar e Iniciar o Servidor em Python
1. Executar `java FootballManagerMain` para rodar a interface e inicializar o servidor.

### Passos para Compilar e Executar o Programa em C
1. Ao inicializar o servidor (`socket_server.py`), os programas em C já são previamente compilados.

### Instruções de Execução Completa do Sistema
1. **Carregar Arquivos**: Selecionar o arquivo FIFA desejado na GUI.
2. **Realizar Buscas e Manipulações de Dados**: Preencher campos de busca, clicar em "Procurar", selecionar um jogador e realizar alterações ou remoções.

## Conclusões e Considerações Finais

### Resumo do Desenvolvimento e Funcionalidades Implementadas
O projeto foi desenvolvido conforme especificado, com a implementação de uma GUI funcional que permite a manipulação de registros de jogadores da FIFA, conexão com um servidor via socket e interação com um programa em C para operações nos arquivos.

## Anexos
- **Diagrama da Classe Jogador**:
  ![ClasseJogador](https://github.com/vmoreli/FootballManager/assets/124844938/321ac07d-d15d-4b57-a91e-f8cc67f95021)

- **Captura de Tela da Interface Gráfica**:
  ![TelaInterfaceInicial](https://github.com/vmoreli/FootballManager/assets/124844938/93f79ac8-5e0c-464d-b4c1-feca0bc42053)

### Capturas de Execução e Resultados de Testes
1. **Abrir Arquivo na Interface**:
   ![InterfaceAbrirArquivo](https://github.com/vmoreli/FootballManager/assets/124844938/71090bdc-c229-495b-a225-b7321d9294a1)
2. **Arquivo Aberto na Interface**:
   ![InterfaceArquivoAberto](https://github.com/vmoreli/FootballManager/assets/124844938/345ac8dc-861e-4dbc-8d35-4a0da6eb5718)
3. **Buscando um Jogador na Interface**:
   ![InterfaceBuscando](https://github.com/vmoreli/FootballManager/assets/124844938/b7ec931b-e98f-42cb-adb6-03bfd7f4c2bc)
4. **Após a Busca na Interface**:
   ![InterfaceAposBusca](https://github.com/vmoreli/FootballManager/assets/124844938/7c0a3451-82d9-4050-b6e0-9691db0c5bae)
5. **Remoção de um Jogador na Interface**:
   ![InterfaceAposRemocao](https://github.com/vmoreli/FootballManager/assets/124844938/44a9622a-6a6c-4a7d-9bac-5f358d7350e3)
6. **Alteração de um Jogador na Interface**:
   ![InterfaceAlteracao](https://github.com/vmoreli/FootballManager/assets/124844938/661cb013-2cbb-467f-8065-9c2b424b271f)
7. **Alterando um Jogador na Interface**:
   ![InterfaceAlterando](https://github.com/vmoreli/FootballManager/assets/124844938/2e6921d5-a482-44fc-94fc-e447a7305bb5)
8. **Jogador Alterado na Interface**:
   ![InterfaceAlterada](https://github.com/vmoreli/FootballManager/assets/124844938/ce0ca167-4c38-4bef-8d1f-6f9999563c24)


Para mais informações, leia o arquivo `Relatorio.md`.
