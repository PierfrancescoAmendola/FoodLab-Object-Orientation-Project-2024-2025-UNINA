package boundary;

import javax.swing.*;
import java.awt.*;
import controller.*;

public class ChefLoginPanel extends JPanel {
	private Controller programma;
	private MainApplication mainApp;
	private JTextField usernameField;
	private JPasswordField passwordField;

	public ChefLoginPanel(Controller p, MainApplication mainApp) {
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
		mainContainer.add(Box.createVerticalStrut(40));
		
		// Login form section
		JPanel formPanel = createLoginFormSection();
		mainContainer.add(formPanel);
		
		add(mainContainer, BorderLayout.CENTER);
	}
	
	private JPanel createHeaderSection() {
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
		headerPanel.setOpaque(false);
		headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// Main title
		JLabel titleLabel = new JLabel("Accesso Chef");
		titleLabel.setFont(KitchenTheme.TITLE_FONT);
		titleLabel.setForeground(KitchenTheme.WARM_ORANGE);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		headerPanel.add(titleLabel);
		
		// Subtitle
		JLabel subtitleLabel = new JLabel("Bentornato nella tua cucina digitale");
		subtitleLabel.setFont(KitchenTheme.SUBTITLE_FONT);
		subtitleLabel.setForeground(KitchenTheme.WARM_BROWN);
		subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		headerPanel.add(subtitleLabel);
		
		return headerPanel;
	}
	
	private JPanel createLoginFormSection() {
		JPanel formPanel = KitchenTheme.createKitchenCard();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
		formPanel.setMaximumSize(new Dimension(450, 500));
		formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// Form title
		JLabel formTitle = new JLabel("Accedi al tuo account");
		formTitle.setFont(new Font("Georgia", Font.BOLD, 20));
		formTitle.setForeground(KitchenTheme.DARK_GRAY);
		formTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.add(formTitle);
		
		formPanel.add(Box.createVerticalStrut(30));
		
		// Username field
		JLabel usernameLabel = new JLabel("Email o Nome Utente");
		usernameLabel.setFont(KitchenTheme.LABEL_FONT);
		usernameLabel.setForeground(KitchenTheme.DARK_GRAY);
		usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.add(usernameLabel);
		
		formPanel.add(Box.createVerticalStrut(8));
		
		usernameField = KitchenTheme.createKitchenTextField("Inserisci email o nome utente");
		usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
		usernameField.addActionListener(e -> passwordField.requestFocus());
		formPanel.add(usernameField);
		
		formPanel.add(Box.createVerticalStrut(20));
		
		// Password field
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setFont(KitchenTheme.LABEL_FONT);
		passwordLabel.setForeground(KitchenTheme.DARK_GRAY);
		passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.add(passwordLabel);
		
		formPanel.add(Box.createVerticalStrut(8));
		
		passwordField = KitchenTheme.createKitchenPasswordField("Inserisci la tua password");
		passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
		passwordField.addActionListener(e -> BottonePrincipalePremuto());
		formPanel.add(passwordField);
		
		formPanel.add(Box.createVerticalStrut(30));
		
		// Buttons section
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setOpaque(false);
		buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// Login button
		JButton loginButton = KitchenTheme.createKitchenButton("Accedi");
		loginButton.addActionListener(e -> BottonePrincipalePremuto());
		loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.add(loginButton);
		
		buttonPanel.add(Box.createVerticalStrut(15));
		
		// Register button
		JButton registerButton = KitchenTheme.createSecondaryButton("Registrati");
		registerButton.addActionListener(e -> {
			programma.registerChef(mainApp);
		});
		registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.add(registerButton);
		
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
	
	public void BottonePrincipalePremuto() {
		String emailOrUsername = usernameField.getText().trim(); 
		String password = new String(passwordField.getPassword());
		
		
		// Check if fields contain placeholder text
		if (emailOrUsername.equals("Inserisci email o nome utente")) emailOrUsername = "";
		if (password.equals("Inserisci la tua password")) password = "";
		
		if (emailOrUsername.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(mainApp, 
				"Attenzione!\nInserisci email/nome utente e password", 
				"Campi obbligatori", JOptionPane.ERROR_MESSAGE);
		} else if (password.length() < 5) {
			JOptionPane.showMessageDialog(mainApp, 
				"Password troppo corta!\nLa password deve contenere almeno 5 caratteri", 
				"Password non valida", JOptionPane.ERROR_MESSAGE);
		} else {
			programma.effettuaLogin(emailOrUsername, password);
		}
	}
}