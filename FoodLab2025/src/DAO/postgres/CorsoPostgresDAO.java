package dao.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.CorsoDAO;
import entity.Corso;

public class CorsoPostgresDAO implements CorsoDAO
{
	@Override
	public boolean insertCorso(Corso corso) throws SQLException 
	{
		Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
		
		//Inserisce un nuovo corso
		String query = "INSERT INTO \"Corso\" (\"ID_Corso\", \"Argomento\", \"Descrizione\", \"DataInizio\", \"Frequenza\", \"MaxPartecipanti\", \"Online\", \"ID_Chef\") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement st = dataBase.prepareStatement(query);
		st.setString(1, corso.getIdCorso()); 							// ID_Corso
		st.setString(2, corso.getArgomento());							// Argomento
		st.setString(3, corso.getDescrizione() );						// Descrizione
		st.setDate(4, java.sql.Date.valueOf(corso.getDataInizio())); 	// DataInizio
		st.setString(5, corso.getFrequenza()); 							// Frequenza
		st.setInt(6, corso.getMaxPartecipanti()); 						// Max partecipanti
		st.setBoolean(7, corso.getModalitaOnline()); 					// Modalita
		st.setString(8, corso.getIdChef()); 							// ID_Chef
		
		int rowsAffected = st.executeUpdate(); //Esegue la query e ritorna il numero di righe interessate
		
		// Chiudi le risorse in ordine inverso
		st.close();
		dataBase.close();
		
		return rowsAffected > 0; //Se sono state interessate righe ritorna true
	}
	
	@Override
	public ArrayList<Corso> findCorsi(String idChef) throws SQLException 
	{
		ArrayList<Corso> elencoCorsi = new ArrayList<Corso>();
		Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
		
		//Seleziona tutti i corsi che gestisce lo chef
		String query = "SELECT * FROM \"Corso\" WHERE \"ID_Chef\" = ?";
		System.out.println("DEBUG CorsoDAO: Eseguendo query: " + query + " con ID_Chef: " + idChef);
		PreparedStatement st = dataBase.prepareStatement(query);
		st.setString(1, idChef);
		ResultSet rs = st.executeQuery();	//Risultato query
		
		//Aggiungi i corsi nell'elencoCorsi
		int count = 0;
		while( rs.next() ) {
			count++;
			System.out.println("DEBUG CorsoDAO: Trovato corso " + count + ": " + rs.getString("Argomento") + " (ID: " + rs.getString("ID_Corso") + ")");
			elencoCorsi.add( new Corso(
					rs.getString("ID_Corso"),
					rs.getString("Argomento"),
					rs.getString("Descrizione"),
					rs.getDate("DataInizio").toString(),
					rs.getString("Frequenza"),
					rs.getInt("MaxPartecipanti"),
					rs.getBoolean("Online"),
					rs.getString("ID_Chef")
				));
			// Debug commentato per evitare problemi nell'interfaccia
			// System.out.print("Argomento: " + rs.getString("Argomento") );
		}
		System.out.println("DEBUG CorsoDAO: Totale corsi trovati: " + count);
		
		// Chiudi le risorse in ordine inverso
		rs.close();
		st.close();
		dataBase.close();
		
		return elencoCorsi;
	}
	
	@Override
	public void eliminaCorso(Corso corso) throws SQLException
	{
		Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
		
		String query = "DELETE FROM \"Corso\" WHERE \"ID_Corso\" = ?";
		PreparedStatement st = dataBase.prepareStatement(query);
		st.setString( 1, corso.getIdCorso() );
		try {
			ResultSet rs = st.executeQuery();	//Risultato query
		} // Non mostrare eccezione quando la delete funziona
		catch(SQLException e){
			System.out.println(e.getMessage());
			if( ! e.getMessage().contains("Nessun risultato è stato restituito dalla query") )
				throw e;
		}
	}
	
	/**
	 * Trova tutti i corsi disponibili nel sistema
	 */
	@Override
	public ArrayList<Corso> findTuttiCorsi() throws SQLException {
		ArrayList<Corso> elencoCorsi = new ArrayList<Corso>();
		Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
		
		String query = "SELECT * FROM \"Corso\" ORDER BY \"DataInizio\" DESC";
		PreparedStatement st = dataBase.prepareStatement(query);
		ResultSet rs = st.executeQuery();
		
		while(rs.next()) {
			elencoCorsi.add(new Corso(
					rs.getString("ID_Corso"),
					rs.getString("Argomento"),
					rs.getString("Descrizione"),
					rs.getDate("DataInizio").toString(),
					rs.getString("Frequenza"),
					rs.getInt("MaxPartecipanti"),
					rs.getBoolean("Online"),
					rs.getString("ID_Chef")
			));
		}
		
		rs.close();
		st.close();
		dataBase.close();
		
		return elencoCorsi;
	}
	
	/**
	 * Trova un corso per ID
	 */
	@Override
	public Corso findCorsoById(String idCorso) throws SQLException {
		Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
		
		String query = "SELECT * FROM \"Corso\" WHERE \"ID_Corso\" = ?";
		PreparedStatement st = dataBase.prepareStatement(query);
		st.setString(1, idCorso);
		ResultSet rs = st.executeQuery();
		
		Corso corso = null;
		if(rs.next()) {
			corso = new Corso(
					rs.getString("ID_Corso"),
					rs.getString("Argomento"),
					rs.getString("Descrizione"),
					rs.getDate("DataInizio").toString(),
					rs.getString("Frequenza"),
					rs.getInt("MaxPartecipanti"),
					rs.getBoolean("Online"),
					rs.getString("ID_Chef")
			);
		}
		
		rs.close();
		st.close();
		dataBase.close();
		
		return corso;
	}
	
