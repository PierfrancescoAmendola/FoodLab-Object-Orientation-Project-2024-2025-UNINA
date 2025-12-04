package entity;

public class Sessione {
    private int idSessione;
    private String data;
    private String oraInizio;
    private boolean online;
    private String idCorso;

    public Sessione(int idSessione, String data, String oraInizio, boolean online, String idCorso) {
        this.idSessione = idSessione;
        this.data = data;
        this.oraInizio = oraInizio;
        this.online = online;
        this.idCorso = idCorso;
    }

    public int getIdSessione() {
        return idSessione;
    }

    public String getData() {
        return data;
    }

    public String getOraInizio() {
        return oraInizio;
    }

    public boolean isOnline() {
        return online;
    }

    public String getIdCorso() {
        return idCorso;
    }
}