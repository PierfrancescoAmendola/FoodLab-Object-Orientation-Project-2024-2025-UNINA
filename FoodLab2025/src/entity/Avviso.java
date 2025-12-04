package entity;

public class Avviso {
    private int idAvviso;
    private String messaggio;
    private String tipoAvviso; // "CORSO_DISPONIBILE", "GENERALE", etc.
    private String idUtente;
    private String idCorso;
    private String dataCreazione;
    private boolean letto;

    public Avviso(int idAvviso, String messaggio, String tipoAvviso, String idUtente, String idCorso, String dataCreazione, boolean letto) {
        this.idAvviso = idAvviso;
        this.messaggio = messaggio;
        this.tipoAvviso = tipoAvviso;
        this.idUtente = idUtente;
        this.idCorso = idCorso;
        this.dataCreazione = dataCreazione;
        this.letto = letto;
    }

    // Costruttore per creare nuovo avviso (senza ID, sarà generato dal database)
    public Avviso(String messaggio, String tipoAvviso, String idUtente, String idCorso) {
        this.messaggio = messaggio;
        this.tipoAvviso = tipoAvviso;
        this.idUtente = idUtente;
        this.idCorso = idCorso;
        this.letto = false;
    }

    // Getters
    public int getIdAvviso() { return idAvviso; }
    public String getMessaggio() { return messaggio; }
    public String getTipoAvviso() { return tipoAvviso; }
    public String getIdUtente() { return idUtente; }
    public String getIdCorso() { return idCorso; }
    public String getDataCreazione() { return dataCreazione; }
    public boolean isLetto() { return letto; }

    // Setters
    public void setIdAvviso(int idAvviso) { this.idAvviso = idAvviso; }
    public void setMessaggio(String messaggio) { this.messaggio = messaggio; }
    public void setTipoAvviso(String tipoAvviso) { this.tipoAvviso = tipoAvviso; }
    public void setIdUtente(String idUtente) { this.idUtente = idUtente; }
    public void setIdCorso(String idCorso) { this.idCorso = idCorso; }
    public void setDataCreazione(String dataCreazione) { this.dataCreazione = dataCreazione; }
    public void setLetto(boolean letto) { this.letto = letto; }

    @Override
    public String toString() {
        return "Avviso{" +
                "idAvviso=" + idAvviso +
                ", messaggio='" + messaggio + '\'' +
                ", tipoAvviso='" + tipoAvviso + '\'' +
                ", idUtente='" + idUtente + '\'' +
                ", idCorso='" + idCorso + '\'' +
                ", dataCreazione='" + dataCreazione + '\'' +
                ", letto=" + letto +
                '}';
    }
}