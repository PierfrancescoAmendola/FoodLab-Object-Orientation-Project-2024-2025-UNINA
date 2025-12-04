package entity;

public class Iscrizione {
    private String idUtente;
    private String idCorso;

    public Iscrizione(String idUtente, String idCorso) {
        this.idUtente = idUtente;
        this.idCorso = idCorso;
    }

    public String getIdUtente() {
        return idUtente;
    }

    public String getIdCorso() {
        return idCorso;
    }
}