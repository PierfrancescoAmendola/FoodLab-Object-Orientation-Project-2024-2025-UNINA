package dao.postgres;

import java.sql.*;
import java.util.ArrayList;

import dao.AvvisoDAO;
import entity.Avviso;

public class AvvisoPostgresDAO implements AvvisoDAO {
    
    /**
     * Inserisce un nuovo avviso nel database
     */
	@Override
    public boolean insertAvviso(Avviso avviso) throws SQLException {
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "INSERT INTO \"AvvisoStudente\" (\"Messaggio\", \"TipoAvviso\", \"ID_Utente\", \"ID_Corso\", \"DataCreazione\", \"Letto\") VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, ?)";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, avviso.getMessaggio());
        st.setString(2, avviso.getTipoAvviso());
        st.setString(3, avviso.getIdUtente());
        st.setString(4, avviso.getIdCorso());
        st.setBoolean(5, avviso.isLetto());
        
        int rowsAffected = st.executeUpdate();
        st.close();
        dataBase.close();
        
        return rowsAffected > 0;
    }
    
    /**
     * Trova tutti gli avvisi per un utente specifico
     */
	@Override
    public ArrayList<Avviso> getAvvisiByUtente(String idUtente) throws SQLException {
        ArrayList<Avviso> avvisi = new ArrayList<>();
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "SELECT * FROM \"AvvisoStudente\" WHERE \"ID_Utente\" = ? ORDER BY \"DataCreazione\" DESC";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, idUtente);
        ResultSet rs = st.executeQuery();
        
        while (rs.next()) {
            avvisi.add(new Avviso(
                rs.getInt("ID_Avviso"),
                rs.getString("Messaggio"),
                rs.getString("TipoAvviso"),
                rs.getString("ID_Utente"),
                rs.getString("ID_Corso"),
                rs.getTimestamp("DataCreazione").toString(),
                rs.getBoolean("Letto")
            ));
        }
        
        rs.close();
        st.close();
        dataBase.close();
        
        return avvisi;
    }
    
    /**
     * Trova gli avvisi non letti per un utente
     */
	@Override
    public ArrayList<Avviso> getAvvisiNonLettiByUtente(String idUtente) throws SQLException {
        ArrayList<Avviso> avvisi = new ArrayList<>();
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "SELECT * FROM \"AvvisoStudente\" WHERE \"ID_Utente\" = ? AND \"Letto\" = false ORDER BY \"DataCreazione\" DESC";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, idUtente);
        ResultSet rs = st.executeQuery();
        
        while (rs.next()) {
            avvisi.add(new Avviso(
                rs.getInt("ID_Avviso"),
                rs.getString("Messaggio"),
                rs.getString("TipoAvviso"),
                rs.getString("ID_Utente"),
                rs.getString("ID_Corso"),
                rs.getTimestamp("DataCreazione").toString(),
                rs.getBoolean("Letto")
            ));
        }
        
        rs.close();
        st.close();
        dataBase.close();
        
        return avvisi;
    }
    
    /**
     * Segna un avviso come letto
     */
	@Override
    public boolean segnaAvvisoComeLetto(int idAvviso) throws SQLException {
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "UPDATE \"AvvisoStudente\" SET \"Letto\" = true WHERE \"ID_Avviso\" = ?";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setInt(1, idAvviso);
        
        int rowsAffected = st.executeUpdate();
        st.close();
        dataBase.close();
        
        return rowsAffected > 0;
    }
    
    /**
     * Elimina un avviso
     */
	@Override
    public boolean deleteAvviso(int idAvviso) throws SQLException {
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "DELETE FROM \"AvvisoStudente\" WHERE \"ID_Avviso\" = ?";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setInt(1, idAvviso);
        
        int rowsAffected = st.executeUpdate();
        st.close();
        dataBase.close();
        
        return rowsAffected > 0;
    }
    
    /**
     * Conta il numero di avvisi non letti per un utente
     */
	@Override
    public int contaAvvisiNonLetti(String idUtente) throws SQLException {
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "SELECT COUNT(*) FROM \"AvvisoStudente\" WHERE \"ID_Utente\" = ? AND \"Letto\" = false";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, idUtente);
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
     * Trova tutti gli avvisi per un corso specifico
     */
	@Override
    public ArrayList<Avviso> getAvvisiByCorso(String idCorso) throws SQLException {
        ArrayList<Avviso> avvisi = new ArrayList<>();
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
        
        String query = "SELECT * FROM \"AvvisoStudente\" WHERE \"ID_Corso\" = ? ORDER BY \"DataCreazione\" DESC";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, idCorso);
        ResultSet rs = st.executeQuery();
        
        while (rs.next()) {
            avvisi.add(new Avviso(
                rs.getInt("ID_Avviso"),
                rs.getString("Messaggio"),
                rs.getString("TipoAvviso"),
                rs.getString("ID_Utente"),
                rs.getString("ID_Corso"),
                rs.getTimestamp("DataCreazione").toString(),
                rs.getBoolean("Letto")
            ));
        }
        
        rs.close();
        st.close();
        dataBase.close();
        
        return avvisi;
    }
}