package boundary;

import entity.*;
import controller.*;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class NotificheChefPanel extends JPanel {
    
    private Controller programma;
    private MainApplication mainApp;
    private Utente chef;
    private JScrollPane scrollPane;
    
    public NotificheChefPanel(Controller p, MainApplication mainApp) {
        this.programma = p;
        this.mainApp = mainApp;
        
        initializePanel();
    }
    
    public void setChef(Utente chef) {
        this.chef = chef;
        refreshContent();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(KitchenTheme.SOFT_GRAY);
        
        // Header with welcome message and back button
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content with notifications
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
                
                // Draw gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, KitchenTheme.WARM_ORANGE,
                    0, getHeight(), KitchenTheme.DEEP_RED
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
        
        String welcomeText = chef != null ? 
            "🔔 Notifiche per Chef " + chef.getNome() : 
            "🔔 Notifiche Chef";
            
        JLabel welcomeLabel = new JLabel(welcomeText);
        welcomeLabel.setFont(new Font("SF Pro Display", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Iscrizioni ai tuoi corsi e aggiornamenti importanti");
        subtitleLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createVerticalStrut(5));
        welcomePanel.add(subtitleLabel);
        
        headerPanel.add(welcomePanel, BorderLayout.CENTER);
        
        // Add back button to the right
        JPanel backPanel = createBackButton();
        headerPanel.add(backPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createBackButton() {
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanel.setOpaque(false);
        backPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JButton backButton = new JButton("← Torna all'Area Chef") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor;
                if (getModel().isPressed()) {
                    bgColor = new Color(255, 255, 255, 40);
                } else if (getModel().isRollover()) {
                    bgColor = new Color(255, 255, 255, 25);
                } else {
                    bgColor = new Color(255, 255, 255, 15);
                }
                
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                g2.setColor(new Color(255, 255, 255, 60));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 6, 6);
                
                super.paintComponent(g);
            }
        };
        
        backButton.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        backButton.setForeground(Color.WHITE);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(160, 35));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        backButton.addActionListener(e -> {
            mainApp.showAreaChef();
        });
        
        backPanel.add(backButton);
        return backPanel;
    }
    
    private JPanel createMainContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(KitchenTheme.SOFT_GRAY);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        if (chef != null) {
            loadNotifications(contentPanel);
        } else {
            // Show placeholder content if no chef is set
            JLabel placeholderLabel = new JLabel("Nessun chef selezionato");
            placeholderLabel.setFont(new Font("SF Pro Text", Font.ITALIC, 16));
            placeholderLabel.setForeground(KitchenTheme.WARM_BROWN);
            placeholderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(placeholderLabel);
        }
        
        return contentPanel;
    }
    
    private void loadNotifications(JPanel contentPanel) {
        try {
            ArrayList<NotificaChef> notifiche = programma.getNotificheByChef(chef.getCodiceFiscale());
            
            if (notifiche.isEmpty()) {
                // Empty state
                JPanel emptyPanel = createEmptyNotificationsPanel();
                contentPanel.add(emptyPanel);
            } else {
                // Show notifications
                for (NotificaChef notifica : notifiche) {
                    JPanel notificaPanel = createNotificaChefPanel(notifica);
                    contentPanel.add(notificaPanel);
                    contentPanel.add(Box.createVerticalStrut(15));
                }
                
                // Add some bottom spacing
                contentPanel.add(Box.createVerticalGlue());
            }
            
        } catch (SQLException e) {
            JLabel errorLabel = new JLabel("❌ Errore nel caricamento delle notifiche: " + e.getMessage());
            errorLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
            errorLabel.setForeground(Color.RED);
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(errorLabel);
        }
    }
    
    private JPanel createEmptyNotificationsPanel() {
        JPanel emptyPanel = new JPanel();
        emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
        emptyPanel.setOpaque(false);
        emptyPanel.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));
        
        JLabel emptyIcon = new JLabel("🔔");
        emptyIcon.setFont(new Font("SF Pro Text", Font.PLAIN, 64));
        emptyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel emptyTitle = new JLabel("Nessuna notifica presente");
        emptyTitle.setFont(new Font("SF Pro Display", Font.BOLD, 20));
        emptyTitle.setForeground(KitchenTheme.WARM_BROWN);
        emptyTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel emptyDesc = new JLabel("Riceverai notifiche quando gli studenti si iscrivono ai tuoi corsi");
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
    
    private JPanel createNotificaChefPanel(NotificaChef notifica) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(notifica.isLetto() ? Color.WHITE : new Color(255, 248, 220)); // Sfondo diverso per non letti
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(notifica.isLetto() ? KitchenTheme.WARM_BROWN : KitchenTheme.WARM_ORANGE, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        // Icona tipo notifica
        String icona = "🎓";
        if ("NUOVA_ISCRIZIONE".equals(notifica.getTipoNotifica())) {
            icona = "🎓";
        } else if ("CORSO_COMPLETATO".equals(notifica.getTipoNotifica())) {
            icona = "✅";
        } else if ("AVVISO_IMPORTANTE".equals(notifica.getTipoNotifica())) {
            icona = "⚠️";
        }
        
        JLabel iconLabel = new JLabel(icona);
        iconLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 24));
        panel.add(iconLabel, BorderLayout.WEST);
        
        // Contenuto messaggio
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setOpaque(false);
        messagePanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        
        JLabel messageLabel = new JLabel("<html>" + notifica.getMessaggio().replace("\\n", "<br>") + "</html>");
        messageLabel.setFont(new Font("SF Pro Text", notifica.isLetto() ? Font.PLAIN : Font.BOLD, 14));
        messageLabel.setForeground(KitchenTheme.DARK_GRAY);
        
        JLabel dateLabel = new JLabel(notifica.getDataCreazione().substring(0, 16)); // Solo data e ora
        dateLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
        dateLabel.setForeground(KitchenTheme.WARM_BROWN);
        
        messagePanel.add(messageLabel);
        messagePanel.add(Box.createVerticalStrut(5));
        messagePanel.add(dateLabel);
        
        panel.add(messagePanel, BorderLayout.CENTER);
        
        // Pulsante segna come letto (solo se non letto) - Design migliorato
        if (!notifica.isLetto()) {
            JButton readButton = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Colori per gli stati del pulsante
                    Color baseColor = KitchenTheme.FRESH_GREEN;
                    Color bgColor;
                    Color iconColor;
                    
                    if (getModel().isPressed()) {
                        bgColor = new Color(baseColor.getRed() - 30, baseColor.getGreen() - 30, baseColor.getBlue() - 30);
                        iconColor = new Color(255, 255, 255, 220);
                    } else if (getModel().isRollover()) {
                        bgColor = baseColor;
                        iconColor = Color.WHITE;
                    } else {
                        bgColor = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 180);
                        iconColor = new Color(255, 255, 255, 200);
                    }
                    
                    // Disegna ombra sottile
                    g2.setColor(new Color(0, 0, 0, 30));
                    g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 8, 8);
                    
                    // Disegna sfondo principale
                    g2.setColor(bgColor);
                    g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 8, 8);
                    
                    // Disegna bordo sottile
                    g2.setColor(new Color(baseColor.getRed() - 20, baseColor.getGreen() - 20, baseColor.getBlue() - 20));
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(1, 1, getWidth() - 5, getHeight() - 5, 6, 6);
                    
                    // Disegna highlight interno
                    g2.setColor(new Color(255, 255, 255, 40));
                    g2.drawRoundRect(2, 2, getWidth() - 7, getHeight() - 7, 4, 4);
                    
                    // Disegna icona check moderna
                    g2.setColor(iconColor);
                    g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    
                    int centerX = getWidth() / 2;
                    int centerY = getHeight() / 2;
                    
                    // Disegna il check mark
                    int[] checkX = {centerX - 6, centerX - 2, centerX + 6};
                    int[] checkY = {centerY, centerY + 4, centerY - 4};
                    
                    g2.drawPolyline(checkX, checkY, 3);
                    
                    super.paintComponent(g);
                }
            };
            
            readButton.setContentAreaFilled(false);
            readButton.setBorderPainted(false);
            readButton.setFocusPainted(false);
            readButton.setPreferredSize(new Dimension(40, 35));
            readButton.setMinimumSize(new Dimension(40, 35));
            readButton.setMaximumSize(new Dimension(40, 35));
            readButton.setToolTipText("Segna come letta");
            readButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            readButton.addActionListener(e -> {
                try {
                    if (programma.segnaNotificaComeLetta(notifica.getIdNotifica())) {
                        // Refresh the content
                        refreshContent();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(mainApp, 
                        "Errore nell'aggiornamento della notifica: " + ex.getMessage(), 
                        "Errore", 
                        JOptionPane.ERROR_MESSAGE);
                }
            });
            
            panel.add(readButton, BorderLayout.EAST);
        }
        
        return panel;
    }
    
    private void refreshContent() {
        removeAll();
        revalidate();
        repaint();
        initializePanel();
    }
}