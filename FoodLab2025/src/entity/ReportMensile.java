package entity;

public class ReportMensile {
    private int mese;
    private int anno;
    private int corsiTotali;
    private int sessioniOnline;
    private int sessioniPratiche;
    private double mediaRicettePratiche;
    private int maxRicettePratiche;
    private int minRicettePratiche;
    private String nomeChef;
    
    public ReportMensile(int mese, int anno, int corsiTotali, int sessioniOnline, 
                        int sessioniPratiche, double mediaRicettePratiche, 
                        int maxRicettePratiche, int minRicettePratiche, String nomeChef) {
        this.mese = mese;
        this.anno = anno;
        this.corsiTotali = corsiTotali;
        this.sessioniOnline = sessioniOnline;
        this.sessioniPratiche = sessioniPratiche;
        this.mediaRicettePratiche = mediaRicettePratiche;
        this.maxRicettePratiche = maxRicettePratiche;
        this.minRicettePratiche = minRicettePratiche;
        this.nomeChef = nomeChef;
    }
    
    // Getters
    public int getMese() { return mese; }
    public int getAnno() { return anno; }
    public int getCorsiTotali() { return corsiTotali; }
    public int getSessioniOnline() { return sessioniOnline; }
    public int getSessioniPratiche() { return sessioniPratiche; }
    public double getMediaRicettePratiche() { return mediaRicettePratiche; }
    public int getMaxRicettePratiche() { return maxRicettePratiche; }
    public int getMinRicettePratiche() { return minRicettePratiche; }
    public String getNomeChef() { return nomeChef; }
    
    public String getNomeMese() {
        String[] nomiMesi = {
            "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
            "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"
        };
        return nomiMesi[mese - 1];
    }
    
    public int getTotaleSessioni() {
        return sessioniOnline + sessioniPratiche;
    }
}