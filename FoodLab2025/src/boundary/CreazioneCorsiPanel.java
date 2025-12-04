package boundary;

import entity.*;
import controller.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class CreazioneCorsiPanel extends JPanel {
	
	private Controller programma;
	private MainApplication mainApp;
	private Utente admin;
	private JScrollPane scrollPane;
	
	// Course type radio buttons
	private JRadioButton onlineRadio;
	private JRadioButton presenceRadio;
	private ButtonGroup courseTypeGroup;
	
	public CreazioneCorsiPanel(Controller p, MainApplication mainApp) {		
		this.programma = p;
		this.mainApp = mainApp;
		
		initializePanel();
	}
	
	public void setChef(Utente chef) {
		this.admin = chef;
		refreshDisplay();
	}
	
	private void refreshDisplay() {
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
        styleScrollBar(scrollPane.getVerticalScrollBar());
        styleScrollBar(scrollPane.getHorizontalScrollBar());
        
        // Main content panel
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw sophisticated shadow with multiple layers
                g2.setColor(new Color(0, 0, 0, 8));
                g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 30, 30);
                
                g2.setColor(new Color(0, 0, 0, 12));
                g2.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 30, 30);
                
                g2.setColor(new Color(0, 0, 0, 6));
                g2.fillRoundRect(1, 1, getWidth() - 1, getHeight() - 1, 30, 30);
                
                // Draw main panel with gradient effect
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 30, 30);
                
                // Draw subtle inner highlight
                g2.setColor(new Color(255, 255, 255, 60));
                g2.drawRoundRect(1, 1, getWidth() - 5, getHeight() - 5, 28, 28);
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        scrollPane.setViewportView(mainPanel);

		// Header section
		createHeader(mainPanel);
		
		// Welcome section
		createWelcomeSection(mainPanel);
		
		// Course creation section
		createCourseCreationSection(mainPanel);

        add(scrollPane, BorderLayout.CENTER);
	}
	
	private void createHeader(JPanel mainPanel) {
		// Header with title and back button
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setOpaque(false);
		headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
		
		JLabel titleLabel = new JLabel("Creazione Corsi");
		titleLabel.setFont(KitchenTheme.TITLE_FONT);
		titleLabel.setForeground(KitchenTheme.WARM_ORANGE);
		headerPanel.add(titleLabel, BorderLayout.WEST);
		
		JButton backButton = createSecondaryButton("← Torna all'Area Chef");
		backButton.addActionListener(e -> {
			mainApp.showAreaChef();
		});
		headerPanel.add(backButton, BorderLayout.EAST);
		
		mainPanel.add(headerPanel);
		mainPanel.add(Box.createVerticalStrut(30));
	}
	
	private void createWelcomeSection(JPanel mainPanel) {
		JPanel welcomePanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				// Draw subtle background
				g2.setColor(new Color(248, 248, 252));
				g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
			}
		};
		welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
		welcomePanel.setOpaque(false);
		welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		welcomePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
		
		JLabel welcomeLabel = new JLabel("Crea e gestisci i tuoi corsi culinari");
		welcomeLabel.setFont(new Font("SF Pro Text", Font.BOLD, 20));
		welcomeLabel.setForeground(KitchenTheme.DARK_GRAY);
		welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		welcomePanel.add(welcomeLabel);
		
		JLabel infoLabel = new JLabel("Crea corsi, sessioni, ricette e gestisci tutto il contenuto didattico");
		infoLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
		infoLabel.setForeground(KitchenTheme.LIGHT_GRAY);
		infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		welcomePanel.add(infoLabel);
		
		mainPanel.add(welcomePanel);
		mainPanel.add(Box.createVerticalStrut(30));
	}
	
	private void createCourseCreationSection(JPanel mainPanel) {
		JPanel coursePanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				// Draw gradient background
				GradientPaint gradient = new GradientPaint(0, 0, new Color(255, 255, 255), 
														  0, getHeight(), new Color(248, 252, 255));
				g2.setPaint(gradient);
				g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
				
				// Draw subtle border
				g2.setColor(new Color(225, 235, 245));
				g2.setStroke(new BasicStroke(1.5f));
				g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 23, 23);
			}
		};
		coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));
		coursePanel.setOpaque(false);
		coursePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		
		// Modern header with icon
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		headerPanel.setOpaque(false);
		
		JLabel iconLabel = new JLabel("👨‍🍳");
		iconLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 32));
		
		JLabel courseTitle = new JLabel("  Crea Nuovo Corso");
		courseTitle.setFont(new Font("SF Pro Display", Font.BOLD, 24));
		courseTitle.setForeground(KitchenTheme.WARM_BROWN);
		
		headerPanel.add(iconLabel);
		headerPanel.add(courseTitle);
		
		coursePanel.add(headerPanel);
		coursePanel.add(Box.createVerticalStrut(10));
		
		JLabel subtitleLabel = new JLabel("Compila tutti i campi per creare un nuovo corso culinario");
		subtitleLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
		subtitleLabel.setForeground(new Color(120, 120, 120));
		subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		coursePanel.add(subtitleLabel);
		coursePanel.add(Box.createVerticalStrut(25));
		
		// Create form with modern grid layout
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setOpaque(false);
		formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.setMaximumSize(new Dimension(600, 700));
		formPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50)); // Padding laterale
		
		// Course name field (full width)
		JPanel nameFieldPanel = createFormField("📚 Argomento del Corso", "Es: Cucina Italiana Avanzata");
		nameFieldPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		nameFieldPanel.setMaximumSize(new Dimension(500, 80));
		JTextField courseNameField = getTextFieldFromPanel(nameFieldPanel);
		formPanel.add(nameFieldPanel);
		formPanel.add(Box.createVerticalStrut(20));
		
		// Date selection with modern dropdowns (European format DD/MM/YYYY)
		JPanel startDatePanel = createDateSelectorPanel("📅 Data Inizio");
		startDatePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		startDatePanel.setMaximumSize(new Dimension(500, 80));
		JComboBox<String> startDayCombo = (JComboBox<String>) startDatePanel.getClientProperty("dayCombo");
		JComboBox<String> startMonthCombo = (JComboBox<String>) startDatePanel.getClientProperty("monthCombo");
		JComboBox<String> startYearCombo = (JComboBox<String>) startDatePanel.getClientProperty("yearCombo");
		formPanel.add(startDatePanel);
		formPanel.add(Box.createVerticalStrut(20));
		
