package boundary;

import entity.*;
import controller.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import boundary.dialog.FiltroDialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class AreaChefPanel extends JPanel {
	
	private Controller programma;
	private MainApplication mainApp;
	private JPanel coursesPanel;
	private JPanel titlePanel;
	private ArrayList<Corso> elencoCorsi;
	private Utente admin;
	private JScrollPane scrollPane;
	
	// Using KitchenTheme colors

	public AreaChefPanel(Controller p, MainApplication mainApp) {		
		this.programma = p;
		this.mainApp = mainApp;
		
		initializePanel();
	}
	
	public void setChef(Utente chef) {
		this.admin = chef;
		// Refresh the display with new chef data
		refreshDisplay();
	}
	
	public void refreshDisplay() {
		// This method can be called to refresh the display when chef data changes
		// Remove all components and recreate them
		removeAll();
		revalidate();
		repaint();
		
		// Recreate the entire panel
		initializePanel();
	}
	
	private void initializePanel() {
		// Main container with kitchen theme
        setBackground(KitchenTheme.SOFT_GRAY);
        setLayout(new BorderLayout());
        
        // Main content panel
        JPanel mainPanel = KitchenTheme.createKithenMainPanel();
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
     // Scrollable main panel
        scrollPane = new JScrollPane(mainPanel) {
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
        
        //Style the scrollbars
        styleScrollBar(scrollPane.getVerticalScrollBar());
        styleScrollBar(scrollPane.getHorizontalScrollBar());
         
        scrollPane.setViewportView(mainPanel);

		// Header section
		createHeader(mainPanel);
		
		// Welcome section
		createWelcomeSection(mainPanel);
		
		// Courses section
		if (admin != null) 
		{ 
			try {
				elencoCorsi = programma.elencaCorsi(admin.getCodiceFiscale());
			}catch(SQLException e) {
				elencoCorsi = null;	//Query fallita
			}
		}
		createCoursesSection(mainPanel);
		
		// Navigation to course creation
		createCourseCreationNavigation(mainPanel);
		
		// Logout section
		createLogoutSection(mainPanel);

		add(mainPanel, BorderLayout.CENTER);
        //add(scrollPane, BorderLayout.CENTER);
	}
	
	private void createHeader(JPanel mainPanel) {
		// Header with icon, title and avatar dropdown
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setOpaque(false);
		headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
		
		// Left side with icon and title
		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		leftPanel.setOpaque(false);
		
		// Utente icon
		JLabel iconLabel = new JLabel();
		try {
			ImageIcon icon = new ImageIcon("src/icons/iconachef.png");
			Image iconImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			iconLabel.setIcon(new ImageIcon(iconImage));
		} catch (Exception e) {
			// Fallback to emoji if icon not found
			iconLabel.setText("👨‍🍳");
			iconLabel.setFont(new Font("Arial", Font.BOLD, 35));
		}
		leftPanel.add(iconLabel);
		
		// Divider bar
		JLabel dividerLabel = new JLabel("|");
		dividerLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 24));
		dividerLabel.setForeground(new Color(200, 200, 200));
		leftPanel.add(dividerLabel);
		
		// Title
		JLabel titleLabel = new JLabel("Area Riservata Chef");
		titleLabel.setFont(KitchenTheme.TITLE_FONT);
		titleLabel.setForeground(KitchenTheme.WARM_ORANGE);
		leftPanel.add(titleLabel);
		
		headerPanel.add(leftPanel, BorderLayout.WEST);
		
		// Create avatar dropdown menu
		JPanel avatarPanel = createAvatarDropdown();
		headerPanel.add(avatarPanel, BorderLayout.EAST);
		
		mainPanel.add(headerPanel);
		mainPanel.add(Box.createVerticalStrut(30));
	}
	
	private JPanel createAvatarDropdown() {
		JPanel avatarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		avatarPanel.setOpaque(false);
		
		// Load chef icon
		ImageIcon chefIcon = null;
		try {
			chefIcon = new ImageIcon(getClass().getResource("/icons/iconachef.png"));
			// Scale the icon to fit the button
			Image img = chefIcon.getImage();
			Image scaledImg = img.getScaledInstance(35, 35, Image.SCALE_SMOOTH);
			chefIcon = new ImageIcon(scaledImg);
		} catch (Exception e) {
			System.out.println("Errore caricamento icona chef: " + e.getMessage());
		}
		
		// Create avatar button with chef icon
		final ImageIcon finalChefIcon = chefIcon;
		JButton avatarButton = new JButton() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				// Draw circular avatar background with gradient
				if (getModel().isPressed()) {
					// Pressed state - darker
					g2.setColor(new Color(KitchenTheme.WARM_ORANGE.getRed(), KitchenTheme.WARM_ORANGE.getGreen(), KitchenTheme.WARM_ORANGE.getBlue(), 200));
				} else if (getModel().isRollover()) {
					// Hover state - medium
					g2.setColor(new Color(KitchenTheme.WARM_ORANGE.getRed(), KitchenTheme.WARM_ORANGE.getGreen(), KitchenTheme.WARM_ORANGE.getBlue(), 150));
				} else {
					// Normal state - light
					g2.setColor(new Color(KitchenTheme.WARM_ORANGE.getRed(), KitchenTheme.WARM_ORANGE.getGreen(), KitchenTheme.WARM_ORANGE.getBlue(), 100));
				}
				
				// Draw main circle
				g2.fillOval(2, 2, getWidth() - 5, getHeight() - 5);
				
				// Draw subtle shadow
				g2.setColor(new Color(0, 0, 0, 20));
				g2.fillOval(3, 3, getWidth() - 5, getHeight() - 5);
				
				// Draw highlight
				g2.setColor(new Color(255, 255, 255, 60));
				g2.fillOval(2, 2, getWidth() - 5, getHeight() - 5);
				
				// Draw border
				g2.setColor(KitchenTheme.WARM_ORANGE);
				g2.setStroke(new BasicStroke(2));
				g2.drawOval(2, 2, getWidth() - 5, getHeight() - 5);
				
				// Draw chef icon in the center if available
				if (finalChefIcon != null) {
					int iconX = (getWidth() - finalChefIcon.getIconWidth()) / 2;
					int iconY = (getHeight() - finalChefIcon.getIconHeight()) / 2;
					g2.drawImage(finalChefIcon.getImage(), iconX, iconY, null);
				}
				
				super.paintComponent(g);
			}
		};

		avatarButton.setContentAreaFilled(false);
		avatarButton.setBorderPainted(false);
		avatarButton.setFocusPainted(false);
		avatarButton.setPreferredSize(new Dimension(55, 55));
		avatarButton.setMaximumSize(new Dimension(55, 55));
		avatarButton.setMinimumSize(new Dimension(55, 55));
		
		// Create dropdown menu with modern styling
		JPopupMenu dropdownMenu = new JPopupMenu() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				// Draw menu background with shadow
				g2.setColor(new Color(0, 0, 0, 10));
				g2.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 12, 12);
				
				g2.setColor(Color.WHITE);
				g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 12, 12);
				
				// Draw border
				g2.setColor(new Color(220, 220, 220));
				g2.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 12, 12);
				
				super.paintComponent(g);
			}
		};
		dropdownMenu.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
		
		// Notifiche menu item
		JMenuItem notificheItem = createStyledMenuItem("🔔 Notifiche", e -> {
			if (admin != null) {
				mainApp.showNotificheChef(admin);
			} else {
				JOptionPane.showMessageDialog(mainApp, "Errore: dati chef non disponibili", "Errore", JOptionPane.ERROR_MESSAGE);
			}
		});
		dropdownMenu.add(notificheItem);
		
		// Report mensile menu item
		JMenuItem reportItem = createStyledMenuItem("📊 Report Mensile", e -> {
			if (admin != null) {
				programma.showReportMensile(admin.getCodiceFiscale());
			} else {
				JOptionPane.showMessageDialog(mainApp, "Errore: dati chef non disponibili", "Errore", JOptionPane.ERROR_MESSAGE);
			}
		});
		dropdownMenu.add(reportItem);
		
		// Informazioni menu item
		JMenuItem infoItem = createStyledMenuItem("ℹ️ Informazioni", e -> {
			showChefInfoDialog();
		});
		dropdownMenu.add(infoItem);
		
		// Separator
		dropdownMenu.addSeparator();
		
		// Logout menu item
		JMenuItem logoutItem = createStyledMenuItem("🚪 Logout", e -> {
			programma.logout();
		});
		dropdownMenu.add(logoutItem);
		
		// Add action listener to avatar button
		avatarButton.addActionListener(e -> {
			dropdownMenu.show(avatarButton, 0, avatarButton.getHeight());
		});
		
		avatarPanel.add(avatarButton);
		return avatarPanel;
	}
	
	private JMenuItem createStyledMenuItem(String text, java.awt.event.ActionListener action) {
		JMenuItem item = new JMenuItem(text) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				if (getModel().isArmed()) {
					// Selected state
					g2.setColor(KitchenTheme.WARM_ORANGE);
					g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
					g2.setColor(Color.WHITE);
					g2.setFont(getFont());
					FontMetrics fm = g2.getFontMetrics();
					int x = (getWidth() - fm.stringWidth(getText())) / 2;
					int y = (getHeight() + fm.getAscent()) / 2 - 2;
					g2.drawString(getText(), x, y);
				} else {
					super.paintComponent(g);
				}
			}
		};
		
		item.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
		item.setForeground(KitchenTheme.DARK_GRAY);
		item.setPreferredSize(new Dimension(150, 35));
		item.setHorizontalAlignment(SwingConstants.CENTER);
		item.addActionListener(action);
		
		return item;
	}
	
	private void showChefInfoDialog() {
		if (admin != null) {
			String info = String.format(
				"Informazioni Chef:\n\n" +
				"Nome: %s\n" +
				"Cognome: %s\n" +
				"Email: %s\n" +
				"Codice Fiscale: %s",
				admin.getNome(),
				admin.getCognome(),
				admin.getEmail(),
				admin.getCodiceFiscale()
			);
			JOptionPane.showMessageDialog(mainApp, info, "Informazioni Chef", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private void createWelcomeSection(JPanel mainPanel) {
		JPanel welcomePanel = KitchenTheme.createRoundedGreyPanel();
		welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
		welcomePanel.setOpaque(false);
		welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		welcomePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
		
		JLabel welcomeLabel = new JLabel("Benvenuto, " + (admin != null ? admin.getNome() : "Chef") + "!");
		welcomeLabel.setFont(new Font("SF Pro Text", Font.BOLD, 20));
		welcomeLabel.setForeground(KitchenTheme.DARK_GRAY);
		welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		welcomePanel.add(welcomeLabel);
		
		JLabel infoLabel = new JLabel("Gestisci i tuoi corsi e crea nuove esperienze culinarie");
		infoLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
		infoLabel.setForeground(KitchenTheme.LIGHT_GRAY);
		infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		welcomePanel.add(infoLabel);
		
		mainPanel.add(welcomePanel);
		mainPanel.add(Box.createVerticalStrut(30));
	}
	
	private void createCoursesSection(JPanel mainPanel) {
		//JPanel coursesPanel = new JPanel() 
		JPanel coursesPanel = KitchenTheme.createRoundedGreyPanel();
		coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
		coursesPanel.setOpaque(false);
		
		coursesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		titlePanel = new JPanel();
		titlePanel.setBackground(new Color(248, 248, 252));
		titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		JLabel coursesTitle = new JLabel("I Tuoi Corsi");
		coursesTitle.setFont(new Font("SF Pro Text", Font.BOLD, 18));
		coursesTitle.setForeground(KitchenTheme.DARK_GRAY);
		coursesTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		titlePanel.setPreferredSize(new Dimension(titlePanel.getWidth(), 80));
		titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
		titlePanel.setBackground(KitchenTheme.SOFT_GRAY);
		coursesPanel.add(Box.createVerticalStrut(15));	//Separatore verticale
		
		//PulsanteFiltro
		JButton filtroButton = KitchenTheme.createKitchenButton("🔍 Filtra Corsi");
		filtroButton.setPreferredSize(new Dimension(150, 35));
		filtroButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		filtroButton.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				FiltroDialog formFiltro = new FiltroDialog();
				formFiltro.setModal(true);
				formFiltro.setVisible(true);
				
				//Form inviato, la finestra di dialogo è chiusa
				String argomento = formFiltro.getArgomento();
				try {
					elencoCorsi = programma.searchFiltrered(admin.getCodiceFiscale(), argomento);
					// Aggiorna la sezione dei corsi
					coursesPanel.removeAll();
					coursesPanel.add(titlePanel);
					generateCards(coursesPanel);
					// Aggiorna lo schermo
					mainPanel.validate();	//Ricalcola layout
					mainPanel.repaint();
				} catch (SQLException ex) {
					elencoCorsi = null;
					ex.printStackTrace();
					JOptionPane.showMessageDialog(filtroButton, "Errore nella ricerca: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		JPanel filtroButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		filtroButtonPanel.setOpaque(false);
		filtroButtonPanel.add(filtroButton);
		
		//Aggiunta alla pannello del titolo
		titlePanel.add(coursesTitle);
		titlePanel.add(filtroButtonPanel);
		//titlePanel.add(Box.createVerticalStrut(30));
		
		coursesPanel.add(titlePanel);
		
		// Load and display courses
		generateCards(coursesPanel);

		mainPanel.add(coursesPanel);
		mainPanel.add(Box.createVerticalStrut(30));
	}
	
	private void generateCards(JPanel panel) {
		
		if (admin != null) {
			//elencoCorsi = programma.elencaCorsi(admin.getCodiceFiscale());
			if (elencoCorsi == null)	//Query fallita
			{
				JLabel errorLabel = new JLabel("Errore nel caricamento dei corsi");
				errorLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
				errorLabel.setForeground(Color.RED);
				errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				errorLabel.setVisible(true);
				panel.add(errorLabel);
			}
			else if (!elencoCorsi.isEmpty()) {	
				for (Corso corso : elencoCorsi) {
					panel.add(createCourseCard(corso));
					panel.add(Box.createVerticalStrut(10));
				}
			} else {// Lo chef non ha corsi
				JLabel noCoursesLabel = new JLabel("Nessun corso creato ancora");
				noCoursesLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
				noCoursesLabel.setForeground(KitchenTheme.LIGHT_GRAY);
				noCoursesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				panel.add(noCoursesLabel);
			}
		}
	}
	
	private JPanel createCourseCard(Corso corso) {
		JPanel cardPanel = new JPanel(new BorderLayout());
		cardPanel.setOpaque(false);
		cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
		cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
		
		JLabel courseNameLabel = new JLabel(corso.getArgomento());
		courseNameLabel.setFont(new Font("SF Pro Text", Font.BOLD, 16));
		courseNameLabel.setForeground(KitchenTheme.DARK_GRAY);
		cardPanel.add(courseNameLabel, BorderLayout.WEST);
		
		JLabel dateLabel = new JLabel("Inizio: " + corso.getDataInizio() + " - Frequenza: " + corso.getFrequenza());
		dateLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
		dateLabel.setForeground(KitchenTheme.LIGHT_GRAY);
		cardPanel.add(dateLabel, BorderLayout.EAST);
		
		return cardPanel;
	}
	
	private void createCourseCreationNavigation(JPanel mainPanel) {
		JPanel navPanel = KitchenTheme.createRoundedPanel(new Color(248, 248, 252), new Color(248, 248, 252)); 
		navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
		navPanel.setOpaque(false);
		navPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		JLabel navTitle = new JLabel("Gestione Corsi");
		navTitle.setFont(new Font("SF Pro Text", Font.BOLD, 18));
		navTitle.setForeground(KitchenTheme.DARK_GRAY);
		navTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		navPanel.add(navTitle);
		navPanel.add(Box.createVerticalStrut(15));
		
		JLabel navInfo = new JLabel("Crea nuovi corsi, sessioni e ricette o gestisci quelli esistenti");
		navInfo.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
		navInfo.setForeground(KitchenTheme.LIGHT_GRAY);
		navInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
		navPanel.add(navInfo);
		navPanel.add(Box.createVerticalStrut(20));
		
		// Navigation buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
		buttonPanel.setOpaque(false);
		
		JButton createButton = createPrimaryButton("➕ Crea nuovo corso");
		createButton.addActionListener(e -> {
			programma.showCreazioneCorsi();
		});
		buttonPanel.add(createButton);
		createButton.setPreferredSize(new Dimension(200, 35));
		
		JButton manageButton = createSecondaryButton("📚 Gestisci Corsi");
		manageButton.addActionListener(e -> {
			programma.showGestioneCorsi();
		});
		buttonPanel.add(manageButton);
		
		navPanel.add(buttonPanel);
		navPanel.add(Box.createVerticalStrut(15));
		mainPanel.add(navPanel);
		mainPanel.add(Box.createVerticalStrut(30));
	}
	
	private void createLogoutSection(JPanel mainPanel) {
		JPanel logoutPanel = new JPanel();
		logoutPanel.setLayout(new BoxLayout(logoutPanel, BoxLayout.Y_AXIS));
		logoutPanel.setOpaque(false);
		logoutPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JButton logoutButton = createSecondaryButton("Logout");
		logoutButton.addActionListener(e -> {
			programma.logout();
		});
		logoutPanel.add(logoutButton);
		
		mainPanel.add(logoutPanel);
	}
	
	
	private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(KitchenTheme.DEEP_RED);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(0, 122, 255, 200));
                } else {
                    g2.setColor(KitchenTheme.WARM_ORANGE);
                }
                
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("SF Pro Text", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        return button;
    }
    
    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(new Color(174, 174, 178, 200));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(174, 174, 178, 100));
                } else {
                    g2.setColor(KitchenTheme.LIGHT_GRAY);
                }
                
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        button.setForeground(KitchenTheme.DARK_GRAY);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 35));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        return button;
    }
    
    private void styleScrollBar(JScrollBar scrollBar) {
        scrollBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(200, 200, 200);
                this.trackColor = new Color(240, 240, 240);
                this.thumbDarkShadowColor = new Color(150, 150, 150);
                this.thumbLightShadowColor = new Color(220, 220, 220);
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            
            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });
    }

	public Utente getAdmin() {
		return admin;
	}
}
