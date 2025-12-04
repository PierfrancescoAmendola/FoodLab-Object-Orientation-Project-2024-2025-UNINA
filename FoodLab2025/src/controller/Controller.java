package controller;

import java.sql.SQLException;
import java.util.ArrayList;

import boundary.*;
import dao.postgres.AvvisoPostgresDAO;
import dao.postgres.IngredientePostgresDAO;
import dao.postgres.SessionePostgresDAO;
import entity.*;
import dao.postgres.*;

import javax.swing.JOptionPane;


public class Controller 
{
	MainApplication mainApp;
	
    Controller()
    {
       System.out.println("Creazione MainApplication...");
       mainApp = new MainApplication(this);
       System.out.println("MainApplication creata, finestra dovrebbe essere visibile");
    }
	
	public void loginChef()
	{
		mainApp.showChefLogin();
	}
	
	public void registerChef(MainApplication mainApp)
	{
		mainApp.showChefRegistrazione();
	}
	
	public static void main(String[] args)
	{
		// Imposta l'icona dell'applicazione per la barra delle applicazioni
		try {
			// Carica l'icona del logo
			java.net.URL iconURL = Controller.class.getResource("/icons/logofoodlab.png");
			if (iconURL != null) {
				javax.swing.ImageIcon icon = new javax.swing.ImageIcon(iconURL);
				// Imposta l'icona per tutte le finestre JFrame che verranno create
				if (java.awt.Taskbar.isTaskbarSupported()) {
					java.awt.Taskbar taskbar = java.awt.Taskbar.getTaskbar();
					if (taskbar.isSupported(java.awt.Taskbar.Feature.ICON_IMAGE)) {
						taskbar.setIconImage(icon.getImage());
						System.out.println("Icona applicazione impostata nella barra delle applicazioni!");
					}
				}
				System.out.println("Logo del progetto caricato correttamente!");
			} else {
				System.err.println("File logo non trovato: /icons/logofoodlab.png");
			}
		} catch (Exception e) {
			System.err.println("Errore nel caricamento del logo: " + e.getMessage());
		}
		
		///Test della connessione al database prima di avviare l'applicazione
		try {
			System.out.println("Test connessione al database...");
			Class.forName("org.postgresql.Driver");
			System.out.println("Driver JDBC PostgreSQL caricato correttamente!");
			
			java.sql.Connection conn = java.sql.DriverManager.getConnection(
				"jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
			System.out.println("Connessione al database riuscita!");
			conn.close();
		} catch (Exception e) {
			System.err.println("Errore connessione database: " + e.getMessage());
			System.err.println("Avvio dell'applicazione senza connessione database...");
			// Non terminiamo l'applicazione, permettiamo il test dell'interfaccia
		}
		
		System.out.println("Avvio dell'applicazione...");
		new Controller();
		System.out.println("Controller creato, applicazione in esecuzione...");
	}

	public void registraChef(Utente chef) 
	{
		try {
			if ( new ChefPostgresDAO().insertChef(chef) )
			{
				JOptionPane.showMessageDialog(null, "Registrazione avvenuta con successo", "Registrazione avvenuta", JOptionPane.INFORMATION_MESSAGE);
				mainApp.showChefLogin();
			}
			else
				JOptionPane.showMessageDialog(null, "Email o nome utente gi\350 in uso", "Errore registrazione", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			String errorMessage = analyzeConnectionError(e);
			JOptionPane.showMessageDialog(null, errorMessage, "Errore registrazione database", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace(); // Per debugging
		}
		
	}

	public void effettuaLogin(String emailOrUsername, String password) {
		try {
			Utente admin = new ChefPostgresDAO().searchChef(emailOrUsername, password);
			//Controlla che le credenziali siano corrette e manda nell'area riservata agli chef
			if ( admin != null )
			{
				mainApp.getAreaChefPanel().setChef(admin);
				mainApp.showAreaChef();
			}
			else
				JOptionPane.showMessageDialog(null, "Credenziali errate. Puoi effettuare il login con la tua email o nome utente.", "Email/nome utente o password errati", JOptionPane.ERROR_MESSAGE);
				//System.out.print("Accesso non valido");
				
		} catch (SQLException e) {
			String errorMessage = analyzeConnectionError(e);
			JOptionPane.showMessageDialog(null, errorMessage, "Errore connessione database", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace(); // Per debugging
		}
	}
	
	/**
	 * Analizza l'errore di connessione e fornisce un messaggio utile con soluzione
	 */
	private String analyzeConnectionError(SQLException e) {
		String originalMessage = e.getMessage().toLowerCase();
		String errorCode = e.getSQLState();
		
		// Errore utente postgres non esiste
		if (originalMessage.contains("role \"postgres\" does not exist") || 
		    originalMessage.contains("password authentication failed")) {
			return "❌ ERRORE DATABASE - Chef PostgreSQL non configurato\n\n" +
				   "🔧 PROBLEMA: L'utente 'postgres' non esiste nel database.\n\n" +
				   "✅ SOLUZIONI:\n" +
				   "1. Crea l'utente postgres:\n" +
				   "   createuser -s postgres\n\n" +
				   "2. Oppure modifica la password dell'utente postgres:\n" +
				   "   psql -c \"ALTER USER postgres PASSWORD 'a';\"\n\n" +
				   "3. Oppure usa un utente esistente modificando il codice\n\n" +
				   "💡 Verifica che PostgreSQL sia avviato e configurato correttamente.";
		}
		
		// Errore connessione database
		if (originalMessage.contains("connection") && originalMessage.contains("refused")) {
			return "❌ ERRORE CONNESSIONE - Server PostgreSQL non raggiungibile\n\n" +
				   "🔧 PROBLEMA: Il server PostgreSQL non è avviato o non accetta connessioni.\n\n" +
				   "✅ SOLUZIONI:\n" +
				   "1. Avvia PostgreSQL:\n" +
				   "   • macOS: brew services start postgresql\n" +
				   "   • Linux: sudo systemctl start postgresql\n" +
				   "   • Windows: Avvia il servizio PostgreSQL\n\n" +
				   "2. Verifica che sia in ascolto sulla porta 5432:\n" +
				   "   lsof -i :5432\n\n" +
				   "💡 Controlla che il server sia configurato per accettare connessioni locali.";
		}
		
		// Database non esiste
		if (originalMessage.contains("database") && originalMessage.contains("does not exist")) {
			return "❌ ERRORE DATABASE - Database 'FoodLab2025' non trovato\n\n" +
				   "🔧 PROBLEMA: Il database 'FoodLab2025' non esiste.\n\n" +
				   "✅ SOLUZIONI:\n" +
				   "1. Crea il database:\n" +
				   "   createdb FoodLab2025\n\n" +
				   "2. Oppure connettiti a psql e crea il database:\n" +
				   "   psql -c \"CREATE DATABASE FoodLab2025;\"\n\n" +
				   "💡 Assicurati di aver eseguito tutti gli script di setup del database.";
		}
		
		// Errore di autenticazione
		if (originalMessage.contains("authentication") || originalMessage.contains("password")) {
			return "❌ ERRORE AUTENTICAZIONE - Password database errata\n\n" +
				   "🔧 PROBLEMA: La password per l'utente 'postgres' è incorretta.\n\n" +
				   "✅ SOLUZIONI:\n" +
				   "1. Reimposta la password dell'utente postgres:\n" +
				   "   psql -c \"ALTER USER postgres PASSWORD 'a';\"\n\n" +
				   "2. Oppure modifica la password nel codice se hai configurato\n" +
				   "   una password diversa da 'a'\n\n" +
				   "💡 L'applicazione è configurata per usare la password 'a' per l'utente postgres.";
		}
		
		// Driver non trovato
		if (originalMessage.contains("driver") || originalMessage.contains("postgresql")) {
			return "❌ ERRORE DRIVER - Driver PostgreSQL mancante\n\n" +
				   "🔧 PROBLEMA: Il driver JDBC PostgreSQL non è disponibile.\n\n" +
				   "✅ SOLUZIONI:\n" +
				   "1. Verifica che il file postgresql-42.7.7.jar sia presente\n" +
				   "   nella cartella src/jdbc/\n\n" +
				   "2. Ricompila con il classpath corretto:\n" +
				   "   javac -cp \":src/jdbc/postgresql-42.7.7.jar:bin\" ...\n\n" +
				   "💡 Il driver è necessario per connettersi a PostgreSQL.";
		}
		
		// Errore generico con dettagli tecnici
		return "❌ ERRORE DATABASE - Problema di connessione\n\n" +
			   "🔧 DETTAGLI TECNICI:\n" +
			   "• Messaggio: " + e.getMessage() + "\n" +
			   "• Codice SQL: " + (errorCode != null ? errorCode : "N/A") + "\n\n" +
			   "✅ AZIONI CONSIGLIATE:\n" +
			   "1. Verifica che PostgreSQL sia avviato\n" +
			   "2. Controlla le credenziali (utente: postgres, password: a)\n" +
			   "3. Assicurati che il database 'FoodLab2025' esista\n" +
			   "4. Verifica la configurazione di rete\n\n" +
			   "💡 Controlla i log di PostgreSQL per maggiori dettagli.";
	}
	
	public void creaCorso(Corso corso) throws SQLException 
	{
		try { 
			System.out.println("DEBUG: Tentativo di creazione corso con parametri:");
			System.out.println("  ID Chef: " + corso.getIdChef());
			System.out.println("  Nome Corso: " + corso.getArgomento());
			System.out.println("  Data Inizio: " + corso.getDataInizio());
			System.out.println("  Frequenza: " + corso.getFrequenza());
			
			// Ignoriamo descrizione e numeroMassimoPartecipanti dato che non sono supportati dalla tabella corrente
			// Nota: dataFine non è usata perché non esiste nella tabella
			if ( new CorsoPostgresDAO().insertCorso(corso) )
			{
				System.out.println("DEBUG: Corso creato con successo!");
				// Messaggio di successo rimosso per evitare duplicazione - gestito dal panel
			}
			else {
				System.out.println("ERROR: insertCorso ha ritornato false");
				JOptionPane.showMessageDialog(null, "Errore nella creazione del corso", "Errore creazione corso", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException e) {
			System.out.println("ERROR SQL: " + e.getMessage());
			e.printStackTrace();
			String errorMessage = analyzeConnectionError(e);
			JOptionPane.showMessageDialog(null, errorMessage, "Errore database creazione corso", JOptionPane.ERROR_MESSAGE);
			throw e;
		} catch (Exception e) {
			System.out.println("ERROR Generico: " + e.getMessage());
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "❌ ERRORE IMPREVISTO\n\n" +
				"🔧 DETTAGLI: " + e.getMessage() + "\n\n" +
				"✅ AZIONI:\n" +
				"1. Riprova l'operazione\n" +
				"2. Verifica i dati inseriti\n" +
				"3. Contatta il supporto se persiste", "Errore creazione corso", JOptionPane.ERROR_MESSAGE);
			throw e;
		}
		 
		
	}
	
	public ArrayList<Corso> elencaCorsi(String idChef) throws SQLException 
	{
		return new CorsoPostgresDAO().findCorsi(idChef);
	}
	
	public static void eliminaCorso(Corso corso) throws SQLException
	{
		new CorsoPostgresDAO().eliminaCorso(corso);
	}
	
	// Metodi per gestire le sessioni
	public void creaSessione(String data, String oraInizio, boolean online, String idCorso) throws SQLException {
		if (new SessionePostgresDAO().insertSessione(data, oraInizio, online, idCorso)) {
			// Sessione creata con successo - nessun messaggio qui, gestito dal panel
			return;
		} else {
			throw new SQLException("Errore nella creazione della sessione");
		}
	}
	
	public static ArrayList<Sessione> elencaSessioni(String idCorso) throws SQLException {
		return new SessionePostgresDAO().findSessioniByCorso(idCorso);
	}
	
	public void eliminaSessione(int idSessione) {
		try {
			if (new SessionePostgresDAO().deleteSessione(idSessione)) {
				JOptionPane.showMessageDialog(null, "Sessione eliminata con successo", "Sessione eliminata", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Errore nell'eliminazione della sessione", "Errore eliminazione sessione", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException e) {
			String errorMessage = analyzeConnectionError(e);
			JOptionPane.showMessageDialog(null, errorMessage, "Errore database eliminazione sessione", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	// Metodi per gestire le ricette
	public void creaRicetta(String nome, int idSessione) throws SQLException {
		if (new RicettaPostgresDAO().insertRicetta(nome, idSessione)) {
			// Ricetta creata con successo - nessun messaggio qui, gestito dal panel
			return;
		} else {
			throw new SQLException("Errore nella creazione della ricetta");
		}
	}
	
	public ArrayList<Ricetta> elencaRicette(int idSessione) throws SQLException {
		return new RicettaPostgresDAO().findRicetteBySessione(idSessione);
	}
	
	public void eliminaRicetta(int idRicetta) {
		try {
			if (new RicettaPostgresDAO().deleteRicetta(idRicetta)) {
				JOptionPane.showMessageDialog(null, "Ricetta eliminata con successo", "Ricetta eliminata", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Errore nell'eliminazione della ricetta", "Errore eliminazione ricetta", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException e) {
			String errorMessage = analyzeConnectionError(e);
			JOptionPane.showMessageDialog(null, errorMessage, "Errore database eliminazione ricetta", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	// Metodi per gestire le iscrizioni
	public void iscriviChef(String idChef, String idCorso) {
		try {
			if (new IscrizionePostgresDAO().insertIscrizione(idChef, idCorso)) {
				JOptionPane.showMessageDialog(null, "Iscrizione effettuata con successo", "Iscrizione effettuata", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Errore nell'iscrizione", "Errore iscrizione", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException e) {
			String errorMessage = analyzeConnectionError(e);
			JOptionPane.showMessageDialog(null, errorMessage, "Errore database iscrizione", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	public ArrayList<Iscrizione> elencaIscrizioniCorso(String idCorso) throws SQLException {
		return new IscrizionePostgresDAO().findIscrizioniByCorso(idCorso);
	}
	
	public ArrayList<Iscrizione> elencaIscrizioniChef(String idChef) throws SQLException {
		return new IscrizionePostgresDAO().findIscrizioniByChef(idChef);
	}
	
	public void disiscriviChef(String idChef, String idCorso) {
		try {
			if (new IscrizionePostgresDAO().deleteIscrizione(idChef, idCorso)) {
				JOptionPane.showMessageDialog(null, "Disiscrizione effettuata con successo", "Disiscrizione effettuata", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Errore nella disiscrizione", "Errore disiscrizione", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException e) {
			String errorMessage = analyzeConnectionError(e);
			JOptionPane.showMessageDialog(null, errorMessage, "Errore database disiscrizione", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	// Metodi per ottenere statistiche dei corsi
	public int getNumeroSessioniCorso(String idCorso) throws SQLException {
		ArrayList<Sessione> sessioni = new SessionePostgresDAO().findSessioniByCorso(idCorso);
		return sessioni.size();
	}
	
	public static int getNumeroIscrittiCorso(String idCorso) throws SQLException {
		ArrayList<Iscrizione> iscrizioni = new IscrizionePostgresDAO().findIscrizioniByCorso(idCorso);
		return iscrizioni.size();
	}
	
	// Metodo per ottenere i dettagli degli iscritti a un corso
	public ArrayList<String> getDettagliIscrittiCorso(String idCorso) throws SQLException {
		return new IscrizionePostgresDAO().findDettagliIscrittiByCorso(idCorso);
	}
	
	// Metodo per il logout
	public void logout() {
		mainApp.showPaginaIniziale();
	}
	
	// Metodo per mostrare il login chef
	public void showChefLogin() {
		mainApp.showChefLogin();
	}
	
	// Metodo per mostrare la gestione corsi
	public void showGestioneCorsi() {
		// Passa i dati dello chef alla pagina di gestione corsi
		Utente currentChef = mainApp.getAreaChefPanel().getAdmin();
		if (currentChef != null) {
			mainApp.getGestioneCorsiPanel().setChef(currentChef);
		}
		mainApp.showGestioneCorsi();
	}
	
	// Metodo per mostrare la creazione corsi
	public void showCreazioneCorsi() {
		// Passa i dati dello chef alla pagina di creazione corsi
		Utente currentChef = mainApp.getAreaChefPanel().getAdmin();
		if (currentChef != null) {
			mainApp.getCreazioneCorsiPanel().setChef(currentChef);
		}
		mainApp.showCreazioneCorsi();
	}
	
	// Metodo per mostrare le notifiche chef
	public void showNotificheChef() {
		// Passa i dati dello chef alla pagina notifiche chef
		Utente currentChef = mainApp.getAreaChefPanel().getAdmin();
		if (currentChef != null) {
			mainApp.showNotificheChef(currentChef);
		}
	}
	
	// Metodo per mostrare l'area chef
	public void showAreaChef() {
		mainApp.showAreaChef();
	}
	
	// Metodo per mostrare il report mensile
	public void showReportMensile(String codiceFiscaleChef) {
		mainApp.showReportMensile(codiceFiscaleChef);
	}
	
	/**
	 * Ottiene una connessione al database
	 * @return Connection al database PostgreSQL
	 * @throws SQLException Se la connessione fallisce
	 */
	public java.sql.Connection getConnection() throws SQLException {
		return java.sql.DriverManager.getConnection(
			"jdbc:postgresql://localhost:5432/FoodLab2025", "postgres", "a");
	}

	public ArrayList<Corso> searchFiltrered(String codiceFiscale, String argomento) throws SQLException {
		return new CorsoPostgresDAO().searchFiltrered(codiceFiscale, argomento);
	}

	public ArrayList<Corso> getCorsiIscritto(String codiceFiscale) throws SQLException {
		// TODO Auto-generated method stub
		return new CorsoPostgresDAO().getCorsiIscritto(codiceFiscale);
	}

	public ArrayList<Corso> getAllCorsi() throws SQLException {
		// TODO Auto-generated method stub
		return new CorsoPostgresDAO().getAllCorsi();
	}

	public boolean insertIscrizione(String codiceFiscale, String idCorso) throws SQLException {
		// TODO Auto-generated method stub
		return insertIscrizione(codiceFiscale, idCorso);
	}

	public ArrayList<Ricetta> getRicetteSessione(int idSessione) throws SQLException{
		// TODO Auto-generated method stub
		return new RicettaPostgresDAO().findRicetteBySessione(idSessione);
	}

	public ArrayList<Sessione> elencaSessioniPratiche(String idCorso) throws SQLException {
		// TODO Auto-generated method stub
		return new SessionePostgresDAO().elencaSessioniPratiche(idCorso);
	}

	public void insertIngrediente(Ingrediente ingrediente) throws SQLException {
		new IngredientePostgresDAO().insertIngrediente(ingrediente);
	}

	public ArrayList<Avviso> getAvvisiByUtente(String codiceFiscale) throws SQLException {
		return new AvvisoPostgresDAO().getAvvisiByUtente(codiceFiscale);
	}

	public void segnaAvvisoComeLetto(int idAvviso) throws SQLException {
		new AvvisoPostgresDAO().segnaAvvisoComeLetto(idAvviso);
	}

	public ArrayList<Sessione> findSessioniByCorso(String idCorso) throws SQLException {
		// TODO Auto-generated method stub
		return new SessionePostgresDAO().findSessioniByCorso(idCorso);
	}

	public int contaIscrittiCorso(String idCorso) throws SQLException {
		// TODO Auto-generated method stub
		return new CorsoPostgresDAO().contaIscrittiCorso(idCorso);
	}

	public boolean isUtenteIscritto(String codiceFiscale, String idCorso) throws SQLException {
		// TODO Auto-generated method stub
		return new CorsoPostgresDAO().isUtenteIscritto(codiceFiscale, idCorso);
	}

	public boolean esisteNotificaAttiva(String codiceFiscale, String idCorso) throws SQLException {
		// TODO Auto-generated method stub
		return new NotificaCorsoPostgresDAO().esisteNotificaAttiva(codiceFiscale, idCorso);
	}

	public boolean insertNotificaCorso(NotificaCorso notifica) throws SQLException {
		// TODO Auto-generated method stub
		return new NotificaCorsoPostgresDAO().insertNotificaCorso(notifica);
	}

	public ArrayList<NotificaChef> getNotificheByChef(String codiceFiscale) throws SQLException {
		// TODO Autco-generated method stub
		return new NotificaChefPostgresDAO().getNotificheByChef(codiceFiscale);
	}

	public boolean segnaNotificaComeLetta(int idNotifica) throws SQLException {
		// TODO Auto-generated method stub
		return new NotificaChefPostgresDAO().segnaNotificaComeLetta(idNotifica);
	}

	public ArrayList<String> getMesiDisponibili(String codiceFiscaleChef) throws SQLException {
		// TODO Auto-generated method stub
		return new ReportPostgresDAO().getMesiDisponibili(codiceFiscaleChef);
	}

	public ReportMensile generaReportMensile(String codiceFiscaleChef, int mese, int anno) throws SQLException {
		// TODO Auto-generated method stub
		return new ReportPostgresDAO().generaReportMensile(codiceFiscaleChef, mese, anno);
	}

	public Utente searchStudente(String emailOrUsername, String password) throws SQLException {
		// TODO Auto-generated method stub
		return new StudentePostgresDAO().searchStudente(emailOrUsername, password);
	}

	public boolean insertStudente(Utente nuovoStudente) throws SQLException {
		// TODO Auto-generated method stub
		return new StudentePostgresDAO().insertStudente(nuovoStudente);
	}

	public ArrayList<Iscrizione> getIscrizioniByCorso(String courseId) throws SQLException {
		// TODO Auto-generated method stub
		return new IscrizionePostgresDAO().getIscrizioniByCorso(courseId);
	}

	public boolean insertAvviso(Avviso avviso) throws SQLException {
		// TODO Auto-generated method stub
		return new AvvisoPostgresDAO().insertAvviso(avviso);
	}

	public ArrayList<Avviso> getAvvisiByCorso(String idCorso) throws SQLException {
		// TODO Auto-generated method stub
		return new AvvisoPostgresDAO().getAvvisiByCorso(idCorso);
	}
}
