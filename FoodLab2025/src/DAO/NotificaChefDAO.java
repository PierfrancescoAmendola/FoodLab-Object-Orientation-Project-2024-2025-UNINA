package DAO;

import java.sql.*;
import java.util.ArrayList;
import entity.NotificaChef;

public class NotificaChefDAO {
    
    /**
     * Inserisce una nuova notifica per il chef nel database
     */
    public static boolean insertNotificaChef(NotificaChef notifica) throws SQLException {
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "INSERT INTO \"NotificaChef\" (\"Messaggio\", \"TipoNotifica\", \"ID_Chef\", \"ID_Corso\", \"DataCreazione\", \"Letto\") VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, ?)";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, notifica.getMessaggio());
        st.setString(2, notifica.getTipoNotifica());
        st.setString(3, notifica.getIdChef());
        st.setString(4, notifica.getIdCorso());
        st.setBoolean(5, notifica.isLetto());
        
        int rowsAffected = st.executeUpdate();
        st.close();
        dataBase.close();
        
        return rowsAffected > 0;
    }
    
    /**
     * Trova tutte le notifiche per un chef specifico
     */
    public static ArrayList<NotificaChef> getNotificheByChef(String idChef) throws SQLException {
        ArrayList<NotificaChef> notifiche = new ArrayList<>();
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "SELECT * FROM \"NotificaChef\" WHERE \"ID_Chef\" = ? ORDER BY \"DataCreazione\" DESC";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, idChef);
        ResultSet rs = st.executeQuery();
        
        while (rs.next()) {
            notifiche.add(new NotificaChef(
                rs.getInt("ID_Notifica"),
                rs.getString("Messaggio"),
                rs.getString("TipoNotifica"),
                rs.getString("ID_Chef"),
                rs.getString("ID_Corso"),
                rs.getTimestamp("DataCreazione").toString(),
                rs.getBoolean("Letto")
            ));
        }
        
        rs.close();
        st.close();
        dataBase.close();
        
        return notifiche;
    }
    
    /**
     * Trova le notifiche non lette per un chef
     */
    public static ArrayList<NotificaChef> getNotificheNonLetteByChef(String idChef) throws SQLException {
        ArrayList<NotificaChef> notifiche = new ArrayList<>();
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "SELECT * FROM \"NotificaChef\" WHERE \"ID_Chef\" = ? AND \"Letto\" = false ORDER BY \"DataCreazione\" DESC";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, idChef);
        ResultSet rs = st.executeQuery();
        
        while (rs.next()) {
            notifiche.add(new NotificaChef(
                rs.getInt("ID_Notifica"),
                rs.getString("Messaggio"),
                rs.getString("TipoNotifica"),
                rs.getString("ID_Chef"),
                rs.getString("ID_Corso"),
                rs.getTimestamp("DataCreazione").toString(),
                rs.getBoolean("Letto")
            ));
        }
        
        rs.close();
        st.close();
        dataBase.close();
        
        return notifiche;
    }
    
    /**
     * Segna una notifica come letta
     */
    public static boolean segnaNotificaComeLetta(int idNotifica) throws SQLException {
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "UPDATE \"NotificaChef\" SET \"Letto\" = true WHERE \"ID_Notifica\" = ?";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setInt(1, idNotifica);
        
        int rowsAffected = st.executeUpdate();
        st.close();
        dataBase.close();
        
        return rowsAffected > 0;
    }
    
    /**
     * Elimina una notifica
     */
    public static boolean deleteNotifica(int idNotifica) throws SQLException {
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "DELETE FROM \"NotificaChef\" WHERE \"ID_Notifica\" = ?";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setInt(1, idNotifica);
        
        int rowsAffected = st.executeUpdate();
        st.close();
        dataBase.close();
        
        return rowsAffected > 0;
    }
    
    /**
     * Conta il numero di notifiche non lette per un chef
     */
    public static int contaNotificheNonLette(String idChef) throws SQLException {
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "SELECT COUNT(*) FROM \"NotificaChef\" WHERE \"ID_Chef\" = ? AND \"Letto\" = false";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, idChef);
        ResultSet rs = st.executeQuery();
        
        int count = 0;
        if (rs.next()) {
            count = rs.getInt(1);
        }
        
        rs.close();
        st.close();
        dataBase.close();
        
        return count;
    }
    
    /**
     * Crea una notifica per iscrizione studente
     */
    public static boolean creaNotificaIscrizione(String idChef, String idCorso, String nomeStudente, String nomeCorso) throws SQLException {
        String messaggio = "🎓 Nuova iscrizione al corso!\n\n" + 
                          "Lo studente " + nomeStudente + " si è iscritto al tuo corso:\n\"" + nomeCorso + "\"";
        
        NotificaChef notifica = new NotificaChef(messaggio, "NUOVA_ISCRIZIONE", idChef, idCorso);
        return insertNotificaChef(notifica);
    }
}