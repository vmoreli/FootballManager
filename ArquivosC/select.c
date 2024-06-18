/*-----------------------------------------------------
Autores: Giordano Santorum Lorenzetto - nUSP 14574017
         Victor Moreli dos Santos - nUSP 14610514
-------------------------------------------------------*/


#include "select.h"

void select_from(char * arquivobin){

    // Abertura do arquivo binario para leitura
    FILE * filebin = fopen(arquivobin, "rb");
    if(filebin == NULL){
        printf("Falha no processamento do arquivo.\n");
        return;
    }

    REG_CAB regCab; // Declara registro de cabeçalho
    readRegCabBin(filebin, &regCab); // Lê registro de cabeçalho

    // Se o arquivo estiver inconsistente, houve falha no processamento do arquivo
    if(regCab.status == '0'){
        printf("Falha no processamento do arquivo.\n");
        fclose(filebin);
        return;
    }

    // Se o numero de arquivos registrados for zero, nao eh necessario fazer a busca
    if(regCab.nroRegArq == 0){
        printf("Registro inexistente.\n\n");
        fclose(filebin);
        return;
    }

    REG_DADO regDado; // Declara registro de dados

    while(readRegDadoBin(filebin, &regDado)){ // Lê registro de dados um por um até chegar ao fim do arquivo

        if(regDado.removido == '0'){ // se o registro lido não estiver removido, imprime na saida padrão
            printf("Nome do Jogador: %s\n", regDado.tamNomeJog == 0 ? "SEM DADO" : regDado.nomeJogador);
            printf("Nacionalidade do Jogador: %s\n", regDado.tamNacionalidade == 0 ? "SEM DADO" : regDado.nacionalidade);
            printf("Clube do Jogador: %s\n", regDado.tamNomeClube == 0 ? "SEM DADO" : regDado.nomeClube);
            printf("\n");
        }

        // liberação de memoria das strings de regDado
        free(regDado.nomeJogador);
        free(regDado.nacionalidade);
        free(regDado.nomeClube);
        
    }    
    
    // fechando o arquivo binário
    fclose(filebin);
}

void select_from_where(char * arquivobin, int num_buscas){

    // Abertura do arquivo binario para leitura
    FILE * filebin = fopen(arquivobin, "rb");
    if(filebin == NULL){
        printf("Falha no processamento do arquivo.\n");
        return;
    }

    REG_CAB regCab; // Declara registro de cabeçalho
    readRegCabBin(filebin, &regCab); // Lê registro de cabeçalho

    // Se o numero de arquivos registrados for zero ou se o arquivo estiver inconsistente,
    // nao eh necessario fazer a busca
    if(regCab.nroRegArq == 0 || regCab.status == '0'){
        printf("Registro inexistente.\n"); 
        fclose(filebin);
        return;
    }

    // Declara structs de registro de dados auxiliares
    REG_DADO regDadoModelo; // struct que serve de modelo para a busca, inicia com valores nulos
    regDadoModelo.id = -1;
    regDadoModelo.idade = -1;
    regDadoModelo.nacionalidade = NULL;
    regDadoModelo.nomeClube = NULL;
    regDadoModelo.nomeJogador = NULL;

    REG_DADO regDado; // struct q armazenará temporariamente registros de dados lidos do arquivo e entrada

    char reg_encontrado = 0; // flag para determinar se ao menos 1 registro foi encontrado
    
    for(int i = 1; i <= num_buscas; i++){

        // Inicio da i-esima busca
        printf("Busca %d\n\n", i);
        
        // lendo os campos e seus valores, e atribuindo eles ao registro modelo
        lerCamposRegParcial(&regDadoModelo);

        // flag de registro encontrado = 0
        reg_encontrado = 0;
        while(readRegDadoBin(filebin, &regDado)){ // Lê registro de dados um por um até chegar ao fim do arquivo

            // se o registro lido nao estiver removido, compara com o registro modelo 
            if(regDado.removido == '0'){
                if(comparaRegDado(regDadoModelo, regDado)){ // compara nomeJog caso o nomeJog do modelo nao for nulo
                    printf("Nome do Jogador: %s\n", regDado.tamNomeJog == 0 ? "SEM DADO" : regDado.nomeJogador);
                    printf("Nacionalidade do Jogador: %s\n", regDado.tamNacionalidade == 0 ? "SEM DADO" : regDado.nacionalidade);
                    printf("Clube do Jogador: %s\n", regDado.tamNomeClube == 0 ? "SEM DADO" : regDado.nomeClube);
                    printf("\n");        
                    reg_encontrado = 1;
                }                
            }

            //liberacao da memoria usada pelo registrador de dados temporario
            free(regDado.nomeJogador);
            free(regDado.nacionalidade);
            free(regDado.nomeClube);

            // se o id foi levado em consideraçao na busca e o registro foi encontrado a busca para pois o id é um campo unico  
            if(regDadoModelo.id != -1 && reg_encontrado == 1) 
                break;                 
        }
        
        // se nenhum registro foi encontrado, informamos o usuario
        if(reg_encontrado == 0) printf("Registro inexistente.\n\n");

        if(regDadoModelo.nacionalidade != NULL) free(regDadoModelo.nacionalidade);
        if(regDadoModelo.nomeClube != NULL) free(regDadoModelo.nomeClube);
        if(regDadoModelo.nomeJogador != NULL) free(regDadoModelo.nomeJogador);

        // resetando todos os campos do registro modelo com valores nulos deixando pronto para a proxima busca
        regDadoModelo.id = -1;
        regDadoModelo.idade = -1;
        regDadoModelo.nacionalidade = NULL;
        regDadoModelo.nomeClube = NULL;
        regDadoModelo.nomeJogador = NULL;

        // retorna o ponteiro do arquivo para o inicio dos registros de dados (pula o cabecalho)
        fseek(filebin, TAM_REG_CAB, SEEK_SET);
    }    

    //fecha o arquivo
    fclose(filebin);
}
