package boundary.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import boundary.KitchenTheme;
import boundary.MainApplication;
import controller.Controller;
import entity.Corso;
import entity.Utente;

public class CreateAnnouncementDialog extends JDialog
{
	private Controller programma;
	private Utente admin;
	
	public CreateAnnouncementDialog(MainApplication mainApp, Controller programma, Utente admin) 
	{
		super(mainApp, "📢 Crea Nuovo Avviso", true);
		this.programma = programma;
		this.setSize(600, 650);
		this.setLocationRelativeTo(mainApp);
		this.setLayout(new BorderLayout());

		// Main panel with gradient background
		JPanel mainPanel = KitchenTheme.createRoundedGreyPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		// Header
		JLabel headerLabel = new JLabel("📢 Crea un Nuovo Avviso");
		headerLabel.setFont(new Font("SF Pro Text", Font.BOLD, 24));
		headerLabel.setForeground(KitchenTheme.FRESH_GREEN);
		headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainPanel.add(headerLabel);

		JLabel subHeaderLabel = new JLabel("Invia comunicazioni importanti ai tuoi studenti");
		subHeaderLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
		subHeaderLabel.setForeground(KitchenTheme.WARM_BROWN);
		subHeaderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainPanel.add(subHeaderLabel);
		mainPanel.add(Box.createVerticalStrut(30));

		// Course selection with modern styling
		JPanel coursePanel = createModernFormField("🎓 Seleziona Corso", "Scegli il corso per cui creare l'avviso");
		JComboBox<String> courseComboBox = new JComboBox<>();
		loadCoursesIntoComboBox(courseComboBox);
		styleModernComboBox(courseComboBox);

		// Replace the placeholder component with the combobox
		Component[] components = coursePanel.getComponents();
		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof JPanel) {
				JPanel innerPanel = (JPanel) components[i];
				if (innerPanel.getComponentCount() > 0 && innerPanel.getComponent(0) instanceof JTextField) {
					innerPanel.removeAll();
					innerPanel.add(courseComboBox, BorderLayout.CENTER);
					break;
				}
			}
		}
		mainPanel.add(coursePanel);
		mainPanel.add(Box.createVerticalStrut(20));

		// Title field
		JPanel titlePanel = createModernFormField("🏷️ Titolo Avviso",
				"Es: Cambio orario lezione, Materiali necessari...");
		JTextField titleField = getTextFieldFromFormPanel(titlePanel);
		mainPanel.add(titlePanel);
		mainPanel.add(Box.createVerticalStrut(20));

		// Content area with modern styling
		JPanel contentPanel = createModernTextAreaField("📝 Contenuto Avviso",
				"Scrivi qui il messaggio completo per i tuoi studenti...");
		JTextArea contentArea = getTextAreaFromFormPanel(contentPanel);
		mainPanel.add(contentPanel);
		mainPanel.add(Box.createVerticalStrut(30));

		// Button panel
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
		buttonPanel.setOpaque(false);

		JButton cancelButton = KitchenTheme.createSecondaryButton("❌ Annulla");
		cancelButton.addActionListener(e -> this.dispose());

		JButton createButton = KitchenTheme.createPrimaryButton("🚀 Crea Avviso");
		createButton.addActionListener(e -> {
			String selectedCourse = (String) courseComboBox.getSelectedItem();
			String title = titleField.getText().trim();
			String content = contentArea.getText().trim();

			if (selectedCourse == null || selectedCourse.equals("Nessun corso disponibile")) {
				JOptionPane.showMessageDialog(this, "⚠️ Seleziona un corso valido", "Corso non selezionato",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (title.isEmpty() || content.isEmpty()) {
				JOptionPane.showMessageDialog(this, "⚠️ Titolo e contenuto sono obbligatori", "Campi mancanti",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Extract course ID and name from selection
			String[] courseParts = selectedCourse.split(" - ");
			String courseId = courseParts[0];
			String courseName = courseParts[1];

			try {
				// Create announcement for all students enrolled in the course
				ArrayList<entity.Iscrizione> iscrizioni = DAO.IscrizioneDAO.getIscrizioniByCorso(courseId);
				int avvisiCreati = 0;

				String messaggioCompleto = title + "\n\n" + content;

				for (entity.Iscrizione iscrizione : iscrizioni) {
					entity.Avviso avviso = new entity.Avviso(messaggioCompleto, "CORSO_ANNUNCIO",
							iscrizione.getIdUtente(), courseId);

					if (DAO.AvvisoDAO.insertAvviso(avviso)) {
						avvisiCreati++;
					}
				}

				JOptionPane.showMessageDialog(this, "🎉 Avviso creato con successo!\n" + "Corso: " + courseName + "\n"
						+ "Studenti notificati: " + avvisiCreati, "Successo", JOptionPane.INFORMATION_MESSAGE);
				this.dispose();

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "❌ Errore nella creazione dell'avviso: " + ex.getMessage(),
						"Errore", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
		});

		buttonPanel.add(cancelButton);
		buttonPanel.add(createButton);
		mainPanel.add(buttonPanel);

		this.add(mainPanel, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	private JPanel createModernFormField(String labelText, String placeholder) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setOpaque(false);
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.setMaximumSize(new Dimension(500, 80));

		JLabel label = new JLabel(labelText);
		label.setForeground(KitchenTheme.WARM_BROWN);
		label.setFont(new Font("SF Pro Text", Font.BOLD, 14));
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(label);

		panel.add(Box.createRigidArea(new Dimension(0, 8)));

		JTextField field = new JTextField(placeholder);
		field.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
		field.setForeground(Color.GRAY);
		field.setPreferredSize(new Dimension(0, 35));
		field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
		field.setAlignmentX(Component.LEFT_ALIGNMENT);
		field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(KitchenTheme.FRESH_GREEN, 2),
				BorderFactory.createEmptyBorder(8, 12, 8, 12)));

		JPanel fieldPanel = new JPanel(new BorderLayout());
		fieldPanel.setOpaque(false);
		fieldPanel.add(field, BorderLayout.CENTER);
		fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		panel.add(fieldPanel);
		return panel;
	}
	
	private void loadCoursesIntoComboBox(JComboBox<String> comboBox) {
		try {
			if (admin != null) {
				ArrayList<Corso> corsi = programma.elencaCorsi(admin.getCodiceFiscale());
				if (corsi != null && !corsi.isEmpty()) {
					for (Corso corso : corsi) {
						comboBox.addItem(corso.getIdCorso() + " - " + corso.getArgomento());
					}
				} else {
					comboBox.addItem("Nessun corso disponibile");
				}
			}
		} catch (SQLException e) {
			comboBox.addItem("Errore nel caricamento corsi");
		}
	}
	
	private void styleModernComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        comboBox.setPreferredSize(new Dimension(0, 35));
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(KitchenTheme.FRESH_GREEN, 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        comboBox.setBackground(Color.WHITE);
    }
	
	private JTextField getTextFieldFromFormPanel(JPanel panel) {
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
	
	private JTextArea getTextAreaFromFormPanel(JPanel panel) {
		for (Component comp : panel.getComponents()) {
			if (comp instanceof JScrollPane) {
				JScrollPane scrollPane = (JScrollPane) comp;
				Component viewport = scrollPane.getViewport().getView();
				if (viewport instanceof JTextArea) {
					return (JTextArea) viewport;
				}
			}
		}
		return null;
	}
	
	private JPanel createModernTextAreaField(String labelText, String placeholder) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setOpaque(false);
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.setMaximumSize(new Dimension(500, 150));

		JLabel label = new JLabel(labelText);
		label.setForeground(KitchenTheme.WARM_BROWN);
		label.setFont(new Font("SF Pro Text", Font.BOLD, 14));
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(label);

		panel.add(Box.createRigidArea(new Dimension(0, 8)));

		JTextArea textArea = new JTextArea(4, 0);
		textArea.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
		textArea.setForeground(Color.GRAY);
		textArea.setText(placeholder);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBorder(BorderFactory.createLineBorder(KitchenTheme.FRESH_GREEN, 2));
		scrollPane.setPreferredSize(new Dimension(500, 100));
		scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

		panel.add(scrollPane);
		return panel;
	}
	
}
