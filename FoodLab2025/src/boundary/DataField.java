package boundary;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JList;

public class DataField extends JPanel 
{

	private JComboBox<String> dayField;
    private JComboBox<String> monthField;
    private JComboBox<String> yearField;
    
	private JPanel monthPanel;
	private JPanel yearPanel;
	private JPanel dayPanel;
	private JLabel monthLabel;
	private JLabel dayLabel;
    
	public DataField() 
	{
		super();
		setBackground(Color.WHITE);
    	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    	setOpaque(false);
    	setAlignmentX(Component.CENTER_ALIGNMENT);
    	
    	JLabel thisTitle = new JLabel("Data di Nascita");
    	thisTitle.setFont(new Font("Georgia", Font.BOLD, 16));
    	thisTitle.setForeground(KitchenTheme.WARM_ORANGE);
    	thisTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
    	this.add(thisTitle);
    	
    	this.add(Box.createVerticalStrut(15));
    	
    	// Date dropdowns with compact layout
    	JPanel datePanel = new JPanel();
    	datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.X_AXIS));
    	datePanel.setOpaque(false);
    	datePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	
    	// Day dropdown
    	dayPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 0));
    	dayPanel.setOpaque(false);
    	dayLabel = new JLabel("Giorno:");
    	dayLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
    	dayLabel.setForeground(KitchenTheme.DARK_GRAY);
//    	dayPanel.add(dayLabel);
    	
    	String[] giorni = new String[31];
    	for (int i = 0; i < 31; i++) {
    		giorni[i] = String.valueOf(i + 1);
    	}
    	dayField = new JComboBox<>(giorni);
    	styleComboBox(dayField);
    	dayPanel.add(dayField);
    	//datePanel.add(dayPanel);
    	datePanel.add(Box.createHorizontalStrut(15));
    	
    	// Month dropdown
    	monthPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 0));
    	monthPanel.setOpaque(false);
    	monthLabel = new JLabel("Mese:");
    	monthLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
    	monthLabel.setForeground(KitchenTheme.DARK_GRAY);
    	monthPanel.add(monthLabel);
    	
    	String[] mesi = {
    		"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
    		"Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"
    	};
    	monthField = new JComboBox<>(mesi);
    	styleComboBox(monthField);
    	monthPanel.add(monthField);
    	datePanel.add(monthPanel);
    	datePanel.add(Box.createHorizontalStrut(15));
    	
    	// Year dropdown
    	yearPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 0));
    	yearPanel.setOpaque(false);
    	JLabel yearLabel = new JLabel("Anno:");
    	yearLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
    	yearLabel.setForeground(KitchenTheme.DARK_GRAY);
    	//yearPanel.add(yearLabel);
    	
    	int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    	String[] anni = new String[currentYear - 1900 - 18 + 1];
    	for (int i = 0; i < anni.length; i++) {
    		anni[i] = String.valueOf(currentYear - 18 - i);
    	}
    	yearField = new JComboBox<>(anni);
    	styleComboBox(yearField);
    	yearPanel.add(yearField);
    	datePanel.add(yearPanel);
    	
    	this.add(datePanel);
		dayPanel.setVisible(false);
		monthPanel.setVisible(false);
		
		yearField.addActionListener( new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(yearField.getSelectedItem() != null)
					monthPanel.setVisible(true);
				else
					monthPanel.setVisible(false);
				monthField.setSelectedItem(null);
			}
		});
		
		monthField.addActionListener( new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String month = (String)monthField.getSelectedItem();
				if(month != null)
				{
					//Calcola il numero di giorni che ha il mese
					int maxDay = getMaxMonthDay( Integer.parseInt(yearField.getSelectedItem().toString()), monthField.getSelectedIndex() + 1);
					dayField.removeAllItems();					
				
			    	for (int i = 0; i < maxDay; i++) {
			    		dayField.addItem(Integer.toString(i+1));
			    	}
			    	dayField.setSelectedItem(null);
			    	dayPanel.setVisible(true);
				}
				else
					dayPanel.setVisible(false);
			}
		});
		
		yearPanel.add(yearLabel);
		yearPanel.add(yearField);
		
		monthPanel.add(monthLabel);
		monthPanel.add(monthField);
		
		dayPanel.add(dayLabel);
		dayPanel.add(dayField);
		
		datePanel.add(yearPanel);
		datePanel.add(monthPanel);
		datePanel.add(dayPanel);
		setVisible(true);
	}
	
	
	
	public static void main (String[] args)
	{
		JFrame frame = new JFrame();
		frame.setBounds(200, 200, 400, 400);
		
		DataField data = new DataField();
		//data.setSize(100,100);
		//data.setLayout(new GridLayout(0, 1));
		//System.out.print( d );
		
		frame.setContentPane(data);
		frame.setVisible(true);
	}
	
	public static int getMaxMonthDay(int currentYear, int month)
	{
		if(month == 2)
			if( currentYear % 4 == 0 )
				return 29;
			else
				return 28;
			
		else if (Arrays.asList(4, 6, 9, 11).contains(month) )
			return 30;
		else 
			return 31;
	}


	private void styleComboBox(JComboBox<String> comboBox) {
		comboBox.setPreferredSize(new Dimension(80, 35));
		comboBox.setMinimumSize(new Dimension(80, 35));
		comboBox.setMaximumSize(new Dimension(80, 35));
		comboBox.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
		comboBox.setBackground(new Color(248, 248, 252));
		comboBox.setForeground(KitchenTheme.DARK_GRAY);
		comboBox.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
		comboBox.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				setFont(new Font("SF Pro Text", Font.PLAIN, 12));
				setHorizontalAlignment(SwingConstants.CENTER);
				if (isSelected) {
					setBackground(KitchenTheme.WARM_ORANGE);
					setForeground(Color.WHITE);
				} else {
					setBackground(new Color(248, 248, 252));
					setForeground(KitchenTheme.DARK_GRAY);
				}
				return this;
			}
		});
	}
}
