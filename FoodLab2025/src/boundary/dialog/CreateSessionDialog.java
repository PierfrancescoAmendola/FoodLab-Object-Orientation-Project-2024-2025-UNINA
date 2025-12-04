package boundary.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

import boundary.KitchenTheme;
import boundary.MainApplication;
import controller.Controller;
import entity.Corso;

public class CreateSessionDialog extends JDialog
{
	public CreateSessionDialog (MainApplication mainApp, Controller programma, Corso corso)
	{
		super(mainApp, "Crea Nuova Sessione - " + corso.getArgomento(), true);
		this.setSize(520, 650);
		this.setLocationRelativeTo(mainApp);
		this.setLayout(new BorderLayout());
	
		// Set this icon to match application logo
		try {
			java.net.URL iconURL = getClass().getResource("/icons/logofoodlab.png");
			if (iconURL != null) {
				ImageIcon icon = new ImageIcon(iconURL);
				this.setIconImage(icon.getImage());
			}
		} catch (Exception e) {
			// Icon loading failed, continue without icon
		}
	
		JPanel mainPanel = KitchenTheme.createGradientPanel(new Color(255, 250, 245), new Color(248, 248, 252));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(35, 40, 35, 40));
	
		// Header section with chef icon
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
		headerPanel.setOpaque(false);
		headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
	
