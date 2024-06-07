/*-----------------------------------------------------
Autores: Giordano Santorum Lorenzetto - nUSP 14574017
         Victor Moreli dos Santos - nUSP 14610514
-------------------------------------------------------*/


#include "insert.h"

void insere_reg_dado(FILE * fDados, REG_DADO_ID ** vetorIndices, REG_DADO regDadoAux, REG_DADO_ID * regIdAux, REG_CAB * regCabDados);

void insert_into(char *arquivoDados, char *arquivoIndice, int numInsert) {
    FILE *fDados = fopen(arquivoDados, "rb+");      // abre arquivo de dados para leitura e escrita
    if (fDados == NULL) {
        printf("Falha no processamento do arquivo.\n");
        return;
    }

    FILE *fId = fopen(arquivoIndice, "rb+");        // abre arquivo de índice para leitura e escrita
    if (fId == NULL) {                              // se o arquivo de índices não existir
        create_index(arquivoDados, arquivoIndice);  // cria o arquivo por meio da funcionalidade 4 (create_index)
        fId = fopen(arquivoIndice, "rb+");
        if (fId == NULL) {                          // verifica se o arquivo foi aberto sem erros
            fclose(fDados);                         // fecha o arquivo que ja estava aberto
            printf("Falha no processamento do arquivo.\n");
            return;
        }
    }

    REG_CAB regCabDados;
    REG_CAB_ID regCabId;

    readRegCabBin(fDados, &regCabDados);            // lê cabecalho do arquivo de dados
    readRegCabId(fId, &regCabId);                   // lê cabecalho do arquivo de índices

    if (regCabDados.status == '0' || regCabId.status == '0') {  // se um dos dois estiver inconsistente, falha no processamento do arquivo
        printf("Falha no processamento do arquivo.\n");
        fclose(fDados);
        fclose(fId);
        return;
    }

    // faz carregamento do arquivo de índices para a memória primária (vetorIndices), status do arquivo de índices = 0
    REG_DADO_ID ** vetorIndices = carregamento(fId, regCabDados.nroRegArq, numInsert); // já aloca espaço para as inserções no vetor

    // Atualiza status do arquivo de dados para inconsistente
    regCabDados.status = '0';
    fseek(fDados, 0, SEEK_SET);
    writeRegCabBin(fDados, regCabDados);

    REG_DADO regDadoAux;    // registro de dados auxiliar para inserção no arquivo de dados

    int menorId;

    for (int i = 0; i < numInsert; i++) {
        // Lê todos os campos do registro de dados a ser inserido
        lerCamposRegCompleto(&regDadoAux);

        // aloca nova struct auxiliar de registro de índice para ser inserida no vetor
        REG_DADO_ID * regIdAux = (REG_DADO_ID *) malloc(sizeof(REG_DADO_ID));

        // função que:
        // - insere no registro de dados
        // - preenche registro de indice com as informações corretas e o insere no vetor de indices
        // - atualiza registro de cabecalho do arquivo de dados se necessario
        insere_reg_dado(fDados, vetorIndices, regDadoAux, regIdAux, &regCabDados);

        // atualiza o menor indice que foi inserido -> reconstrucao do arquivo de indices deve comecar a partir do menor indice inserido!
        if(i == 0 || regIdAux->id < menorId)
            menorId = regIdAux->id;

        // libera memória das strings de regDadoAux
        free(regDadoAux.nomeJogador);
        free(regDadoAux.nacionalidade);
        free(regDadoAux.nomeClube);
    }

    regCabDados.status = '1';           // atualiza o status para consistente
    fseek(fDados, 0, SEEK_SET);         // vai para o início
    writeRegCabBin(fDados, regCabDados);    // escreve novo cabeçalho

    reescrita(fId, vetorIndices, regCabDados.nroRegArq, menorId);    // reescreve arquivo de índices a partir do vetor, status = 1

    desalocaVetorIndices(&vetorIndices, regCabDados.nroRegArq);

    // fecha os arquivos
    fclose(fDados);
    fclose(fId);

    // binario na tela para os arquivos
    binarioNaTela(arquivoDados);
    binarioNaTela(arquivoIndice);
}

