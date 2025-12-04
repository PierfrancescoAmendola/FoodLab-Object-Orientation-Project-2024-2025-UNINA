package DAO;

import java.sql.*;
import java.util.ArrayList;
import entity.Utente;
import entity.Corso;

public class StudenteDAO extends Utente{
	public StudenteDAO(String nome, String cognome, String dataNascita, String username, String email,
			String password) {
		super(nome, cognome, dataNascita, username, email, password);
		// TODO Auto-generated constructor stub
	}

	public StudenteDAO(String codiceFiscale, String nome, String cognome, String dataNascita, String username,
			String email, String password) {
		super(codiceFiscale, nome, cognome, dataNascita, username, email, password);
		// TODO Auto-generated constructor stub
	}

	public static Utente searchStudente(String emailOrUsername, String password) throws SQLException {
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "SELECT * FROM \"Utente\" WHERE (LOWER(\"Email\") = ? OR LOWER(\"Username\") = ?) AND \"Password\" = ? ";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, emailOrUsername.toLowerCase());
        st.setString(2, emailOrUsername.toLowerCase());
        st.setString(3, password);
        //st.setBoolean(4, false); // Chef = false per studenti
        
        ResultSet rs = st.executeQuery();
        
        Utente foundUtente = null;
        if (rs.next()) {
            foundUtente = new Utente(
                rs.getString("CodiceFiscale"),
                rs.getString("Nome"),
                rs.getString("Cognome"),
                rs.getDate("DataNascita").toString(),
                rs.getString("Username"),
                rs.getString("Email"),
                rs.getString("Password")
            );
        }
        
        rs.close();
        st.close();
        dataBase.close();
        
        return foundUtente;
    }
    
    
    
    public static boolean insertStudente(Utente studente) throws SQLException {
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "INSERT INTO \"Utente\" (\"CodiceFiscale\", \"Nome\", \"Cognome\", \"DataNascita\", \"Username\", \"Email\", \"Password\", \"Chef\") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, studente.getCodiceFiscale());
        st.setString(2, studente.getNome());
        st.setString(3, studente.getCognome());
        st.setDate(4, java.sql.Date.valueOf(studente.getDataNascita())); // Converte stringa in SQL Date
        st.setString(5, studente.getUsername());
        st.setString(6, studente.getEmail());
        st.setString(7, studente.getPassword());
        
        int rowsAffected = st.executeUpdate();
        
        st.close();
        dataBase.close();
        
        return rowsAffected > 0;
    }
    
    /**
     * Trova uno studente per codice fiscale
     */
    public static Utente findStudenteByCodiceFiscale(String codiceFiscale) throws SQLException {
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "SELECT * FROM \"Utente\" WHERE \"CodiceFiscale\" = ?";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, codiceFiscale);
        
        ResultSet rs = st.executeQuery();
        
        Utente studente = null;
        if (rs.next()) {
            studente = new Utente(
                rs.getString("CodiceFiscale"),
                rs.getString("Nome"),
                rs.getString("Cognome"),
                rs.getDate("DataNascita").toString(),
                rs.getString("Username"),
                rs.getString("Email"),
                rs.getString("Password")
            );
        }
        
        rs.close();
        st.close();
        dataBase.close();
        
        return studente;
    }
}