package boundary.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import boundary.KitchenTheme;
import boundary.MainApplication;
import controller.Controller;
import entity.Corso;

public class ShowAnnouncementsDialog extends JDialog
{
	public ShowAnnouncementsDialog(MainApplication mainApp, Controller programma, Corso corso) 
	{
		super(mainApp, "Avvisi per " + corso.getArgomento(), true);
		this.setSize(700, 600);
		this.setLocationRelativeTo(mainApp);
		this.setLayout(new BorderLayout());

		// Main panel with modern styling
		JPanel mainPanel = KitchenTheme.createGradientPanel(new Color(248, 250, 252), new Color(255, 255, 255));
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Header panel
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setOpaque(false);

		JLabel titleLabel = new JLabel("📢 Avvisi del Corso");
		titleLabel.setFont(new Font("SF Pro Text", Font.BOLD, 20));
		titleLabel.setForeground(KitchenTheme.FRESH_GREEN);

		JLabel courseLabel = new JLabel(corso.getArgomento());
		courseLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
		courseLabel.setForeground(KitchenTheme.WARM_BROWN);

		headerPanel.add(titleLabel, BorderLayout.NORTH);
		headerPanel.add(courseLabel, BorderLayout.SOUTH);

		// Content panel with scroll
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setOpaque(false);

		// Load announcements from database
		try {
			ArrayList<entity.Avviso> avvisi = DAO.AvvisoDAO.getAvvisiByCorso(corso.getIdCorso());

			if (avvisi != null && !avvisi.isEmpty()) {
				for (entity.Avviso avviso : avvisi) {
					JPanel avvisoPanel = createModernAvvisoPanel(avviso);
					contentPanel.add(avvisoPanel);
					contentPanel.add(Box.createVerticalStrut(10));
				}
			} else {
				JPanel emptyPanel = createEmptyAvvisiPanel();
				contentPanel.add(emptyPanel);
			}
		} catch (Exception e) {
			JLabel errorLabel = new JLabel("⚠️ Errore nel caricamento degli avvisi: " + e.getMessage());
			errorLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
			errorLabel.setForeground(Color.RED);
			errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(errorLabel);
		}

		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		// Button panel
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.setOpaque(false);

		JButton refreshButton = KitchenTheme.createSecondaryButton("🔄 Aggiorna");
		refreshButton.addActionListener(e -> {
			this.dispose();
			new ShowAnnouncementsDialog(mainApp, programma, corso); // Riapri con dati aggiornati
		});

		JButton closeButton = KitchenTheme.createSecondaryButton("❌ Chiudi");
		closeButton.addActionListener(e -> this.dispose());

		buttonPanel.add(refreshButton);
		buttonPanel.add(closeButton);

		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.add(mainPanel);
		this.setVisible(true);
	}
	
	// Metodi helper per la gestione moderna degli avvisi
	private JPanel createModernAvvisoPanel(entity.Avviso avviso) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(KitchenTheme.WARM_BROWN, 1),
				BorderFactory.createEmptyBorder(15, 15, 15, 15)));
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

		// Icona e info principale
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setOpaque(false);

		JLabel iconLabel = new JLabel("📢");
		iconLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 20));
		iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

		JLabel messageLabel = new JLabel("<html>" + avviso.getMessaggio().replace("\\n", "<br>") + "</html>");
		messageLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
		messageLabel.setForeground(KitchenTheme.DARK_GRAY);

		headerPanel.add(iconLabel, BorderLayout.WEST);
		headerPanel.add(messageLabel, BorderLayout.CENTER);

		// Footer con data
		JLabel dateLabel = new JLabel(avviso.getDataCreazione().substring(0, 16));
		dateLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
		dateLabel.setForeground(KitchenTheme.WARM_BROWN);

		panel.add(headerPanel, BorderLayout.CENTER);
		panel.add(dateLabel, BorderLayout.SOUTH);

		return panel;
	}
	
	private JPanel createEmptyAvvisiPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));

		JLabel iconLabel = new JLabel("📝");
		iconLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 48));
		iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel titleLabel = new JLabel("Nessun avviso ancora");
		titleLabel.setFont(new Font("SF Pro Text", Font.BOLD, 18));
		titleLabel.setForeground(KitchenTheme.WARM_BROWN);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel subtitleLabel = new JLabel("Gli avvisi creati per questo corso appariranno qui");
		subtitleLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
		subtitleLabel.setForeground(KitchenTheme.LIGHT_GRAY);
		subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		panel.add(iconLabel);
		panel.add(Box.createVerticalStrut(15));
		panel.add(titleLabel);
		panel.add(Box.createVerticalStrut(8));
		panel.add(subtitleLabel);

		return panel;
	}
}
