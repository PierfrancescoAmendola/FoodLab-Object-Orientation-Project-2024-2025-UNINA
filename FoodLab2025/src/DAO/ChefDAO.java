package DAO;

import java.sql.*;
import entity.Utente;

public class ChefDAO extends Utente
{
	// private Connection dataBase;
	public ChefDAO(String codiceFiscale, String nome, String cognome, String dataNascita, String username, String email, String password) 
	{
		super(codiceFiscale, nome, cognome, dataNascita, username,  email, password);
	}

	static public Utente searchChef(String emailOrUsername, String password) throws SQLException 
	{
		// Connection dataBase 
		Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
		
		// Determina se l'input è un'email o un nome utente
		// Prima cerca per email se l'input contiene "@" e sembra un'email valida
		boolean seemsLikeEmail = emailOrUsername.contains("@") && emailOrUsername.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
		
		Utente foundUtente = null;
		
		// Prima prova a cercare per email se sembra un'email
//		if (seemsLikeEmail) {
		String emailQuery = "SELECT * FROM \"Utente\" WHERE (LOWER(\"Email\") = ? OR LOWER(\"Username\") = ?) AND \"Password\" = ? AND \"Chef\" = ?";
		PreparedStatement emailSt = dataBase.prepareStatement(emailQuery);
		emailSt.setString(1, emailOrUsername.toLowerCase());
		emailSt.setString(2, emailOrUsername.toLowerCase());
		emailSt.setString(3, password);
		emailSt.setBoolean(4, true);
		ResultSet emailRs = emailSt.executeQuery();
		
		if (emailRs.next()) {
			foundUtente = new Utente(
				emailRs.getString("CodiceFiscale"),
				emailRs.getString("Nome"),
				emailRs.getString("Cognome"),
				emailRs.getDate("DataNascita").toString(),
				emailRs.getString("Username").toString(),
				emailRs.getString("Email"),
				emailRs.getString("Password")
			);
		}
		
		dataBase.close();
		return foundUtente;
	}

	static public boolean insertChef(Utente Utente) throws SQLException 
	{
		Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
		
		// Inserisci il nuovo Utente
		String insertQuery = "INSERT INTO \"Utente\" (\"CodiceFiscale\", \"Nome\", \"Cognome\", \"DataNascita\", \"Username\", \"Email\", \"Password\", \"Chef\" ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement insertSt = dataBase.prepareStatement(insertQuery);
		insertSt.setString(1, Utente.getCodiceFiscale() );
		insertSt.setString(2, Utente.getNome() );
		insertSt.setString(3, Utente.getCognome() );
		insertSt.setDate(4, java.sql.Date.valueOf(Utente.getDataNascita())); // Converte stringa in SQL Date
		insertSt.setString(5, Utente.getUsername() );
		insertSt.setString(6, Utente.getEmail() );
		insertSt.setString(7, Utente.getPassword() );
		insertSt.setBoolean(8, true);
		
		
		int rowsAffected = insertSt.executeUpdate();
		dataBase.close();
		
		return rowsAffected > 0;
	}
}
