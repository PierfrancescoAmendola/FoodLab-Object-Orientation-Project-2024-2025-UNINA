package entity;

public class Ricetta {
    private int idRicetta;
    private String nome;
    private int idSessione;

    public Ricetta(int idRicetta, String nome, int idSessione) {
        this.idRicetta = idRicetta;
        this.nome = nome;
        this.idSessione = idSessione;
    }

    public int getIdRicetta() {
        return idRicetta;
    }

    public String getNome() {
        return nome;
    }

    public int getIdSessione() {
        return idSessione;
    }
}