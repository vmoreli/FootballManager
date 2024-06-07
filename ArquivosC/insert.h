/*-----------------------------------------------------
Autores: Giordano Santorum Lorenzetto - nUSP 14574017
         Victor Moreli dos Santos - nUSP 14610514

Função de inserção
-------------------------------------------------------*/


#ifndef INSERTH
    #define INSERTH

    #include "op_bin.h"
    #include "util.h"
    #include "reg.h"
    #include "indice.h"
    #include "create.h"

    // insere no arquivo de dados com reaproveitamento de espaço pela forma best fit,
    // também atualiza arquivo de índice                
    void insert_into(char * arquivoDados, char * arquivoIndice, int numInsert);


#endif