		JLabel iconLabel = new JLabel("👨‍🍳");
		iconLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 32));
		iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		headerPanel.add(iconLabel);
		headerPanel.add(Box.createVerticalStrut(10));
	
		JLabel titleLabel = new JLabel("Organizza una Nuova Sessione");
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
	
		// Date selection with dropdowns (European format DD/MM/YYYY)
		JLabel dateLabel = new JLabel("📅 Quando si svolgerà?");
		dateLabel.setFont(new Font("SF Pro Text", Font.BOLD, 16));
		dateLabel.setForeground(KitchenTheme.DARK_GRAY);
		dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.add(dateLabel);
		formPanel.add(Box.createVerticalStrut(15));
	
		JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		datePanel.setOpaque(false);
	
		// Day dropdown
		JComboBox<String> dayCombo = new JComboBox<>();
		for (int i = 1; i <= 31; i++) {
			dayCombo.addItem(String.format("%02d", i));
		}
		styleDateComboBox(dayCombo, "Giorno");
	
		// Month dropdown
		JComboBox<String> monthCombo = new JComboBox<>();
		String[] months = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
		for (String month : months) {
			monthCombo.addItem(month);
		}
		styleDateComboBox(monthCombo, "Mese");
	
		// Year dropdown
		JComboBox<String> yearCombo = new JComboBox<>();
		int currentYear = java.time.LocalDate.now().getYear();
		for (int i = currentYear; i <= currentYear + 2; i++) {
			yearCombo.addItem(String.valueOf(i));
		}
		styleDateComboBox(yearCombo, "Anno");
	
		datePanel.add(dayCombo);
		datePanel.add(new JLabel("/"));
		datePanel.add(monthCombo);
		datePanel.add(new JLabel("/"));
		datePanel.add(yearCombo);
	
		formPanel.add(datePanel);
		formPanel.add(Box.createVerticalStrut(30));
	
		// Time selection
		JLabel timeLabel = new JLabel("🕐 A che ora inizia?");
		timeLabel.setFont(new Font("SF Pro Text", Font.BOLD, 16));
		timeLabel.setForeground(KitchenTheme.DARK_GRAY);
		timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.add(timeLabel);
		formPanel.add(Box.createVerticalStrut(15));
	
		JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
		timePanel.setOpaque(false);
	
		JComboBox<String> hourCombo = new JComboBox<>();
		for (int i = 8; i <= 22; i++) {
			hourCombo.addItem(String.format("%02d", i));
		}
		styleDateComboBox(hourCombo, "Ora");
	
		JComboBox<String> minuteCombo = new JComboBox<>();
		for (int i = 0; i < 60; i += 15) {
			minuteCombo.addItem(String.format("%02d", i));
		}
		styleDateComboBox(minuteCombo, "Minuti");
	
		timePanel.add(hourCombo);
		timePanel.add(new JLabel(":"));
		timePanel.add(minuteCombo);
	
		formPanel.add(timePanel);
		formPanel.add(Box.createVerticalStrut(30));
	
		// Location selection with beautiful radio buttons
		JLabel locationLabel = new JLabel("� Dove si svolge?");
		locationLabel.setFont(new Font("SF Pro Text", Font.BOLD, 16));
		locationLabel.setForeground(KitchenTheme.DARK_GRAY);
		locationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.add(locationLabel);
		formPanel.add(Box.createVerticalStrut(15));
	
		JPanel locationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		locationPanel.setOpaque(false);
	
		ButtonGroup locationGroup = new ButtonGroup();
	
		JRadioButton homeRadio = new JRadioButton("🏡 A Casa");
		homeRadio.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
		homeRadio.setForeground(KitchenTheme.DARK_GRAY);
		homeRadio.setOpaque(false);
		homeRadio.setSelected(true);
	
		JRadioButton onlineRadio = new JRadioButton("💻 Online");
		onlineRadio.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
		onlineRadio.setForeground(KitchenTheme.DARK_GRAY);
		onlineRadio.setOpaque(false);
	
		locationGroup.add(homeRadio);
		locationGroup.add(onlineRadio);
	
		locationPanel.add(homeRadio);
		locationPanel.add(onlineRadio);
	
		formPanel.add(locationPanel);
		formPanel.add(Box.createVerticalStrut(40));
	
		// Create button with enhanced styling
		JButton createButton = new JButton("✨ Crea la Sessione");
		createButton.setFont(new Font("SF Pro Text", Font.BOLD, 16));
		createButton.setForeground(Color.WHITE);
		createButton.setBackground(new Color(255, 140, 0)); // Distinct orange color
		createButton.setPreferredSize(new Dimension(300, 50));
		createButton.setMaximumSize(new Dimension(300, 50));
		createButton
				.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 100, 0), 2),
						BorderFactory.createEmptyBorder(8, 15, 8, 15)));
		createButton.setFocusPainted(false);
		createButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		createButton.setOpaque(true);
	
		// Enhanced button effect
		createButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				createButton.setBackground(new Color(220, 120, 0));
			}
	
			public void mouseExited(java.awt.event.MouseEvent evt) {
				createButton.setBackground(new Color(255, 140, 0));
			}
		});
	
		createButton.addActionListener(e -> {
			String day = (String) dayCombo.getSelectedItem();
			String month = (String) monthCombo.getSelectedItem();
			String year = (String) yearCombo.getSelectedItem();
			String hour = (String) hourCombo.getSelectedItem();
			String minute = (String) minuteCombo.getSelectedItem();
	
			// VALIDAZIONE COMPLETA PRIMA DI CREARE LA SESSIONE
	
			// 1. Validazione campi vuoti
			if (day == null || month == null || year == null || hour == null || minute == null) {
				JOptionPane.showMessageDialog(this, "❌ Errore: Tutti i campi data e ora sono obbligatori!",
						"Campi mancanti", JOptionPane.ERROR_MESSAGE);
				return;
			}
	
			// 2. Validazione formato data con messaggi specifici
			String validationError = validateSessionDate(day, month, year);
			if (validationError != null) {
				JOptionPane.showMessageDialog(this, validationError, "Data non valida", JOptionPane.ERROR_MESSAGE);
				return; // Rimane nel this per permettere la correzione
			}
	
			// 3. Validazione data nel futuro (ma non troppo lontano)
			if (!isDateInFuture(day, month, year)) {
				JOptionPane.showMessageDialog(this,
						"❌ La data della sessione deve essere nel futuro!\n\n" + "Data selezionata: " + day + "/"
								+ month + "/" + year + "\n" + "Data odierna: " + getCurrentDate(),
						"Data nel passato", JOptionPane.ERROR_MESSAGE);
				return;
			}
	
			// 4. Validazione data non troppo lontana nel futuro (max 2 anni)
			if (!isDateReasonable(day, month, year)) {
				JOptionPane.showMessageDialog(this,
						"❌ La data della sessione è troppo lontana nel futuro!\n\n" + "Data selezionata: " + day + "/"
								+ month + "/" + year + "\n"
								+ "Le sessioni possono essere pianificate massimo 2 anni in anticipo.",
						"Data troppo lontana", JOptionPane.ERROR_MESSAGE);
				return;
			}
	
			// 5. Validazione data sessione rispetto alla data di inizio del corso
			String courseStartValidation = validateSessionVsCourseStartDate(day, month, year, corso);
			if (courseStartValidation != null) {
				JOptionPane.showMessageDialog(this, courseStartValidation, "Data incompatibile con il corso",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
	
			// Convert European format to database format (YYYY-MM-DD)
			String dataDatabase = year + "-" + month + "-" + day;
			String ora = hour + ":" + minute;
			boolean online = onlineRadio.isSelected();
	
			// Se arriviamo qui, tutti i controlli sono passati
			try {
				programma.creaSessione(dataDatabase, ora, online, corso.getIdCorso());
	
				String locationText = online ? "online" : "a casa";
				String message = String.format(
						"🎉 Perfetto!\n\nLa sessione del %s/%s/%s alle %s:%s (%s) è stata creata con successo!", day,
						month, year, hour, minute, locationText);
	
				// Show success message first
				JOptionPane.showMessageDialog(this, message, "Sessione Creata!", JOptionPane.INFORMATION_MESSAGE);
	
				// Ask if user wants to add another session
				int response = JOptionPane.showConfirmDialog(this, "✨ Vuoi aggiungere un'altra sessione?",
						"Aggiungi altra sessione?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	
				if (response == JOptionPane.YES_OPTION) {
					// Clear the form for a new session
					dayCombo.setSelectedIndex(0);
					monthCombo.setSelectedIndex(0);
					yearCombo.setSelectedIndex(0);
					hourCombo.setSelectedIndex(0);
					minuteCombo.setSelectedIndex(0);
					homeRadio.setSelected(true);
					onlineRadio.setSelected(false);
				} else {
					// Close this and return to course management
					this.dispose();
					SwingUtilities.invokeLater(() -> {
						mainApp.refreshGestioneCorsiArea();
					});
				}
	
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this,
						"❌ Errore durante la creazione della sessione: " + ex.getMessage(), "Errore",
						JOptionPane.ERROR_MESSAGE);
			}
		});
	
		formPanel.add(createButton);
	
		mainPanel.add(formPanel);
		this.add(mainPanel, BorderLayout.CENTER);
		this.setVisible(true);
		formPanel.add(createButton);
	
		mainPanel.add(formPanel);
		this.add(mainPanel, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	private void styleDateComboBox(JComboBox<String> comboBox, String tooltip) {
		comboBox.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
		comboBox.setPreferredSize(new Dimension(80, 35));
		comboBox.setMaximumSize(new Dimension(80, 35));
		comboBox.setBackground(Color.WHITE);
		comboBox.setForeground(new Color(50, 50, 50));
		comboBox.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 2),
						BorderFactory.createEmptyBorder(5, 8, 5, 8)));
		comboBox.setToolTipText(tooltip);
		comboBox.setFocusable(true);
		comboBox.setOpaque(true);
		// Ensure the renderer shows the selected value
		comboBox.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				setFont(new Font("SF Pro Text", Font.PLAIN, 14));
				if (isSelected) {
					setBackground(new Color(255, 140, 0));
					setForeground(Color.WHITE);
				} else {
					setBackground(Color.WHITE);
					setForeground(new Color(50, 50, 50));
				}
				return this;
			}
		});
	}
	
	/**
	 * Valida che la data della sessione non sia precedente alla data di inizio del
	 * corso
	 */
	private String validateSessionVsCourseStartDate(String day, String month, String year, Corso corso) {
		try {
			java.time.LocalDate sessionDate = java.time.LocalDate.of(Integer.parseInt(year), Integer.parseInt(month),
					Integer.parseInt(day));

			// Parsa la data di inizio del corso (formato YYYY-MM-DD)
			String courseStartDate = corso.getDataInizio();
			java.time.LocalDate courseStart = java.time.LocalDate.parse(courseStartDate);

			if (sessionDate.isBefore(courseStart)) {
				return "❌ La data della sessione non può essere precedente alla data di inizio del corso!\n\n"
						+ "Data sessione selezionata: " + day + "/" + month + "/" + year + "\n" + "Data inizio corso '"
						+ corso.getArgomento() + "': " + formatDateEuropean(courseStart) + "\n\n"
						+ "Le sessioni devono essere programmate a partire dalla data di inizio del corso.";
			}

			return null; // Nessun errore

		} catch (Exception e) {
			return "❌ Errore nella validazione delle date: " + e.getMessage();
		}
	}
	
	/**
	 * Ottiene la data corrente in formato DD/MM/YYYY
	 */
	private String getCurrentDate() {
		java.time.LocalDate today = java.time.LocalDate.now();
		return String.format("%02d/%02d/%d", today.getDayOfMonth(), today.getMonthValue(), today.getYear());
	}
	
	/**
	 * Formatta una LocalDate in formato europeo (DD/MM/YYYY)
	 */
	private String formatDateEuropean(java.time.LocalDate date) {
		return String.format("%02d/%02d/%d", date.getDayOfMonth(), date.getMonthValue(), date.getYear());
	}
	
	/**
	 * Controlla se la data è ragionevole (non troppo lontana nel futuro)
	 */
	private boolean isDateReasonable(String day, String month, String year) {
		try {
			java.time.LocalDate selectedDate = java.time.LocalDate.of(Integer.parseInt(year), Integer.parseInt(month),
					Integer.parseInt(day));

			java.time.LocalDate today = java.time.LocalDate.now();
			java.time.LocalDate maxDate = today.plusYears(2); // Massimo 2 anni nel futuro

			return selectedDate.isBefore(maxDate) || selectedDate.isEqual(maxDate);

		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Controlla se la data è nel futuro
	 */
	private boolean isDateInFuture(String day, String month, String year) {
		try {
			java.time.LocalDate selectedDate = java.time.LocalDate.of(Integer.parseInt(year), Integer.parseInt(month),
					Integer.parseInt(day));

			java.time.LocalDate today = java.time.LocalDate.now();
			return selectedDate.isAfter(today);

		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Valida la data della sessione e restituisce un messaggio di errore specifico
	 * se non valida
	 */
	private String validateSessionDate(String day, String month, String year) {
		try {
			int d = Integer.parseInt(day);
			int m = Integer.parseInt(month);
			int y = Integer.parseInt(year);

			// Controlli specifici con messaggi dettagliati
			if (y < 2024) {
				return "❌ Anno non valido: " + year + "\n\n" + "L'anno deve essere almeno 2024 o successivo.";
			}

			if (m < 1 || m > 12) {
				return "❌ Mese non valido: " + month + "\n\n" + "Il mese deve essere compreso tra 01 e 12.";
			}

			if (d < 1 || d > 31) {
				return "❌ Giorno non valido: " + day + "\n\n" + "Il giorno deve essere compreso tra 01 e 31.";
			}

			// Controllo giorni per mese specifico
			int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

			// Controllo anno bisestile
			boolean isLeapYear = (y % 4 == 0 && y % 100 != 0) || (y % 400 == 0);
			if (isLeapYear) {
				daysInMonth[1] = 29;
			}

			if (d > daysInMonth[m - 1]) {
				String monthName = getMonthName(m);
				return "❌ Data non valida: " + day + "/" + month + "/" + year + "\n\n" + "Il mese di " + monthName + " "
						+ year + " ha solo " + daysInMonth[m - 1] + " giorni.";
			}

			// Prova a creare la data per validazione finale
			String dateString = year + "-" + String.format("%02d", m) + "-" + String.format("%02d", d);
			java.sql.Date.valueOf(dateString);

			return null; // Nessun errore

		} catch (NumberFormatException e) {
			return "❌ Formato data non valido!\n\n" + "Assicurati che giorno, mese e anno siano numeri validi.";
		} catch (IllegalArgumentException e) {
			return "❌ Data non esistente: " + day + "/" + month + "/" + year + "\n\n"
					+ "Questa data non esiste nel calendario.";
		} catch (Exception e) {
			return "❌ Errore nella validazione della data: " + e.getMessage();
		}
	}
	
	/**
	 * Ottiene il nome del mese
	 */
	private String getMonthName(int month) {
		String[] months = { "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto",
				"Settembre", "Ottobre", "Novembre", "Dicembre" };
		return months[month - 1];
	}
}
