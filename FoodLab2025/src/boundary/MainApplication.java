package boundary;

import controller.Controller;
import entity.Utente;
import javax.swing.*;
import java.awt.*;

/**
 * Main application window that uses CardLayout for smooth navigation
 * between different pages without the open/close effect
 */
public class MainApplication extends JFrame {
    private static final long serialVersionUID = 1L;
    private Controller controller;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    
    // Card names for navigation
    public static final String PAGINA_INIZIALE = "PAGINA_INIZIALE";
    public static final String CHEF_LOGIN = "CHEF_LOGIN";
    public static final String CHEF_REGISTRAZIONE = "CHEF_REGISTRAZIONE";
    public static final String AREA_CHEF = "AREA_CHEF";
    public static final String GESTIONE_CORSI = "GESTIONE_CORSI";
    public static final String CREAZIONE_CORSI = "CREAZIONE_CORSI";
    public static final String NOTIFICHE_CHEF = "NOTIFICHE_CHEF";
    private static final String STUDENTE_LOGIN = "STUDENTE_LOGIN";
	private static final String STUDENTE_REGISTRAZIONE = "STUDENTE_REGISTRAZIONE";
	private static final String AREA_STUDENTE = "AREA_STUDENTE";
    
    public MainApplication(Controller controller) {
        this.controller = controller;
        initializeFrame();
        setupCardLayout();
        showPaginaIniziale();
        setVisible(true); 
    }
    
    private void initializeFrame() {
        setTitle("FoodLab Unina");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Set application icon
        try {
            java.net.URL iconURL = getClass().getResource("/icons/logofoodlab.png");
            if (iconURL != null) {
                ImageIcon icon = new ImageIcon(iconURL);
                setIconImage(icon.getImage());
                System.out.println("Icona finestra impostata con successo!");
            } else {
                System.err.println("File icona non trovato: /icons/logofoodlab.png");
            }
        } catch (Exception e) {
            System.err.println("Impossibile caricare l'icona dell'applicazione: " + e.getMessage());
        }
    }
    
    private void setupCardLayout() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        JScrollPane cardScroll = new JScrollPane(cardPanel);
        
        //Velocizza scorriento
        cardScroll.getVerticalScrollBar().setUnitIncrement(15);
        cardScroll.getHorizontalScrollBar().setUnitIncrement(15);
        
        //add(cardScroll);
        // Create all the panels
        PaginaInizialePanel paginaIniziale = new PaginaInizialePanel(controller, this);
        ChefLoginPanel chefLogin = new ChefLoginPanel(controller, this);
        ChefRegistrazionePanel chefRegistrazione = new ChefRegistrazionePanel(controller, this);
        AreaChefPanel areaChef = new AreaChefPanel(controller, this);
        GestioneCorsiPanel gestioneCorsi = new GestioneCorsiPanel(controller, this);
        CreazioneCorsiPanel creazioneCorsi = new CreazioneCorsiPanel(controller, this);
        NotificheChefPanel notificheChef = new NotificheChefPanel(controller, this);
        StudenteLoginPanel studenteLogin = new StudenteLoginPanel(controller, this);
        StudenteRegistrazionePanel studenteRegistrazione = new StudenteRegistrazionePanel(controller, this);
        AreaStudentePanel areaStudente = new AreaStudentePanel(controller, this);
        
        // Add panels to card layout
        cardPanel.add(paginaIniziale, PAGINA_INIZIALE);
        cardPanel.add(chefLogin, CHEF_LOGIN);
        cardPanel.add(chefRegistrazione, CHEF_REGISTRAZIONE);
        cardPanel.add(areaChef, AREA_CHEF);
        cardPanel.add(gestioneCorsi, GESTIONE_CORSI);
        cardPanel.add(creazioneCorsi, CREAZIONE_CORSI);
        cardPanel.add(notificheChef, NOTIFICHE_CHEF);
        cardPanel.add(studenteLogin, STUDENTE_LOGIN);
        cardPanel.add(studenteRegistrazione, STUDENTE_REGISTRAZIONE);
        cardPanel.add(areaStudente, AREA_STUDENTE);
        
