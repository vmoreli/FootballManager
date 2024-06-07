/*-----------------------------------------------------
Autores: Giordano Santorum Lorenzetto - nUSP 14574017
         Victor Moreli dos Santos - nUSP 14610514
-------------------------------------------------------*/


#include "create.h"

// Função que implementa a funcionalidade 1: CREATE TABLE
void create_table(char * arquivoIn, char * arquivoOut){
    FILE * fIn = fopen(arquivoIn, "r");     // arquivoIn: nome do arquivo csv de entrada
    if(fIn == NULL){
        printf("Erro na abertura do .csv\n");
        exit(1);
    }
    FILE * fOut = fopen(arquivoOut, "wb");  // arquivoOut: nome do arquivo bin de saída
    if(fOut == NULL){
        printf("Erro na abertura do .bin\n");
        exit(1);
    }

    REG_CAB cabecalho;      // receberá o cabecalho
    REG_DADO dados;         // recebera cada registro de dados
    
    // escreve cabecalho com status  = 0 (inconsistente)
    cabecalho.status = '0';
    cabecalho.nroRegArq = 0;
    writeRegCabBin(fOut, cabecalho);    // escreve cabecalho indicando que arquivo está *inconsistente*

    //pula primeira linha do .csv (cabeçalho do csv)
    while(getc(fIn) != '\n'){
        continue;
    }

    //lendo todos os valores
    while (readRegDadoCsv(fIn, &dados)){ // lê o registro de dados do csv até o fim do arquivo
            
        writeRegDadoBin(fOut, dados);   // escreve o registro de dados lido no binario
        cabecalho.nroRegArq++;          // incrementa o numero de registros no arquivo

        // desaloca o que for necessario
        free(dados.nomeJogador);
        free(dados.nacionalidade);
        free(dados.nomeClube);
    }

    // preenche cabecalho em memoria primaria
    cabecalho.status = '1';
    cabecalho.topo = -1;
    cabecalho.proxByteOffset = ftell(fOut); // atualiza prox byte offset
    cabecalho.nroRegRem = 0;

    
    fseek(fOut, 0, SEEK_SET);           // volta para o inicio
    writeRegCabBin(fOut, cabecalho);    // escreve o cabecalho no arquivo binario, indicando que o arquivo está *consistente*

    // fecha os arquivos
    fclose(fIn);
    fclose(fOut);

    // chama função fornecida binarioNaTela
    binarioNaTela(arquivoOut);
}

// Função que implementa a funcionalidade 4: CREATE INDEX 
bool create_index(char * arquivoDados, char * arquivoIndice){
    FILE * fDados = fopen(arquivoDados, "rb");     // arquivoDados: nome do arquivo de dados de entrada
    if(fDados == NULL){
        printf("Falha no processamento do arquivo.\n");
        return false;
    }

    FILE * fId = fopen(arquivoIndice, "wb");     // arquivoIndice: nome do arquivo de indice de saída
    if(fId == NULL){
        printf("Falha no processamento do arquivo.\n");
        fclose(fDados);
        return false;
    }

    REG_CAB reg_cab;            // cabecalho do arquivo de dados
    REG_CAB_ID reg_cab_id;      // cabecalho do arquivo de indice
    REG_DADO_ID * reg_dado_id;    // auxiliar para escrita no arquivo de indice

    readRegCabBin(fDados, &reg_cab);    // lê cabecalho do arquivo de dados

    if(reg_cab.status == '0'){          // se o arquivo de dados estiver inconsistente
        printf("Falha no processamento do arquivo.\n");
        fclose(fDados);
        fclose(fId);
        return false;
    }

    int num_reg_total = reg_cab.nroRegArq + reg_cab.nroRegRem;  // calcula total de registros no arquivo

    reg_cab_id.status = 0;
    writeRegCabId(fId, reg_cab_id);     // escreve cabecalho com status = 0 (inconsistente)    

    // primeiramente todos os registros de dados de indice serao criados em memoria primaria num vetor,
    // será feita inserção ordenada nesse vetor e, então, será criado o arquivo refletindo-o
    REG_DADO_ID ** vetorId = (REG_DADO_ID **) malloc(reg_cab.nroRegArq * sizeof(REG_DADO_ID *));
    if(vetorId == NULL){
        printf("sem espaço");
        exit(1);
    }

    // insercao ordenada no vetor de indices
    int i = 0, count = 0; // i - controle do loop, count - nro de registros adicionados no vetor até aquela etapa
    while (i < num_reg_total){      // Enquanto ainda não foram lidos todos os registros no arquivo
        reg_dado_id = (REG_DADO_ID *) malloc(sizeof(REG_DADO_ID));
        if(reg_dado_id == NULL){
            printf("sem espaço");
            exit(1);
        }
        
        if(readRegDadoIdFromSrc(fDados, reg_dado_id)){      // se a leitura do registro retornou true, insercao ordenada no vetor de indices
            insert_ordenado(vetorId, reg_dado_id, count);
            count++;
        } else {                                            // se a leitura do registro retornou false, o registro estava removido logicamente
            free(reg_dado_id);                              // libera memoria do registro removido
            reg_dado_id = NULL;
        }
            
        i++;
    }

    // estando o vetor devidamente ordenado com todos os registros de dados de indice, reescreve o arquivo de indices
    reescrita(fId, vetorId, reg_cab.nroRegArq, 0);     // ao final da reescrita: status = '1'

    // libera memoria do vetor de registros de índice
    desalocaVetorIndices(&vetorId, count);
    free(vetorId);
    vetorId = NULL;
    
    
    // fecha os arquivos
    fclose(fDados);
    fclose(fId);
    return true;
}