package boundary;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import controller.Controller;
import entity.ReportMensile;

public class ReportMensilePanel extends JPanel {
    private Controller programma;
    private MainApplication mainApp;
    private String codiceFiscaleChef;
    private JComboBox<String> meseComboBox;
    private JPanel statsPanel;
    private JPanel chartsPanel;
    private ReportMensile report;
    
    public ReportMensilePanel(Controller p, MainApplication mainApp, String codiceFiscaleChef) {
        this.programma = p;
        this.mainApp = mainApp;
        this.codiceFiscaleChef = codiceFiscaleChef;
        
//        try {
//            this.report = programma.getReportMensile();
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(mainApp, 
//                "Errore di connessione al database: " + e.getMessage(),
//                "Errore", JOptionPane.ERROR_MESSAGE);
//            this.report = null;
//        }
        
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(KitchenTheme.SOFT_GRAY);
        
        // Main container with enhanced styling
        JPanel mainContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Enhanced gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(248, 250, 255),
                    0, getHeight(), new Color(255, 248, 240)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Subtle pattern overlay
                g2.setColor(new Color(255, 255, 255, 20));
                for (int i = 0; i < getWidth(); i += 60) {
                    for (int j = 0; j < getHeight(); j += 60) {
                        g2.fillOval(i, j, 3, 3);
                    }
                }
            }
        };
        mainContainer.setLayout(new BorderLayout());
        mainContainer.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        mainContainer.add(headerPanel, BorderLayout.NORTH);
        
