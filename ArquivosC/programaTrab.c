/*-----------------------------------------------------
Autores: Giordano Santorum Lorenzetto - nUSP 14574017
         Victor Moreli dos Santos - nUSP 14610514

Programa Principal
-------------------------------------------------------*/


#include "select.h"
#include "create.h"
#include "insert.h"
#include "delete.h"


int main(void){

    int comando;

    scanf(" %d", &comando);
    
    getchar();//descarta o '\n' da entrada

    // strings para nomes de arquivos
    char * arquivoIn;
    char * arquivoOut;
    char * arquivoIndice;
    char * arquivoDados;

    switch (comando){
    case 1: // caso 1 = create_table

        // lendo o nome dos arquivos
        arquivoIn = lerStr();// arquivo de entrada [nome].csv
        arquivoOut = lerStr();// arquivo de saida  [nome].bin

        //chamada da funcao
        create_table(arquivoIn, arquivoOut);

        //liberacao da memoria
        free(arquivoIn);
        free(arquivoOut);
        break;
    case 2: //caso 2 = select_from
        
        // lendo o nome dos arquivos
        arquivoIn = lerStr();// arquivo de entrada [nome].bin

        //chamada da funcao
        select_from(arquivoIn);

        //liberacao da memoria
        free(arquivoIn);
        break;
    case 3: //caso 3 = select_from_where 

        int numBuscas; // numero de buscas que serão feitas

        // lendo o nome dos arquivos
        arquivoIn = lerStr();// arquivo de entrada [nome].bin

        //lendo o numero de buscas
        scanf(" %d", &numBuscas);
        getchar(); // descarta o '\n' da entrada

        //chamada da funcao
        select_from_where(arquivoIn, numBuscas);
        
        //liberacao da memoria
        free(arquivoIn);
        break;
    case 4: //caso 4 = create index 

        // lendo o nome dos arquivos
        arquivoIn = lerStr();// arquivo de entrada [nome].csv
        arquivoOut = lerStr();// arquivo de saida  [nome].bin

        //chamada da funcao
        if(create_index(arquivoIn, arquivoOut))
            binarioNaTela(arquivoOut);  // chama função fornecida binarioNaTela

        //liberacao da memoria
        free(arquivoIn);
        free(arquivoOut);
        break;
    case 5: //caso 5 = delete_from_where 

        int numRem; // numero de remoções que serão feitas

        // lendo o nome dos arquivos
        arquivoDados = lerStr();// arquivo de dados [nome].bin
        arquivoIndice = lerStr();// arquivo de indice [nome].bin

        //lendo o numero de remoções
        scanf(" %d", &numRem);
        getchar(); // descarta o '\n' da entrada

        //chamada da funcao
        delete_from_where(arquivoDados, arquivoIndice, numRem);
        
        //liberacao da memoria
        free(arquivoDados);
        free(arquivoIndice);
        break;
    case 6: //caso 6 = insert_into 

        int numInsert; // numero de insercoes que serão feitas

        // lendo o nome dos arquivos
        arquivoDados = lerStr();// arquivo de dados [nome].bin
        arquivoIndice = lerStr();// arquivo de indice [nome].bin

        //lendo o numero de insercoes
        scanf(" %d", &numInsert);
        getchar(); // descarta o '\n' da entrada

        //chamada da funcao
        insert_into(arquivoDados, arquivoIndice, numInsert);
        
        //liberacao da memoria
        free(arquivoDados);
        free(arquivoIndice);
        break;
    default:
            binarioNaTela("indice4.bin");  // chama função fornecida binarioNaTela
        break;
    }

    
    return 0;
}