// função que:
// - insere registro de dados (regDadoAux) no arquivo de dados (fDados)
// - preenche registro de indice (regIdAux) com as informações corretas e o insere no vetor de indices (vetorIndices)
// - atualiza registro de cabecalho do arquivo de dados (regCabDados)
void insere_reg_dado(FILE * fDados, REG_DADO_ID ** vetorIndices, REG_DADO regDadoAux, REG_DADO_ID * regIdAux, REG_CAB * regCabDados){
    // variáveis auxiliares para uso na inserção
    int tam;    // tamanho do arquivo onde a busca se encontra
    int dif;
    long prox;  // byteoffset do proximo da lista de removidos
    //char removido;  // usado para pular char de removido dos registros
    long pos, ant;  // pos - byteoffset do atual, ant - byteoffset do anterior 

    if (regCabDados->nroRegRem == 0) {   // Se não houver registros removidos, insere no fim
        // Inserir no fim
        preencheRegId(regIdAux, regDadoAux.id, regCabDados->proxByteOffset); // preenche registro de indice auxiliar
        insereFimArqDados(fDados, regCabDados, regDadoAux); // insere registro de dados no fim do arquivo, atualiza proxByteOffset do cabecalho
        insert_ordenado(vetorIndices, regIdAux, regCabDados->nroRegArq);  // insere ordenado no vetor de índices

    } else {    // Se há registros removidos, possível reaproveitamento de espaço

        // Possível inserção em espaço reaproveitado

        fseek(fDados, regCabDados->topo + 1, SEEK_SET);  // vai para o topo da lista dos logicamente removidos, pulando o char de removido
        ant = regCabDados->topo;                     // salva o anterior
        //fread(&removido, sizeof(char), 1, fDados);  // pula o removido
        fread(&tam, sizeof(int), 1, fDados);        // lê o tamanho do registro atual

        // se o tamanho do registro logicamente removido for maior que o tamanho do registro que será inserido
        if (tam >= regDadoAux.tamanhoRegistro) {
            // Inserir no topo

            dif = tam - regDadoAux.tamanhoRegistro; // calcula diferença de tamanho entre o espaço disponível e o registro
            regDadoAux.tamanhoRegistro = tam;           // novo tamanho do registro é o espaço disponível
            fread(&prox, sizeof(long), 1, fDados);      // lê o próximo
            regCabDados->topo = prox;                    // o topo recebe o próximo da lista de removidos
            fseek(fDados, ant, SEEK_SET);               // volto para o anterior (nesse caso, é o início do registro que estava no topo)
            writeRegDadoBin(fDados, regDadoAux);        // escreve registro
            preencheLixo(fDados, dif);                  // preenche lixo ($)
            preencheRegId(regIdAux, regDadoAux.id, ant);
            //regIdAux->byteoffset = ant;             // preenche byteoffset do registro auxiliar de índice
            insert_ordenado(vetorIndices, regIdAux, regCabDados->nroRegArq); // insere ordenado no vetor de índices
            regCabDados->nroRegRem--;  // diminui nro de removidos do arquivo de dados

        } else {        // inserção não ocorrerá no topo
            
            while (tam < regDadoAux.tamanhoRegistro) {      // enquanto o tamanho do registro onde se encontra é menor do que o que será inserido
                
                ant = ftell(fDados);    //ant guarda posição do anterior antes do campo prox 
                fread(&prox, sizeof(long), 1, fDados);  // lê o próximo

                if(prox == -1)
                    break;

                fseek(fDados, prox + 1, SEEK_SET);  // vai para o próximo, já pulando o char de removido
                pos = prox;                     // atualiza posição atual para o inicio do registro atual
                //fread(&removido, sizeof(char), 1, fDados); // pula o removido
                fread(&tam, sizeof(int), 1, fDados);       // lê o tamanho

            }

            // saiu em posição disponível para reaproveitamento ou chegou no fim da lista

            if (prox == (long) -1){  // inserção no fim da lista

                // Inserir no fim
                preencheRegId(regIdAux, regDadoAux.id, regCabDados->proxByteOffset); // preenche registro de indice auxiliar
                insereFimArqDados(fDados, regCabDados, regDadoAux); // insere registro de dados no fim do arquivo, atualiza proxByteOffset do cabecalho
                insert_ordenado(vetorIndices, regIdAux, regCabDados->nroRegArq);  // insere ordenado no vetor de índices
                
            } else {
                // Insere no "meio" da lista, com re-aproveitamento de espaço
            
                int dif = tam - regDadoAux.tamanhoRegistro; // salva diferença entre espaço disponível e tamanho do registro
                regDadoAux.tamanhoRegistro = tam;   // novo tamanho do registro = espaço disponível

                // atualização da lista (proximo do anterior = proximo do atual)
                fread(&prox, sizeof(long), 1, fDados);  // pega o proximo do atual
                fseek(fDados, ant, SEEK_SET);           // vai para o anterior
                fwrite(&prox, sizeof(long), 1, fDados); // proximo do anterior = proximo do atual
                fseek(fDados, pos, SEEK_SET);           // volta para o atual

                writeRegDadoBin(fDados, regDadoAux); // escreve novo registro
                preencheLixo(fDados, dif);  // preenche lixo
                regCabDados->nroRegRem--; // diminui nro de removidos do arquivo de dados 
                preencheRegId(regIdAux, regDadoAux.id, pos); // preenche byteoffset e id do registro de índice auxiliar
                insert_ordenado(vetorIndices, regIdAux, regCabDados->nroRegArq); // insere ordenado no vetor de índices
            }
        }
    }

    regCabDados->nroRegArq++; // atualiza cabecalho com novo numero de registros arquivados 

}