        // Content area with styled scrollpane
        JScrollPane scrollPane = new JScrollPane() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 80));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        // Style scrollbars
        styleScrollBar(scrollPane.getVerticalScrollBar());
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Statistics panel
        statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setOpaque(false);
        contentPanel.add(statsPanel);
        
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Charts panel
        chartsPanel = new JPanel();
        chartsPanel.setLayout(new BoxLayout(chartsPanel, BoxLayout.Y_AXIS));
        chartsPanel.setOpaque(false);
        contentPanel.add(chartsPanel);
        
        scrollPane.setViewportView(contentPanel);
        mainContainer.add(scrollPane, BorderLayout.CENTER);
        
        add(mainContainer, BorderLayout.CENTER);
        
        // Load initial data
        loadCurrentMonthReport();
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Elegant gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 140, 0, 30),
                    getWidth(), getHeight(), new Color(255, 140, 0, 10)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                
                // Subtle border
                g2.setColor(new Color(255, 140, 0, 50));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
            }
        };
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        // Back button with enhanced styling
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setOpaque(false);
        JButton backButton = createStyledBackButton("← Torna all'Area Chef");
        backButton.addActionListener(e -> {
            programma.showAreaChef();
        });
        backPanel.add(backButton);
        headerPanel.add(backPanel);
        
        headerPanel.add(Box.createVerticalStrut(15));
        
        // Enhanced title section
        JPanel titleSection = new JPanel();
        titleSection.setLayout(new BoxLayout(titleSection, BoxLayout.Y_AXIS));
        titleSection.setOpaque(false);
        
        // Icon and title container
        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        titleContainer.setOpaque(false);
        
        // Report icon
        JLabel iconLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon("src/icons/iconareport.png");
            Image iconImage = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(iconImage));
        } catch (Exception e) {
            iconLabel.setText("📊");
            iconLabel.setFont(new Font("Apple Color Emoji", Font.BOLD, 40));
        }
        titleContainer.add(iconLabel);
        
        JLabel titleLabel = new JLabel("Report Mensile Chef");
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 32));
        titleLabel.setForeground(KitchenTheme.WARM_ORANGE);
        titleContainer.add(titleLabel);
        
        titleSection.add(titleContainer);
        titleSection.add(Box.createVerticalStrut(8));
        
        // Enhanced subtitle
        JLabel subtitleLabel = new JLabel("Analisi dettagliata delle tue attività culinarie");
        subtitleLabel.setFont(new Font("SF Pro Text", Font.ITALIC, 16));
        subtitleLabel.setForeground(KitchenTheme.WARM_BROWN);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleSection.add(subtitleLabel);
        
        headerPanel.add(titleSection);
        headerPanel.add(Box.createVerticalStrut(25));
        
        // Enhanced month selection
        JPanel selectionPanel = createMonthSelectionPanel();
        headerPanel.add(selectionPanel);
        
        return headerPanel;
    }
    
    private JButton createStyledBackButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(new Color(KitchenTheme.WARM_BROWN.getRed(), 
                                         KitchenTheme.WARM_BROWN.getGreen(), 
                                         KitchenTheme.WARM_BROWN.getBlue(), 180));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(KitchenTheme.WARM_BROWN.getRed(), 
                                         KitchenTheme.WARM_BROWN.getGreen(), 
                                         KitchenTheme.WARM_BROWN.getBlue(), 120));
                } else {
                    g2.setColor(new Color(KitchenTheme.WARM_BROWN.getRed(), 
                                         KitchenTheme.WARM_BROWN.getGreen(), 
                                         KitchenTheme.WARM_BROWN.getBlue(), 80));
                }
                
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                
                // Border
                g2.setColor(KitchenTheme.WARM_BROWN);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        button.setForeground(KitchenTheme.WARM_BROWN);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private JPanel createMonthSelectionPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Enhanced card styling with shadow
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 20, 20);
                
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 20, 20);
                
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 20, 20);
                
                // Subtle border
                g2.setColor(new Color(KitchenTheme.LIGHT_GRAY.getRed(), 
                                     KitchenTheme.LIGHT_GRAY.getGreen(), 
                                     KitchenTheme.LIGHT_GRAY.getBlue(), 100));
                g2.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 20, 20);
            }
        };
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(700, 90));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Calendar icon and label
        JPanel labelContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        labelContainer.setOpaque(false);
        
        JLabel iconLabel = new JLabel("📅");
        iconLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 20));
        labelContainer.add(iconLabel);
        
        JLabel label = new JLabel("Seleziona Periodo:");
        label.setFont(new Font("SF Pro Text", Font.BOLD, 16));
        label.setForeground(KitchenTheme.DARK_GRAY);
        labelContainer.add(label);
        
        panel.add(labelContainer);
        
        // Enhanced combo box
        meseComboBox = new JComboBox<String>() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Custom styling
                if (isEnabled()) {
                    g2.setColor(new Color(248, 248, 252));
                } else {
                    g2.setColor(new Color(240, 240, 240));
                }
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                
                g2.setColor(KitchenTheme.LIGHT_GRAY);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                
                super.paintComponent(g);
            }
        };
        
        styleComboBox(meseComboBox);
        populateMonthComboBox();
        
        meseComboBox.addActionListener(e -> generateReport());
        panel.add(meseComboBox);
        
        // Enhanced refresh button
        JButton refreshButton = createRefreshButton();
        refreshButton.addActionListener(e -> {
            populateMonthComboBox();
            generateReport();
        });
        panel.add(refreshButton);
        
        return panel;
    }
    
    private JButton createRefreshButton() {
        JButton button = new JButton("🔄 Aggiorna") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor;
                if (getModel().isPressed()) {
                    bgColor = KitchenTheme.WARM_ORANGE.darker();
                } else if (getModel().isRollover()) {
                    bgColor = new Color(KitchenTheme.WARM_ORANGE.getRed(), 
                                       KitchenTheme.WARM_ORANGE.getGreen(), 
                                       KitchenTheme.WARM_ORANGE.getBlue(), 220);
                } else {
                    bgColor = KitchenTheme.WARM_ORANGE;
                }
                
                // Button background with shadow
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 15, 15);
                
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 15, 15);
                
                // Highlight
                g2.setColor(new Color(255, 255, 255, 40));
                g2.drawRoundRect(1, 1, getWidth() - 4, getHeight() - 4, 13, 13);
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void populateMonthComboBox() {
        meseComboBox.removeAllItems();
        
        try {
            ArrayList<String> mesi = programma.getMesiDisponibili(codiceFiscaleChef);
            
            if (mesi.isEmpty()) {
                // Add current month if no data available
                Calendar cal = Calendar.getInstance();
                String currentMonth = String.format("%02d/%d", cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
                meseComboBox.addItem(currentMonth);
            } else {
                for (String mese : mesi) {
                    meseComboBox.addItem(mese);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Errore nel caricamento dei mesi: " + e.getMessage(),
                "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setPreferredSize(new Dimension(120, 40));
        comboBox.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(KitchenTheme.DARK_GRAY);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            new KitchenTheme.RoundedBorder(8, KitchenTheme.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
    
    private void loadCurrentMonthReport() {
        Calendar cal = Calendar.getInstance();
        generateReportForMonth(cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
    }
    
    private void generateReport() {
        String selectedMonth = (String) meseComboBox.getSelectedItem();
        if (selectedMonth != null) {
            String[] parts = selectedMonth.split("/");
            int mese = Integer.parseInt(parts[0]);
            int anno = Integer.parseInt(parts[1]);
            generateReportForMonth(mese, anno);
        }
    }
    
    private void generateReportForMonth(int mese, int anno) {
        try {
            ReportMensile report = programma.generaReportMensile(codiceFiscaleChef, mese, anno);
            updateStatsPanel(report);
            updateChartsPanel(report);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Errore nella generazione del report: " + e.getMessage(),
                "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateStatsPanel(ReportMensile report) {
        statsPanel.removeAll();
        
        // Title card
        JPanel titleCard = KitchenTheme.createKitchenCard();
        titleCard.setLayout(new BoxLayout(titleCard, BoxLayout.Y_AXIS));
        titleCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        titleCard.setMaximumSize(new Dimension(800, 120));
        titleCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel reportTitle = new JLabel(String.format("📈 Report di %s %d", report.getNomeMese(), report.getAnno()));
        reportTitle.setFont(new Font("SF Pro Display", Font.BOLD, 24));
        reportTitle.setForeground(KitchenTheme.WARM_ORANGE);
        reportTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleCard.add(reportTitle);
        
        titleCard.add(Box.createVerticalStrut(10));
        
        JLabel chefLabel = new JLabel("👨‍🍳 Chef: " + report.getNomeChef());
        chefLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
        chefLabel.setForeground(KitchenTheme.DARK_GRAY);
        chefLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleCard.add(chefLabel);
        
        statsPanel.add(titleCard);
        statsPanel.add(Box.createVerticalStrut(20));
        
        // Stats grid
        JPanel statsGrid = new JPanel(new GridLayout(2, 2, 20, 20));
        statsGrid.setOpaque(false);
        statsGrid.setMaximumSize(new Dimension(800, 300));
        statsGrid.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Corsi totali
        JPanel corsiCard = createStatCard("🎓", "Corsi Totali", String.valueOf(report.getCorsiTotali()), KitchenTheme.WARM_ORANGE);
        statsGrid.add(corsiCard);
        
        // Sessioni totali
        JPanel sessioniCard = createStatCard("📚", "Sessioni Totali", String.valueOf(report.getTotaleSessioni()), KitchenTheme.SAGE_GREEN);
        statsGrid.add(sessioniCard);
        
        // Sessioni online vs pratiche
        String sessioniText = String.format("💻 %d Online | 👥 %d Pratiche", report.getSessioniOnline(), report.getSessioniPratiche());
        JPanel tipoSessioniCard = createStatCard("⚖️", "Distribuzione Sessioni", sessioniText, KitchenTheme.WARM_BROWN);
        statsGrid.add(tipoSessioniCard);
        
        // Ricette statistics (only for practical sessions)
        String ricetteText = String.format("📊 %.1f Media | 🔝 %d Max | 🔻 %d Min", 
            report.getMediaRicettePratiche(), report.getMaxRicettePratiche(), report.getMinRicettePratiche());
        JPanel ricetteCard = createStatCard("🍽️", "Ricette (Sessioni Pratiche)", ricetteText, KitchenTheme.TERRACOTTA);
        statsGrid.add(ricetteCard);
        
        statsPanel.add(statsGrid);
        
        statsPanel.revalidate();
        statsPanel.repaint();
    }
    
    private JPanel createStatCard(String icon, String title, String value, Color accentColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Multi-layer shadow for depth
                g2.setColor(new Color(0, 0, 0, 8));
                g2.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 20, 20);
                
                g2.setColor(new Color(0, 0, 0, 12));
                g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 20, 20);
                
                g2.setColor(new Color(0, 0, 0, 6));
                g2.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 20, 20);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, Color.WHITE,
                    0, getHeight(), new Color(accentColor.getRed(), 
                                             accentColor.getGreen(), 
                                             accentColor.getBlue(), 20)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 20, 20);
                
                // Accent border
                g2.setColor(new Color(accentColor.getRed(), 
                                     accentColor.getGreen(), 
                                     accentColor.getBlue(), 120));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 20, 20);
                
                // Inner highlight
                g2.setColor(new Color(255, 255, 255, 80));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(1, 1, getWidth() - 6, getHeight() - 6, 18, 18);
            }
        };
        
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));
        
        // Icon with background circle
        JPanel iconContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Icon background circle
                g2.setColor(new Color(accentColor.getRed(), 
                                     accentColor.getGreen(), 
                                     accentColor.getBlue(), 30));
                int size = Math.min(getWidth(), getHeight()) - 10;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                g2.fillOval(x, y, size, size);
                
                // Circle border
                g2.setColor(new Color(accentColor.getRed(), 
                                     accentColor.getGreen(), 
                                     accentColor.getBlue(), 80));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(x, y, size, size);
            }
        };
        iconContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));
        iconContainer.setOpaque(false);
        iconContainer.setPreferredSize(new Dimension(60, 60));
        iconContainer.setMaximumSize(new Dimension(60, 60));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 28));
        iconContainer.add(iconLabel);
        
        card.add(iconContainer);
        card.add(Box.createVerticalStrut(15));
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        titleLabel.setForeground(accentColor.darker());
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);
        
        card.add(Box.createVerticalStrut(8));
        
        // Value with enhanced styling
        JLabel valueLabel = new JLabel("<html><div style='text-align: center; color: #" + 
            String.format("%02x%02x%02x", KitchenTheme.DARK_GRAY.getRed(), 
                         KitchenTheme.DARK_GRAY.getGreen(), 
                         KitchenTheme.DARK_GRAY.getBlue()) + 
            ";'>" + value + "</div></html>");
        valueLabel.setFont(new Font("SF Pro Display", Font.BOLD, 18));
        valueLabel.setForeground(KitchenTheme.DARK_GRAY);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(valueLabel);
        
        // Add hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
                card.repaint();
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                card.repaint();
            }
        });
        
        return card;
    }
    
    private void updateChartsPanel(ReportMensile report) {
        chartsPanel.removeAll();
        
        // Charts container
        JPanel chartsContainer = new JPanel(new GridLayout(1, 2, 20, 0));
        chartsContainer.setOpaque(false);
        chartsContainer.setMaximumSize(new Dimension(800, 400));
        chartsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Pie chart for session types
        JPanel pieChartPanel = createSessionTypesPieChart(report);
        chartsContainer.add(pieChartPanel);
        
        // Bar chart for recipes statistics
        JPanel barChartPanel = createRecipesBarChart(report);
        chartsContainer.add(barChartPanel);
        
        chartsPanel.add(chartsContainer);
        
        chartsPanel.revalidate();
        chartsPanel.repaint();
    }
    
    private JPanel createSessionTypesPieChart(ReportMensile report) {
        JPanel container = KitchenTheme.createKitchenCard();
        container.setLayout(new BorderLayout());
        
        // Titolo
        JLabel titleLabel = new JLabel("Distribuzione Tipo Sessioni", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 16));
        titleLabel.setForeground(KitchenTheme.DARK_GRAY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        container.add(titleLabel, BorderLayout.NORTH);
        
        // Pannello grafico personalizzato
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                int centerX = width / 2;
                int centerY = height / 2;
                int radius = Math.min(width, height) / 3;
                
                int online = report.getSessioniOnline();
                int pratiche = report.getSessioniPratiche();
                int totale = online + pratiche;
                
                if (totale > 0) {
                    // Calcola angoli
                    double onlineAngle = (online * 360.0) / totale;
                    double praticheAngle = (pratiche * 360.0) / totale;
                    
                    // Disegna segmenti
                    g2.setColor(KitchenTheme.SAGE_GREEN);
                    g2.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 0, (int) onlineAngle);
                    
                    g2.setColor(KitchenTheme.WARM_ORANGE);
                    g2.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, (int) onlineAngle, (int) praticheAngle);
                    
                    // Bordo
                    g2.setColor(KitchenTheme.DARK_GRAY);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
                } else {
                    // Nessuna sessione
                    g2.setColor(KitchenTheme.LIGHT_GRAY);
                    g2.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
                    
                    g2.setColor(KitchenTheme.DARK_GRAY);
                    g2.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
                    String text = "Nessuna sessione";
                    FontMetrics fm = g2.getFontMetrics();
                    int textX = centerX - fm.stringWidth(text) / 2;
                    int textY = centerY + fm.getAscent() / 2;
                    g2.drawString(text, textX, textY);
                }
            }
        };
        chartPanel.setPreferredSize(new Dimension(300, 200));
        container.add(chartPanel, BorderLayout.CENTER);
        
        // Legenda
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        legendPanel.setOpaque(false);
        
        if (report.getSessioniOnline() > 0) {
            JPanel onlineLegend = createLegendItem("💻 Online (" + report.getSessioniOnline() + ")", KitchenTheme.SAGE_GREEN);
            legendPanel.add(onlineLegend);
        }
        
        if (report.getSessioniPratiche() > 0) {
            JPanel praticheLegend = createLegendItem("👥 Pratiche (" + report.getSessioniPratiche() + ")", KitchenTheme.WARM_ORANGE);
            legendPanel.add(praticheLegend);
        }
        
        container.add(legendPanel, BorderLayout.SOUTH);
        
        return container;
    }
    
    private JPanel createLegendItem(String text, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        item.setOpaque(false);
        
        // Quadratino colorato
        JPanel colorBox = new JPanel();
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(12, 12));
        colorBox.setBorder(BorderFactory.createLineBorder(KitchenTheme.DARK_GRAY, 1));
        item.add(colorBox);
        
        // Testo
        JLabel label = new JLabel(text);
        label.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
        label.setForeground(KitchenTheme.DARK_GRAY);
        item.add(label);
        
        return item;
    }
    
    private JPanel createRecipesBarChart(ReportMensile report) {
        JPanel container = KitchenTheme.createKitchenCard();
        container.setLayout(new BorderLayout());
        
        // Titolo
        JLabel titleLabel = new JLabel("Statistiche Ricette (Sessioni Pratiche)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 16));
        titleLabel.setForeground(KitchenTheme.DARK_GRAY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        container.add(titleLabel, BorderLayout.NORTH);
        
        // Pannello grafico a barre
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                int margin = 40;
                int chartWidth = width - 2 * margin;
                int chartHeight = height - 2 * margin;
                
                if (report.getSessioniPratiche() > 0) {
                    double media = report.getMediaRicettePratiche();
                    int max = report.getMaxRicettePratiche();
                    int min = report.getMinRicettePratiche();
                    
                    // Trova valore massimo per scalare
                    double maxValue = Math.max(Math.max(media, max), min);
                    if (maxValue == 0) maxValue = 1;
                    
                    // Larghezza barre
                    int barWidth = chartWidth / 4;
                    int barSpacing = barWidth / 2;
                    
                    // Disegna barre
                    drawBar(g2, margin + barSpacing, margin, barWidth, chartHeight, media, maxValue, "Media", KitchenTheme.TERRACOTTA);
                    drawBar(g2, margin + barSpacing + barWidth + barSpacing, margin, barWidth, chartHeight, max, maxValue, "Max", KitchenTheme.TERRACOTTA);
                    drawBar(g2, margin + barSpacing + 2 * (barWidth + barSpacing), margin, barWidth, chartHeight, min, maxValue, "Min", KitchenTheme.TERRACOTTA);
                } else {
                    // Nessuna sessione pratica
                    g2.setColor(KitchenTheme.LIGHT_GRAY);
                    g2.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
                    String text = "Nessuna sessione pratica";
                    FontMetrics fm = g2.getFontMetrics();
                    int textX = (width - fm.stringWidth(text)) / 2;
                    int textY = height / 2;
                    g2.drawString(text, textX, textY);
                }
            }
            
            private void drawBar(Graphics2D g2, int x, int y, int width, int height, double value, double maxValue, String label, Color color) {
                // Calcola altezza barra
                int barHeight = (int) ((value / maxValue) * height * 0.8);
                int barY = y + height - barHeight - 20;
                
                // Disegna barra
                g2.setColor(color);
                g2.fillRect(x, barY, width, barHeight);
                
                // Bordo barra
                g2.setColor(color.darker());
                g2.setStroke(new BasicStroke(1));
                g2.drawRect(x, barY, width, barHeight);
                
                // Valore sopra la barra
                g2.setColor(KitchenTheme.DARK_GRAY);
                g2.setFont(new Font("SF Pro Text", Font.BOLD, 12));
                String valueText = String.format("%.1f", value);
                FontMetrics fm = g2.getFontMetrics();
                int textX = x + (width - fm.stringWidth(valueText)) / 2;
                int textY = barY - 5;
                g2.drawString(valueText, textX, textY);
                
                // Label sotto la barra
                g2.setFont(new Font("SF Pro Text", Font.PLAIN, 11));
                fm = g2.getFontMetrics();
                textX = x + (width - fm.stringWidth(label)) / 2;
                textY = y + height;
                g2.drawString(label, textX, textY);
            }
        };
        chartPanel.setPreferredSize(new Dimension(300, 200));
        container.add(chartPanel, BorderLayout.CENTER);
        
        return container;
    }
    
    private void styleScrollBar(JScrollBar scrollBar) {
        scrollBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(200, 200, 200, 150);
                this.trackColor = new Color(240, 240, 240, 100);
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
}