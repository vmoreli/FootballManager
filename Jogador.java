public class Jogador {

    private int id;
    private int idade;
    private String nomeJogador;
    private String nacionalidade;
    private String nomeClube;

    public Jogador(int id, int idade, String nomeJogador, String nacionalidade, String nomeClube) {
        this.id = id;
        this.idade = idade;
        this.nomeJogador = nomeJogador;
        this.nacionalidade = nacionalidade;
        this.nomeClube = nomeClube;
    }

    //Construtor a partir de uma String no formato "<id>,<idade>,<nomeJogador>,<nacionalidade>,<nomeClube>"
    public Jogador(String jogString) {
        String[] splitStrJog;
        splitStrJog =  jogString.split(",");
        this.id = Integer.parseInt(splitStrJog[0]);
        this.idade = Integer.parseInt(splitStrJog[1]);
        this.nomeJogador = splitStrJog[2];
        this.nacionalidade = splitStrJog[3];
        this.nomeClube = splitStrJog[4];
    }

    //Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getNomeJogador() {
        return nomeJogador;
    }

    public void setNomeJogador(String nomeJogador) {
        this.nomeJogador = nomeJogador;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getNomeClube() {
        return nomeClube;
    }

    public void setNomeClube(String nomeClube) {
        this.nomeClube = nomeClube;
    }

    public String toString1() {
        // Essa string será o jogador resultante no formato:
        // "<numCampos> <nomeCampo1> <valorCampo1> ... <nomeCampoN> <valorCampoN>"
        String jogador = "";
        int numCampos = 0; // numero de campos especificados

        if(id != -1){
            numCampos++;
            jogador = jogador.concat(" id " + id);
        }
        if(idade != -1){
            numCampos++;
            jogador = jogador.concat(" idade " + idade);
        }
        if(nomeJogador != null){
            numCampos++;
            jogador = jogador.concat(" nomeJogador \"" + nomeJogador + "\"");
        }
        if(nacionalidade != null){
            numCampos++;
            jogador = jogador.concat(" nacionalidade \"" + nacionalidade + "\"");
        }
        if(nomeClube != null){
            numCampos++;
            jogador = jogador.concat(" nomeClube \"" + nomeClube + "\"");
        }
        jogador = Integer.toString(numCampos).concat(jogador.concat("\n"));
        return  jogador;
    }

    @Override
    public String toString() {
        // Essa string será o jogador resultante no formato:
        // "<id> <idade> <"nomeJogador"> <"nacionalidade"> <"nomeClube">"
        String jogador = "";

        if(id != -1){
            jogador = jogador.concat(id + " ");
        }else{
            jogador = jogador.concat("NULO ");
        }
        if(idade != -1){
            jogador = jogador.concat(idade + " ");
        }else{
            jogador = jogador.concat("NULO ");
        }
        if(nomeJogador != null){
            jogador = jogador.concat("\"" + nomeJogador + "\" ");
        }else{
            jogador = jogador.concat("NULO ");
        }
        if(nacionalidade != null){
            jogador = jogador.concat("\"" + nacionalidade + "\" ");
        }else{
            jogador = jogador.concat("NULO ");
        }
        if(nomeClube != null){
            jogador = jogador.concat("\"" + nomeClube + "\" ");
        }else{
            jogador = jogador.concat("NULO ");
        }
        return  jogador;
    }
}

