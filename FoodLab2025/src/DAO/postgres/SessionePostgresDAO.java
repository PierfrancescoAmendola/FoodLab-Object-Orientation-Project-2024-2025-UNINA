package dao.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.SessioneDAO;
import entity.Sessione;

public class SessionePostgresDAO implements SessioneDAO {

    @Override
	public boolean insertSessione(String data, String oraInizio, boolean online, String idCorso) throws SQLException {
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");

        String query = "INSERT INTO \"Sessione\" (\"Data\", \"OraInizio\", \"Online\", \"ID_Corso\") VALUES (?, ?, ?, ?)";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setDate(1, java.sql.Date.valueOf(data));
        st.setTime(2, java.sql.Time.valueOf(oraInizio+":00"));
        st.setBoolean(3, online);
        st.setString(4, idCorso);

        int rowsAffected = st.executeUpdate();
        st.close();
        dataBase.close();

        return rowsAffected > 0;
    }

    @Override
	public ArrayList<Sessione> findSessioniByCorso(String idCorso) throws SQLException {
        ArrayList<Sessione> elencoSessioni = new ArrayList<>();
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");

        String query = "SELECT * FROM \"Sessione\" WHERE \"ID_Corso\" = ? ORDER BY \"Data\", \"OraInizio\"";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, idCorso);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            elencoSessioni.add(new Sessione(
                rs.getInt("ID_Sessione"),
                rs.getDate("Data").toString(),
                rs.getTime("OraInizio").toString(),
                rs.getBoolean("Online"),
                rs.getString("ID_Corso")
            ));
        }

        rs.close();
        st.close();
        dataBase.close();

        return elencoSessioni;
    }

    @Override
	public boolean deleteSessione(int idSessione) throws SQLException {
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");

        String query = "DELETE FROM \"Sessione\" WHERE \"ID_Sessione\" = ?";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setInt(1, idSessione);
        
        int rowsAffected = st.executeUpdate();
        st.close();
        dataBase.close();

        return rowsAffected > 0;
    }

	@Override
	public ArrayList<Sessione> elencaSessioniPratiche(String idCorso) throws SQLException {
		ArrayList<Sessione> elencoSessioni = new ArrayList<>();
		Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");

        String query = "SELECT * FROM \"Sessione\" WHERE \"Online\" = ? AND \"ID_Corso\" = ?";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setBoolean(1, false);
        st.setString(2, idCorso);
        ResultSet rs = st.executeQuery();
        
        //Creazione elenco
        while (rs.next()) {
            elencoSessioni.add(new Sessione(
                rs.getInt("ID_Sessione"),
                rs.getDate("Data").toString(),
                rs.getTime("OraInizio").toString(),
                rs.getBoolean("Online"),
                rs.getString("ID_Corso")
            ));
        }
        
        //Chiusura e restituzione elenco
        st.close();
        dataBase.close();
        return elencoSessioni;
	}
}