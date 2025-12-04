package dao.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.IscrizioneDAO;
import entity.Iscrizione;

public class IscrizionePostgresDAO implements IscrizioneDAO {

    @Override
	public boolean insertIscrizione(String idUtente, String idCorso) throws SQLException {
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");

        String query = "INSERT INTO \"Iscrizione\" (\"ID_Utente\", \"ID_Corso\") VALUES (?, ?)";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, idUtente);
        st.setString(2, idCorso);

        int rowsAffected = st.executeUpdate();
        
        // Se l'iscrizione è avvenuta con successo, invia notifica al chef
        if (rowsAffected > 0) {
            try {
                // Ottieni informazioni del corso e dello chef
                entity.Corso corso = new CorsoPostgresDAO().findCorsoById(idCorso);
                if (corso != null) {
                    // Ottieni informazioni dello studente
                    entity.Utente studente = new StudentePostgresDAO().findStudenteByCodiceFiscale(idUtente);
                    String nomeUtente = studente != null ? studente.getNome() + " " + studente.getCognome() : "Utente";
                    
                    // Crea notifica per il chef
                    new NotificaChefPostgresDAO().creaNotificaIscrizione(
                        corso.getIdChef(), 
                        idCorso, 
                        nomeUtente, 
                        corso.getArgomento()
                    );
                }
            } catch (Exception e) {
                // Log dell'errore ma non bloccare l'iscrizione
                System.err.println("Errore nell'invio della notifica al chef: " + e.getMessage());
            }
        }
        
        st.close();
        dataBase.close();

        return rowsAffected > 0;
    }

    @Override
	public ArrayList<Iscrizione> findIscrizioniByCorso(String idCorso) throws SQLException {
        ArrayList<Iscrizione> elencoIscrizioni = new ArrayList<>();
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");

        String query = "SELECT * FROM \"Iscrizione\" WHERE \"ID_Corso\" = ?";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, idCorso);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            elencoIscrizioni.add(new Iscrizione(
                rs.getString("ID_Utente"),
                rs.getString("ID_Corso")
            ));
        }

        rs.close();
        st.close();
        dataBase.close();

        return elencoIscrizioni;
    }

    @Override
	public ArrayList<Iscrizione> findIscrizioniByChef(String idUtente) throws SQLException {
        ArrayList<Iscrizione> elencoIscrizioni = new ArrayList<>();
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");

        String query = "SELECT * FROM \"Iscrizione\" WHERE \"ID_Utente\" = ?";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, idUtente);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            elencoIscrizioni.add(new Iscrizione(
                rs.getString("ID_Utente"),
                rs.getString("ID_Corso")
            ));
        }

        rs.close();
        st.close();
        dataBase.close();

        return elencoIscrizioni;
    }

    @Override
	public boolean deleteIscrizione(String idUtente, String idCorso) throws SQLException {
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");

        String query = "DELETE FROM \"Iscrizione\" WHERE \"ID_Utente\" = ? AND \"ID_Corso\" = ?";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, idUtente);
        st.setString(2, idCorso);

        int rowsAffected = st.executeUpdate();
        st.close();
        dataBase.close();

        return rowsAffected > 0;
    }
    
    @Override
	public ArrayList<String> findDettagliIscrittiByCorso(String idCorso) throws SQLException {
        ArrayList<String> dettagliIscritti = new ArrayList<>();
        Connection dataBase = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");

        String query = "SELECT u.\"Nome\", u.\"Cognome\" FROM \"Iscrizioni\" i " +
                      "JOIN \"Utenti\" u ON i.\"ID_Utente\" = u.\"CodiceFiscale\" " +
                      "WHERE i.\"ID_Corso\" = ?";
        PreparedStatement st = dataBase.prepareStatement(query);
        st.setString(1, idCorso);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            String nomeCompleto = rs.getString("Nome") + " " + rs.getString("Cognome");
            dettagliIscritti.add(nomeCompleto);
        }

        rs.close();
        st.close();
        dataBase.close();

        return dettagliIscritti;
    }
    
    /**
     * Alias per findIscrizioniByCorso per compatibilità
     */
    @Override
	public ArrayList<Iscrizione> getIscrizioniByCorso(String idCorso) throws SQLException {
        return findIscrizioniByCorso(idCorso);
    }
}