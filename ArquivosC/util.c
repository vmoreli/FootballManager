/*-----------------------------------------------------
Autores: Giordano Santorum Lorenzetto - nUSP 14574017
         Victor Moreli dos Santos - nUSP 14610514
-------------------------------------------------------*/


#include "util.h"

void binarioNaTela(char *nomeArquivoBinario) { /* Você não precisa entender o código dessa função. */

	/* Use essa função para comparação no run.codes. Lembre-se de ter fechado (fclose) o arquivo anteriormente.
	*  Ela vai abrir de novo para leitura e depois fechar (você não vai perder pontos por isso se usar ela). */

	unsigned long i, cs;
	unsigned char *mb;
	size_t fl;
	FILE *fs;
	if(nomeArquivoBinario == NULL || !(fs = fopen(nomeArquivoBinario, "rb"))) {
		fprintf(stderr, "ERRO AO ESCREVER O BINARIO NA TELA (função binarioNaTela): não foi possível abrir o arquivo que me passou para leitura. Ele existe e você tá passando o nome certo? Você lembrou de fechar ele com fclose depois de usar?\n");
		return;
	}
	fseek(fs, 0, SEEK_END);
	fl = ftell(fs);
	fseek(fs, 0, SEEK_SET);
	mb = (unsigned char *) malloc(fl);
	fread(mb, 1, fl, fs);

	cs = 0;
	for(i = 0; i < fl; i++) {
		cs += (unsigned long) mb[i];
	}
	printf("%lf\n", (cs / (double) 100));
	free(mb);
	fclose(fs);
}

void scan_quote_string(char *str) {

	/*
	*	Use essa função para ler um campo string delimitado entre aspas (").
	*	Chame ela na hora que for ler tal campo. Por exemplo:
	*
	*	A entrada está da seguinte forma:
	*		nomeDoCampo "MARIA DA SILVA"
	*
	*	Para ler isso para as strings já alocadas str1 e str2 do seu programa, você faz:
	*		scanf("%s", str1); // Vai salvar nomeDoCampo em str1
	*		scan_quote_string(str2); // Vai salvar MARIA DA SILVA em str2 (sem as aspas)
	*
	*/

	char R;

	while((R = getchar()) != EOF && isspace(R)); // ignorar espaços, \r, \n...

	if(R == 'N' || R == 'n') { // campo NULO
		getchar(); getchar(); getchar(); // ignorar o "ULO" de NULO.
		strcpy(str, ""); // copia string vazia
	} else if(R == '\"') {
		if(scanf("%[^\"]", str) != 1) { // ler até o fechamento das aspas
			strcpy(str, "");
		}
		getchar(); // ignorar aspas fechando
	} else if(R != EOF){ // vc tá tentando ler uma string que não tá entre aspas! Fazer leitura normal %s então, pois deve ser algum inteiro ou algo assim...
		str[0] = R;
		scanf("%s", &str[1]);
	} else { // EOF
		strcpy(str, "");
	}

}

// Função que lê uma string do arquivo de entrada
char * lerStr(){
    int i = 0; // variável auxiliar para o preenchimento da string
    char aux; // variável que vai receber o caractere do arquivo de entrada
    char * str = (char *) malloc(1); // alocação da string que será lida
    if(str == NULL){
        fprintf(stderr, "Erro ao alocar memoria na funcao lerStr()\n");
        exit(1);
    }

	// le caractere de stdin até que o caractere lido seja um espaço '\n' ou seja o fim do arquivo
    while(((aux = (char) getc(stdin)) != ' ') && aux != '\n' && aux != EOF){
        str = (char *) realloc(str, i + 2); // realoca string gerando espaço para o caractere atual e para o '\0'
        str[i] = aux; // adiciona o caractere lido na string
        i++;
    }
    str[i] = '\0'; // adicionando terminador nulo na string
    
    return str;
}

// Função que preenche o espaço no arquivo de dados com '$'
void preencheLixo(FILE * fDados, int espaco){
	char lixo = '$';
	for (int i = 0; i < espaco; i++)
	{
		fwrite(&lixo, sizeof(char), 1, fDados);
	}
	
    
}

void readQuoteField(char ** string, int * tam){
	*string = (char *)calloc(50, sizeof(char));
	if (*string == NULL) exit(1);
	scan_quote_string(*string);
	if(strcmp(*string, "") == 0)
		*tam = 0;
	else
		*tam = strlen(*string);
	getchar();
}

