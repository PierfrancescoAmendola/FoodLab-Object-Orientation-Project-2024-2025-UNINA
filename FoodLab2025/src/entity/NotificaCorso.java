package entity;

public class NotificaCorso {
    private int idNotifica;
    private String idUtente;
    private String idCorso;
    private String dataRichiesta;
    private boolean attiva;

    public NotificaCorso(int idNotifica, String idUtente, String idCorso, String dataRichiesta, boolean attiva) {
        this.idNotifica = idNotifica;
        this.idUtente = idUtente;
        this.idCorso = idCorso;
        this.dataRichiesta = dataRichiesta;
        this.attiva = attiva;
    }

    // Costruttore per creare nuova notifica (senza ID, sarà generato dal database)
    public NotificaCorso(String idUtente, String idCorso) {
        this.idUtente = idUtente;
        this.idCorso = idCorso;
        this.attiva = true;
    }

    // Getters
    public int getIdNotifica() { return idNotifica; }
    public String getIdUtente() { return idUtente; }
    public String getIdCorso() { return idCorso; }
    public String getDataRichiesta() { return dataRichiesta; }
    public boolean isAttiva() { return attiva; }

    // Setters
    public void setIdNotifica(int idNotifica) { this.idNotifica = idNotifica; }
    public void setIdUtente(String idUtente) { this.idUtente = idUtente; }
    public void setIdCorso(String idCorso) { this.idCorso = idCorso; }
    public void setDataRichiesta(String dataRichiesta) { this.dataRichiesta = dataRichiesta; }
    public void setAttiva(boolean attiva) { this.attiva = attiva; }

    @Override
    public String toString() {
        return "NotificaCorso{" +
                "idNotifica=" + idNotifica +
                ", idUtente='" + idUtente + '\'' +
                ", idCorso='" + idCorso + '\'' +
                ", dataRichiesta='" + dataRichiesta + '\'' +
                ", attiva=" + attiva +
                '}';
    }
}