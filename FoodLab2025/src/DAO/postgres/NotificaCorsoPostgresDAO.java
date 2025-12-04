package dao.postgres;

import java.sql.*;
import java.util.ArrayList;

import dao.NotificaCorsoDAO;
import entity.NotificaCorso;

public class NotificaCorsoPostgresDAO implements NotificaCorsoDAO {
    
    /**
     * Inserisce una nuova richiesta di notifica per un corso
     */
    @Override
	public boolean insertNotificaCorso(NotificaCorso notifica) throws SQLException {
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "INSERT INTO \"NotificaCorso\" (\"ID_Utente\", \"ID_Corso\", \"DataRichiesta\", \"Attiva\") VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, notifica.getIdUtente());
        st.setString(2, notifica.getIdCorso());
        st.setBoolean(3, notifica.isAttiva());
        
        int rowsAffected = st.executeUpdate();
        st.close();
        dataBase.close();
        
        return rowsAffected > 0;
    }
    
    /**
     * Trova tutte le notifiche attive per un corso
     */
    @Override
	public ArrayList<NotificaCorso> getNotificheAtiveByCorso(String idCorso) throws SQLException {
        ArrayList<NotificaCorso> notifiche = new ArrayList<>();
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "SELECT * FROM \"NotificaCorso\" WHERE \"ID_Corso\" = ? AND \"Attiva\" = true";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, idCorso);
        ResultSet rs = st.executeQuery();
        
        while (rs.next()) {
            notifiche.add(new NotificaCorso(
                rs.getInt("ID_Notifica"),
                rs.getString("ID_Utente"),
                rs.getString("ID_Corso"),
                rs.getTimestamp("DataRichiesta").toString(),
                rs.getBoolean("Attiva")
            ));
        }
        
        rs.close();
        st.close();
        dataBase.close();
        
        return notifiche;
    }
    
    /**
     * Trova tutte le notifiche per un utente
     */
    @Override
	public ArrayList<NotificaCorso> getNotificheByUtente(String idUtente) throws SQLException {
        ArrayList<NotificaCorso> notifiche = new ArrayList<>();
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "SELECT * FROM \"NotificaCorso\" WHERE \"ID_Utente\" = ? ORDER BY \"DataRichiesta\" DESC";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, idUtente);
        ResultSet rs = st.executeQuery();
        
        while (rs.next()) {
            notifiche.add(new NotificaCorso(
                rs.getInt("ID_Notifica"),
                rs.getString("ID_Utente"),
                rs.getString("ID_Corso"),
                rs.getTimestamp("DataRichiesta").toString(),
                rs.getBoolean("Attiva")
            ));
        }
        
        rs.close();
        st.close();
        dataBase.close();
        
        return notifiche;
    }
    
    /**
     * Verifica se esiste già una notifica attiva per un utente e corso
     */
    @Override
	public boolean esisteNotificaAttiva(String idUtente, String idCorso) throws SQLException {
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "SELECT COUNT(*) FROM \"NotificaCorso\" WHERE \"ID_Utente\" = ? AND \"ID_Corso\" = ? AND \"Attiva\" = true";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, idUtente);
        st.setString(2, idCorso);
        ResultSet rs = st.executeQuery();
        
        boolean esiste = false;
        if (rs.next()) {
            esiste = rs.getInt(1) > 0;
        }
        
        rs.close();
        st.close();
        dataBase.close();
        
        return esiste;
    }
    
    /**
     * Disattiva una notifica
     */
    @Override
	public boolean disattivaNotifica(int idNotifica) throws SQLException {
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "UPDATE \"NotificaCorso\" SET \"Attiva\" = false WHERE \"ID_Notifica\" = ?";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setInt(1, idNotifica);
        
        int rowsAffected = st.executeUpdate();
        st.close();
        dataBase.close();
        
        return rowsAffected > 0;
    }
    
    /**
     * Disattiva tutte le notifiche per un utente e corso specifico
     */
    @Override
	public boolean disattivaNotificheUtentCorso(String idUtente, String idCorso) throws SQLException {
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "UPDATE \"NotificaCorso\" SET \"Attiva\" = false WHERE \"ID_Utente\" = ? AND \"ID_Corso\" = ?";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, idUtente);
        st.setString(2, idCorso);
        
        int rowsAffected = st.executeUpdate();
        st.close();
        dataBase.close();
        
        return rowsAffected > 0;
    }
}