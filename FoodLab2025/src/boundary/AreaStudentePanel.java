package boundary;

import entity.*;
import controller.*;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class AreaStudentePanel extends JPanel {
    
    private Controller programma;
    private MainApplication mainApp;
    private Utente studente;
    private JScrollPane scrollPane;
    private ArrayList<Corso> corsiIscritto;
    public AreaStudentePanel(Controller p, MainApplication mainApp) {
        this.programma = p;
        this.mainApp = mainApp;
        
        initializePanel();
    }
    
    public void setStudente(Utente studente) {
        this.studente = studente;
        refreshContent();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(KitchenTheme.SOFT_GRAY);
        
        // Header with welcome message and avatar
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content with courses
        JPanel mainContentPanel = createMainContentPanel();
        scrollPane = new JScrollPane(mainContentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, KitchenTheme.WARM_ORANGE,
                    0, getHeight(), KitchenTheme.SOFT_GRAY
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 120));
        
        // Welcome section
        JPanel welcomePanel = new JPanel();
        welcomePanel.setOpaque(false);
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        String welcomeText = studente != null ? 
            getWelcomeMessage(studente.getNome()) : 
            "Dashboard Studente";
            
        JLabel welcomeLabel = new JLabel(welcomeText);
        welcomeLabel.setFont(new Font("SF Pro Display", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("I tuoi corsi di cucina e le ultime novità");
        subtitleLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createVerticalStrut(5));
        welcomePanel.add(subtitleLabel);
        
        headerPanel.add(welcomePanel, BorderLayout.CENTER);
        
        // Add avatar dropdown to the right
        JPanel avatarPanel = createAvatarDropdown();
        headerPanel.add(avatarPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createAvatarDropdown() {
        JPanel avatarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        avatarPanel.setOpaque(false);
        avatarPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Load student icon
        ImageIcon studentIcon = null;
        try {
            studentIcon = new ImageIcon(getClass().getResource("/icons/iconastudent.png"));
            // Scale the icon to fit the button
            Image img = studentIcon.getImage();
            Image scaledImg = img.getScaledInstance(35, 35, Image.SCALE_SMOOTH);
            studentIcon = new ImageIcon(scaledImg);
        } catch (Exception e) {
            System.out.println("Errore caricamento icona studente: " + e.getMessage());
        }
        
        // Create avatar button with student icon
        final ImageIcon finalStudentIcon = studentIcon;
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
                
                // Draw student icon in the center if available
                if (finalStudentIcon != null) {
                    int iconX = (getWidth() - finalStudentIcon.getIconWidth()) / 2;
                    int iconY = (getHeight() - finalStudentIcon.getIconHeight()) / 2;
                    g2.drawImage(finalStudentIcon.getImage(), iconX, iconY, null);
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
        
        // Profile menu item
        JMenuItem profileItem = createStyledMenuItem("👤 Il Mio Profilo", e -> {
            showStudentProfileDialog();
        });
        dropdownMenu.add(profileItem);
        
        // Notifications menu item
        JMenuItem notificationsItem = createStyledMenuItem("🔔 Notifiche", e -> {
            showNotificationsDialog();
        });
        dropdownMenu.add(notificationsItem);
        
        // Certificates menu item
        JMenuItem certificatesItem = createStyledMenuItem("🏆 I Miei Certificati", e -> {
            JOptionPane.showMessageDialog(mainApp, "Sezione certificati in sviluppo!", "Certificati", JOptionPane.INFORMATION_MESSAGE);
        });
        dropdownMenu.add(certificatesItem);
        
        // Preferences menu item
        JMenuItem preferencesItem = createStyledMenuItem("⚙️ Preferenze", e -> {
            JOptionPane.showMessageDialog(mainApp, "Sezione preferenze in sviluppo!", "Preferenze", JOptionPane.INFORMATION_MESSAGE);
        });
        dropdownMenu.add(preferencesItem);
        
        // Separator
        dropdownMenu.addSeparator();
        
        // Logout menu item
        JMenuItem logoutItem = createStyledMenuItem("🚪 Logout", e -> {
            mainApp.showPaginaIniziale();
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
                    int x = 15;
                    int y = (getHeight() + fm.getAscent()) / 2 - 2;
                    g2.drawString(text, x, y);
                } else {
                    super.paintComponent(g);
                }
            }
        };
        
        item.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        item.setForeground(KitchenTheme.DARK_GRAY);
        item.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        item.setOpaque(false);
        item.addActionListener(action);
        
        return item;
    }
    
    private void showStudentProfileDialog() {
        if (studente == null) return;
        
        String profileInfo = String.format(
            "📝 PROFILO STUDENTE\n\n" +
            "👤 Nome: %s %s\n" +
            "📧 Email: %s\n" +
            "🆔 Username: %s\n" +
            "📅 Data Nascita: %s\n",
            studente.getNome(),
            studente.getCognome(),
            studente.getEmail(),
            studente.getUsername(),
            studente.getDataNascita()
        );
        
        JOptionPane.showMessageDialog(mainApp, profileInfo, "Il Mio Profilo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showNotificationsDialog() {
        if (studente == null) return;
        
        try {
            ArrayList<Avviso> avvisi = programma.getAvvisiByUtente(studente.getCodiceFiscale());
            
            JDialog dialog = new JDialog(mainApp, "🔔 Le Tue Notifiche", true);
            dialog.setSize(500, 400);
            dialog.setLocationRelativeTo(mainApp);
            dialog.setLayout(new BorderLayout());
            
            // Header panel
            JPanel headerPanel = new JPanel();
            headerPanel.setBackground(KitchenTheme.WARM_ORANGE);
            headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            
            JLabel titleLabel = new JLabel("🔔 Le Tue Notifiche");
            titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 18));
            titleLabel.setForeground(Color.WHITE);
            headerPanel.add(titleLabel);
            
            dialog.add(headerPanel, BorderLayout.NORTH);
            
            // Content panel
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(KitchenTheme.SOFT_GRAY);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            if (avvisi.isEmpty()) {
                JLabel emptyLabel = new JLabel("📭 Nessuna notifica presente");
                emptyLabel.setFont(new Font("SF Pro Text", Font.ITALIC, 16));
                emptyLabel.setForeground(KitchenTheme.WARM_BROWN);
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                contentPanel.add(Box.createVerticalGlue());
                contentPanel.add(emptyLabel);
                contentPanel.add(Box.createVerticalGlue());
            } else {
                for (Avviso avviso : avvisi) {
                    JPanel avvisoPanel = createAvvisoPanel(avviso);
                    contentPanel.add(avvisoPanel);
                    contentPanel.add(Box.createVerticalStrut(10));
                }
            }
            
            JScrollPane scrollPane = new JScrollPane(contentPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setBorder(null);
            dialog.add(scrollPane, BorderLayout.CENTER);
            
            // Button panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(KitchenTheme.SOFT_GRAY);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
            
            JButton closeButton = KitchenTheme.createKitchenButton("Chiudi");
            closeButton.addActionListener(e -> dialog.dispose());
            buttonPanel.add(closeButton);
            
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            dialog.setVisible(true);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainApp, 
                "Errore nel caricamento delle notifiche: " + e.getMessage(), 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JPanel createAvvisoPanel(Avviso avviso) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(avviso.isLetto() ? Color.WHITE : new Color(255, 248, 220)); // Sfondo diverso per non letti
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(avviso.isLetto() ? KitchenTheme.WARM_BROWN : KitchenTheme.WARM_ORANGE, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        // Icona tipo avviso
        String icona = "📢";
        if ("CORSO_DISPONIBILE".equals(avviso.getTipoAvviso())) {
            icona = "🎯";
        }
        
        JLabel iconLabel = new JLabel(icona);
        iconLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 20));
        panel.add(iconLabel, BorderLayout.WEST);
        
        // Contenuto messaggio
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setOpaque(false);
        messagePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        JLabel messageLabel = new JLabel("<html>" + avviso.getMessaggio() + "</html>");
        messageLabel.setFont(new Font("SF Pro Text", avviso.isLetto() ? Font.PLAIN : Font.BOLD, 13));
        messageLabel.setForeground(KitchenTheme.DARK_GRAY);
        
        JLabel dateLabel = new JLabel(avviso.getDataCreazione().substring(0, 16)); // Solo data e ora
        dateLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 11));
        dateLabel.setForeground(KitchenTheme.WARM_BROWN);
        
        messagePanel.add(messageLabel);
        messagePanel.add(Box.createVerticalStrut(3));
        messagePanel.add(dateLabel);
        
        panel.add(messagePanel, BorderLayout.CENTER);
        
        // Pulsante segna come letto (solo se non letto)
        if (!avviso.isLetto()) {
            JButton readButton = new JButton("✓");
            readButton.setFont(new Font("SF Pro Text", Font.BOLD, 12));
            readButton.setForeground(KitchenTheme.WARM_ORANGE);
            readButton.setBackground(Color.WHITE);
            readButton.setBorder(BorderFactory.createLineBorder(KitchenTheme.WARM_ORANGE, 1));
            readButton.setPreferredSize(new Dimension(30, 30));
            readButton.setToolTipText("Segna come letto");
            readButton.addActionListener(e -> {
                try {
                    programma.segnaAvvisoComeLetto(avviso.getIdAvviso());
                    // Refresh del dialog
                    showNotificationsDialog();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(mainApp, 
                        "Errore nell'aggiornamento: " + ex.getMessage(), 
                        "Errore", 
                        JOptionPane.ERROR_MESSAGE);
                }
            });
            panel.add(readButton, BorderLayout.EAST);
        }
        
        return panel;
    }

    private JPanel createMainContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(KitchenTheme.SOFT_GRAY);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        
        // My Courses Section
        JLabel coursesTitle = new JLabel("I Miei Corsi");
        coursesTitle.setFont(new Font("SF Pro Display", Font.BOLD, 24));
        coursesTitle.setForeground(KitchenTheme.DARK_GRAY);
        coursesTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(coursesTitle);
        
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Courses Grid
        JPanel coursesGrid = createCoursesGrid();
        contentPanel.add(coursesGrid);
        
        contentPanel.add(Box.createVerticalStrut(20));
        
        // New Course Enrollment Section
        JPanel enrollmentPanel = createEnrollmentSection();
        contentPanel.add(enrollmentPanel);
        
        contentPanel.add(Box.createVerticalStrut(40));
        
        // News and Announcements Section
        JLabel newsTitle = new JLabel("Novità e Avvisi");
        newsTitle.setFont(new Font("SF Pro Display", Font.BOLD, 24));
        newsTitle.setForeground(KitchenTheme.DARK_GRAY);
        newsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(newsTitle);
        
        contentPanel.add(Box.createVerticalStrut(20));
        
        // News Panel
        JPanel newsPanel = createNewsPanel();
        contentPanel.add(newsPanel);
        
        return contentPanel;
    }
    
    private JPanel createCoursesGrid() {
        JPanel gridContainer = new JPanel();
        gridContainer.setLayout(new BoxLayout(gridContainer, BoxLayout.Y_AXIS));
        gridContainer.setOpaque(false);
        gridContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        if (studente == null) {
            JLabel noCoursesLabel = new JLabel("Effettua il login per vedere i tuoi corsi");
            noCoursesLabel.setFont(new Font("SF Pro Text", Font.ITALIC, 16));
            noCoursesLabel.setForeground(KitchenTheme.WARM_BROWN);
            gridContainer.add(noCoursesLabel);
            return gridContainer;
        }
        
   
        try {
    		corsiIscritto = programma.getCorsiIscritto(studente.getCodiceFiscale());
        
	        if (corsiIscritto.isEmpty()) {
	            JLabel noCoursesLabel = new JLabel("Non sei ancora iscritto a nessun corso");
	            noCoursesLabel.setFont(new Font("SF Pro Text", Font.ITALIC, 16));
	            noCoursesLabel.setForeground(KitchenTheme.WARM_BROWN);
	            gridContainer.add(noCoursesLabel);
	            return gridContainer;
	        }
	        
	        // Create rows of 3 courses each
	        JPanel currentRow = null;
	        for (int i = 0; i < corsiIscritto.size(); i++) {
	            if (i % 3 == 0) {
	                currentRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
	                currentRow.setOpaque(false);
	                currentRow.setAlignmentX(Component.LEFT_ALIGNMENT);
	                gridContainer.add(currentRow);
	                if (i > 0) {
	                    gridContainer.add(Box.createVerticalStrut(20));
	                }
	            }
	            
	            JPanel courseCard = createCourseCard(corsiIscritto.get(i));
	            currentRow.add(courseCard);
	        }
        
	    } catch (SQLException e) {
	        JLabel errorLabel = new JLabel("Errore nel caricamento dei corsi: " + e.getMessage());
	        errorLabel.setFont(new Font("SF Pro Text", Font.ITALIC, 16));
	        errorLabel.setForeground(Color.RED);
	        gridContainer.add(errorLabel);
	    }
        
        return gridContainer;
    }
    
    private JPanel createCourseCard(Corso corso) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Card shadow
                g2.setColor(new Color(0, 0, 0, 8));
                g2.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 15, 15);
                
                // Card background
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 15, 15);
                
                // Hover effect
                if (getMousePosition() != null) {
                    g2.setColor(new Color(KitchenTheme.WARM_ORANGE.getRed(), 
                                        KitchenTheme.WARM_ORANGE.getGreen(), 
                                        KitchenTheme.WARM_ORANGE.getBlue(), 20));
                    g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 15, 15);
                }
            }
        };
        
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(280, 180));
        card.setMaximumSize(new Dimension(280, 180));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setOpaque(false);
        
        // Course info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(corso.getArgomento());
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 16));
        titleLabel.setForeground(KitchenTheme.DARK_GRAY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel idLabel = new JLabel("ID: " + corso.getIdCorso());
        idLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
        idLabel.setForeground(KitchenTheme.WARM_BROWN);
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel dateLabel = new JLabel("Inizio: " + corso.getDataInizio());
        dateLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
        dateLabel.setForeground(KitchenTheme.WARM_BROWN);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel frequencyLabel = new JLabel("Frequenza: " + corso.getFrequenza());
        frequencyLabel.setFont(new Font("SF Pro Text", Font.BOLD, 12));
        frequencyLabel.setForeground(KitchenTheme.WARM_ORANGE);
        frequencyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(idLabel);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(dateLabel);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(frequencyLabel);
        
        card.add(infoPanel, BorderLayout.CENTER);
        
        // Aggiungi listener per aprire i dettagli del corso
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showCourseDetailsDialog(corso);
            }
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(255, 248, 220));
                card.repaint();
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.repaint();
            }
        });
        
        return card;
    }
    
    private void showCourseDetailsDialog(Corso corso) {
        if (studente == null || corso == null) return;
        
        JDialog dialog = new JDialog(mainApp, "📚 Dettagli Corso", true);
        dialog.setSize(700, 600);
        dialog.setLocationRelativeTo(mainApp);
        dialog.setLayout(new BorderLayout());
        
        // Header panel con gradiente
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, KitchenTheme.WARM_ORANGE, 
                                                         0, getHeight(), KitchenTheme.DEEP_RED);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        headerPanel.setPreferredSize(new Dimension(0, 100));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("📚 " + corso.getArgomento());
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel idLabel = new JLabel("ID: " + corso.getIdCorso());
        idLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        idLabel.setForeground(new Color(255, 255, 255, 180));
        
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(idLabel, BorderLayout.SOUTH);
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        dialog.add(headerPanel, BorderLayout.NORTH);
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(KitchenTheme.SOFT_GRAY);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        // Informazioni base del corso
        JPanel infoPanel = createCourseInfoPanel(corso);
        contentPanel.add(infoPanel);
        
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Sezioni e ricette (se disponibili)
        try {
            ArrayList<Sessione> sessioni = programma.findSessioniByCorso(corso.getIdCorso());
            JPanel sessionPanel = createSessionsPanel(sessioni);
            contentPanel.add(sessionPanel);
        } catch (SQLException e) {
            JLabel errorLabel = new JLabel("⚠️ Impossibile caricare le sessioni del corso");
            errorLabel.setFont(new Font("SF Pro Text", Font.ITALIC, 14));
            errorLabel.setForeground(Color.RED);
            contentPanel.add(errorLabel);
        }
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        dialog.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(KitchenTheme.SOFT_GRAY);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        
        JButton closeButton = KitchenTheme.createKitchenButton("Chiudi");
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);
        
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private JPanel createCourseInfoPanel(Corso corso) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        // Titolo sezione
        JLabel sectionTitle = new JLabel("ℹ️ Informazioni del Corso");
        sectionTitle.setFont(new Font("SF Pro Display", Font.BOLD, 18));
        sectionTitle.setForeground(KitchenTheme.DARK_GRAY);
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(sectionTitle, BorderLayout.NORTH);
        
        // Griglia delle informazioni
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 30, 20));
        gridPanel.setBackground(Color.WHITE);
        
        try {
            // Informazione Data Inizio
            JPanel datePanel = createInfoCard("📅", "Data Inizio", corso.getDataInizio());
            gridPanel.add(datePanel);
            
            // Informazione Frequenza
            JPanel frequencyPanel = createInfoCard("🔄", "Frequenza", corso.getFrequenza());
            gridPanel.add(frequencyPanel);
            
            // Informazione Chef
            String chefName = "Chef ID " + corso.getIdChef(); // Placeholder per ora
            JPanel chefPanel = createInfoCard("👨‍🍳", "Chef Responsabile", chefName);
            gridPanel.add(chefPanel);
            
            // Informazione Iscritti
            int numeroIscritti =programma.contaIscrittiCorso(corso.getIdCorso());
            JPanel enrolledPanel = createInfoCard("👥", "Studenti Iscritti", numeroIscritti + " persone");
            gridPanel.add(enrolledPanel);
            
        } catch (SQLException e) {
            JPanel errorPanel = createInfoCard("⚠️", "Errore", "Dati non disponibili");
            gridPanel.add(errorPanel);
        }
        
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        return mainPanel;
    }
    
    private JPanel createInfoCard(String icon, String title, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(248, 249, 250));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 24));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SF Pro Text", Font.BOLD, 12));
        titleLabel.setForeground(KitchenTheme.WARM_BROWN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel("<html><center>" + value + "</center></html>");
        valueLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        valueLabel.setForeground(KitchenTheme.DARK_GRAY);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(valueLabel);
        
        return card;
    }
    
    private JPanel createSessionsPanel(ArrayList<Sessione> sessioni) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        // Titolo sezione
        JLabel sectionTitle = new JLabel("📅 Sessioni del Corso");
        sectionTitle.setFont(new Font("SF Pro Display", Font.BOLD, 18));
        sectionTitle.setForeground(KitchenTheme.DARK_GRAY);
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(sectionTitle, BorderLayout.NORTH);
        
        if (sessioni.isEmpty()) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setBackground(new Color(248, 249, 250));
            emptyPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
            ));
            emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
            
            JLabel emptyIcon = new JLabel("📭");
            emptyIcon.setFont(new Font("SF Pro Text", Font.PLAIN, 48));
            emptyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel emptyLabel = new JLabel("Nessuna sessione programmata");
            emptyLabel.setFont(new Font("SF Pro Text", Font.BOLD, 16));
            emptyLabel.setForeground(KitchenTheme.WARM_BROWN);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel emptyDesc = new JLabel("Le sessioni verranno aggiunte dal chef responsabile");
            emptyDesc.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
            emptyDesc.setForeground(new Color(120, 120, 120));
            emptyDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            emptyPanel.add(emptyIcon);
            emptyPanel.add(Box.createVerticalStrut(10));
            emptyPanel.add(emptyLabel);
            emptyPanel.add(Box.createVerticalStrut(5));
            emptyPanel.add(emptyDesc);
            
            mainPanel.add(emptyPanel, BorderLayout.CENTER);
        } else {
            // Lista sessioni organizzata
            JPanel sessionsContainer = new JPanel();
            sessionsContainer.setLayout(new BoxLayout(sessionsContainer, BoxLayout.Y_AXIS));
            sessionsContainer.setBackground(Color.WHITE);
            
            for (int i = 0; i < sessioni.size(); i++) {
                Sessione sessione = sessioni.get(i);
                JPanel sessionCard = createSessionCard(sessione, i + 1);
                sessionsContainer.add(sessionCard);
                
                if (i < sessioni.size() - 1) {
                    sessionsContainer.add(Box.createVerticalStrut(15));
                }
            }
            
            mainPanel.add(sessionsContainer, BorderLayout.CENTER);
        }
        
        return mainPanel;
    }
    
    private JPanel createSessionCard(Sessione sessione, int numero) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(248, 249, 250));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Numero sessione
        JLabel numeroLabel = new JLabel("Sessione " + numero);
        numeroLabel.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        numeroLabel.setForeground(KitchenTheme.WARM_ORANGE);
        
        // Informazioni sessione
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel dataLabel = new JLabel("📅 " + sessione.getData());
        dataLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 13));
        dataLabel.setForeground(KitchenTheme.DARK_GRAY);
        
        JLabel oraLabel = new JLabel("🕐 " + sessione.getOraInizio());
        oraLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 13));
        oraLabel.setForeground(KitchenTheme.DARK_GRAY);
        
        JLabel modalitaLabel = new JLabel(sessione.isOnline() ? "💻 Online" : "🏫 In Presenza");
        modalitaLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 13));
        modalitaLabel.setForeground(sessione.isOnline() ? new Color(34, 139, 34) : KitchenTheme.WARM_BROWN);
        
        infoPanel.add(dataLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(oraLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(modalitaLabel);
        
        card.add(numeroLabel, BorderLayout.NORTH);
        card.add(Box.createVerticalStrut(8));
        card.add(infoPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createNewsPanel() {
        JPanel newsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Panel background
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Shadow
                g2.setColor(new Color(0, 0, 0, 5));
                g2.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 15, 15);
            }
        };
        
        newsPanel.setLayout(new BoxLayout(newsPanel, BoxLayout.Y_AXIS));
        newsPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        newsPanel.setOpaque(false);
        newsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        newsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        
        JLabel newsLabel = new JLabel("📢 Nessun avviso al momento");
        newsLabel.setFont(new Font("SF Pro Text", Font.ITALIC, 16));
        newsLabel.setForeground(KitchenTheme.WARM_BROWN);
        newsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel infoLabel = new JLabel("Gli avvisi dei tuoi chef appariranno qui");
        infoLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        infoLabel.setForeground(KitchenTheme.WARM_BROWN);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        newsPanel.add(Box.createVerticalGlue());
        newsPanel.add(newsLabel);
        newsPanel.add(Box.createVerticalStrut(10));
        newsPanel.add(infoLabel);
        newsPanel.add(Box.createVerticalGlue());
        
        return newsPanel;
    }
    
    private JPanel createEnrollmentSection() {
        JPanel enrollmentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, KitchenTheme.WARM_ORANGE,
                    0, getHeight(), new Color(255, 204, 153)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Shadow
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 15, 15);
            }
        };
        
        enrollmentPanel.setLayout(new BoxLayout(enrollmentPanel, BoxLayout.Y_AXIS));
        enrollmentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        enrollmentPanel.setOpaque(false);
        enrollmentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        enrollmentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        JLabel titleLabel = new JLabel("🎯 Scopri Nuovi Corsi");
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Esplora tutti i corsi disponibili e iscriviti a quelli che preferisci");
        subtitleLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton enrollButton = new JButton("Esplora Corsi") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(new Color(245, 245, 245));
                } else if (getModel().isRollover()) {
                    g2.setColor(Color.WHITE);
                } else {
                    g2.setColor(new Color(250, 250, 250));
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        
        enrollButton.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        enrollButton.setForeground(KitchenTheme.WARM_ORANGE);
        enrollButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        enrollButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        enrollButton.setFocusPainted(false);
        enrollButton.setContentAreaFilled(false);
        enrollButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        enrollButton.addActionListener(e -> {
            showAvailableCoursesDialog();
        });
        
        enrollmentPanel.add(titleLabel);
        enrollmentPanel.add(Box.createVerticalStrut(8));
        enrollmentPanel.add(subtitleLabel);
        enrollmentPanel.add(Box.createVerticalStrut(15));
        enrollmentPanel.add(enrollButton);
        
        return enrollmentPanel;
    }
    
    private void showAvailableCoursesDialog() {
        if (studente == null) return;
        
        try {
            ArrayList<Corso> tuttiCorsi = programma.getAllCorsi();
            
            JDialog dialog = new JDialog(mainApp, "🎯 Corsi Disponibili", true);
            dialog.setSize(900, 700);
            dialog.setLocationRelativeTo(mainApp);
            dialog.setLayout(new BorderLayout());
            
            // Header panel con gradiente
            JPanel headerPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    GradientPaint gradient = new GradientPaint(0, 0, KitchenTheme.WARM_ORANGE, 
                                                             0, getHeight(), new Color(255, 100, 50));
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            headerPanel.setLayout(new BorderLayout());
            headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
            headerPanel.setPreferredSize(new Dimension(0, 100));
            
            JPanel titlePanel = new JPanel();
            titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
            titlePanel.setOpaque(false);
            
            JLabel titleLabel = new JLabel("🎯 Esplora Tutti i Corsi");
            titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 24));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel subtitleLabel = new JLabel("Scopri e iscriviti ai corsi che ti interessano di più");
            subtitleLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
            subtitleLabel.setForeground(new Color(255, 255, 255, 180));
            subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            titlePanel.add(titleLabel);
            titlePanel.add(Box.createVerticalStrut(5));
            titlePanel.add(subtitleLabel);
            headerPanel.add(titlePanel, BorderLayout.WEST);
            
            dialog.add(headerPanel, BorderLayout.NORTH);
            
            // Content panel
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(KitchenTheme.SOFT_GRAY);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            if (tuttiCorsi.isEmpty()) {
                JLabel emptyLabel = new JLabel("📭 Nessun corso disponibile al momento");
                emptyLabel.setFont(new Font("SF Pro Text", Font.ITALIC, 16));
                emptyLabel.setForeground(KitchenTheme.WARM_BROWN);
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                contentPanel.add(Box.createVerticalGlue());
                contentPanel.add(emptyLabel);
                contentPanel.add(Box.createVerticalGlue());
            } else {
                for (Corso corso : tuttiCorsi) {
                    JPanel coursePanel = createAvailableCoursePanel(corso, dialog);
                    contentPanel.add(coursePanel);
                    contentPanel.add(Box.createVerticalStrut(15));
                }
            }
            
            JScrollPane scrollPane = new JScrollPane(contentPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);	//Sensibilita rotellina
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.getHorizontalScrollBar().setUnitIncrement(16); //Sensibilita rotellina
            scrollPane.setBorder(null);
            dialog.add(scrollPane, BorderLayout.CENTER);
            
            // Button panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(KitchenTheme.SOFT_GRAY);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
            
            JButton closeButton = KitchenTheme.createKitchenButton("Chiudi");
            closeButton.addActionListener(e -> dialog.dispose());
            buttonPanel.add(closeButton);
            
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            dialog.setVisible(true);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainApp, 
                "Errore nel caricamento dei corsi: " + e.getMessage(), 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JPanel createEmptyCoursesPanel() {
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(Color.WHITE);
        emptyPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(50, 50, 50, 50)
        ));
        emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
        
        JLabel emptyIcon = new JLabel("📚");
        emptyIcon.setFont(new Font("SF Pro Text", Font.PLAIN, 64));
        emptyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel emptyTitle = new JLabel("Nessun corso disponibile");
        emptyTitle.setFont(new Font("SF Pro Display", Font.BOLD, 20));
        emptyTitle.setForeground(KitchenTheme.WARM_BROWN);
        emptyTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel emptyDesc = new JLabel("I corsi verranno aggiunti presto dai nostri chef");
        emptyDesc.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        emptyDesc.setForeground(new Color(120, 120, 120));
        emptyDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        emptyPanel.add(emptyIcon);
        emptyPanel.add(Box.createVerticalStrut(15));
        emptyPanel.add(emptyTitle);
        emptyPanel.add(Box.createVerticalStrut(8));
        emptyPanel.add(emptyDesc);
        
        return emptyPanel;
    }
    
    private JPanel createModernCourseCard(Corso corso) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setPreferredSize(new Dimension(400, 200));
        
        // Header del corso
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(corso.getArgomento());
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 16));
        titleLabel.setForeground(KitchenTheme.DARK_GRAY);
        
        JLabel idBadge = new JLabel("ID: " + corso.getIdCorso());
        idBadge.setFont(new Font("SF Pro Text", Font.PLAIN, 11));
        idBadge.setForeground(Color.WHITE);
        idBadge.setBackground(KitchenTheme.WARM_ORANGE);
        idBadge.setOpaque(true);
        idBadge.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(idBadge, BorderLayout.EAST);
        
        // Info panel centrale
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        JPanel dateRow = createInfoRow("📅", "Data Inizio", corso.getDataInizio());
        JPanel frequencyRow = createInfoRow("🔄", "Frequenza", corso.getFrequenza());
        JPanel chefRow = createInfoRow("👨‍🍳", "Chef", "ID " + corso.getIdChef());
        
        infoPanel.add(dateRow);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(frequencyRow);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(chefRow);
        
        // Footer con azioni
        JPanel footerPanel = createCourseFooter(corso);
        
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(footerPanel, BorderLayout.SOUTH);
        
        // Effetto hover
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(248, 249, 250));
                card.repaint();
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.repaint();
            }
        });
        
        return card;
    }
    
    private JPanel createInfoRow(String icon, String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon + " ");
        iconLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        
        JLabel labelText = new JLabel(label + ": ");
        labelText.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
        labelText.setForeground(KitchenTheme.WARM_BROWN);
        
        JLabel valueText = new JLabel(value);
        valueText.setFont(new Font("SF Pro Text", Font.BOLD, 12));
        valueText.setForeground(KitchenTheme.DARK_GRAY);
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(iconLabel);
        leftPanel.add(labelText);
        
        row.add(leftPanel, BorderLayout.WEST);
        row.add(valueText, BorderLayout.EAST);
        
        return row;
    }
    
    private JPanel createCourseFooter(Corso corso) {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        
        try {
            boolean giaIscritto = programma.isUtenteIscritto(studente.getCodiceFiscale(), corso.getIdCorso());
            int numeroIscritti = programma.contaIscrittiCorso(corso.getIdCorso());
            int limitePartecipanti = 20;
            
            // Status a sinistra
            JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            statusPanel.setOpaque(false);
            
            if (giaIscritto) {
                JLabel statusLabel = new JLabel("✅ Già iscritto");
                statusLabel.setFont(new Font("SF Pro Text", Font.BOLD, 13));
                statusLabel.setForeground(new Color(34, 139, 34));
                statusPanel.add(statusLabel);
            } else {
                JLabel countLabel = new JLabel(numeroIscritti + "/" + limitePartecipanti + " posti");
                countLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
                countLabel.setForeground(numeroIscritti >= limitePartecipanti ? Color.RED : KitchenTheme.WARM_BROWN);
                statusPanel.add(countLabel);
            }
            
            // Pulsante a destra
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            buttonPanel.setOpaque(false);
            
            if (!giaIscritto) {
                if (numeroIscritti >= limitePartecipanti) {
                    // Pulsante notifica
                    boolean notificaGiaRichiesta = programma.esisteNotificaAttiva(studente.getCodiceFiscale(), corso.getIdCorso());
                    if (!notificaGiaRichiesta) {
                        JButton notifyBtn = createCompactNotifyButton(corso);
                        buttonPanel.add(notifyBtn);
                    } else {
                        JLabel notifiedLabel = new JLabel("🔔 Notifica attiva");
                        notifiedLabel.setFont(new Font("SF Pro Text", Font.ITALIC, 12));
                        notifiedLabel.setForeground(KitchenTheme.WARM_ORANGE);
                        buttonPanel.add(notifiedLabel);
                    }
                } else {
                    // Pulsante iscrizione
                    JButton enrollBtn = createCompactEnrollButton(corso);
                    buttonPanel.add(enrollBtn);
                }
            }
            
            footer.add(statusPanel, BorderLayout.WEST);
            footer.add(buttonPanel, BorderLayout.EAST);
            
        } catch (SQLException e) {
            JLabel errorLabel = new JLabel("❌ Errore caricamento");
            errorLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 11));
            errorLabel.setForeground(Color.RED);
            footer.add(errorLabel, BorderLayout.CENTER);
        }
        
        return footer;
    }
    
    private JButton createCompactEnrollButton(Corso corso) {
        JButton button = new JButton("Partecipa") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor = getModel().isPressed() ? KitchenTheme.DEEP_RED :
                               getModel().isRollover() ? new Color(255, 160, 50) : KitchenTheme.WARM_ORANGE;
                
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("SF Pro Text", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(90, 32));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(mainApp, 
                "Vuoi iscriverti al corso:\\n\\\"" + corso.getArgomento() + "\\\"?", 
                "Conferma Iscrizione", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (programma.insertIscrizione(studente.getCodiceFiscale(), corso.getIdCorso())) {
                        JOptionPane.showMessageDialog(mainApp, 
                            "🎉 Iscrizione completata con successo!\\nTroverai il corso nella tua dashboard.", 
                            "Iscrizione Completata", 
                            JOptionPane.INFORMATION_MESSAGE);
                        refreshContent();
                        showAvailableCoursesDialog(); // Refresh dialog
                    } else {
                        JOptionPane.showMessageDialog(mainApp, 
                            "❌ Errore durante l'iscrizione.", 
                            "Errore", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(mainApp, 
                        "Errore: " + ex.getMessage(), 
                        "Errore", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        return button;
    }
    
    private JButton createCompactNotifyButton(Corso corso) {
        JButton button = new JButton("🔔") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor = getModel().isPressed() ? new Color(255, 140, 0) :
                               getModel().isRollover() ? new Color(255, 165, 0) : Color.WHITE;
                Color borderColor = KitchenTheme.WARM_ORANGE;
                
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 6, 6);
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        button.setForeground(KitchenTheme.WARM_ORANGE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(32, 32));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText("Notificami quando disponibile");
        
        button.addActionListener(e -> {
            try {
                NotificaCorso notifica = new NotificaCorso(studente.getCodiceFiscale(), corso.getIdCorso());
                if (programma.insertNotificaCorso(notifica)) {
                    JOptionPane.showMessageDialog(mainApp, 
                        "✅ Notifica attivata! Ti avviseremo quando il corso sarà disponibile.", 
                        "Notifica Attivata", 
                        JOptionPane.INFORMATION_MESSAGE);
                    showAvailableCoursesDialog(); // Refresh
                } else {
                    JOptionPane.showMessageDialog(mainApp, 
                        "❌ Errore nell'attivazione della notifica.", 
                        "Errore", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainApp, 
                    "Errore: " + ex.getMessage(), 
                    "Errore", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        return button;
    }
    
    private JPanel createAvailableCoursePanel(Corso corso, JDialog dialog) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(KitchenTheme.WARM_BROWN, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        // Info panel (sinistra)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(corso.getArgomento());
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 16));
        titleLabel.setForeground(KitchenTheme.DARK_GRAY);
        
        JLabel idLabel = new JLabel("ID: " + corso.getIdCorso());
        idLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
        idLabel.setForeground(KitchenTheme.WARM_BROWN);
        
        JLabel dateLabel = new JLabel("📅 " + corso.getDataInizio());
        dateLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
        dateLabel.setForeground(KitchenTheme.WARM_BROWN);
        
        JLabel frequencyLabel = new JLabel("🔄 " + corso.getFrequenza());
        frequencyLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
        frequencyLabel.setForeground(KitchenTheme.WARM_BROWN);
        
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(idLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(dateLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(frequencyLabel);
        
        panel.add(infoPanel, BorderLayout.CENTER);
        
        // Button panel (destra)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(150, 90));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        try {
            boolean giaIscritto = programma.isUtenteIscritto(studente.getCodiceFiscale(), corso.getIdCorso());
            int numeroIscritti = programma.contaIscrittiCorso(corso.getIdCorso());
            
            if (giaIscritto) {
                JLabel alreadyEnrolledLabel = new JLabel("✅ Già iscritto");
                alreadyEnrolledLabel.setFont(new Font("SF Pro Text", Font.BOLD, 13));
                alreadyEnrolledLabel.setForeground(new Color(34, 139, 34));
                buttonPanel.add(alreadyEnrolledLabel);
                
            } else {
                // Simuliamo un limite massimo di 20 partecipanti per corso
                int limitePartecipanti = 20;
                
                if (numeroIscritti >= limitePartecipanti) {
                    // Corso pieno - offri notifica
                    JLabel fullLabel = new JLabel("🚫 Corso pieno (" + numeroIscritti + "/" + limitePartecipanti + ")");
                    fullLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
                    fullLabel.setForeground(Color.RED);
                    buttonPanel.add(fullLabel);
                    
                    buttonPanel.add(Box.createVerticalStrut(5));
                    
                    boolean notificaGiaRichiesta = programma.esisteNotificaAttiva(studente.getCodiceFiscale(), corso.getIdCorso());
                    
                    if (notificaGiaRichiesta) {
                        JLabel notificationLabel = new JLabel("🔔 Notifica attiva");
                        notificationLabel.setFont(new Font("SF Pro Text", Font.ITALIC, 12));
                        notificationLabel.setForeground(KitchenTheme.WARM_ORANGE);
                        buttonPanel.add(notificationLabel);
                    } else {
                        JButton notifyButton = new JButton("🔔 Notifica") {
                            @Override
                            protected void paintComponent(Graphics g) {
                                Graphics2D g2 = (Graphics2D) g;
                                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                                
                                Color bgColor;
                                Color borderColor = KitchenTheme.WARM_ORANGE;
                                
                                if (getModel().isPressed()) {
                                    bgColor = new Color(KitchenTheme.WARM_ORANGE.getRed(), 
                                                      KitchenTheme.WARM_ORANGE.getGreen(), 
                                                      KitchenTheme.WARM_ORANGE.getBlue(), 50);
                                } else if (getModel().isRollover()) {
                                    bgColor = new Color(KitchenTheme.WARM_ORANGE.getRed(), 
                                                      KitchenTheme.WARM_ORANGE.getGreen(), 
                                                      KitchenTheme.WARM_ORANGE.getBlue(), 30);
                                } else {
                                    bgColor = Color.WHITE;
                                }
                                
                                // Draw button with rounded corners
                                g2.setColor(bgColor);
                                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                                
                                // Draw border
                                g2.setColor(borderColor);
                                g2.setStroke(new BasicStroke(1.5f));
                                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 10, 10);
                                
                                super.paintComponent(g);
                            }
                        };
                        
                        notifyButton.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
                        notifyButton.setForeground(KitchenTheme.WARM_ORANGE);
                        notifyButton.setContentAreaFilled(false);
                        notifyButton.setBorderPainted(false);
                        notifyButton.setFocusPainted(false);
                        notifyButton.setPreferredSize(new Dimension(140, 35));
                        notifyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        notifyButton.addActionListener(e -> {
                            try {
                                NotificaCorso notifica = new NotificaCorso(studente.getCodiceFiscale(), corso.getIdCorso());
                                if (programma.insertNotificaCorso(notifica)) {
                                    JOptionPane.showMessageDialog(mainApp, 
                                        "✅ Notifica attivata! Ti avviseremo quando il corso sarà disponibile.", 
                                        "Notifica Attivata", 
                                        JOptionPane.INFORMATION_MESSAGE);
                                    
                                    buttonPanel.remove(notifyButton);
                                    JLabel alreadyEnrolledLabel = new JLabel("✅ Già iscritto");
                                    alreadyEnrolledLabel.setFont(new Font("SF Pro Text", Font.BOLD, 13));
                                    alreadyEnrolledLabel.setForeground(new Color(34, 139, 34));
                                    buttonPanel.add(alreadyEnrolledLabel);
                                    
                                    dialog.revalidate();	//Aggiorna
                                    dialog.repaint();
                                    //showAvailableCoursesDialog(); // Refresh
                                } else {
                                    JOptionPane.showMessageDialog(mainApp, 
                                        "❌ Errore nell'attivazione della notifica.", 
                                        "Errore", 
                                        JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(mainApp, 
                                    "Errore: " + ex.getMessage(), 
                                    "Errore", 
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        });
                        buttonPanel.add(notifyButton);
                    }
                } else {
                    // Corso disponibile - consenti iscrizione
                    JLabel availableLabel = new JLabel("✅ Disponibile (" + numeroIscritti + "/" + limitePartecipanti + ")");
                    availableLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
                    availableLabel.setForeground(new Color(34, 139, 34));
                    buttonPanel.add(availableLabel);
                    
                    buttonPanel.add(Box.createVerticalStrut(5));
                    
                    // Creo un pulsante con lo stile Kitchen del progetto
                    JButton enrollButton = new JButton("📚 Partecipa") {
                        @Override
                        protected void paintComponent(Graphics g) {
                            Graphics2D g2 = (Graphics2D) g;
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            
                            Color bgColor;
                            if (getModel().isPressed()) {
                                bgColor = KitchenTheme.DEEP_RED;
                            } else if (getModel().isRollover()) {
                                bgColor = new Color(KitchenTheme.WARM_ORANGE.getRed(), 
                                                  KitchenTheme.WARM_ORANGE.getGreen(), 
                                                  KitchenTheme.WARM_ORANGE.getBlue(), 200);
                            } else {
                                bgColor = KitchenTheme.WARM_ORANGE;
                            }
                            
                            // Draw button with rounded corners and shadow
                            g2.setColor(new Color(0, 0, 0, 30));
                            g2.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 15, 15);
                            
                            g2.setColor(bgColor);
                            g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 15, 15);
                            
                            // Add inner highlight
                            g2.setColor(new Color(255, 255, 255, 50));
                            g2.drawRoundRect(1, 1, getWidth() - 4, getHeight() - 4, 13, 13);
                            
                            super.paintComponent(g);
                        }
                    };
                    
                    enrollButton.setFont(new Font("SF Pro Text", Font.BOLD, 13));
                    enrollButton.setForeground(Color.WHITE);
                    enrollButton.setContentAreaFilled(false);
                    enrollButton.setBorderPainted(false);
                    enrollButton.setFocusPainted(false);
                    enrollButton.setPreferredSize(new Dimension(140, 45));
                    enrollButton.setMinimumSize(new Dimension(140, 45));
                    enrollButton.setMaximumSize(new Dimension(140, 45));
                    enrollButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    enrollButton.addActionListener(e -> {
                        int confirm = JOptionPane.showConfirmDialog(mainApp, 
                            "Vuoi iscriverti al corso:\n\"" + corso.getArgomento() + "\"?", 
                            "Conferma Iscrizione", 
                            JOptionPane.YES_NO_OPTION);
                        
                        if (confirm == JOptionPane.YES_OPTION) {
                            try {
                                if (programma.insertIscrizione(studente.getCodiceFiscale(), corso.getIdCorso())) {
                                    JOptionPane.showMessageDialog(mainApp, 
                                        "🎉 Iscrizione completata con successo!\nTroverai il corso nella tua dashboard.", 
                                        "Iscrizione Completata", 
                                        JOptionPane.INFORMATION_MESSAGE);
//                                    refreshContent();
                                    // Aggiorna la dashboard principale
                                    buttonPanel.remove(enrollButton);
                                    JLabel alreadyEnrolledLabel = new JLabel("✅ Già iscritto");
                                    alreadyEnrolledLabel.setFont(new Font("SF Pro Text", Font.BOLD, 13));
                                    alreadyEnrolledLabel.setForeground(new Color(34, 139, 34));
                                    buttonPanel.add(alreadyEnrolledLabel);
                                    
                                    dialog.revalidate();	//Aggiorna
                                    dialog.repaint();
                                    //showAvailableCoursesDialog(); // Refresh del dialog
                                } else {
                                    JOptionPane.showMessageDialog(mainApp, 
                                        "❌ Errore durante l'iscrizione.", 
                                        "Errore", 
                                        JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(mainApp, 
                                    "Errore: " + ex.getMessage(), 
                                    "Errore", 
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
                    buttonPanel.add(enrollButton);
                    System.out.println("DEBUG: Pulsante Partecipa aggiunto per corso " + corso.getIdCorso());
                }
            }
            
        } catch (SQLException e) {
            JLabel errorLabel = new JLabel("❌ Errore caricamento");
            errorLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 11));
            errorLabel.setForeground(Color.RED);
            buttonPanel.add(errorLabel);
        }
        
        panel.add(buttonPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void refreshContent() {
        removeAll();
        initializePanel();
        revalidate();
        repaint();
    }
    
    /**
     * Determina il messaggio di benvenuto in base al genere del nome
     */
    private String getWelcomeMessage(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return "Benvenuto!";
        }
        
        String nomeMinuscolo = nome.trim().toLowerCase();
        
        // Lista di nomi femminili comuni in Italia
        String[] nomiFemminili = {
            "alice", "anna", "antonella", "barbara", "beatrice", "carla", "chiara", "clara", "daniela", 
            "elena", "elisabetta", "emma", "francesca", "giorgia", "giulia", "grazia", "laura", 
            "lucia", "luisa", "maria", "marina", "marta", "michela", "nicole", "paola", "rachele", 
            "roberta", "sara", "serena", "silvia", "sofia", "stefania", "valentina", "valeria", 
            "vanessa", "vera", "virginia", "arianna", "azzurra", "bianca", "camilla", "caterina", 
            "cecilia", "claudia", "cristina", "debora", "donatella", "elisa", "emanuela", "federica", 
            "flavia", "gabriella", "gaia", "giovanna", "ilaria", "irene", "isabella", "jessica", 
            "katia", "letizia", "lorena", "manuela", "margherita", "marianna", "martina", "matilde", 
            "monica", "nadia", "natalia", "nicoletta", "noemi", "patrizia", "raffaella", "rebecca", 
            "rita", "rosanna", "sabrina", "samantha", "sandra", "simona", "susanna", "tiziana", 
            "viviana", "veronica", "elena", "alessandra", "alessia", "ambra", "aurora", "benedetta", 
            "costanza", "diletta", "eleonora", "ginevra", "giuseppina", "ludovica", "maddalena", 
            "miriam", "natalina", "palmira", "pia", "rosalia", "rosaria", "stella", "teresa", "viola"
        };
        
        // Lista di suffissi femminili comuni
        String[] suffissiFemminili = {"a", "ella", "ina", "etta", "iana", "anna"};
        
        // Controlla se il nome è nella lista dei nomi femminili
        for (String nomeFem : nomiFemminili) {
            if (nomeMinuscolo.equals(nomeFem)) {
                return "Benvenuta, " + nome + "!";
            }
        }
        
        // Controlla i suffissi femminili
        for (String suffisso : suffissiFemminili) {
            if (nomeMinuscolo.endsWith(suffisso) && nomeMinuscolo.length() > suffisso.length()) {
                return "Benvenuta, " + nome + "!";
            }
        }
        
        // Di default, assume maschile
        return "Benvenuto, " + nome + "!";
    }
}