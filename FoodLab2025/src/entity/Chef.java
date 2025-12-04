package entity;

public class Chef 
{
	private String codiceFiscale;
	private String nome;
	private String cognome;
	private String dataNascita;
	private String username;
	private String email;
	private String password;
	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getDataNascita() {
		return dataNascita;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	
	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getUsername() {
		return username;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setUsername(String email) {
		this.email = username;
	}
	
	public Chef(String nome, String cognome, String dataNascita, String username, String email, String password) {
		super();
		//Genera codice fisccale
		codiceFiscale = nome.substring(0, Math.min(2, nome.length())).toUpperCase() + 
				                      cognome.substring(0, Math.min(2, cognome.length())).toUpperCase() + 
				                      String.format("%04d", (int)(Math.random() * 10000));
		this.nome = nome;
		this.cognome = cognome;
		this.dataNascita = dataNascita;
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Chef(String codiceFiscale, String nome, String cognome, String dataNascita, String username,  String email, String password) {
		
		this.codiceFiscale = codiceFiscale;
		this.nome = nome;
		this.cognome = cognome;
		this.dataNascita = dataNascita;
		this.email = username;
		this.email = email;
		this.password = password;
	}
}
