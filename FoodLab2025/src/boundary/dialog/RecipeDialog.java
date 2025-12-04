package boundary.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import boundary.KitchenTheme;
import boundary.MainApplication;
import controller.Controller;
import entity.Corso;
import entity.Sessione;

public class RecipeDialog extends JDialog
{
	private Controller programma;
	
	public RecipeDialog(MainApplication mainApp, Controller controller, Corso corso) {
		super(mainApp, "Crea Nuova Ricetta - " + corso.getArgomento(), true);
		programma = controller;
		this.setSize(500, 550);
		this.setLocationRelativeTo(mainApp);
		this.setLayout(new BorderLayout());
		this.setModal(true);
		
		// Set dialog icon to match application logo
		try 
		{
			java.net.URL iconURL = getClass().getResource("/icons/logofoodlab.png");
			if (iconURL != null) {
				ImageIcon icon = new ImageIcon(iconURL);
				this.setIconImage(icon.getImage());
			}
		} catch (Exception e) {
			// Icon loading failed, continue without icon
		}
		
		JPanel mainPanel = KitchenTheme.createRoundedGreyPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(35, 40, 35, 40));
		
		// Header section with cooking icon
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
		headerPanel.setOpaque(false);
		headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel iconLabel = new JLabel("🍳");
		iconLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 32));
		iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		headerPanel.add(iconLabel);
		headerPanel.add(Box.createVerticalStrut(10));
		
		JLabel titleLabel = new JLabel("Crea una Nuova Ricetta");
		titleLabel.setFont(new Font("SF Pro Text", Font.BOLD, 20));
		titleLabel.setForeground(KitchenTheme.WARM_ORANGE);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		headerPanel.add(titleLabel);
		
		JLabel courseLabel = new JLabel("per il corso " + corso.getArgomento());
		courseLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
		courseLabel.setForeground(KitchenTheme.LIGHT_GRAY);
		courseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		headerPanel.add(courseLabel);
		
		mainPanel.add(headerPanel);
		mainPanel.add(Box.createVerticalStrut(35));
		
		// Form section with modern styling
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setOpaque(false);
		formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// Session selection with beautiful styling
		JLabel sessionLabel = new JLabel("📍 Seleziona la Sessione:");
		sessionLabel.setFont(new Font("SF Pro Text", Font.BOLD, 16));
		sessionLabel.setForeground(KitchenTheme.DARK_GRAY);
		sessionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.add(sessionLabel);
		formPanel.add(Box.createVerticalStrut(15));
		
		JComboBox<String> sessionComboBox = new JComboBox<>();
		loadSessionsIntoComboBox(sessionComboBox, corso.getIdCorso());
		KitchenTheme.styleModernSessionComboBox(sessionComboBox);
		sessionComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.add(sessionComboBox);
		formPanel.add(Box.createVerticalStrut(30));
		
		// Recipe name with elegant input field
		JLabel nameLabel = new JLabel("👨‍🍳 Nome della Ricetta:");
		nameLabel.setFont(new Font("SF Pro Text", Font.BOLD, 16));
		nameLabel.setForeground(KitchenTheme.DARK_GRAY);
		nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.add(nameLabel);
		formPanel.add(Box.createVerticalStrut(15));
		
		JTextField nameField = new JTextField();
		nameField.setFont(new Font("SF Pro Text", Font.PLAIN, 15));
		nameField.setPreferredSize(new Dimension(380, 45));
		nameField.setMaximumSize(new Dimension(380, 45));
		nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
		nameField.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
			BorderFactory.createEmptyBorder(10, 15, 10, 15)
		));
		nameField.setBackground(Color.WHITE);
		nameField.setForeground(KitchenTheme.DARK_GRAY);
		nameField.setText("Es: Spaghetti alla Carbonara");
		nameField.setForeground(Color.LIGHT_GRAY);
		
		// Placeholder text behavior
		nameField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (nameField.getText().equals("Es: Spaghetti alla Carbonara")) {
					nameField.setText("");
					nameField.setForeground(KitchenTheme.DARK_GRAY);
				}
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				if (nameField.getText().isEmpty()) {
					nameField.setText("Es: Spaghetti alla Carbonara");
					nameField.setForeground(Color.LIGHT_GRAY);
				}
			}
		});
		
		formPanel.add(nameField);
		formPanel.add(Box.createVerticalStrut(40));
		
		// Create button with enhanced styling
		JButton createButton = new JButton("🎯 Crea la Ricetta");
		createButton.setFont(new Font("SF Pro Text", Font.BOLD, 16));
		createButton.setForeground(Color.WHITE);
		createButton.setBackground(new Color(255, 140, 0)); // Distinct orange color
		createButton.setPreferredSize(new Dimension(300, 50));
		createButton.setMaximumSize(new Dimension(300, 50));
		createButton.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(200, 100, 0), 2),
			BorderFactory.createEmptyBorder(8, 15, 8, 15)
		));
		createButton.setFocusPainted(false);
		createButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		createButton.setOpaque(true);
		
		// Enhanced button effects
		createButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				createButton.setBackground(new Color(220, 120, 0));
			}
			public void mouseExited(java.awt.event.MouseEvent evt) {
				createButton.setBackground(new Color(255, 140, 0));
			}
		});
		
		createButton.addActionListener(e -> {
			String selectedSession = (String) sessionComboBox.getSelectedItem();
			String nomeRicetta = nameField.getText().trim();
			
			// VALIDAZIONE COMPLETA PRIMA DI CREARE LA RICETTA
			
			// 1. Validazione selezione sessione
			if (selectedSession == null || selectedSession.equals("Nessuna sessione disponibile")) {
				JOptionPane.showMessageDialog(this, 
					"❌ Errore: Devi selezionare una sessione valida!\n\n" +
					"Le ricette devono essere associate a una sessione specifica del corso.", 
					"Sessione non selezionata", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// 2. Validazione nome ricetta dettagliata
			String recipeValidation = validateRecipeName(nomeRicetta);
			if (recipeValidation != null) {
				JOptionPane.showMessageDialog(this, recipeValidation, "Nome ricetta non valido", JOptionPane.ERROR_MESSAGE);
				nameField.requestFocus();
				return;
			}
			
			try {
				// Extract session ID from selection
				String sessionId = selectedSession.split(" - ")[0];
				programma.creaRicetta(nomeRicetta, Integer.parseInt(sessionId));
				
				String message = String.format("🎉 Fantastico!\n\nLa ricetta '%s' è stata aggiunta con successo alla sessione!", 
					nomeRicetta);
				
				// Close dialog first to avoid double-click issue
				this.dispose();
				
				// Then show success message and return to course management  
				SwingUtilities.invokeLater(() -> {
					JOptionPane.showMessageDialog(mainApp, message, "Ricetta Creata!", JOptionPane.INFORMATION_MESSAGE);
					// Return to the main course management view by refreshing
					mainApp.refreshGestioneCorsiArea(); 
				});
				
			} catch (Exception ex) {
				String errorMessage;
				if (ex.getMessage() != null && ex.getMessage().contains("Impossibile aggiungere ricette a sessioni online")) {
					errorMessage = "🚫 Ricette non consentite per sessioni online!\n\n" +
								 "Le ricette possono essere create solo per sessioni pratiche (in presenza).\n\n" +
								 "💡 Soluzione:\n" +
								 "• Seleziona una sessione \"Pratica\" dal menu a tendina\n" +
								 "• Le sessioni online sono indicate con \"Online\" nel nome";
				} else {
					errorMessage = "❌ Errore durante la creazione della ricetta: " + ex.getMessage();
				}
				
				JOptionPane.showMessageDialog(this, errorMessage, "Errore", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		formPanel.add(createButton);
		
		mainPanel.add(formPanel);
		this.add(mainPanel, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	private void styleModernComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        comboBox.setPreferredSize(new Dimension(0, 35));
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(KitchenTheme.FRESH_GREEN, 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        comboBox.setBackground(Color.WHITE);
    }
	
	private void loadSessionsIntoComboBox(JComboBox<String> comboBox, String idCorso) {
		try {
			ArrayList<Sessione> sessioni = programma.elencaSessioniPratiche(idCorso);
			if (sessioni != null && !sessioni.isEmpty()) {
				for (Sessione sessione : sessioni) {
					String sessionInfo = String.format("%d - in data %s ore %s", 
						sessione.getIdSessione(), 
						sessione.getData(), 
						sessione.getOraInizio()
					);
					comboBox.addItem(sessionInfo);
				}
			} else {
				comboBox.addItem("Nessuna sessione disponibile");
			}
		} catch (SQLException e) {
			comboBox.addItem("Errore nel caricamento sessioni");
		}
	}
	
	// Method to style session combo boxes for recipe creation
//	private void styleModernSessionComboBox(JComboBox<String> comboBox) {
//		comboBox.setFont(new Font("SF Pro Text", Font.PLAIN, 15));
//		comboBox.setPreferredSize(new Dimension(380, 45));
//		comboBox.setMaximumSize(new Dimension(380, 45));
//		comboBox.setBackground(Color.WHITE);
//		comboBox.setForeground(KitchenTheme.DARK_GRAY);
//		comboBox.setBorder(
//				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
//						BorderFactory.createEmptyBorder(10, 15, 10, 15)));
//		comboBox.setFocusable(true);
//	}
	
	/**
	 * Valida il nome della ricetta con controlli specifici
	 */
	private String validateRecipeName(String nomeRicetta) {
		// 1. Controllo vuoto o placeholder
		if (nomeRicetta.isEmpty()) {
			return "❌ Il nome della ricetta è obbligatorio!\n\n" + "Inserisci un nome descrittivo per la ricetta.";
		}

		if (nomeRicetta.equals("Es: Spaghetti alla Carbonara")) {
			return "❌ Il nome della ricetta non è valido!\n\n"
					+ "Sostituisci il testo di esempio con il nome reale della ricetta.";
		}

		// 2. Controllo lunghezza minima
		if (nomeRicetta.length() < 3) {
			return "❌ Il nome della ricetta è troppo corto!\n\n" + "Il nome deve essere di almeno 3 caratteri.\n"
					+ "Nome attuale: '" + nomeRicetta + "' (" + nomeRicetta.length() + " caratteri)";
		}

		// 3. Controllo lunghezza massima (aggiornato per database)
		if (nomeRicetta.length() > 150) {
			return "❌ Il nome della ricetta è troppo lungo!\n\n" + "Il nome deve essere massimo 150 caratteri.\n"
					+ "Nome attuale: " + nomeRicetta.length() + " caratteri";
		}

		// 4. Controllo caratteri non validi
		if (nomeRicetta.matches(".*[<>\"'&].*")) {
			return "❌ Il nome della ricetta contiene caratteri non validi!\n\n"
					+ "I caratteri < > \" ' & non sono permessi.\n" + "Nome attuale: '" + nomeRicetta + "'";
		}

		// 5. Controllo solo spazi
		if (nomeRicetta.trim().isEmpty()) {
			return "❌ Il nome della ricetta non può contenere solo spazi!\n\n"
					+ "Inserisci un nome valido per la ricetta.";
		}

		// 6. Controllo caratteri numerici eccessivi (max 50% numeri)
		long digitCount = nomeRicetta.chars().filter(Character::isDigit).count();
		if (digitCount > nomeRicetta.length() / 2) {
			return "❌ Il nome della ricetta contiene troppi numeri!\n\n"
					+ "Usa un nome più descrittivo con parole significative.\n" + "Nome attuale: '" + nomeRicetta + "'";
		}

		return null; // Nessun errore
	}
}
