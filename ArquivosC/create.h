/*-----------------------------------------------------
Autores: Giordano Santorum Lorenzetto - nUSP 14574017
         Victor Moreli dos Santos - nUSP 14610514

Funções de criação:
 - tabela (create_table)
 - índice (create_index)
-------------------------------------------------------*/


#ifndef CREATE_TABLEH
    #define CREATE_TABLEH

    #include "op_bin.h"
    #include "op_csv.h"
    #include "util.h"
    #include "reg.h"
    #include "indice.h"

    // Função que implementa a funcionalidade 1: CREATE TABLE
    void create_table(char * arquivoIn, char * arquivoOut);
    // Função que implementa a funcionalidade 4: CREATE_INDEX
    bool create_index(char * arquivoDados, char * arquivoIndice);


#endif