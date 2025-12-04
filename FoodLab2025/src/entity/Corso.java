package entity;

public class Corso
{
	private String idCorso;
	private String argomento;
	private String descrizione;
	private String dataInizio;
	private String frequenza;
	private int maxPartecipanti;
	private String idChef;
	private boolean modalitaOnline;
	
	public Corso(String idCorso, String argomento, String descrizione, String dataInizio, 
			String frequenza, int maxPartecipanti, boolean modalitaOnline, String idChef) 
	{
		super();
		this.idCorso = idCorso;
		this.argomento = argomento;
		this.descrizione = descrizione;
		this.dataInizio = dataInizio;
		this.frequenza = frequenza;
		this.maxPartecipanti = maxPartecipanti;
		this.modalitaOnline = modalitaOnline;
		this.idChef = idChef;
	}
	
	public Corso(String argomento, String descrizione, String dataInizio, 
			String frequenza, int maxPartecipanti, boolean modalitaOnline, String idChef) 
	{
		super();
		idCorso  = "CRS" + System.currentTimeMillis() % 1000000; //ID_Corso generato
		this.idCorso = idCorso;
		this.argomento = argomento;
		this.descrizione = descrizione;
		this.dataInizio = dataInizio;
		this.frequenza = frequenza;
		this.maxPartecipanti = maxPartecipanti;
		this.modalitaOnline = modalitaOnline;
		this.idChef = idChef;
	}

	public String getIdCorso() {
		return idCorso;
	}

	public String getArgomento() {
		return argomento;
	}

	public String getDescrizione() {
		return descrizione;
	}
	
	public String getDataInizio() {
		return dataInizio;
	}

	public String getFrequenza() {
		return frequenza;
	}
	
	public int getMaxPartecipanti() {
		return maxPartecipanti;
	}
	
	public boolean getModalitaOnline() {
		return modalitaOnline;
	}
	
	public String getIdChef() {
		return idChef;
	}
	
	
}

