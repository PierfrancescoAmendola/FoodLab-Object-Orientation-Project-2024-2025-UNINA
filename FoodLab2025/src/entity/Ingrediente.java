package entity;

public class Ingrediente 
{
	private int idRicetta;
	private String nome;
	private int quantita;
	
	public Ingrediente(int idRicetta, String nome, int quantita) {
		super();
		this.idRicetta = idRicetta;
		this.nome = nome;
		this.quantita = quantita;
	}

	public int getIdRicetta() {
		return idRicetta;
	}

	public String getNome() {
		return nome;
	}

	public int getQuantita() {
		return quantita;
	}

	public void setIdRicetta(int idRicetta) {
		this.idRicetta = idRicetta;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}
}
