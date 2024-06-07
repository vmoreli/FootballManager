/*-----------------------------------------------------
Autores: Giordano Santorum Lorenzetto - nUSP 14574017
         Victor Moreli dos Santos - nUSP 14610514

Operações em binários
-------------------------------------------------------*/


#ifndef OP_BINH
    #define OP_BINH

    #include <stdio.h>
    #include <stdlib.h>
    #include <stdbool.h>
    #include "reg.h"

    // Nesse  .h se encontram todas as funções que fazem operações básicas em arquivos binários
    
    // Função que lê uma string de um arquivo binário
    void lerStrBin(FILE * file, char ** dest, int tam);
    // Função que lê um registro de cabeçalho de um arquivo binário, preenchendo o regCab
    void readRegCabBin(FILE * filebin, REG_CAB * regCab);
    // Função que lê um registro de dados de um arquivo binário, preenchendo o regDado
    int readRegDadoBin(FILE * filebin, REG_DADO * regDado);
    // Função que escreve um registro de cabeçalho num arquivo binário
    void writeRegCabBin(FILE * fOut, REG_CAB cabecalho);
    // Função que escreve um registro de dados num arquivo binário
    void writeRegDadoBin(FILE * file, REG_DADO dados);
    // Função que lê um registro de cabeçalho de um arquivo de índice, preenchendo o regCabId
    void readRegCabId(FILE * fileId, REG_CAB_ID * regCabId);
    // Função que lê um registro de dados p/ índice de um arquivo de índice, preenchendo o regDadoId
    void readRegDadoId(FILE * fileId, REG_DADO_ID * regDadoId);
    // Função que escreve um registro de cabeçalho num arquivo de índice
    void writeRegCabId(FILE * fOut, REG_CAB_ID cabecalho);
    // Função que escreve um registro de dados num arquivo de índice
    void writeRegDadoId(FILE * file, REG_DADO_ID dados);
    // Função que lê um registro de dados p/ índice de um arquivo de dados, preenchendo regDadoId
    bool readRegDadoIdFromSrc(FILE * filebin, REG_DADO_ID * regDadoId);
    // Insere um registro no fim do arquivo de dados
    void insereFimArqDados(FILE * fDados, REG_CAB * regCabDados, REG_DADO regDado);


#endif