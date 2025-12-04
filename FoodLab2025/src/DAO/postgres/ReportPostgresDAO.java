package dao.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.ReportDAO;
import entity.ReportMensile;

public class ReportPostgresDAO implements ReportDAO {
    private Connection connection;
    
    public ReportPostgresDAO() throws SQLException {
    	this(DriverManager.getConnection("jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a"));
    }
    public ReportPostgresDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Genera il report mensile per uno chef specifico
     * @param codiceFiscaleChef Il codice fiscale dello chef
     * @param mese Il mese (1-12)
     * @param anno L'anno
     * @return Il report mensile
     * @throws SQLException Se ci sono errori nel database
     */
    @Override
	public ReportMensile generaReportMensile(String codiceFiscaleChef, int mese, int anno) throws SQLException {
        // Query per ottenere i dati del report
        String query = """
            WITH chef_data AS (
                SELECT u."Nome" || ' ' || u."Cognome" as nome_chef
                FROM "Utente" u 
                WHERE u."CodiceFiscale" = ?
            ),
            corsi_mese AS (
                SELECT c."ID_Corso"
                FROM "Corso" c
                WHERE c."ID_Chef" = ? 
                AND EXTRACT(YEAR FROM c."DataInizio") = ? 
                AND EXTRACT(MONTH FROM c."DataInizio") = ?
            ),
            sessioni_data AS (
                SELECT 
                    s."Online",
                    s."ID_Sessione",
                    COALESCE(COUNT(r."ID_Ricetta"), 0) as num_ricette
                FROM "Sessione" s
                LEFT JOIN "Ricetta" r ON s."ID_Sessione" = r."ID_Sessione"
                WHERE s."ID_Corso" IN (SELECT "ID_Corso" FROM corsi_mese)
                AND EXTRACT(YEAR FROM s."Data") = ?
                AND EXTRACT(MONTH FROM s."Data") = ?
                GROUP BY s."ID_Sessione", s."Online"
            )
            SELECT 
                cd.nome_chef,
                COUNT(DISTINCT cm."ID_Corso") as corsi_totali,
                COUNT(CASE WHEN sd."Online" = true THEN 1 END) as sessioni_online,
                COUNT(CASE WHEN sd."Online" = false THEN 1 END) as sessioni_pratiche,
                COALESCE(AVG(CASE WHEN sd."Online" = false THEN sd.num_ricette END), 0) as media_ricette_pratiche,
                COALESCE(MAX(CASE WHEN sd."Online" = false THEN sd.num_ricette END), 0) as max_ricette_pratiche,
                COALESCE(MIN(CASE WHEN sd."Online" = false THEN sd.num_ricette END), 0) as min_ricette_pratiche
            FROM chef_data cd
            LEFT JOIN corsi_mese cm ON true
            LEFT JOIN sessioni_data sd ON true
            GROUP BY cd.nome_chef
            """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, codiceFiscaleChef);
            stmt.setString(2, codiceFiscaleChef);
            stmt.setInt(3, anno);
            stmt.setInt(4, mese);
            stmt.setInt(5, anno);
            stmt.setInt(6, mese);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nomeChef = rs.getString("nome_chef");
                    int corsiTotali = rs.getInt("corsi_totali");
                    int sessioniOnline = rs.getInt("sessioni_online");
                    int sessioniPratiche = rs.getInt("sessioni_pratiche");
                    double mediaRicettePratiche = rs.getDouble("media_ricette_pratiche");
                    int maxRicettePratiche = rs.getInt("max_ricette_pratiche");
                    int minRicettePratiche = rs.getInt("min_ricette_pratiche");

                    return new ReportMensile(mese, anno, corsiTotali, sessioniOnline, 
                                           sessioniPratiche, mediaRicettePratiche, 
                                           maxRicettePratiche, minRicettePratiche, nomeChef);
                }
            }
        }

        // Se non ci sono dati, restituisci un report vuoto
        return new ReportMensile(mese, anno, 0, 0, 0, 0.0, 0, 0, "Chef");
    }

    /**
     * Ottiene la lista dei mesi disponibili per i report di uno chef
     * @param codiceFiscaleChef Il codice fiscale dello chef
     * @return Lista di stringhe nel formato "MM/YYYY"
     * @throws SQLException Se ci sono errori nel database
     */
    @Override
	public ArrayList<String> getMesiDisponibili(String codiceFiscaleChef) throws SQLException {
        String query = """
            SELECT DISTINCT 
                EXTRACT(MONTH FROM s."Data") as mese,
                EXTRACT(YEAR FROM s."Data") as anno
            FROM "Sessione" s
            JOIN "Corso" c ON s."ID_Corso" = c."ID_Corso"
            WHERE c."ID_Chef" = ?
            ORDER BY anno DESC, mese DESC
            """;

        ArrayList<String> mesi = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, codiceFiscaleChef);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int mese = rs.getInt("mese");
                    int anno = rs.getInt("anno");
                    mesi.add(String.format("%02d/%d", mese, anno));
                }
            }
        }
        
        return mesi;
    }
}