	/**
	 * Verifica se un utente è già iscritto a un corso
	 */
	@Override
	public boolean isUtenteIscritto(String idUtente, String idCorso) throws SQLException {
		Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
		
		String query = "SELECT COUNT(*) FROM \"Iscrizione\" WHERE \"ID_Utente\" = ? AND \"ID_Corso\" = ?";
		PreparedStatement st = dataBase.prepareStatement(query);
		st.setString(1, idUtente);
		st.setString(2, idCorso);
		ResultSet rs = st.executeQuery();
		
		boolean iscritto = false;
		if(rs.next()) {
			iscritto = rs.getInt(1) > 0;
		}
		
		rs.close();
		st.close();
		dataBase.close();
		
		return iscritto;
	}
	
	/**
	 * Conta il numero di iscritti a un corso
	 */
	@Override
	public int contaIscrittiCorso(String idCorso) throws SQLException {
		Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
		
		String query = "SELECT COUNT(*) FROM \"Iscrizione\" WHERE \"ID_Corso\" = ?";
		PreparedStatement st = dataBase.prepareStatement(query);
		st.setString(1, idCorso);
		ResultSet rs = st.executeQuery();
		
		int count = 0;
		if(rs.next()) {
			count = rs.getInt(1);
		}
		
		rs.close();
		st.close();
		dataBase.close();
		
		return count;
	}
	
	/**
	 * Trova tutti i corsi disponibili nel database
	 */
	@Override
	public ArrayList<Corso> getAllCorsi() throws SQLException {
		ArrayList<Corso> corsi = new ArrayList<>();
		Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
		
		String query = "SELECT * FROM \"Corso\" ORDER BY \"DataInizio\" DESC";
		PreparedStatement st = dataBase.prepareStatement(query);
		ResultSet rs = st.executeQuery();
		
		while(rs.next()) {
			Corso corso = new Corso(
					rs.getString("ID_Corso"),
					rs.getString("Argomento"),
					rs.getString("Descrizione"),
					rs.getDate("DataInizio").toString(),
					rs.getString("Frequenza"),
					rs.getInt("MaxPartecipanti"),
					rs.getBoolean("Online"),
					rs.getString("ID_Chef")
				);
			corsi.add(corso);
		}
		
		rs.close();
		st.close();
		dataBase.close();
		
		return corsi;
	}
	
	@Override
	public ArrayList<Corso> searchFiltrered(String idChef, String argomento) throws SQLException 
	{
		ArrayList<Corso> elencoCorsi = new ArrayList<Corso>();
		Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
		
		//Seleziona tutti i corsi che gestisce lo chef
		if(argomento == null) //Controlla che l'argomento è valido
			argomento = "";
		String query = "SELECT * FROM \"Corso\" WHERE \"ID_Chef\" = ? AND \"Argomento\" LIKE '%" + argomento + "%'";
		PreparedStatement st = dataBase.prepareStatement(query);
		
		
		
		st.setString(1, idChef);
		//Esecuzione query
		ResultSet rs = st.executeQuery();	//Risultato query
		int count = 0;
		while( rs.next() ) {
			count++;
			//System.out.println("DEBUG CorsoDAO: Trovato corso " + count + ": " + rs.getString("Argomento") + " (ID: " + rs.getString("ID_Corso") + ")");
			elencoCorsi.add( new Corso(
					rs.getString("ID_Corso"),
					rs.getString("Argomento"),
					rs.getString("Descrizione"),
					rs.getDate("DataInizio").toString(),
					rs.getString("Frequenza"),
					rs.getInt("MaxPartecipanti"),
					rs.getBoolean("Online"),
					rs.getString("ID_Chef")
				));
			// Debug commentato per evitare problemi nell'interfaccia
			// System.out.print("Argomento: " + rs.getString("Argomento") );
		}
		System.out.println("DEBUG CorsoDAO: Totale corsi di argomento " + argomento + " trovati: " + count);
		
		rs.close();
		st.close();
		dataBase.close();
		return elencoCorsi;
	}
	
	@Override
	public ArrayList<Corso> getCorsiIscritto(String idStudente) throws SQLException {
        ArrayList<Corso> corsiIscritto = new ArrayList<>();
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "SELECT c.* " +
                      "FROM \"Corso\" c " +
                      "JOIN \"Iscrizione\" i ON c.\"ID_Corso\" = i.\"ID_Corso\" " +
                      "WHERE i.\"ID_Utente\" = ? " +
                      "ORDER BY c.\"DataInizio\" DESC";
        
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, idStudente);
        ResultSet rs = st.executeQuery();
        
        while (rs.next()) {
            corsiIscritto.add(new Corso(
            		rs.getString("ID_Corso"),
					rs.getString("Argomento"),
					rs.getString("Descrizione"),
					rs.getDate("DataInizio").toString(),
					rs.getString("Frequenza"),
					rs.getInt("MaxPartecipanti"),
					rs.getBoolean("Online"),
					rs.getString("ID_Chef")
            ));
        }
        
        rs.close();
        st.close();
        dataBase.close();
        
        return corsiIscritto;
    }
}
