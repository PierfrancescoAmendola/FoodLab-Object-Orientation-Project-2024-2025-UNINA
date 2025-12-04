package boundary;

import controller.*;
import javax.swing.*;
import java.awt.*;

public class PaginaInizialePanel extends JPanel {
	private Controller programma;
	private MainApplication mainApp;
	private static final long serialVersionUID = 1L;

	public PaginaInizialePanel(Controller p, MainApplication mainApp) {
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
		mainContainer.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
		
		// Header section with kitchen vibes
		JPanel headerPanel = createHeaderSection();
		mainContainer.add(headerPanel);
		mainContainer.add(Box.createVerticalStrut(40));
		
		// Welcome section
		JPanel welcomePanel = createWelcomeSection();
		mainContainer.add(welcomePanel);
		mainContainer.add(Box.createVerticalStrut(50));
		
		// Options section
		JPanel optionsPanel = createOptionsSection();
		mainContainer.add(optionsPanel);
		
		add(mainContainer, BorderLayout.CENTER);
	}
	
	private JPanel createHeaderSection() {
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
		headerPanel.setOpaque(false);
		headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// Logo section
		JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		logoPanel.setOpaque(false);
		
		// Try to load logo, fallback to text if not found
		JLabel logoLabel = new JLabel();
		try {
			ImageIcon logoIcon = new ImageIcon("src/icons/logofoodlab.png");
			// Use high-quality scaling for better image quality
			Image logoImage = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
			logoLabel.setIcon(new ImageIcon(logoImage));
		} catch (Exception e) {
			// Fallback to text if icon not found
			logoLabel.setText("🍳");
			logoLabel.setFont(new Font("Arial", Font.BOLD, 70));
		}
		logoPanel.add(logoLabel);
		headerPanel.add(logoPanel);
		
		// Main title
		JLabel titleLabel = new JLabel("FoodLab Unina");
		titleLabel.setFont(KitchenTheme.TITLE_FONT);
		titleLabel.setForeground(KitchenTheme.WARM_ORANGE);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		headerPanel.add(titleLabel);
		
		// Subtitle
		JLabel subtitleLabel = new JLabel("Dove la passione per la cucina prende vita");
		subtitleLabel.setFont(KitchenTheme.SUBTITLE_FONT);
		subtitleLabel.setForeground(KitchenTheme.WARM_BROWN);
		subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		headerPanel.add(subtitleLabel);
		
		return headerPanel;
	}
	
	private JPanel createWelcomeSection() {
		JPanel welcomePanel = KitchenTheme.createKitchenCard();
		welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
		welcomePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		welcomePanel.setMaximumSize(new Dimension(600, 200));
		welcomePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel welcomeLabel = new JLabel("Benvenuti nella nostra cucina digitale!");
		welcomeLabel.setFont(new Font("Georgia", Font.BOLD, 20));
		welcomeLabel.setForeground(KitchenTheme.DARK_GRAY);
		welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		welcomePanel.add(welcomeLabel);
		
		welcomePanel.add(Box.createVerticalStrut(15));
		
		JLabel descLabel = new JLabel("Scegli il tuo ruolo e inizia la tua avventura culinaria");
		descLabel.setFont(KitchenTheme.LABEL_FONT);
		descLabel.setForeground(KitchenTheme.WARM_BROWN);
		descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		welcomePanel.add(descLabel);
		
		return welcomePanel;
	}
	
	private JPanel createOptionsSection() {
		JPanel optionsPanel = new JPanel();
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.X_AXIS));
		optionsPanel.setOpaque(false);
		optionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// Chef option
		JPanel chefPanel = createOptionCard("CHEF", "Sei un esperto culinario?", 
			"Accedi alla tua area riservata per creare e gestire i tuoi corsi di cucina", 
			"Accedi come Chef", () -> programma.loginChef());
		
		// Student option
		JPanel studentPanel = createOptionCard("STUDENTE", "Vuoi imparare a cucinare?", 
			"Scopri i nostri corsi e inizia il tuo percorso culinario", 
			"Accedi come Studente", () -> mainApp.showStudenteLogin());
		
		// Add panels with proper alignment
		optionsPanel.add(Box.createHorizontalGlue());
		optionsPanel.add(chefPanel);
		optionsPanel.add(Box.createHorizontalStrut(40));
		optionsPanel.add(studentPanel);
		optionsPanel.add(Box.createHorizontalGlue());
		
		return optionsPanel;
	}
	
	private JPanel createOptionCard(String title, String question, String description, String buttonText, Runnable action) {
		JPanel card = KitchenTheme.createKitchenCard();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
		card.setPreferredSize(new Dimension(280, 300));
		card.setMaximumSize(new Dimension(280, 300));
		
		// Icon section
		JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		iconPanel.setOpaque(false);
		
		JLabel iconLabel = new JLabel();
		String iconPath = "";
		if (title.equals("CHEF")) {
			iconPath = "src/icons/iconachef.png";
		} else if (title.equals("STUDENTE")) {
			iconPath = "src/icons/iconastudent.png";
		}
		
		try {
			ImageIcon icon = new ImageIcon(iconPath);
			// Use high-quality scaling for better image quality
			Image iconImage = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
			iconLabel.setIcon(new ImageIcon(iconImage));
		} catch (Exception e) {
			// Fallback to emoji if icon not found
			if (title.equals("CHEF")) {
				iconLabel.setText("👨‍🍳");
			} else {
				iconLabel.setText("👨‍🎓");
			}
			iconLabel.setFont(new Font("Arial", Font.BOLD, 45));
		}
		iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		iconPanel.add(iconLabel);
		card.add(iconPanel);
		
		card.add(Box.createVerticalStrut(15));
		
		// Title
		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font("Georgia", Font.BOLD, 24));
		titleLabel.setForeground(KitchenTheme.WARM_ORANGE);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		card.add(titleLabel);
		
		card.add(Box.createVerticalStrut(10));
		
		// Question
		JLabel questionLabel = new JLabel("<html><div style='text-align: center;'>" + question + "</div></html>");
		questionLabel.setFont(KitchenTheme.LABEL_FONT);
		questionLabel.setForeground(KitchenTheme.DARK_GRAY);
		questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		card.add(questionLabel);
		
		card.add(Box.createVerticalStrut(15));
		
		// Description
		JLabel descLabel = new JLabel("<html><div style='text-align: center;'>" + description + "</div></html>");
		descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		descLabel.setForeground(KitchenTheme.WARM_BROWN);
		descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		card.add(descLabel);
		
		card.add(Box.createVerticalStrut(25));
		
		// Button
		JButton button = KitchenTheme.createKitchenButton(buttonText);
		button.addActionListener(e -> action.run());
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		card.add(button);
		
		return card;
	}
}
