package boundary;

import javax.swing.*;
import java.awt.*;
import controller.*;
import java.util.Calendar;

public class ChefRegistrazionePanel extends JPanel {
	private Controller programma;
	private MainApplication mainApp;
    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField emailField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> giornoComboBox;
    private JComboBox<String> meseComboBox;
    private JComboBox<String> annoComboBox;
    
    public ChefRegistrazionePanel(Controller p, MainApplication mainApp) {
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
    	mainContainer.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
    	
    	// Header section
    	JPanel headerPanel = createHeaderSection();
    	mainContainer.add(headerPanel);
    	mainContainer.add(Box.createVerticalStrut(30));
    	
    	// Registration form section
    	JPanel formPanel = createRegistrationFormSection();
    	add(mainContainer, BorderLayout.CENTER);
    	mainContainer.add(formPanel);
    }
    
    private JPanel createHeaderSection() {
    	JPanel headerPanel = new JPanel();
    	headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
    	headerPanel.setOpaque(false);
    	headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	
    	// Main title
    	JLabel titleLabel = new JLabel("Registrazione Chef");
    	titleLabel.setFont(KitchenTheme.TITLE_FONT);
    	titleLabel.setForeground(KitchenTheme.WARM_ORANGE);
    	titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	headerPanel.add(titleLabel);
    	
    	// Subtitle
    	JLabel subtitleLabel = new JLabel("Unisciti alla nostra comunità culinaria");
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
    	formPanel.setMaximumSize(new Dimension(500, 650));
    	formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	
    	// Form title
    	JLabel formTitle = new JLabel("Crea il tuo account chef");
    	formTitle.setFont(new Font("Georgia", Font.BOLD, 20));
    	formTitle.setForeground(KitchenTheme.DARK_GRAY);
    	formTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
    	formPanel.add(formTitle);
    	
    	formPanel.add(Box.createVerticalStrut(15));
    	
    	// Personal information section
    	JPanel personalSection = createPersonalInfoSection();
    	formPanel.add(personalSection);
    	
    	formPanel.add(Box.createVerticalStrut(10));
    	
    	// Account information section
    	JPanel accountSection = createAccountInfoSection();
    	formPanel.add(accountSection);
    	
    	formPanel.add(Box.createVerticalStrut(10));
    	
    	// Date of birth section
    	JPanel dateSection = new DataField();//createDateOfBirthSection();
    			
    	//new DataField();
    	formPanel.add(dateSection);
    	
    	formPanel.add(Box.createVerticalStrut(15));
    	
    	// Buttons section
    	JPanel buttonSection = createButtonSection();
    	formPanel.add(buttonSection);
    	
    	return formPanel;
    }
    
    private JPanel createPersonalInfoSection() {
    	JPanel section = new JPanel();
    	section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
    	section.setOpaque(false);
    	section.setAlignmentX(Component.CENTER_ALIGNMENT);
    	
    	JLabel sectionTitle = new JLabel("Informazioni Personali");
    	sectionTitle.setFont(new Font("Georgia", Font.BOLD, 16));
    	sectionTitle.setForeground(KitchenTheme.WARM_ORANGE);
    	sectionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
    	section.add(sectionTitle);
    	
    	section.add(Box.createVerticalStrut(15));
    	
    	// Nome field
    	JLabel nomeLabel = new JLabel("Nome");
    	nomeLabel.setFont(KitchenTheme.LABEL_FONT);
    	nomeLabel.setForeground(KitchenTheme.DARK_GRAY);
    	nomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	section.add(nomeLabel);
    	
    	section.add(Box.createVerticalStrut(5));
    	
    	nomeField = KitchenTheme.createKitchenTextField("Inserisci il tuo nome");
    	nomeField.setAlignmentX(Component.CENTER_ALIGNMENT);
    	section.add(nomeField);
    	
    	section.add(Box.createVerticalStrut(15));
    	
    	// Cognome field
    	JLabel cognomeLabel = new JLabel("Cognome");
    	cognomeLabel.setFont(KitchenTheme.LABEL_FONT);
    	cognomeLabel.setForeground(KitchenTheme.DARK_GRAY);
    	cognomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	section.add(cognomeLabel);
    	
    	section.add(Box.createVerticalStrut(5));
    	
    	cognomeField = KitchenTheme.createKitchenTextField("Inserisci il tuo cognome");
    	cognomeField.setAlignmentX(Component.CENTER_ALIGNMENT);
    	section.add(cognomeField);
    	
    	return section;
    }
    