        setContentPane(cardScroll);
//        setContentPane(cardPanel);
    }
    
    /**
     * Navigate to a specific card
     */
    public void showCard(String cardName) {
        cardLayout.show(cardPanel, cardName);
    }
    
    /**
     * Show the initial page
     */
    public void showPaginaIniziale() {
        showCard(PAGINA_INIZIALE);
    }
    
    /**
     * Show chef login page
     */
    public void showChefLogin() {
        showCard(CHEF_LOGIN);
    }
    
    /**
     * Show chef registration page
     */
    public void showChefRegistrazione() {
        showCard(CHEF_REGISTRAZIONE);
    }
    
    /**
     * Show chef area page
     */
    public void showAreaChef() {
        showCard(AREA_CHEF);
    }
    
    /**
     * Show course management page
     */
    public void showGestioneCorsi() {
        showCard(GESTIONE_CORSI);
    }
    
    /**
     * Show course creation page
     */
    public void showCreazioneCorsi() {
        showCard(CREAZIONE_CORSI);
    }
    
    /**
     * Get the AreaChefPanel for updates
     */
    public AreaChefPanel getAreaChefPanel() {
        return (AreaChefPanel) cardPanel.getComponent(3);
    }
    
    /**
     * Get the GestioneCorsiPanel for updates
     */
    public GestioneCorsiPanel getGestioneCorsiPanel() {
        return (GestioneCorsiPanel) cardPanel.getComponent(4);
    }
    
    /**
     * Get the CreazioneCorsiPanel for updates
     */
    public CreazioneCorsiPanel getCreazioneCorsiPanel() {
        return (CreazioneCorsiPanel) cardPanel.getComponent(5);
    }
    
    /**
     * Get the NotificheChefPanel for updates
     */
    public NotificheChefPanel getNotificheChefPanel() {
        return (NotificheChefPanel) cardPanel.getComponent(6);
    }
    
    /**
     * Show chef notifications page
     */
    public void showNotificheChef(Utente chef) {
        NotificheChefPanel panel = getNotificheChefPanel();
        if (panel != null) {
            panel.setChef(chef);
            showCard(NOTIFICHE_CHEF);
        } else {
            System.err.println("NotificheChefPanel not found!");
        }
    }
    
    /**
     * Show monthly report page
     */
    public void showReportMensile(String codiceFiscaleChef) {
        try {
            ReportMensilePanel reportPanel = new ReportMensilePanel(controller, this, codiceFiscaleChef);
            // Add to cards if not exists
            if (getComponentByName("REPORT_MENSILE") == null) {
                cardPanel.add(reportPanel, "REPORT_MENSILE");
            } else {
                // Replace existing panel
                cardPanel.remove(getComponentByName("REPORT_MENSILE"));
                cardPanel.add(reportPanel, "REPORT_MENSILE");
            }
            showCard("REPORT_MENSILE");
        } catch (Exception e) {
            System.err.println("ERRORE in showReportMensile: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore nell'apertura del report: " + e.getMessage(), 
                "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Helper method to find component by name
     */
    private Component getComponentByName(String name) {
        for (Component comp : cardPanel.getComponents()) {
            if (name.equals(comp.getName())) {
                return comp;
            }
        }
        return null;
    }
    
    /**
     * Refresh the chef area to update course list
     */
    public void refreshChefArea() {
        AreaChefPanel areaChefPanel = getAreaChefPanel();
        if (areaChefPanel != null) {
            areaChefPanel.refreshDisplay();
        }
    }
    
    public void refreshGestioneCorsiArea() 
    {
    	GestioneCorsiPanel gestioneCorsi = getGestioneCorsiPanel();
        if( gestioneCorsi != null)
        		gestioneCorsi.refreshDisplay();
    }
    
    /**
     * Show student login page
     */
    public void showStudenteLogin() {
        showCard(STUDENTE_LOGIN);
    }
    
    public void showStudenteRegistrazione() {
        showCard(STUDENTE_REGISTRAZIONE);
    }
    
    /**
     * Show student area with student data
     */
    public void showAreaStudente(Utente studente) {
        AreaStudentePanel panel = getAreaStudentePanel();
        if (panel != null) {
            panel.setStudente(studente);
            showCard(AREA_STUDENTE);
        } else {
            System.err.println("AreaStudentePanel not found!");
        }
    }
    
    /**
     * Get the AreaStudentePanel for updates
     */
    public AreaStudentePanel getAreaStudentePanel() {
        // Search through all components to find AreaStudentePanel
        for (int i = 0; i < cardPanel.getComponentCount(); i++) {
            Component comp = cardPanel.getComponent(i);
            if (comp instanceof AreaStudentePanel) {
                return (AreaStudentePanel) comp;
            }
        }
        return null;
    }
    
    /**
     * Get the current card panel for updates
     */
    public JPanel getCardPanel() {
        return cardPanel;
    	}

}
