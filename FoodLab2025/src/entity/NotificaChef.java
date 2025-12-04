package entity;

/**
 * Classe che rappresenta una notifica per gli chef
 */
public class NotificaChef {
    private int idNotifica;
    private String messaggio;
    private String tipoNotifica;
    private String idChef;
    private String idCorso;
    private String dataCreazione;
    private boolean letto;
    
    // Costruttore per nuove notifiche (senza ID)
    public NotificaChef(String messaggio, String tipoNotifica, String idChef, String idCorso) {
        this.messaggio = messaggio;
        this.tipoNotifica = tipoNotifica;
        this.idChef = idChef;
        this.idCorso = idCorso;
        this.letto = false;
    }
    
    // Costruttore completo (con ID per notifiche dal database)
    public NotificaChef(int idNotifica, String messaggio, String tipoNotifica, String idChef, String idCorso, String dataCreazione, boolean letto) {
        this.idNotifica = idNotifica;
        this.messaggio = messaggio;
        this.tipoNotifica = tipoNotifica;
        this.idChef = idChef;
        this.idCorso = idCorso;
        this.dataCreazione = dataCreazione;
        this.letto = letto;
    }
    
    // Getters e Setters
    public int getIdNotifica() {
        return idNotifica;
    }
    
    public void setIdNotifica(int idNotifica) {
        this.idNotifica = idNotifica;
    }
    
    public String getMessaggio() {
        return messaggio;
    }
    
    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }
    
    public String getTipoNotifica() {
        return tipoNotifica;
    }
    
    public void setTipoNotifica(String tipoNotifica) {
        this.tipoNotifica = tipoNotifica;
    }
    
    public String getIdChef() {
        return idChef;
    }
    
    public void setIdChef(String idChef) {
        this.idChef = idChef;
    }
    
    public String getIdCorso() {
        return idCorso;
    }
    
    public void setIdCorso(String idCorso) {
        this.idCorso = idCorso;
    }
    
    public String getDataCreazione() {
        return dataCreazione;
    }
    
    public void setDataCreazione(String dataCreazione) {
        this.dataCreazione = dataCreazione;
    }
    
    public boolean isLetto() {
        return letto;
    }
    
    public void setLetto(boolean letto) {
        this.letto = letto;
    }
    
    @Override
    public String toString() {
        return "NotificaChef{" +
                "idNotifica=" + idNotifica +
                ", messaggio='" + messaggio + '\'' +
                ", tipoNotifica='" + tipoNotifica + '\'' +
                ", idChef='" + idChef + '\'' +
                ", idCorso='" + idCorso + '\'' +
                ", dataCreazione='" + dataCreazione + '\'' +
                ", letto=" + letto +
                '}';
    }
}