    private JPanel createAccountInfoSection() {
    	JPanel section = new JPanel();
    	section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
    	section.setOpaque(false);
    	section.setAlignmentX(Component.CENTER_ALIGNMENT);
    	
    	JLabel sectionTitle = new JLabel("Informazioni Account");
    	sectionTitle.setFont(new Font("Georgia", Font.BOLD, 16));
    	sectionTitle.setForeground(KitchenTheme.WARM_ORANGE);
    	sectionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
    	section.add(sectionTitle);
    	
    	section.add(Box.createVerticalStrut(15));
    	
    	// Email field
    	JLabel emailLabel = new JLabel("Email");
    	emailLabel.setFont(KitchenTheme.LABEL_FONT);
    	emailLabel.setForeground(KitchenTheme.DARK_GRAY);
    	emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	section.add(emailLabel);
    	
    	section.add(Box.createVerticalStrut(5));
    	
    	emailField = KitchenTheme.createKitchenTextField("Inserisci la tua email");
    	emailField.setAlignmentX(Component.CENTER_ALIGNMENT);
    	section.add(emailField);
    	
    	section.add(Box.createVerticalStrut(15));
    	
    	// Username field
    	JLabel usernameLabel = new JLabel("Nome Utente");
    	usernameLabel.setFont(KitchenTheme.LABEL_FONT);
    	usernameLabel.setForeground(KitchenTheme.DARK_GRAY);
    	usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	section.add(usernameLabel);
    	
    	section.add(Box.createVerticalStrut(5));
    	
    	usernameField = KitchenTheme.createKitchenTextField("Scegli un nome utente");
    	usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
    	section.add(usernameField);
    	
    	section.add(Box.createVerticalStrut(15));
    	
    	// Password field
    	JLabel passwordLabel = new JLabel("Password");
    	passwordLabel.setFont(KitchenTheme.LABEL_FONT);
    	passwordLabel.setForeground(KitchenTheme.DARK_GRAY);
    	passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	section.add(passwordLabel);
    	
    	section.add(Box.createVerticalStrut(5));
    	
    	passwordField = KitchenTheme.createKitchenPasswordField("Scegli una password sicura");
    	passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
    	section.add(passwordField);
    	
    	return section;
    }
    
    
    private JPanel createButtonSection() {
    	JPanel buttonPanel = new JPanel();
    	buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
    	buttonPanel.setOpaque(false);
    	buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	
    	// Register button
    	JButton registerButton = KitchenTheme.createKitchenButton("Registrati");
    	registerButton.addActionListener(e -> BottoneRegistrazionePremuto());
    	registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    	buttonPanel.add(registerButton);
    	
    	buttonPanel.add(Box.createVerticalStrut(15));
    	
    	// Back to login button
    	JButton backButton = KitchenTheme.createSecondaryButton("Torna al Login");
    	backButton.addActionListener(e -> {
    		programma.showChefLogin();
    	});
    	backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    	buttonPanel.add(backButton);
    	
    	return buttonPanel;
    }
    
    public void BottoneRegistrazionePremuto() {
    	String nome = nomeField.getText().trim();
    	String cognome = cognomeField.getText().trim();
    	String email = emailField.getText().trim();
    	String username = usernameField.getText().trim();
    	String password = new String(passwordField.getPassword());
    	
    	// Check if fields contain placeholder text
    	if (nome.equals("Inserisci il tuo nome")) nome = "";
    	if (cognome.equals("Inserisci il tuo cognome")) cognome = "";
    	if (email.equals("Inserisci la tua email")) email = "";
    	if (username.equals("Scegli un nome utente")) username = "";
    	if (password.equals("Scegli una password sicura")) password = "";
    	
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
    	
    	if (!email.contains("@")) {
    		JOptionPane.showMessageDialog(mainApp, 
    			"Email non valida!\nInserisci un'email valida", 
    			"Email non valida", JOptionPane.ERROR_MESSAGE);
    		return;
    	}
    	
    	// Get date values
    	int giorno = Integer.parseInt((String) giornoComboBox.getSelectedItem());
    	int mese = meseComboBox.getSelectedIndex() + 1; // Convert from 0-based to 1-based
    	int anno = Integer.parseInt((String) annoComboBox.getSelectedItem());
    	
    	// Comprehensive date validation for birth date
    	String birthDateValidation = validateBirthDate(giorno, mese, anno);
    	if (birthDateValidation != null) {
    		JOptionPane.showMessageDialog(mainApp, birthDateValidation, "Data di nascita non valida", JOptionPane.ERROR_MESSAGE);
    		return;
    	}
    	
    	String dataNascita = String.format("%04d-%02d-%02d", anno, mese, giorno);
    	
    	programma.registraChef(new entity.Utente(nome, cognome, dataNascita, username, email , password) );
    }
    
