package boundary.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import boundary.KitchenTheme;
import boundary.MainApplication;
import controller.Controller;
import entity.Corso;
import entity.Ricetta;
import entity.Sessione;

public class IngredientDialog extends JDialog 
{
	private Controller programma;
	private Corso corso;
	
	private JPanel sessionFieldPanel;
	private JPanel recipesFieldPanel;
	private JPanel ingredientFieldPanel;
	
	private JComboBox<String> sessionComboBox;
	private JComboBox<String> recipesComboBox;
	private JButton sendButton;
	private JTextField ingredientNameField;
	private JSpinner amountField;
	
	private ArrayList<Sessione> elencoSessioni;
	private ArrayList<Ricetta> elencoRicette;
	
	public IngredientDialog(MainApplication mainApp, Controller controller, Corso corso) {
		super(mainApp, "Crea nuovo ingrediente ", true);	//Nome finestra
		programma = controller;
		this.corso = corso;
		
		this.setSize(500, 550);
		this.setLocationRelativeTo(mainApp);
		this.setLayout(new BorderLayout());
		this.setModal(true);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(35, 40, 35, 40));
		
		// Header section with cooking icon
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
		headerPanel.setOpaque(false);
		headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel iconLabel = new JLabel("🥕");
		iconLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 32));
		iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		headerPanel.add(iconLabel);
		headerPanel.add(Box.createVerticalStrut(10));
		
		JLabel titleLabel = new JLabel("Aggingi un ingrediente");
		titleLabel.setFont(new Font("SF Pro Text", Font.BOLD, 20));
		titleLabel.setForeground(KitchenTheme.WARM_ORANGE);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		headerPanel.add(titleLabel);
		
		//Close button
		headerPanel.add(Box.createHorizontalStrut(10));//Separatore
		JButton closeButton = KitchenTheme.createKitchenButton("Chiudi");
		closeButton.addActionListener(e -> {
			this.dispose();
		});
		headerPanel.add(closeButton);
		
		//Setting header size
		headerPanel.setPreferredSize(new Dimension(400, 100));
		headerPanel.setMaximumSize(headerPanel.getPreferredSize());
		
		// Form section with modern styling
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setOpaque(false);
		formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// Session selection with beautiful styling
		JLabel sessionLabel = new JLabel("📍 Seleziona la Sessione:");
		sessionComboBox = new JComboBox<>();
		formPanel.add(createSelectionField(sessionFieldPanel, sessionComboBox, sessionLabel));
		loadSessionsIntoComboBox(sessionComboBox, corso.getIdCorso());
		
		sessionComboBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int sessionIndex = sessionComboBox.getSelectedIndex() - 1;
				if(sessionIndex >= 0)	//Sessione selezionata
				{
					recipesComboBox.removeAllItems();
					loadRecipesIntoComboBox(recipesComboBox, elencoSessioni.get(sessionIndex));
					recipesFieldPanel.setVisible(true);
				}
				else	//Nessun elemento selezionato
				{
					recipesComboBox.setSelectedIndex(0);
					recipesFieldPanel.setVisible(false);
				}
			}
		});
		
		// Recipes selection
		JLabel recipesLabel = new JLabel("👨‍🍳 Nome della Ricetta:");
		recipesComboBox = new JComboBox<>();
		recipesFieldPanel = createSelectionField(recipesFieldPanel, recipesComboBox, recipesLabel);
		recipesFieldPanel.setVisible(false);
		formPanel.add(recipesFieldPanel);
		
		recipesComboBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int recipeIndex = recipesComboBox.getSelectedIndex() - 1; 
				
				if(recipeIndex >= 0)	//Sessione selezionata
				{
					ingredientFieldPanel.setVisible(true);
					sendButton.setVisible(true);
				}
				else	//Nessun elemento selezionato
				{
					ingredientFieldPanel.setVisible(false);
					sendButton.setVisible(false);
				}
			}
		});
		
		//Ingredients selection
		ingredientFieldPanel = new JPanel();
		ingredientFieldPanel.setLayout(new GridLayout(0, 2, 20, 30));
		ingredientFieldPanel.setVisible(false);
		ingredientFieldPanel.setMaximumSize(new Dimension(400,100));
		
		//Ingredient name
		JLabel ingredientNameLabel = new JLabel("Nome ingrediente");
		setFieldNameLabel(ingredientNameLabel);
		
		ingredientNameField = KitchenTheme.createKitchenTextField("");
		
		//Quantity
		JLabel amountLabel = new JLabel("Quantità");
		setFieldNameLabel(ingredientNameLabel);
		amountLabel.setPreferredSize(new Dimension(150, 30));
		amountLabel.setMaximumSize(amountLabel.getPreferredSize());
		
		amountField = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
		amountField.setPreferredSize(new Dimension(60,30));
		amountField.setMaximumSize(amountField.getPreferredSize());
		
		//Adding to form
		ingredientFieldPanel.add(ingredientNameLabel);
		ingredientFieldPanel.add(ingredientNameField);
		ingredientFieldPanel.add(amountLabel);
		ingredientFieldPanel.add(amountField);
		formPanel.add(ingredientFieldPanel);
		
		//Send button
		formPanel.add(Box.createHorizontalStrut(10));	//Separatore dal bottone
		sendButton = KitchenTheme.createPrimaryButton("🎯 Crea l'ingrediente");
		sendButton.addActionListener(e -> 
		{
			//Retrive data inserted
			int idRicetta = elencoRicette.get(recipesComboBox.getSelectedIndex()-1).getIdRicetta();
			String name = ingredientNameField.getText();
			int amount;
			
			try 
			{
				amount = (Integer) amountField.getValue();
				//controlla che i dati siano compilati
				if(name == null || name.equals(""))
					JOptionPane.showMessageDialog(this, "Inserire il nome dell'ingrediente", "Dati non validi", JOptionPane.ERROR_MESSAGE);
				else
				{
					try {
						programma.insertIngrediente(new entity.Ingrediente(idRicetta, name, amount));
						//Successo operazione
						JOptionPane.showMessageDialog(this, "Ingrediente registrato!", "Successo", JOptionPane.INFORMATION_MESSAGE);
					} catch (SQLException e1) {	//Operazione fallita
						JOptionPane.showMessageDialog(this, e1.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
					}
				}
			} catch (Exception e1) {	//Conversione fallita
				JOptionPane.showMessageDialog(this, "La quantità deve essere un numero meggiore di 0", "Dati non validi", JOptionPane.ERROR_MESSAGE);
			}	
		});	
		sendButton.setVisible(false);
		formPanel.add(sendButton);
		
		//Adding components do dialog Window
		mainPanel.add(headerPanel);
		mainPanel.add(formPanel);
		this.add(mainPanel);
		this.setVisible(true);
	}
	
	private void loadSessionsIntoComboBox(JComboBox<String> comboBox, String idCorso) {
		try 
		{
			elencoSessioni = programma.elencaSessioniPratiche(idCorso);
			
			if (elencoSessioni != null && !elencoSessioni.isEmpty()) //Controlla che ci siano delle sessioni
			{
				comboBox.addItem("Seleziona sessione");
				for (Sessione sessione : elencoSessioni) 
				{
					String sessionInfo = String.format("%d - in data %s alle ore %s", 
						sessione.getIdSessione(), 
						sessione.getData(), 
						sessione.getOraInizio()
					);
					comboBox.addItem(sessionInfo);
				}
			} else {
				comboBox.addItem("Nessuna sessione disponibile");
			}
		} catch (SQLException e) {
			comboBox.addItem("Errore nel caricamento sessioni");
		}
	}
	
	public JPanel createSelectionField (JPanel fieldPanel, JComboBox<String> comboBox, JLabel infoLabel)
	{
		//Initialize panel
		fieldPanel = new JPanel();
		fieldPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		//Setting label
		setFieldNameLabel(infoLabel);
		//Setting comboBox
		KitchenTheme.styleModernSessionComboBox(comboBox);
		comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		//Adding to panel
		fieldPanel.add(infoLabel);
		fieldPanel.add(Box.createVerticalStrut(15));
		fieldPanel.add(comboBox);
		fieldPanel.add(Box.createVerticalStrut(20));
		//Setting panel dimesion
		fieldPanel.setPreferredSize(new Dimension(350, 100));
		fieldPanel.setMaximumSize(fieldPanel.getPreferredSize());
		return fieldPanel;
	}
	
	private void loadRecipesIntoComboBox(JComboBox<String> comboBox, Sessione sessione) 
	{
		try 
		{
			elencoRicette = programma.elencaRicette(sessione.getIdSessione());
			if(elencoRicette != null && !elencoRicette.isEmpty())
			{
				comboBox.addItem("Seleziona ricetta");
				for(Ricetta ricetta : elencoRicette)
				{
					String ricettaInfo = String.format("ID: %d - Nome: %s", 
						ricetta.getIdRicetta(), 
						ricetta.getNome()
					);
					comboBox.addItem(ricettaInfo);
				}
			}else
				comboBox.addItem("Nessuna ricetta");	
		} catch (SQLException e) {
			comboBox.addItem("Errore nel caricamento ricette");
		}
		comboBox.setSelectedIndex(0);
	}
	
	private void setFieldNameLabel(JLabel infoLabel)
	{
		infoLabel.setFont(new Font("SF Pro Text", Font.BOLD, 16));
		infoLabel.setForeground(KitchenTheme.DARK_GRAY);
		infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
}
