/*-----------------------------------------------------
Autores: Giordano Santorum Lorenzetto - nUSP 14574017
         Victor Moreli dos Santos - nUSP 14610514

Funções que manipulam índice
-------------------------------------------------------*/


#ifndef INDICEH
    #define INDICEH

    #include "op_bin.h"
    #include "util.h"
    #include "reg.h"

    // Função que realiza carregamento na memória primária de um arquivo de índice em disco,
    // retorna um vetor com os registros de índice do arquivo
    //
    // Argumento numInsert: - caso o carregamento esteja sendo feito para realizar inserções, numInsert = números de inserções
    //                        que seram feitas, espaço já reservado no vetor;
    //                      - caso não, numInsert = 0
    //                      * O uso desse argumento evita reallocs nos casos de inserção  
    REG_DADO_ID ** carregamento(FILE * fId, int nroRegArq, int numInsert);
    //reconstrói o arquivo de índices a partir do vetor em memória primária
    void reescrita(FILE * fId, REG_DADO_ID ** vetorId, int nroRegArq, int menorId);
    //faz inserção ordenada num vetor de índices
    void insert_ordenado(REG_DADO_ID ** vetorId, REG_DADO_ID * regDadoId, int count);
    // faz a remoção de um índice em um vetor de índices
    void remove_indice(REG_DADO_ID ** vetorId, int id, int nroRegArq);
    // funcao que retorna o offset de um registro de dados a partir da sua chave primaria
    long get_offset_arqdados(REG_DADO_ID ** vetorId, int id, int nroRegArq);
    // retorna byteoffset no arquivo de INDICE de um registro por busca binária no vetor de indices
    long get_offset_arqindice(REG_DADO_ID ** vetorId, int id, int nroRegArq);
    // libera a memória alocada para o carregamento dos índices em memória primária
    void desalocaVetorIndices(REG_DADO_ID *** vetorIndices, int tamanho);
    // preenche um registro de indice com valores fornecidos
    void preencheRegId(REG_DADO_ID * regId, int id, long byteoffset);

#endif