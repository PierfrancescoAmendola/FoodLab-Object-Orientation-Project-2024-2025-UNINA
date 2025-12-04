package boundary;

import entity.*;
import controller.*;
import javax.swing.*;

import boundary.dialog.FiltroDialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class GestioneCorsiPanel extends JPanel {

	private Controller programma;
	private MainApplication mainApp;
	private Utente admin;
	private JScrollPane scrollPane;
	private ArrayList<Corso> elencoCorsi;
	private JPanel mainPanel;

	public GestioneCorsiPanel(Controller p, MainApplication mainApp) {
		this.programma = p;
		this.mainApp = mainApp;

		initializePanel();
	}

	public void setChef(Utente chef) {
		this.admin = chef;
		refreshDisplay();
	}
	
	public void refreshDisplay() {
		removeAll();
		revalidate();
		repaint();
		initializePanel();
	}

	private void initializePanel() {
		// Main container with kitchen theme
		setBackground(KitchenTheme.SOFT_GRAY);
		setLayout(new BorderLayout());

		// Scrollable main panel
		scrollPane = new JScrollPane() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(KitchenTheme.SOFT_GRAY);
				g2.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		scrollPane.setBorder(null);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.getVerticalScrollBar().setBlockIncrement(32);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
		scrollPane.getHorizontalScrollBar().setBlockIncrement(32);

		// Style the scrollbars
		KitchenTheme.styleScrollBar(scrollPane.getVerticalScrollBar());
		KitchenTheme.styleScrollBar(scrollPane.getHorizontalScrollBar());

		// Main content panel
		mainPanel = KitchenTheme.createKithenMainPanel();
		mainPanel.setOpaque(false);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		scrollPane.add(mainPanel);
		scrollPane.setViewportView(mainPanel);

		// Header section
		createHeader(mainPanel);

		// Welcome section
		createWelcomeSection(mainPanel);

		// Courses management section
		try {
			if (admin != null)
				elencoCorsi = programma.elencaCorsi(admin.getCodiceFiscale());
		} catch (SQLException e) {
			elencoCorsi = null; // Errore con il database
		}
		createCoursesManagementSection(mainPanel);

		// Announcements section
		createAnnouncementsSection(mainPanel);

		add(scrollPane, BorderLayout.CENTER);
	}

	private void createHeader(JPanel mainPanel) {
		// Header with title and back button
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setOpaque(false);
		headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

		JLabel titleLabel = new JLabel("Gestione Corsi");
		titleLabel.setFont(KitchenTheme.TITLE_FONT);
		titleLabel.setForeground(KitchenTheme.WARM_ORANGE);
		headerPanel.add(titleLabel, BorderLayout.WEST);

		JButton backButton = KitchenTheme.createSecondaryButton("← Torna all'Area Chef");
		backButton.addActionListener(e -> {
			mainApp.showAreaChef();
		});
		headerPanel.add(backButton, BorderLayout.EAST);

		mainPanel.add(headerPanel);
		mainPanel.add(Box.createVerticalStrut(30));
	}

	private void createWelcomeSection(JPanel mainPanel) {
		JPanel welcomePanel = KitchenTheme.createRoundedGreyPanel();
		welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
		welcomePanel.setOpaque(false);
		welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		welcomePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

		JLabel welcomeLabel = new JLabel("Gestisci i tuoi corsi e comunica con gli studenti");
		welcomeLabel.setFont(new Font("SF Pro Text", Font.BOLD, 20));
		welcomeLabel.setForeground(KitchenTheme.DARK_GRAY);
		welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		welcomePanel.add(welcomeLabel);

		JLabel infoLabel = new JLabel("Visualizza informazioni, iscritti e crea avvisi per i tuoi corsi");
		infoLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
		infoLabel.setForeground(KitchenTheme.LIGHT_GRAY);
		infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		welcomePanel.add(infoLabel);

		mainPanel.add(welcomePanel);
		mainPanel.add(Box.createVerticalStrut(30));
	}

	private void createCoursesManagementSection(JPanel mainPanel) {
		JPanel coursesPanel = KitchenTheme.createRoundedGreyPanel();
		coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
		coursesPanel.setOpaque(false);
		coursesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Pannello titolo corsi, co filtro
		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(new Color(248, 248, 252));
		titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		JLabel coursesTitle = new JLabel("I Tuoi Corsi");
		coursesTitle.setFont(new Font("SF Pro Text", Font.BOLD, 18));
		coursesTitle.setForeground(KitchenTheme.DARK_GRAY);
		coursesTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Aggiunto pulsante Filtra
		JButton filtroButton = KitchenTheme.createKitchenButton("🔍 Filtra Corsi");
		filtroButton.setPreferredSize(new Dimension(150, 35));
		filtroButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		filtroButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FiltroDialog formFiltro = new FiltroDialog();
				formFiltro.setModal(true);
				formFiltro.setVisible(true);

				// Form inviato, la finestra di dialogo è chiusa
				String argomento = formFiltro.getArgomento();
				try {
					elencoCorsi = programma.searchFiltrered(admin.getCodiceFiscale(), argomento);
					// Aggiorna la sezione dei corsi
					coursesPanel.removeAll();
					coursesPanel.add(titlePanel);
					for (Corso corso : elencoCorsi) {
						coursesPanel.add(createCourseManagementCard(corso));
						coursesPanel.add(Box.createVerticalStrut(10));
					}
					// Aggiorna lo schermo
					mainPanel.validate(); // Ricalcola layout
					mainPanel.repaint();
				} catch (SQLException ex) {
					elencoCorsi = null;
					ex.printStackTrace();
					JOptionPane.showMessageDialog(filtroButton, "Errore nella ricerca: " + ex.getMessage(), "Errore",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		titlePanel.add(coursesTitle);
		coursesPanel.add(Box.createVerticalStrut(15)); // Spazio tra titolo e filtro
		titlePanel.add(filtroButton);

		titlePanel.setPreferredSize(new Dimension(titlePanel.getWidth(), 80));
		titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
		titlePanel.setBackground(KitchenTheme.SOFT_GRAY);

		coursesPanel.add(titlePanel);
		coursesPanel.add(Box.createVerticalStrut(15));

		// Load and display courses
		if (admin != null) {
//				elencoCorsi = programma.elencaCorsi(admin.getCodiceFiscale());
			if (elencoCorsi == null) {
				JLabel errorLabel = new JLabel("Errore nel caricamento dei corsi");
				errorLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
				errorLabel.setForeground(Color.RED);
				errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				coursesPanel.add(errorLabel);
			} else if (elencoCorsi != null && !elencoCorsi.isEmpty()) {
				for (Corso corso : elencoCorsi) {
					coursesPanel.add(createCourseManagementCard(corso));
					coursesPanel.add(Box.createVerticalStrut(10));
				}
			} else {
				JLabel noCoursesLabel = new JLabel("Nessun corso creato ancora");
				noCoursesLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
				noCoursesLabel.setForeground(KitchenTheme.LIGHT_GRAY);
				noCoursesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				coursesPanel.add(noCoursesLabel);
			}
		}

		mainPanel.add(coursesPanel);
		mainPanel.add(Box.createVerticalStrut(30));
	}

	private JPanel createCourseManagementCard(Corso corso) {
		JPanel cardPanel = KitchenTheme.createRoundedPanel(Color.WHITE, new Color(220, 220, 220));
		cardPanel.setLayout(new BorderLayout());
		cardPanel.setOpaque(false);
		cardPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
		cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

		// Course info
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setOpaque(false);

		JLabel courseNameLabel = new JLabel(corso.getArgomento());
		courseNameLabel.setFont(new Font("SF Pro Text", Font.BOLD, 16));
		courseNameLabel.setForeground(KitchenTheme.DARK_GRAY);
		infoPanel.add(courseNameLabel);

		JLabel dateLabel = new JLabel("Inizio: " + corso.getDataInizio() + " - Frequenza: " + corso.getFrequenza());
		dateLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
		dateLabel.setForeground(KitchenTheme.LIGHT_GRAY);
		infoPanel.add(dateLabel);

		// Get number of enrolled students
		try {
			int numIscritti = programma.getNumeroIscrittiCorso(corso.getIdCorso());
			JLabel enrolledLabel = new JLabel("Iscritti: " + numIscritti);
			enrolledLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
			enrolledLabel.setForeground(KitchenTheme.WARM_ORANGE);
			infoPanel.add(enrolledLabel);
		} catch (SQLException e) {
			JLabel errorLabel = new JLabel("Errore nel caricamento iscritti");
			errorLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
			errorLabel.setForeground(Color.RED);
			infoPanel.add(errorLabel);
		}

		cardPanel.add(infoPanel, BorderLayout.WEST);

		// Action buttons with dropdown
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
		buttonPanel.setOpaque(false);

		// Create dropdown menu for course actions
		JButton actionButton = KitchenTheme.createStyledActionButton("⚙️ Azioni");
		JPopupMenu actionMenu = createStyledPopupMenu();

		JMenuItem detailsItem = new JMenuItem("📋 Dettagli Corso");
		detailsItem.addActionListener(e -> showCourseDetails(corso));
		actionMenu.add(detailsItem);

		JMenuItem sessionItem = new JMenuItem("📅 Aggiungi Sessione");
		sessionItem.addActionListener(e -> showAddSessionDialog(corso));
		actionMenu.add(sessionItem);

		JMenuItem recipeItem = new JMenuItem("🍳 Aggiungi Ricetta");
		recipeItem.addActionListener(e -> showAddRecipeDialog(corso));
		actionMenu.add(recipeItem);
		
		JMenuItem ingredientItem = new JMenuItem("🥕 Aggiungi Ingrediente");
		ingredientItem.addActionListener(e -> showAddIngredientDialog(corso));
		actionMenu.add(ingredientItem);

		JMenuItem deleteItem = new JMenuItem("📅 Elimina corso");
		deleteItem.addActionListener(e -> {
			try {
				programma.eliminaCorso(corso);
				refreshDisplay();
				mainApp.refreshChefArea();
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(mainApp, "⚠️ " + e1.getMessage() + "\n" + corso.getIdCorso(),
						"Errore eliminazione", JOptionPane.WARNING_MESSAGE);
//				showStyledMessage, 
//						"Valore non valido", JOptionPane.WARNING_MESSAGE);
			}
		});
		actionMenu.add(deleteItem);

		actionMenu.addSeparator();

		JMenuItem announcementsItem = new JMenuItem("📢 Avvisi");
		announcementsItem.addActionListener(e -> showAnnouncementsDialog(corso));
		actionMenu.add(announcementsItem);

		actionButton.addActionListener(e -> {
			actionMenu.show(actionButton, 0, actionButton.getHeight());
		});
		buttonPanel.add(actionButton);

		cardPanel.add(buttonPanel, BorderLayout.EAST);

		return cardPanel;
	}

	private void createAnnouncementsSection(JPanel mainPanel) {
		JPanel announcementsPanel = KitchenTheme.createRoundedGreyPanel();
		announcementsPanel.setLayout(new BoxLayout(announcementsPanel, BoxLayout.Y_AXIS));
		announcementsPanel.setOpaque(false);
		announcementsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JLabel announcementsTitle = new JLabel("Gestione Avvisi");
		announcementsTitle.setFont(new Font("SF Pro Text", Font.BOLD, 18));
		announcementsTitle.setForeground(KitchenTheme.DARK_GRAY);
		announcementsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		announcementsPanel.add(announcementsTitle);
		announcementsPanel.add(Box.createVerticalStrut(15));

		JLabel infoLabel = new JLabel("Crea avvisi per comunicare con gli studenti dei tuoi corsi");
		infoLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
		infoLabel.setForeground(KitchenTheme.LIGHT_GRAY);
		infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		announcementsPanel.add(infoLabel);
		announcementsPanel.add(Box.createVerticalStrut(20));

		JButton createAnnouncementButton = KitchenTheme.createPrimaryButton("Crea Nuovo Avviso");
		createAnnouncementButton.addActionListener(e -> {
			showCreateAnnouncementDialog();
		});
		createAnnouncementButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		announcementsPanel.add(createAnnouncementButton);

		mainPanel.add(announcementsPanel);
	}

	private void showCourseDetails(Corso corso) {
		JDialog dialog = new boundary.dialog.InfoCorsoDialog(mainApp, programma, corso);
	}

	private void showAnnouncementsDialog(Corso corso) {
		JDialog dialog = new boundary.dialog.ShowAnnouncementsDialog(mainApp, programma, corso);
	}

	private void showCreateAnnouncementDialog() {
		JDialog dialog = new boundary.dialog.CreateAnnouncementDialog(mainApp, programma,  admin);
	}

	private void showAddSessionDialog(Corso corso) {
		JDialog dialog = new boundary.dialog.CreateSessionDialog(mainApp, programma, corso);
	}

	private void showAddRecipeDialog(Corso corso) {
		JDialog dialog = new boundary.dialog.RecipeDialog(mainApp, programma, corso);
	}
	
	private void showAddIngredientDialog(Corso corso) {
		JDialog dialog = new boundary.dialog.IngredientDialog(mainApp, programma, corso);
	}

	private void styleDialogField(JTextField field) {
		field.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
		field.setBorder(new AppleStyleBorder(10));
		field.setPreferredSize(new Dimension(350, 45));
		field.setMaximumSize(new Dimension(350, 45));
		field.setAlignmentX(Component.CENTER_ALIGNMENT);
		field.setOpaque(true);
		field.setBackground(Color.WHITE);
		field.setForeground(new Color(30, 30, 30));
	}

	private JPopupMenu createStyledPopupMenu() {
		JPopupMenu popupMenu = new JPopupMenu() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				// Draw menu background with shadow
				g2.setColor(new Color(0, 0, 0, 15));
				g2.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 15, 15);

				g2.setColor(Color.WHITE);
				g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 15, 15);

				// Draw border
				g2.setColor(new Color(220, 220, 220));
				g2.setStroke(new BasicStroke(1));
				g2.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 15, 15);

				super.paintComponent(g);
			}
		};
		popupMenu.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
		return popupMenu;
	}

	// Apple-style border class
	static class AppleStyleBorder implements javax.swing.border.Border {
		private int radius;

		AppleStyleBorder(int radius) {
			this.radius = radius;
		}

		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// Draw subtle outer border
			g2.setColor(new Color(0, 0, 0, 8));
			g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);

			// Draw inner highlight
			g2.setColor(new Color(255, 255, 255, 40));
			g2.drawRoundRect(x + 1, y + 1, width - 3, height - 3, radius - 2, radius - 2);
		}

		public Insets getBorderInsets(Component c) {
			return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
		}

		public boolean isBorderOpaque() {
			return false;
		}
	}
}
