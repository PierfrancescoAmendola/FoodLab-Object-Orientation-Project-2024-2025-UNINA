package boundary;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FiltroPanel extends JDialog {
	private JPanel mainPanel;
	private JLabel title;
	private JPanel argomentoPanel; 
	private JLabel argomentoLabel;
	private JTextField argomentoField;

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public FiltroPanel() {
		// Configurazione base del dialogo
		setTitle("Filtro Corsi");
		setSize(450, 280);
		setLocationRelativeTo(null); // Centra nella schermata
		setResizable(false);
		
		// Pannello principale con sfondo
		mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradiente di sfondo
                GradientPaint gradient = new GradientPaint(
                    0, 0, KitchenTheme.SOFT_GRAY,
                    0, getHeight(), new Color(255, 250, 240) // Soft cream color
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                
                // Bordo sottile
                g2.setColor(KitchenTheme.LIGHT_GRAY);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };
        
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
		
		// Titolo con icona
		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		titlePanel.setOpaque(false);
		
		// Icona per il titolo
		JLabel iconLabel = new JLabel();
		try {
			ImageIcon icon = new ImageIcon("src/icons/iconafiltro.png");
			Image iconImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			iconLabel.setIcon(new ImageIcon(iconImage));
		} catch (Exception e) {
			// Fallback emoji se l'icona non esiste
			iconLabel.setText("🔍");
			iconLabel.setFont(new Font("Arial", Font.BOLD, 20));
		}
		titlePanel.add(iconLabel);
		
		title = new JLabel("Ricerca Corso");
		title.setFont(new Font("SF Pro Text", Font.BOLD, 20));
		title.setForeground(KitchenTheme.WARM_ORANGE);
		titlePanel.add(title);
		
		// Contenitore del form con stile moderno
		JPanel formContainer = new JPanel() {
			@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Sfondo bianco con angoli arrotondati
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                // Bordo sottile
                g2.setColor(new Color(KitchenTheme.LIGHT_GRAY.getRed(), 
                					  KitchenTheme.LIGHT_GRAY.getGreen(), 
                					  KitchenTheme.LIGHT_GRAY.getBlue(), 100));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
		};
		formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
		formContainer.setOpaque(false);
		formContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		// Descrizione sopra il campo input
		JLabel descriptionLabel = new JLabel("Inserisci l'argomento del corso da cercare:");
		descriptionLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
		descriptionLabel.setForeground(KitchenTheme.DARK_GRAY);
		descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		formContainer.add(descriptionLabel);
		formContainer.add(Box.createVerticalStrut(10));
		
		// Campo di ricerca migliorato
		argomentoField = KitchenTheme.createKitchenTextField("Inserisci argomento...");
		argomentoField.setPreferredSize(new Dimension(350, 40));
		argomentoField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		argomentoField.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
		formContainer.add(argomentoField);
		
		// Pannello pulsanti centrati
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
		buttonPanel.setOpaque(false);
		
		JButton cancelButton = createCancelButton("Annulla");
		cancelButton.addActionListener(e -> dispose());
		
		JButton sendButton = KitchenTheme.createKitchenButton("Cerca Corso");
		sendButton.setPreferredSize(new Dimension(150, 40));
		sendButton.addActionListener(e -> dispose());
		
		buttonPanel.add(cancelButton);
		buttonPanel.add(sendButton);
		
		// Assemblaggio interfaccia
		mainPanel.add(titlePanel);
		mainPanel.add(Box.createVerticalStrut(15));
		mainPanel.add(formContainer);
		mainPanel.add(Box.createVerticalStrut(10));
		mainPanel.add(buttonPanel);
		
		// Imposta il pannello nel dialogo
		getContentPane().add(mainPanel);
		
		// Centra il dialogo
		setLocationRelativeTo(null);
	}
	
	private JButton createCancelButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(new Color(220, 220, 220));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(240, 240, 240));
                } else {
                    g2.setColor(Color.WHITE);
                }
                
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                
                // Bordo
                g2.setColor(KitchenTheme.LIGHT_GRAY);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        button.setForeground(KitchenTheme.DARK_GRAY);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(100, 40));
        
        return button;
    }
	
	public String getArgomento() {
		// Non restituisce il placeholder
		String text = argomentoField.getText();
		if (text.equals("Inserisci argomento...")) {
			return "";
		}
		return text;
	}
	
	public static void main(String[] args) {
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		JDialog f = new FiltroPanel();
		f.setVisible(true);
	}
}
