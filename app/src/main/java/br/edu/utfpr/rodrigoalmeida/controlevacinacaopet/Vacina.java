package br.edu.utfpr.rodrigoalmeida.controlevacinacaopet;

import java.util.Comparator;

public class Vacina {

    public static Comparator ordenacaoCrescente = new Comparator<Vacina>() {
        @Override
        public int compare(Vacina vacina1, Vacina vacina2) {
            return vacina1.getNome().compareToIgnoreCase(vacina2.getNome());
    }
    };

    public static Comparator ordenacaoDecrescente = new Comparator<Vacina>() {
        @Override
        public int compare(Vacina vacina1, Vacina vacina2) {
            return -1 * vacina1.getNome().compareToIgnoreCase(vacina2.getNome());
        }
    };

    private String nome;
    private String dataNascimento;
    private String telefone;
    private String sexo;
    private String racaPet;
    private String nomePet;
    private float valorVacina;
    private Tipo tipo;



    public String getTelefone() {
        return telefone;
    }

    public Vacina(String nome, String dataNascimento, String telefone, String sexo, String racaPet, String nomePet) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.sexo = sexo;
        this.racaPet = racaPet;
        this.nomePet = nomePet;
        this.valorVacina = valorVacina;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getRacaPet() {
        return racaPet;
    }

    public void setRacaPet(String racaPet) {
        this.racaPet = racaPet;
    }

    public String getNomePet() {
        return nomePet;
    }

    public void setNomePet(String nomePet) {
        this.nomePet = nomePet;
    }

    public Vacina(String nome) {
        this.nome = nome;

    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) { this.nome = nome; }

    public float getValorVacina() {return valorVacina;}

    public void setValorVacina(float valorVacina) {this.valorVacina = valorVacina; }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        // Dados do tutor
        stringBuilder.append("Dados Tutor:\n");
        stringBuilder.append("Nome: ").append(nome).append("\n");
        stringBuilder.append("Telefone: ").append(telefone).append("\n");
        stringBuilder.append("Gênero: ").append(sexo).append("\n\n");

        // Dados do PET
        stringBuilder.append("Dados PET:\n");
        stringBuilder.append("Nome: ").append(nomePet).append("\n");
        stringBuilder.append("Raça: ").append(racaPet).append("\n");
        stringBuilder.append("Data de Nascimento: ").append(dataNascimento).append("\n");
        stringBuilder.append("Sexo: ").append(sexo).append("\n");

        return stringBuilder.toString();
    }
}