// Função que lê n campos do arquivo de entrada e atribui a uma struct reg, 'parcial' pois é usada quando
// não é necessário informar todos os campos do registro.
// lê nro de campos -> nome do campo -> valor do campo
void lerCamposRegParcial(REG_DADO * reg){

	int n;
	// lendo o numero de campos que serao lidos
	scanf(" %d", &n);
	getchar(); //descarta o ' ' da entrada
	
	char * campo;
    reg->id = -1;
    reg->idade = -1;
    reg->nacionalidade = NULL;
    reg->nomeClube = NULL;
    reg->nomeJogador = NULL;

	for (int j = 0; j < n; j++){
		campo = lerStr();
		if(!strcmp(campo, "id")){
			scanf(" %d", &reg->id);
			getchar(); //descarta o ' ' ou '\n' da entrada
		}else if (!strcmp(campo, "idade")){
			scanf(" %d", &reg->idade);
			getchar(); //descarta o ' ' ou '\n' da entrada
		}else if (!strcmp(campo, "nacionalidade")){
			//tenta alocar memoria para o campo caso nao consiga sai do programa
			if((reg->nacionalidade = (char *) calloc(sizeof(char), 50)) == NULL) exit(1); 
			scan_quote_string(reg->nacionalidade); // lendo a nacionalidade inserida pelo usuario
			getchar(); //descarta o ' ' ou '\n' da entrada
		}else if (!strcmp(campo, "nomeJogador")){
			//tenta alocar memoria para o campo caso nao consiga sai do programa
			if((reg->nomeJogador = (char *) calloc(sizeof(char), 50)) == NULL) exit(1);
			scan_quote_string(reg->nomeJogador); // lendo o nome do jogador inserido pelo usuario
			getchar(); //descarta o ' ' ou '\n' da entrada
		}else if (!strcmp(campo, "nomeClube")){
			//tenta alocar memoria para o campo caso nao consiga sai do programa
			if((reg->nomeClube = (char *) calloc(sizeof(char), 50)) == NULL) exit(1);
			scan_quote_string(reg->nomeClube); // lendo o nome do clube inserido pelo usuario
			getchar(); //descarta o ' ' ou '\n' da entrada
		}
		
		free(campo);
	}
}

// Função que lê todos campos do arquivo de entrada e atribui a uma struct reg, 'completo' pois é usada quando
// são informados todos os campos do registro, mesmo quando este é 'NULO'
// lê id -> idade -> nome do jogador -> nacionalidade -> nome do clube
void lerCamposRegCompleto(REG_DADO * reg){
	// Lê o id
	char id[20];
	scan_quote_string(id);
	reg->id = atoi(id);
	
	getchar();  // pula o espaço

	// Lê a idade
	char idade[10];
	scan_quote_string(idade);   // retorna "" se for NULO
	if(strcmp(idade, "") == 0) strcpy(idade, "-1"); // Se for "", preenche com -1
	reg->idade = atoi(idade);

	getchar();  // pula o espaço

	// Lê o nome do jogador e salva o tamanho
	readQuoteField(&(reg->nomeJogador), &(reg->tamNomeJog));

	// Lê a nacionalidade e salva o tamanho
	readQuoteField(&(reg->nacionalidade), &(reg->tamNacionalidade));

	// Lê o nome do clube e salva o tamanho
	readQuoteField(&(reg->nomeClube), &(reg->tamNomeClube));

	reg->removido = '0';  // não removido logicamente
	reg->tamanhoRegistro = 33 + reg->tamNomeJog + reg->tamNacionalidade + reg->tamNomeClube;    // calcula tamanho
	reg->prox = -1;       // inicializa prox com -1
}

// Compara dois registros campo a campo
bool comparaRegDado(REG_DADO reg1, REG_DADO reg2){
	// Compara todos os campos dos registros, e retorna 'True' caso todos os campos não nulos de reg1 forem iguais aos campos de reg2

	return  (reg1.id == -1 || reg1.id == reg2.id)&& // compara os IDs
			(reg1.idade == -1 || reg1.idade == reg2.idade)&& // compara as idades
			(reg1.nacionalidade == NULL || !strcmp(reg1.nacionalidade, reg2.nacionalidade)) && // compara as nacionalidades
			(reg1.nomeClube == NULL || !strcmp(reg1.nomeClube, reg2.nomeClube))&& // compara os nomes de clube
			(reg1.nomeJogador == NULL || !strcmp(reg1.nomeJogador, reg2.nomeJogador)); // compara os nomes dos jogadores
}