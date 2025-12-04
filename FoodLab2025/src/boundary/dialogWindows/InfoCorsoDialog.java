package boundary.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import boundary.KitchenTheme;
//import boundary.framelication;
import entity.Corso;
import entity.Ricetta;
import entity.Sessione;
import controller.*;

public class InfoCorsoDialog extends JDialog
{
	//private framelication frame;
	private controller.Controller programma;
	
	public InfoCorsoDialog (JFrame frame, Controller controller, Corso corso) {
		super(frame, "Dettagli Corso - " + corso.getArgomento(), true);
		programma = controller;
		this.setSize(650, 550);
		this.setResizable(false);
		this.setLocationRelativeTo(frame);
		this.setLayout(new BorderLayout());
		
		// Create scroll pane for content
		JScrollPane scrollPane = KitchenTheme.createScrollPanel();
		scrollPane.setBorder(null);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.getVerticalScrollBar().setBlockIncrement(32);
		
		// Style scroll bar
		KitchenTheme.styleScrollBar(scrollPane.getVerticalScrollBar());
		
		//Main panel
		JPanel mainPanel = KitchenTheme.createRoundedGreyPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
		
		// Title section
		JLabel titleLabel = new JLabel("📋 Dettagli Corso");
		titleLabel.setFont(new Font("SF Pro Text", Font.BOLD, 22));
		titleLabel.setForeground(KitchenTheme.WARM_ORANGE);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainPanel.add(titleLabel);
		mainPanel.add(Box.createVerticalStrut(8));
		
		JLabel courseNameLabel = new JLabel(corso.getArgomento());
		courseNameLabel.setFont(new Font("SF Pro Text", Font.BOLD, 18));
		courseNameLabel.setForeground(KitchenTheme.DARK_GRAY);
		courseNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainPanel.add(courseNameLabel);
		mainPanel.add(Box.createVerticalStrut(25));
		
		// Course info section
		JPanel infoPanel = createInfoSection(corso);
		mainPanel.add(infoPanel);
		mainPanel.add(Box.createVerticalStrut(20));
		
		// Sessions section
		JPanel sessionsPanel = createSessionsSection(corso);
		mainPanel.add(sessionsPanel);
		mainPanel.add(Box.createVerticalStrut(20));
		
		scrollPane.setViewportView(mainPanel);
		this.add(scrollPane, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	private JPanel createInfoSection(Corso corso) {
		JPanel infoPanel = KitchenTheme.createRoundedPanel(Color.WHITE, new Color(220, 220, 220));
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setOpaque(false);
		infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		JLabel infoTitle = new JLabel("ℹ️ Informazioni Corso");
		infoTitle.setFont(new Font("SF Pro Text", Font.BOLD, 16));
		infoTitle.setForeground(KitchenTheme.WARM_ORANGE);
		infoTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		infoPanel.add(infoTitle);
		infoPanel.add(Box.createVerticalStrut(15));
		
		try {
			int numIscritti = programma.getNumeroIscrittiCorso(corso.getIdCorso());
			
			JLabel dateLabel = new JLabel("📅 Data Inizio: " + corso.getDataInizio());
			dateLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
			dateLabel.setForeground(KitchenTheme.DARK_GRAY);
			dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			infoPanel.add(dateLabel);
			infoPanel.add(Box.createVerticalStrut(8));
			
			JLabel freqLabel = new JLabel("🔄 Frequenza: " + corso.getFrequenza());
			freqLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
			freqLabel.setForeground(KitchenTheme.DARK_GRAY);
			freqLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			infoPanel.add(freqLabel);
			infoPanel.add(Box.createVerticalStrut(8));
			
			JLabel enrolledLabel = new JLabel("👥 Iscritti: " + numIscritti);
			enrolledLabel.setFont(new Font("SF Pro Text", Font.BOLD, 14));
			enrolledLabel.setForeground(KitchenTheme.WARM_ORANGE);
			enrolledLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			infoPanel.add(enrolledLabel);
			
		} catch (SQLException e) {
			JLabel errorLabel = new JLabel("❌ Errore nel caricamento informazioni");
			errorLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
			errorLabel.setForeground(Color.RED);
			errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			infoPanel.add(errorLabel);
		}
		
		return infoPanel;
	}
	
	private JPanel createSessionsSection(Corso corso) {
		JPanel sessionsPanel = KitchenTheme.createRoundedPanel(Color.WHITE, new Color(220, 220, 220));
		sessionsPanel.setLayout(new BoxLayout(sessionsPanel, BoxLayout.Y_AXIS));
		sessionsPanel.setOpaque(false);
		sessionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		JLabel sessionsTitle = new JLabel("📅 Sessioni");
		sessionsTitle.setFont(new Font("SF Pro Text", Font.BOLD, 16));
		sessionsTitle.setForeground(KitchenTheme.WARM_ORANGE);
		sessionsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		sessionsPanel.add(sessionsTitle);
		sessionsPanel.add(Box.createVerticalStrut(15));
		
		try {
			ArrayList<Sessione> sessioni = programma.elencaSessioni(corso.getIdCorso());
			if (sessioni != null && !sessioni.isEmpty()) {
				for (Sessione sessione : sessioni) {
					JPanel sessionCard = KitchenTheme.createRoundedPanel(Color.WHITE, new Color(220, 220, 220));
					sessionCard.setLayout(new BoxLayout(sessionCard, BoxLayout.Y_AXIS));
					
					JLabel sessionLabel = new JLabel("• " + sessione.getData() + " " + sessione.getOraInizio() + 
						(sessione.isOnline() ? " (Online)" : " (Presenza)"));
					sessionLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
					sessionLabel.setForeground(KitchenTheme.DARK_GRAY);
					sessionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
					sessionCard.add(sessionLabel);
					sessionsPanel.add(sessionCard);
					//Aggiunta delle ricette se è pratica
					if( !sessione.isOnline() )	//Sessione pratica
					{
						JPanel ricetteSessionePanel = createRecipesSection(sessione);
						sessionCard.add(ricetteSessionePanel);
						sessionsPanel.add(Box.createVerticalStrut(5));
					}
				}
			} else {
				JLabel noSessionsLabel = new JLabel("Nessuna sessione programmata");
				noSessionsLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
				noSessionsLabel.setForeground(KitchenTheme.LIGHT_GRAY);
				noSessionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				sessionsPanel.add(noSessionsLabel);
			}
		} catch (SQLException e) {
			JLabel errorLabel = new JLabel("❌ Errore nel caricamento sessioni");
			errorLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
			errorLabel.setForeground(Color.RED);
			errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			sessionsPanel.add(errorLabel);
		}
		return sessionsPanel;
	}
	
	private JPanel createRecipesSection(Sessione sessione) {
		JPanel recipesPanel = new JPanel();
		recipesPanel.setLayout(new BoxLayout(recipesPanel, BoxLayout.Y_AXIS));
		recipesPanel.setOpaque(false);
		recipesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		JLabel recipesTitle = new JLabel("🍳 Ricette");
		recipesTitle.setFont(new Font("SF Pro Text", Font.BOLD, 16));
		recipesTitle.setForeground(KitchenTheme.WARM_ORANGE);
		recipesTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
		recipesPanel.add(recipesTitle);
		recipesPanel.add(Box.createVerticalStrut(15));
		
		try {
			ArrayList<Ricetta> ricette = programma.getRicetteSessione(sessione.getIdSessione());
			if (ricette != null && !ricette.isEmpty()) {
				for (Ricetta ricetta : ricette) {
					recipesPanel.add(Box.createHorizontalStrut(5));
					JLabel recipeLabel = new JLabel("• " + ricetta.getNome());
					recipeLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
					recipeLabel.setForeground(KitchenTheme.DARK_GRAY);
					recipeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
					recipesPanel.add(recipeLabel);
					recipesPanel.add(Box.createVerticalStrut(5));
				}
			} else {
				JLabel noRecipesLabel = new JLabel("Nessuna ricetta disponibile");
				noRecipesLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
				noRecipesLabel.setForeground(KitchenTheme.LIGHT_GRAY);
				noRecipesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				recipesPanel.add(noRecipesLabel);
			}
		} catch (SQLException e) {
			JLabel errorLabel = new JLabel("❌ Errore nel caricamento ricette");
			errorLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
			errorLabel.setForeground(Color.RED);
			errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			recipesPanel.add(errorLabel);
		}
		return recipesPanel;
	}	
}
