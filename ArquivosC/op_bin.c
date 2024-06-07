/*-----------------------------------------------------
Autores: Giordano Santorum Lorenzetto - nUSP 14574017
         Victor Moreli dos Santos - nUSP 14610514
-------------------------------------------------------*/

#include "op_bin.h"


// Função que lê uma string de um arquivo binário
void lerStrBin(FILE * filebin, char ** str, int tam){
    *str = (char *) malloc(tam + 1);
    for (int i = 0; i < tam; i++)
    {
        fread(*str + i, sizeof(char), 1, filebin); // lê byte a byte
    }
    (*str)[tam] = '\0'; // coloca o \0 no fim da string
}


//Função que lê um registro de cabeçalho de um arquivo binário, preenchendo o regCab
void readRegCabBin(FILE * filebin, REG_CAB * regCab){
    fread(&(regCab -> status), sizeof(char), 1, filebin);   // lê o status
    fread(&(regCab -> topo), sizeof(long), 1, filebin);     // lê o topo
    fread(&(regCab -> proxByteOffset), sizeof(long), 1, filebin);   // lê o proxByteOffset
    fread(&(regCab -> nroRegArq), sizeof(int), 1, filebin);     // lê o nroRegArq
    fread(&(regCab -> nroRegRem), sizeof(int), 1, filebin);     // lê o nroRegRem
}

// Função que escreve um registro de cabeçalho num arquivo binário
void writeRegCabBin(FILE * fOut, REG_CAB cabecalho){
    fwrite(&(cabecalho.status), sizeof(char), 1, fOut);     // escreve o status
    fwrite(&cabecalho.topo, sizeof(long), 1, fOut);         // escreve o topo
    fwrite(&cabecalho.proxByteOffset, sizeof(long), 1, fOut);   // escreve o proxByteOffset
    fwrite(&cabecalho.nroRegArq, sizeof(int), 1, fOut);     // escreve o nroRegArq
    fwrite(&cabecalho.nroRegRem, sizeof(int), 1, fOut);     // escreve o nroRegRem
}

//Função que lê um registro de dados de um arquivo binário, preenchendo o regDado
int readRegDadoBin(FILE * filebin, REG_DADO * regDado){
    // Lê o char de removido
    if(fread(&(regDado -> removido), sizeof(char), 1, filebin) == 0)
        return 0;
    // Lê o tamanho do registro
    fread(&(regDado -> tamanhoRegistro), sizeof(int), 1, filebin);
    // Lê o prox
    fread(&(regDado -> prox), sizeof(long), 1, filebin);
    // Lê o id
    fread(&(regDado -> id), sizeof(int), 1, filebin);
    // Lê a idade
    fread(&(regDado -> idade), sizeof(int), 1, filebin);
    // Lê o tamanho do nome do jogador
    fread(&(regDado -> tamNomeJog), sizeof(int), 1, filebin);
    // Lê o nome do jogador
    lerStrBin(filebin, &(regDado -> nomeJogador), regDado -> tamNomeJog);
    // Lê o tamanho da nacionalidade
    fread(&(regDado -> tamNacionalidade), sizeof(int), 1, filebin);
    // Lê a nacionalidade
    lerStrBin(filebin, &(regDado -> nacionalidade), regDado -> tamNacionalidade);
    // Lê o tamanho do nome do clube
    fread(&(regDado -> tamNomeClube), sizeof(int), 1, filebin);
    // Lê o nome do clube
    lerStrBin(filebin, &(regDado -> nomeClube), regDado -> tamNomeClube);

    return 1;
}


