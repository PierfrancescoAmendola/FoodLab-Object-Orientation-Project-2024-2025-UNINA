package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entity.Ricetta;

public class RicettaDAO {

    public static boolean insertRicetta(String nome, int idSessione) throws SQLException {
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");

        String query = "INSERT INTO \"Ricetta\" (\"Nome\", \"ID_Sessione\") VALUES (?, ?)";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, nome);
        st.setInt(2, idSessione);

        int rowsAffected = st.executeUpdate();
        st.close();
        dataBase.close();

        return rowsAffected > 0;
    }

    public static ArrayList<Ricetta> findRicetteBySessione(int idSessione) throws SQLException {
        ArrayList<Ricetta> elencoRicette = new ArrayList<>();
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");

        String query = "SELECT * FROM \"Ricetta\" WHERE \"ID_Sessione\" = ?";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setInt(1, idSessione);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            elencoRicette.add(new Ricetta(
                rs.getInt("ID_Ricetta"),
                rs.getString("Nome"),
                rs.getInt("ID_Sessione")
            ));
        }

        rs.close();
        st.close();
        dataBase.close();

        return elencoRicette;
    }

    public static boolean deleteRicetta(int idRicetta) throws SQLException {
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");

        String query = "DELETE FROM \"Ricetta\" WHERE \"ID_Ricetta\" = ?";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setInt(1, idRicetta);

        int rowsAffected = st.executeUpdate();
        st.close();
        dataBase.close();

        return rowsAffected > 0;
    }
}