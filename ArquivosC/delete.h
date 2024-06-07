/*-----------------------------------------------------
Autores: Giordano Santorum Lorenzetto - nUSP 14574017
         Victor Moreli dos Santos - nUSP 14610514

Função de remoção
-------------------------------------------------------*/


#ifndef DELETEH
    #define DELETEH

    #include "op_bin.h"
    #include "op_csv.h"
    #include "util.h"
    #include "reg.h"
    #include "indice.h"
    #include "create.h"
    
    /* faz numRem remoções diferentes de registros de dados no arquivo <arquivoBin>
     cada uma dessas remoções recebe um numero m que deterina quantos campos serão especi-
     ficados como requisitos da remoção seguidos de m nomes de campos e seus valores
     Exemplo: 2 id 29382 nacionalide "RUSSIA"*/
    void delete_from_where(char * arquivoBin, char * arquivoIndice, int numRem);


#endif