// Função que escreve um registro de dados num arquivo binário
void writeRegDadoBin(FILE * file, REG_DADO dados){
    // escrever removido
    fwrite(&(dados.removido), sizeof(char), 1, file);
    // escrever tamanho registro
    fwrite(&(dados.tamanhoRegistro), sizeof(int), 1, file);
    // escrever prox
    fwrite(&(dados.prox), sizeof(long), 1, file);
    // escrever id
    fwrite(&(dados.id), sizeof(int), 1, file);
    // escrever idade
    fwrite(&(dados.idade), sizeof(int), 1, file);
    // escrever tamanho nome jogador
    fwrite(&(dados.tamNomeJog), sizeof(int), 1, file);
    // escrever nome jogador
    fwrite(dados.nomeJogador, sizeof(char), dados.tamNomeJog, file);
    // escrever tamanho nacionalidade
    fwrite(&(dados.tamNacionalidade), sizeof(int), 1, file);
    // escrever nacionalidade
    fwrite(dados.nacionalidade, sizeof(char), dados.tamNacionalidade, file);
    // escrever tamanho nome clube
    fwrite(&(dados.tamNomeClube), sizeof(int), 1, file);
    // escrever nome clube
    fwrite(dados.nomeClube, sizeof(char), dados.tamNomeClube, file);
}

//Função que lê um registro de cabeçalho de um arquivo de índice, preenchendo o regCabId
void readRegCabId(FILE * fileId, REG_CAB_ID * regCabId){
    fread(&(regCabId -> status), sizeof(char), 1, fileId);   // lê o status
}

// Função que escreve um registro de cabeçalho num arquivo de índice
void writeRegCabId(FILE * fOut, REG_CAB_ID cabecalho){
    fwrite(&(cabecalho.status), sizeof(char), 1, fOut);     // escreve o status
}

// Função que lê um registro de dados p/ índice de um arquivo de índice, preenchendo o regDadoId
void readRegDadoId(FILE * fileId, REG_DADO_ID * regDadoId){
    // Lê o id
    fread(&(regDadoId -> id), sizeof(int), 1, fileId);
    // Lê o byteoffset
    fread(&(regDadoId -> byteoffset), sizeof(long), 1, fileId);
}

// Função que lê um registro de dados p/ índice de um arquivo de dados, preenchendo regDadoId.
// retorna false se o registro estiver removido, true se o registro for lido corretamente
bool readRegDadoIdFromSrc(FILE * filebin, REG_DADO_ID * regDadoId){
    // variáveis auxiliares para leitura dos campos do arquivo de dados
    char removido;
    int tamanhoRegistro;
    long prox;

    // calculo do byteoffset do registro no arquivo de dados
    long byteoffset_atual = ftell(filebin);

    // Lê o char de removido
    fread(&removido, sizeof(char), 1, filebin);
    // Lê o tamanho do registro
    fread(&tamanhoRegistro, sizeof(int), 1, filebin);

    long byteoffset_prox = byteoffset_atual + (long) tamanhoRegistro; // calcula o byteoffset do proximo registro

    if(removido == '1'){
        fseek(filebin, byteoffset_prox, SEEK_SET);   // se o registro estiver removido, pula o registro
        return false;   // retorna false se o registro estiver removido logicamente, nao há necessidade de leitura dos demais campos
    }

    // Registro é válido para inserção no arquivo de índices

    // Pula o campo prox
    fread(&prox, sizeof(long), 1, filebin);

    // Lê o id
    fread(&(regDadoId->id), sizeof(int), 1, filebin);
    // Lê byteoffset
    regDadoId->byteoffset = byteoffset_atual;

    fseek(filebin, byteoffset_prox, SEEK_SET); // pula para o byteoffset do proximo registro

    return true;    // retorna true se o registro for lido corretamente
}


// Função que escreve um registro de dados num arquivo de índice
void writeRegDadoId(FILE * file, REG_DADO_ID dados){
    // escrever id
    fwrite(&(dados.id), sizeof(int), 1, file);
    // escrever byteoffset
    fwrite(&(dados.byteoffset), sizeof(long), 1, file);
}

// Insere um registro no fim do arquivo de dados
void insereFimArqDados(FILE * fDados, REG_CAB * regCabDados, REG_DADO regDado){
    fseek(fDados, regCabDados->proxByteOffset, SEEK_SET);    // pula para o próximo byte offset vazio para inserção (fim do arquivo) disponível no cabecalho
    writeRegDadoBin(fDados, regDado);                    // escreve registro de dados
    regCabDados->proxByteOffset += regDado.tamanhoRegistro; // atualiza próximo byteoffset vazio para inserção
}