    /**
     * Validates a birth date with comprehensive checks
     * @param day Day of birth (1-31)
     * @param month Month of birth (1-12)
     * @param year Year of birth
     * @return null if valid, error message if invalid
     */
    private String validateBirthDate(int day, int month, int year) {
    	// Level 1: Basic range validation
    	if (month < 1 || month > 12) {
    		return "🚫 Mese non valido!\n\nIl mese deve essere compreso tra 1 e 12.\n\n💡 Seleziona un mese valido dal menu a tendina.";
    	}
    	
    	if (day < 1 || day > 31) {
    		return "🚫 Giorno non valido!\n\nIl giorno deve essere compreso tra 1 e 31.\n\n💡 Seleziona un giorno valido dal menu a tendina.";
    	}
    	
    	// Level 2: Check if day exists in the selected month (including leap year check)
    	int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    	
    	// Check for leap year and adjust February
    	if (isLeapYear(year)) {
    		daysInMonth[1] = 29; // February has 29 days in leap years
    	}
    	
    	if (day > daysInMonth[month - 1]) {
    		String monthName = getMonthName(month);
    		String leapYearInfo = (month == 2 && isLeapYear(year)) ? 
    			String.format(" (%d è un anno bisestile, quindi febbraio ha 29 giorni)", year) :
    			(month == 2 ? String.format(" (%d non è un anno bisestile, quindi febbraio ha 28 giorni)", year) : "");
    		
    		return String.format("🚫 Data impossibile!\n\n%s %d non può avere %d giorni%s.\n\n💡 %s può avere al massimo %d giorni.", 
    			monthName, year, day, leapYearInfo, monthName, daysInMonth[month - 1]);
    	}
    	
    	// Level 3: Check if the date is not in the future
    	Calendar today = Calendar.getInstance();
    	Calendar birthDate = Calendar.getInstance();
    	birthDate.set(year, month - 1, day); // Calendar months are 0-based
    	
    	if (birthDate.after(today)) {
    		return "🚫 Data di nascita nel futuro!\n\nLa data di nascita non può essere nel futuro.\n\n💡 Seleziona una data precedente ad oggi.";
    	}
    	
    	// Level 4: Check if the person is at least 16 years old (reasonable age for chef registration)
    	Calendar minimumAge = Calendar.getInstance();
    	minimumAge.add(Calendar.YEAR, -16);
    	
    	if (birthDate.after(minimumAge)) {
    		int age = today.get(Calendar.YEAR) - year;
    		if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
    			age--;
    		}
    		return String.format("🚫 Età minima non raggiunta!\n\nDevi avere almeno 16 anni per registrarti come chef.\nAttualmente hai %d anni.\n\n💡 Potrai registrarti quando compirai 16 anni.", age);
    	}
    	
    	// Level 5: Check if the person is not unreasonably old (data integrity check)
    	Calendar maximumAge = Calendar.getInstance();
    	maximumAge.add(Calendar.YEAR, -120);
    	
    	if (birthDate.before(maximumAge)) {
    		return "🚫 Data di nascita troppo antica!\n\nLa data inserita risulta essere di oltre 120 anni fa.\n\n💡 Verifica di aver inserito l'anno corretto.";
    	}
    	
    	return null; // Date is valid
    }
    
    /**
     * Determines if a year is a leap year
     * @param year The year to check
     * @return true if the year is a leap year, false otherwise
     */
    private boolean isLeapYear(int year) {
    	// A year is a leap year if:
    	// 1. It's divisible by 4 AND
    	// 2. If it's divisible by 100, then it must also be divisible by 400
    	return (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0);
    }
    
    private String getMonthName(int month) {
    	String[] monthNames = {
    		"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
    		"Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"
    	};
    	return monthNames[month - 1];
    }
}