//		// Date selection with modern dropdowns (European format DD/MM/YYYY)
//		JPanel endDatePanel = createDateSelectorPanel("📅 Data Fine");
//		endDatePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
//		endDatePanel.setMaximumSize(new Dimension(500, 80));
//		JComboBox<String> endDayCombo = (JComboBox<String>) endDatePanel.getClientProperty("dayCombo");
//		JComboBox<String> endMonthCombo = (JComboBox<String>) endDatePanel.getClientProperty("monthCombo");
//		JComboBox<String> endYearCombo = (JComboBox<String>) endDatePanel.getClientProperty("yearCombo");
//		formPanel.add(endDatePanel);
//		formPanel.add(Box.createVerticalStrut(20));
		
		// Two-column layout for frequency and max participants
		JPanel detailsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
		detailsPanel.setOpaque(false);
		detailsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		detailsPanel.setMaximumSize(new Dimension(500, 80));
		
		JPanel frequencyPanel = createFrequencySelector();
		frequencyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JComboBox<String> frequencyCombo = getComboBoxFromPanel(frequencyPanel);
		
		JPanel maxParticipantsPanel = createFormField("👥 Max Partecipanti", "Es: 20");
		maxParticipantsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JTextField maxParticipantsField = getTextFieldFromPanel(maxParticipantsPanel);
		
		detailsPanel.add(frequencyPanel);
		detailsPanel.add(maxParticipantsPanel);
		formPanel.add(detailsPanel);
		formPanel.add(Box.createVerticalStrut(20));
		
		// Course type selection (Online/In Presence)
		JPanel courseTypePanel = createCourseTypeSelector();
		courseTypePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.add(courseTypePanel);
		formPanel.add(Box.createVerticalStrut(25));
		
		// Description field (full width)
		JPanel descriptionPanel = createTextAreaField("📝 Descrizione", "Inserisci una descrizione dettagliata del corso...");
		descriptionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JTextArea descriptionArea = getTextAreaFromPanel(descriptionPanel);
		formPanel.add(descriptionPanel);
		formPanel.add(Box.createVerticalStrut(30));
		
		// Action buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
		buttonPanel.setOpaque(false);
		
		JButton clearButton = createSecondaryButton("🗑️ Cancella");
		clearButton.addActionListener(e -> {
			courseNameField.setText("Es: Cucina Italiana Avanzata");
			courseNameField.setForeground(Color.LIGHT_GRAY);
			// Reset date dropdowns to first day, first month, current year
			startDayCombo.setSelectedIndex(0);
			startMonthCombo.setSelectedIndex(0);
			startYearCombo.setSelectedIndex(0);
			
			
			frequencyCombo.setSelectedIndex(1); // Reset to "Settimanale"
			maxParticipantsField.setText("Es: 20");
			maxParticipantsField.setForeground(Color.LIGHT_GRAY);
			descriptionArea.setText("Inserisci una descrizione dettagliata del corso...");
			descriptionArea.setForeground(Color.LIGHT_GRAY);
			// Reset radio buttons
			clearCourseTypeSelection();
		});
		
		JButton createButton = createPrimaryButton("✨ Crea Corso");
		createButton.addActionListener(e -> {
			String nomeCorso = courseNameField.getText().trim();
			// Get start date from dropdowns and format as DD/MM/YYYY for display, YYYY-MM-DD for database
			String startDay = (String) startDayCombo.getSelectedItem();
			String startMonth = (String) startMonthCombo.getSelectedItem();
			String startYear = (String) startYearCombo.getSelectedItem();
			String dataInizioDisplay = startDay + "/" + startMonth + "/" + startYear; // European format for user
			String dataInizioDatabase = startYear + "-" + startMonth + "-" + startDay; // Database format
			
//			// Get end date from dropdowns and format as DD/MM/YYYY for display, YYYY-MM-DD for database
//			String endDay = (String) endDayCombo.getSelectedItem();
//			String endMonth = (String) endMonthCombo.getSelectedItem();
//			String endYear = (String) endYearCombo.getSelectedItem();
//			String dataFineDisplay = endDay + "/" + endMonth + "/" + endYear; // European format for user
//			String dataFineDatabase = endYear + "-" + endMonth + "-" + endDay; // Database format
			
			String frequenza = (String) frequencyCombo.getSelectedItem();
			String maxPartecipanti = maxParticipantsField.getText().trim();
			String descrizione = descriptionArea.getText().trim();
			boolean tipologia = getSelectedCourseType().equals("💻 Online");
			
			// Check for placeholders and empty fields
			if (nomeCorso.isEmpty() || nomeCorso.equals("Es: Cucina Italiana Avanzata") ||
				frequenza == null ||
				maxPartecipanti.isEmpty() || maxPartecipanti.equals("Es: 20")) {
				showStyledMessage("⚠️ Tutti i campi sono obbligatori e devono essere compilati correttamente", "Campi mancanti", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// Validate dates (check if it's a valid date)
			if (!isValidEuropeanDate(startDay, startMonth, startYear)  )
				//	|| !isValidEuropeanDate(endDay, endMonth, endYear)) 
			{
				showStyledMessage("⚠️ La data selezionata non è valida (es: 31/02/2025)", "Data non valida", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			if(descrizione == null || descrizione == "" || descrizione == "Inserisci una descrizione dettagliata del corso...")
			{
				showStyledMessage("⚠️ Campo descrizione è vuoto", "Campi non compilati", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			try {
				int maxPart = Integer.parseInt(maxPartecipanti);
				if (maxPart <= 0) {
					showStyledMessage("⚠️ Il numero massimo di partecipanti deve essere maggiore di 0", 
									"Valore non valido", JOptionPane.WARNING_MESSAGE);
					return;
				}
			} catch (NumberFormatException ex) {
				showStyledMessage("⚠️ Il numero massimo di partecipanti deve essere un numero valido", 
								"Valore non valido", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			if (admin != null) {
				try {
					// Non aggiungiamo più la tipologia al nome del corso per evitare nomi troppo lunghi
					int maxPart = Integer.parseInt(maxPartecipanti);
					
					programma.creaCorso(new Corso(nomeCorso, descrizione, dataInizioDatabase, frequenza, maxPart, tipologia, admin.getCodiceFiscale() ));
					
					// Clear fields after successful creation
					courseNameField.setText("Es: Cucina Italiana Avanzata");
					courseNameField.setForeground(Color.LIGHT_GRAY);
					// Reset dates dropdowns
					startDayCombo.setSelectedIndex(0);
					startMonthCombo.setSelectedIndex(0);
					startYearCombo.setSelectedIndex(0);
					
//					endDayCombo.setSelectedIndex(0);
//					endMonthCombo.setSelectedIndex(0);
//					endYearCombo.setSelectedIndex(0);
					
					frequencyCombo.setSelectedIndex(1); // Reset to "Settimanale"
					maxParticipantsField.setText("Es: 20");
					maxParticipantsField.setForeground(Color.LIGHT_GRAY);
					descriptionArea.setText("Inserisci una descrizione dettagliata del corso...");
					descriptionArea.setForeground(Color.LIGHT_GRAY);
					clearCourseTypeSelection();
					
					// Mostra messaggio di successo e aggiorna la lista corsi
					showStyledMessage("🎉 Corso creato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
					
					// Aggiorna automaticamente la lista dei corsi nell'area chef
					if (mainApp != null) {
						mainApp.refreshChefArea();
					}
					
					//Torna all'area personale
					programma.showAreaChef();
				} catch (Exception ex) {
					showStyledMessage("❌ Errore durante la creazione del corso: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace(); // Per debug
				}
			}
		});
		
		buttonPanel.add(clearButton);
		buttonPanel.add(createButton);
		formPanel.add(buttonPanel);
		
		coursePanel.add(formPanel);
		mainPanel.add(coursePanel);
		mainPanel.add(Box.createVerticalStrut(40));
	}
	
	
	private void styleTextField(JTextField field) {
		field.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
		field.setBorder(new AppleStyleBorder(8));
		field.setPreferredSize(new Dimension(300, 40));
		field.setMaximumSize(new Dimension(300, 40));
		field.setAlignmentX(Component.CENTER_ALIGNMENT);
		field.setOpaque(true);
		field.setBackground(new Color(255, 255, 255, 250));
		field.setForeground(new Color(30, 30, 30));
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
        button.setPreferredSize(new Dimension(200, 40));
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
        button.setPreferredSize(new Dimension(150, 35));
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
    
    // Helper methods for the modern form
    private JPanel createFormField(String labelText, String placeholder) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setOpaque(false);
        fieldPanel.setMaximumSize(new Dimension(240, 80));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SF Pro Text", Font.BOLD, 13));
        label.setForeground(KitchenTheme.WARM_BROWN);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setOpaque(false);
        inputPanel.setMaximumSize(new Dimension(240, 45));
        
        JTextField textField = new JTextField();
        textField.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        textField.setBorder(new ModernFieldBorder());
        textField.setPreferredSize(new Dimension(240, 45));
        textField.setBackground(Color.WHITE);
        textField.setForeground(KitchenTheme.DARK_GRAY);
        
        // Add placeholder functionality
        textField.setText(placeholder);
        textField.setForeground(Color.LIGHT_GRAY);
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(KitchenTheme.DARK_GRAY);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.LIGHT_GRAY);
                }
            }
        });
        
        inputPanel.add(textField, BorderLayout.CENTER);
        
        fieldPanel.add(label);
        fieldPanel.add(Box.createVerticalStrut(8));
        fieldPanel.add(inputPanel);
        
        return fieldPanel;
    }
    
    private JPanel createTextAreaField(String labelText, String placeholder) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setOpaque(false);
        fieldPanel.setMaximumSize(new Dimension(500, 120));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SF Pro Text", Font.BOLD, 13));
        label.setForeground(KitchenTheme.WARM_BROWN);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setOpaque(false);
        inputPanel.setMaximumSize(new Dimension(500, 90));
        
        JTextArea textArea = new JTextArea(4, 0);
        textArea.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(KitchenTheme.DARK_GRAY);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        // Add placeholder functionality
        textArea.setText(placeholder);
        textArea.setForeground(Color.LIGHT_GRAY);
        textArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textArea.getText().equals(placeholder)) {
                    textArea.setText("");
                    textArea.setForeground(KitchenTheme.DARK_GRAY);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (textArea.getText().isEmpty()) {
                    textArea.setText(placeholder);
                    textArea.setForeground(Color.LIGHT_GRAY);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(new ModernFieldBorder());
        scrollPane.setPreferredSize(new Dimension(500, 90));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        inputPanel.add(scrollPane, BorderLayout.CENTER);
        
        fieldPanel.add(label);
        fieldPanel.add(Box.createVerticalStrut(8));
        fieldPanel.add(inputPanel);
        
        return fieldPanel;
    }
    
    // Modern date selector with dropdowns (European format DD/MM/YYYY)
    private JPanel createDateSelectorPanel(String labelText) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setOpaque(false);
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SF Pro Text", Font.BOLD, 13));
        label.setForeground(KitchenTheme.WARM_BROWN);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        inputPanel.setOpaque(false);
        inputPanel.setBorder(new ModernFieldBorder());
        inputPanel.setMaximumSize(new Dimension(500, 45));
        inputPanel.setPreferredSize(new Dimension(500, 45));
        
        // Day dropdown
        JComboBox<String> dayCombo = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            dayCombo.addItem(String.format("%02d", i));
        }
        styleDateComboBox(dayCombo);
        
        // Month dropdown  
        JComboBox<String> monthCombo = new JComboBox<>();
        String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        for (String month : months) {
            monthCombo.addItem(month);
        }
        styleDateComboBox(monthCombo);
        
        // Year dropdown
        JComboBox<String> yearCombo = new JComboBox<>();
        int currentYear = java.time.LocalDate.now().getYear();
        for (int i = currentYear; i <= currentYear + 3; i++) {
            yearCombo.addItem(String.valueOf(i));
        }
        styleDateComboBox(yearCombo);
        
        inputPanel.add(dayCombo);
        inputPanel.add(new JLabel("/"));
        inputPanel.add(monthCombo);
        inputPanel.add(new JLabel("/"));
        inputPanel.add(yearCombo);
        
        // Store combo boxes as client properties for easy access
        fieldPanel.putClientProperty("dayCombo", dayCombo);
        fieldPanel.putClientProperty("monthCombo", monthCombo);
        fieldPanel.putClientProperty("yearCombo", yearCombo);
        
        fieldPanel.add(label);
        fieldPanel.add(Box.createVerticalStrut(8));
        fieldPanel.add(inputPanel);
        
        return fieldPanel;
    }
    
    // Style method for date combo boxes
    private void styleDateComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        comboBox.setPreferredSize(new Dimension(70, 35));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(KitchenTheme.DARK_GRAY);
        comboBox.setBorder(BorderFactory.createEmptyBorder());
    }
    
    private JPanel createCourseTypeSelector() {
        JPanel typePanel = new JPanel();
        typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.Y_AXIS));
        typePanel.setOpaque(false);
        typePanel.setMaximumSize(new Dimension(500, 90));
        
        JLabel label = new JLabel("🏠 Modalità del Corso");
        label.setFont(new Font("SF Pro Text", Font.BOLD, 13));
        label.setForeground(KitchenTheme.WARM_BROWN);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        radioPanel.setOpaque(false);
        
        courseTypeGroup = new ButtonGroup();
        
        onlineRadio = new JRadioButton("💻 Online");
        onlineRadio.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        onlineRadio.setOpaque(false);
        onlineRadio.setForeground(KitchenTheme.DARK_GRAY);
        
        presenceRadio = new JRadioButton("🏫 In Presenza");
        presenceRadio.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        presenceRadio.setOpaque(false);
        presenceRadio.setForeground(KitchenTheme.DARK_GRAY);
        presenceRadio.setSelected(true); // Default selection
        
        courseTypeGroup.add(onlineRadio);
        courseTypeGroup.add(presenceRadio);
        
        radioPanel.add(onlineRadio);
        radioPanel.add(Box.createHorizontalStrut(30));
        radioPanel.add(presenceRadio);
        
        typePanel.add(label);
        typePanel.add(Box.createVerticalStrut(8));
        typePanel.add(radioPanel);
        
        return typePanel;
    }
    
    private String getSelectedCourseType() {
        if (onlineRadio.isSelected()) {
            return "Online";
        } else if (presenceRadio.isSelected()) {
            return "In Presenza";
        }
        return "In Presenza"; // default
    }
    
    private void clearCourseTypeSelection() {
        presenceRadio.setSelected(true); // Reset to default
    }
    
    private void showStyledMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(mainApp, message, title, messageType);
    }
    
    // Date format validation method
    private boolean isValidDateFormat(String date) {
        try {
            // Check if the date matches YYYY-MM-DD pattern
            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return false;
            }
            // Try to parse it as a valid date
            java.sql.Date.valueOf(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // Validate European date format (DD/MM/YYYY)
    private boolean isValidEuropeanDate(String day, String month, String year) {
        try {
            int d = Integer.parseInt(day);
            int m = Integer.parseInt(month);
            int y = Integer.parseInt(year);
            
            // Basic range checks
            if (d < 1 || d > 31 || m < 1 || m > 12 || y < 2024) {
                return false;
            }
            
            // Create a proper date string and validate
            String dateString = year + "-" + month + "-" + day;
            java.sql.Date.valueOf(dateString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // Helper methods to safely get components from panels
    private JTextField getTextFieldFromPanel(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel innerPanel = (JPanel) comp;
                for (Component innerComp : innerPanel.getComponents()) {
                    if (innerComp instanceof JTextField) {
                        return (JTextField) innerComp;
                    }
                }
            }
        }
        return null;
    }
    
    private JComboBox<String> getComboBoxFromPanel(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel innerPanel = (JPanel) comp;
                for (Component innerComp : innerPanel.getComponents()) {
                    if (innerComp instanceof JComboBox) {
                        return (JComboBox<String>) innerComp;
                    }
                }
            }
        }
        return null;
    }
    
    private JTextArea getTextAreaFromPanel(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel innerPanel = (JPanel) comp;
                for (Component innerComp : innerPanel.getComponents()) {
                    if (innerComp instanceof JScrollPane) {
                        JScrollPane scrollPane = (JScrollPane) innerComp;
                        Component view = scrollPane.getViewport().getView();
                        if (view instanceof JTextArea) {
                            return (JTextArea) view;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    private JPanel createFrequencySelector() {
        JPanel frequencyPanel = new JPanel();
        frequencyPanel.setLayout(new BoxLayout(frequencyPanel, BoxLayout.Y_AXIS));
        frequencyPanel.setOpaque(false);
        frequencyPanel.setMaximumSize(new Dimension(240, 80));
        
        JLabel label = new JLabel("🔄 Frequenza");
        label.setFont(new Font("SF Pro Text", Font.BOLD, 13));
        label.setForeground(KitchenTheme.WARM_BROWN);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setOpaque(false);
        inputPanel.setMaximumSize(new Dimension(240, 45));
        
        String[] frequencies = {"Giornaliera", "Settimanale", "Mensile"};
        JComboBox<String> comboBox = new JComboBox<>(frequencies);
        comboBox.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        comboBox.setBorder(new ModernFieldBorder());
        comboBox.setPreferredSize(new Dimension(240, 45));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(KitchenTheme.DARK_GRAY);
        comboBox.setSelectedIndex(1); // Default to "Settimanale"
        
        // Style the combo box
        comboBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("▼");
                button.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
                button.setForeground(KitchenTheme.WARM_ORANGE);
                button.setBackground(Color.WHITE);
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setContentAreaFilled(false);
                return button;
            }
        });
        
        inputPanel.add(comboBox, BorderLayout.CENTER);
        
        frequencyPanel.add(label);
        frequencyPanel.add(Box.createVerticalStrut(8));
        frequencyPanel.add(inputPanel);
        
        return frequencyPanel;
    }
    
    // Modern field border
    static class ModernFieldBorder implements javax.swing.border.Border {
        
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw subtle shadow
            g2.setColor(new Color(0, 0, 0, 15));
            g2.drawRoundRect(x + 1, y + 1, width - 3, height - 3, 12, 12);
            
            // Draw main border
            if (c.hasFocus()) {
                g2.setColor(KitchenTheme.WARM_ORANGE);
                g2.setStroke(new BasicStroke(2f));
            } else {
                g2.setColor(new Color(220, 220, 220));
                g2.setStroke(new BasicStroke(1f));
            }
            g2.drawRoundRect(x, y, width - 1, height - 1, 12, 12);
            
            // Draw inner highlight
            g2.setColor(new Color(255, 255, 255, 100));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(x + 1, y + 1, width - 3, height - 3, 10, 10);
        }
        
        public Insets getBorderInsets(Component c) {
            return new Insets(12, 15, 12, 15);
        }
        
        public boolean isBorderOpaque() {
            return false;
        }
    }
}

