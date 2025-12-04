package boundary;

import entity.*;
import controller.*;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class StudenteRegistrazionePanel extends JPanel {
	private Controller programma;
	private MainApplication mainApp;
	private JTextField nomeField;
	private JTextField cognomeField;
	private JTextField emailField;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JPasswordField confirmPasswordField;

	public StudenteRegistrazionePanel(Controller p, MainApplication mainApp) {
		this.programma = p;
		this.mainApp = mainApp;
		
		initializePanel();
	}
	
	private void initializePanel() {
		setLayout(new BorderLayout());
		setBackground(KitchenTheme.SOFT_GRAY);
		
		// Main container with kitchen theme
		JPanel mainContainer = KitchenTheme.createKitchenPanel();
		mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
		mainContainer.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
		
		// Header section
		JPanel headerPanel = createHeaderSection();
		mainContainer.add(headerPanel);
		mainContainer.add(Box.createVerticalStrut(30));
		
		// Registration form section
		JPanel formPanel = createRegistrationFormSection();
		mainContainer.add(formPanel);
		
		add(mainContainer, BorderLayout.CENTER);
	}
	
	private JPanel createHeaderSection() {
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
		headerPanel.setOpaque(false);
		headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// Main title
		JLabel titleLabel = new JLabel("Registrazione Studente");
		titleLabel.setFont(KitchenTheme.TITLE_FONT);
		titleLabel.setForeground(KitchenTheme.WARM_ORANGE);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		headerPanel.add(titleLabel);
		
		// Subtitle
		JLabel subtitleLabel = new JLabel("Unisciti alla nostra community culinaria");
		subtitleLabel.setFont(KitchenTheme.SUBTITLE_FONT);
		subtitleLabel.setForeground(KitchenTheme.WARM_BROWN);
		subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		headerPanel.add(subtitleLabel);
		
		return headerPanel;
	}
	
	private JPanel createRegistrationFormSection() {
		JPanel formPanel = KitchenTheme.createKitchenCard();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		formPanel.setMaximumSize(new Dimension(450, 650));
		formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// Form title
		JLabel formTitle = new JLabel("Crea il tuo account");
		formTitle.setFont(new Font("Georgia", Font.BOLD, 20));
		formTitle.setForeground(KitchenTheme.DARK_GRAY);
		formTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.add(formTitle);
		
		formPanel.add(Box.createVerticalStrut(25));
		
		// Nome field
		JLabel nomeLabel = new JLabel("Nome");
		nomeLabel.setFont(KitchenTheme.LABEL_FONT);
		nomeLabel.setForeground(KitchenTheme.DARK_GRAY);
		nomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.add(nomeLabel);
		
		formPanel.add(Box.createVerticalStrut(5));
		
		nomeField = KitchenTheme.createKitchenTextField("Inserisci il tuo nome");
		nomeField.setAlignmentX(Component.CENTER_ALIGNMENT);
		nomeField.addActionListener(e -> cognomeField.requestFocus());
		formPanel.add(nomeField);
		
		formPanel.add(Box.createVerticalStrut(15));
		
		// Cognome field
		JLabel cognomeLabel = new JLabel("Cognome");
		cognomeLabel.setFont(KitchenTheme.LABEL_FONT);
		cognomeLabel.setForeground(KitchenTheme.DARK_GRAY);
		cognomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.add(cognomeLabel);
		
		formPanel.add(Box.createVerticalStrut(5));
		
		cognomeField = KitchenTheme.createKitchenTextField("Inserisci il tuo cognome");
		cognomeField.setAlignmentX(Component.CENTER_ALIGNMENT);
		cognomeField.addActionListener(e -> emailField.requestFocus());
		formPanel.add(cognomeField);
		
		formPanel.add(Box.createVerticalStrut(15));
		
		// Email field
		JLabel emailLabel = new JLabel("Email");
		emailLabel.setFont(KitchenTheme.LABEL_FONT);
		emailLabel.setForeground(KitchenTheme.DARK_GRAY);
		emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.add(emailLabel);
		
		formPanel.add(Box.createVerticalStrut(5));
		
		emailField = KitchenTheme.createKitchenTextField("Inserisci la tua email");
		emailField.setAlignmentX(Component.CENTER_ALIGNMENT);
		emailField.addActionListener(e -> usernameField.requestFocus());
		formPanel.add(emailField);
		
		formPanel.add(Box.createVerticalStrut(15));
		
		// Username field
		JLabel usernameLabel = new JLabel("Nome Utente");
		usernameLabel.setFont(KitchenTheme.LABEL_FONT);
		usernameLabel.setForeground(KitchenTheme.DARK_GRAY);
		usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.add(usernameLabel);
		
		formPanel.add(Box.createVerticalStrut(5));
		
		usernameField = KitchenTheme.createKitchenTextField("Inserisci nome utente");
		usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
		usernameField.addActionListener(e -> passwordField.requestFocus());
		formPanel.add(usernameField);
		
		formPanel.add(Box.createVerticalStrut(15));
		
		// Password field
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setFont(KitchenTheme.LABEL_FONT);
		passwordLabel.setForeground(KitchenTheme.DARK_GRAY);
		passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.add(passwordLabel);
		
		formPanel.add(Box.createVerticalStrut(5));
		
		passwordField = KitchenTheme.createKitchenPasswordField("Inserisci password");
		passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
		passwordField.addActionListener(e -> confirmPasswordField.requestFocus());
		formPanel.add(passwordField);
		
		formPanel.add(Box.createVerticalStrut(15));
		
		// Confirm Password field
		JLabel confirmPasswordLabel = new JLabel("Conferma Password");
		confirmPasswordLabel.setFont(KitchenTheme.LABEL_FONT);
		confirmPasswordLabel.setForeground(KitchenTheme.DARK_GRAY);
		confirmPasswordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.add(confirmPasswordLabel);
		
		formPanel.add(Box.createVerticalStrut(5));
		
		confirmPasswordField = KitchenTheme.createKitchenPasswordField("Conferma password");
		confirmPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);
		confirmPasswordField.addActionListener(e -> performRegistration());
		formPanel.add(confirmPasswordField);
		
		formPanel.add(Box.createVerticalStrut(25));
		
		// Buttons section
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setOpaque(false);
		buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// Register button
		JButton registerButton = KitchenTheme.createKitchenButton("Registrati");
		registerButton.addActionListener(e -> performRegistration());
		registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.add(registerButton);
		
		buttonPanel.add(Box.createVerticalStrut(15));
		
		// Login button
		JButton loginButton = KitchenTheme.createSecondaryButton("Hai già un account? Accedi");
		loginButton.addActionListener(e -> {
			mainApp.showStudenteLogin();
		});
		loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.add(loginButton);
		
		buttonPanel.add(Box.createVerticalStrut(15));
		
		// Back button
		JButton backButton = KitchenTheme.createSecondaryButton("Torna Indietro");
		backButton.addActionListener(e -> {
			mainApp.showPaginaIniziale();
		});
		backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.add(backButton);
		
		formPanel.add(buttonPanel);
		
		return formPanel;
	}
	
	private void performRegistration() {
		String nome = nomeField.getText().trim(); 
		String cognome = cognomeField.getText().trim();
		String email = emailField.getText().trim();
		String username = usernameField.getText().trim();
		String password = new String(passwordField.getPassword());
		String confirmPassword = new String(confirmPasswordField.getPassword());
		
		// Check if fields contain placeholder text
		if (nome.equals("Inserisci il tuo nome")) nome = "";
		if (cognome.equals("Inserisci il tuo cognome")) cognome = "";
		if (email.equals("Inserisci la tua email")) email = "";
		if (username.equals("Inserisci nome utente")) username = "";
		if (password.equals("Inserisci password")) password = "";
		if (confirmPassword.equals("Conferma password")) confirmPassword = "";
		
		// Validation
		if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(mainApp, 
				"Attenzione!\nTutti i campi sono obbligatori", 
				"Campi mancanti", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (password.length() < 5) {
			JOptionPane.showMessageDialog(mainApp, 
				"Password troppo corta!\nLa password deve contenere almeno 5 caratteri", 
				"Password non valida", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (!password.equals(confirmPassword)) {
			JOptionPane.showMessageDialog(mainApp, 
				"Le password non coincidono!\nRiprova", 
				"Password diverse", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (!email.contains("@") || !email.contains(".")) {
			JOptionPane.showMessageDialog(mainApp, 
				"Email non valida!\nInserisci un indirizzo email corretto", 
				"Email non valida", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		try {
			// Check if username or email already exists
			Utente existingStudente = programma.searchStudente(username, password);
			if (existingStudente != null) {
				JOptionPane.showMessageDialog(mainApp, 
					"Nome utente già esistente!\nScegli un altro nome utente", 
					"Utente già registrato", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// Create new student - generate a temporary codice fiscale
			String codiceFiscale = username.toUpperCase() + "00" + (int)(Math.random() * 1000);
			Utente nuovoStudente = new Utente(codiceFiscale, nome, cognome, "1990-01-01", username, email, password);
			boolean registrationSuccess = programma.insertStudente(nuovoStudente);
			
			if (registrationSuccess) {
				JOptionPane.showMessageDialog(mainApp, 
					"Registrazione completata con successo!\nBenvenuto/a " + nome + " " + cognome + "!", 
					"Registrazione riuscita", 
					JOptionPane.INFORMATION_MESSAGE);
				
				// Clear fields
				clearFields();
				
				// Navigate to login
				mainApp.showStudenteLogin();
			} else {
				JOptionPane.showMessageDialog(mainApp, 
					"Errore durante la registrazione!\nRiprova più tardi", 
					"Errore", 
					JOptionPane.ERROR_MESSAGE);
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(mainApp, 
				"Errore di connessione al database: " + e.getMessage(), 
				"Errore database", 
				JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void clearFields() {
		nomeField.setText("Inserisci il tuo nome");
		cognomeField.setText("Inserisci il tuo cognome");
		emailField.setText("Inserisci la tua email");
		usernameField.setText("Inserisci nome utente");
		passwordField.setText("");
		confirmPasswordField.setText("");
	}
}