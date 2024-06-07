/*-----------------------------------------------------
Autores: Giordano Santorum Lorenzetto - nUSP 14574017
         Victor Moreli dos Santos - nUSP 14610514

Define registros de dados e de cabeçalho, tanto para arquivo de dados quanto para arquivo de índices
-------------------------------------------------------*/



#ifndef REGH
    #define REGH

    // Esse arquivo define as structs do registro de cabecalho e do registro de dados
    
    #define TAM_REG_CAB 25      // tamanho do registro de cabecalho
    #define TAM_REG_CAB_ID 1    // tamanho do registro de cabecalho de indice


    typedef struct reg_cabecalho_ REG_CAB;
    typedef struct reg_dados_ REG_DADO;
    typedef struct reg_cabecalho_id_ REG_CAB_ID;
    typedef struct reg_dados_id_ REG_DADO_ID;

    struct reg_cabecalho_{
        char status;            // 0 - inconsistente, 1 - consistente
        long topo;              // primeiro registro removido da lista
        long proxByteOffset;    // proxima posicao vazia para insercao
        int nroRegArq;          // nro de registros do arquivo
        int nroRegRem;          // nro de registros removidos do arquivo
    };

    struct reg_dados_{
        //Controle
        char removido;
        int tamanhoRegistro;
        long prox;

        //Campos de tamanho fixo
        int id;
        int idade;

        //Indicadores de tamanho
        int tamNomeJog;
        int tamNacionalidade;
        int tamNomeClube;

        //Campos de tamanho variável
        char * nomeJogador;
        char * nacionalidade;
        char * nomeClube;
    };

    struct reg_cabecalho_id_{
        char status;            // 0 - inconsistente, 1 - consistente
    };

    struct reg_dados_id_{
        int id;                 // chave primária
        long byteoffset;        // byteoffset do registro no arquivo de dados
    };

#endif