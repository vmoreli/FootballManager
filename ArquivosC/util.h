/*-----------------------------------------------------
Autores: Giordano Santorum Lorenzetto - nUSP 14574017
         Victor Moreli dos Santos - nUSP 14610514

Funções auxiliares e fornecidas
-------------------------------------------------------*/



#ifndef UTILH
    #define UTILH

    #include <stdio.h>
    #include <stdlib.h>
    #include <stdbool.h>
    #include <ctype.h>
    #include <string.h>
    #include "reg.h"

    //funções fornecidas
    void binarioNaTela(char *nomeArquivoBinario);
    void scan_quote_string(char *str);

    // lê string do arquivo de entrada
    char * lerStr();
    // Função que lê um campo com "" e atualiza o tamanho da str (se for NULO => tam = 0)
    void readQuoteField(char ** string, int * tam);
    // Função que preenche o espaço no arquivo de dados com '$'
    void preencheLixo(FILE * fDados, int espaco);
    // Função que lê n campos do arquivo de entrada e atribui a uma struct reg
    void lerCamposRegParcial(REG_DADO * reg);
    // Função que lê todos campos do arquivo de entrada e atribui a uma struct reg
    void lerCamposRegCompleto(REG_DADO * reg);
    // Compara dois registros campo a campo
    bool comparaRegDado(REG_DADO reg1, REG_DADO reg2);


#endif