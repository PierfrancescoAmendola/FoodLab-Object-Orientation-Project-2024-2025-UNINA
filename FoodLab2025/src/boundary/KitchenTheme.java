package boundary;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.Border;

import entity.Corso;

/**
 * Kitchen-themed UI styling with warm colors and culinary vibes
 */
public class KitchenTheme {
    
    // Kitchen color palette
    public static final Color WARM_ORANGE = new Color(255, 140, 0);      // Chef orange
    public static final Color DEEP_RED = new Color(220, 20, 60);         // Tomato red
    public static final Color CREAM_WHITE = new Color(255, 248, 220);    // Cream
    public static final Color WARM_BROWN = new Color(139, 69, 19);       // Wood brown
    public static final Color FRESH_GREEN = new Color(34, 139, 34);      // Fresh herbs
    public static final Color GOLDEN_YELLOW = new Color(255, 215, 0);    // Golden
    public static final Color SOFT_GRAY = new Color(245, 245, 245);      // Soft background
    public static final Color DARK_GRAY = new Color(64, 64, 64);         // Text dark
    public static final Color LIGHT_GRAY = new Color(200, 200, 200);     // Borders
    
    // Additional colors for reports
    public static final Color SAGE_GREEN = new Color(158, 176, 136);     // Sage green
    public static final Color TERRACOTTA = new Color(217, 151, 101);     // Terracotta
    
    // Kitchen fonts
    public static final Font TITLE_FONT = new Font("Georgia", Font.BOLD, 36);
    public static final Font SUBTITLE_FONT = new Font("Georgia", Font.ITALIC, 18);
    public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 16);
    public static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    public static final Font FIELD_FONT = new Font("Arial", Font.PLAIN, 14);
    
    /**
     * Create a kitchen-themed primary button
     */
    public static JButton createKitchenButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor;
                if (getModel().isPressed()) {
                    bgColor = DEEP_RED;
                } else if (getModel().isRollover()) {
                    bgColor = new Color(WARM_ORANGE.getRed(), WARM_ORANGE.getGreen(), WARM_ORANGE.getBlue(), 200);
                } else {
                    bgColor = WARM_ORANGE;
                }
                
                // Draw button with rounded corners and shadow
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 20, 20);
                
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 20, 20);
                
                // Add inner highlight
                g2.setColor(new Color(255, 255, 255, 50));
                g2.drawRoundRect(1, 1, getWidth() - 4, getHeight() - 4, 18, 18);
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    /**
     * Create a kitchen-themed secondary button
     */
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor;
                if (getModel().isPressed()) {
                    bgColor = new Color(WARM_BROWN.getRed(), WARM_BROWN.getGreen(), WARM_BROWN.getBlue(), 200);
                } else if (getModel().isRollover()) {
                    bgColor = new Color(WARM_BROWN.getRed(), WARM_BROWN.getGreen(), WARM_BROWN.getBlue(), 100);
                } else {
                    bgColor = new Color(WARM_BROWN.getRed(), WARM_BROWN.getGreen(), WARM_BROWN.getBlue(), 50);
                }
                
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                // Add border
                g2.setColor(WARM_BROWN);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setForeground(WARM_BROWN);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    /**
     * Create a kitchen-themed text field
     */
    public static JTextField createKitchenTextField(String placeholder) {
        JTextField field = new JTextField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw background
                g2.setColor(CREAM_WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                
                // Draw border
                g2.setColor(LIGHT_GRAY);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                
                super.paintComponent(g);
            }
        };
        
        field.setFont(FIELD_FONT);
        field.setForeground(DARK_GRAY);
        field.setOpaque(false);
        field.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        field.setPreferredSize(new Dimension(300, 40));
        
        // Add placeholder functionality
        field.setForeground(LIGHT_GRAY);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(DARK_GRAY);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(LIGHT_GRAY);
                }
            }
        });
        
        return field;
    }
    
    /**
     * Create a kitchen-themed password field
     */
    public static JPasswordField createKitchenPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw background
                g2.setColor(CREAM_WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                
                // Draw border
                g2.setColor(LIGHT_GRAY);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                
                super.paintComponent(g);
            }
        };
        
        field.setFont(FIELD_FONT);
        field.setForeground(DARK_GRAY);
        field.setOpaque(false);
        field.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        field.setPreferredSize(new Dimension(300, 40));
        
        // Add placeholder functionality
        field.setForeground(LIGHT_GRAY);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (String.valueOf(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(DARK_GRAY);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getPassword().length == 0) {
                    field.setText(placeholder);
                    field.setForeground(LIGHT_GRAY);
                }
            }
        });
        
        return field;
    }
    
    /**
     * Create a kitchen-themed label
     */
    public static JLabel createKitchenLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
    
    /**
     * Create a kitchen-themed panel with background
     */
    public static JPanel createKitchenPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, SOFT_GRAY,
                    0, getHeight(), CREAM_WHITE
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Add subtle pattern
                g2.setColor(new Color(255, 255, 255, 30));
                for (int i = 0; i < getWidth(); i += 40) {
                    for (int j = 0; j < getHeight(); j += 40) {
                        g2.fillOval(i, j, 2, 2);
                    }
                }
            }
        };
        return panel;
    }
    
    /**
     * Create a kitchen-themed card panel
     */
    public static JPanel createKitchenCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw shadow
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 25, 25);
                
                // Draw card background
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 25, 25);
                
                // Add subtle border
                g2.setColor(new Color(WARM_ORANGE.getRed(), WARM_ORANGE.getGreen(), WARM_ORANGE.getBlue(), 30));
                g2.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 25, 25);
            }
        };
        card.setOpaque(false);
        return card;
    }
    
    /**
     * Rounded border for cards and panels
     */
    public static class RoundedBorder implements Border {
        private int radius;
        private Color color;
        
        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 4, radius / 4, radius / 4, radius / 4);
        }
        
        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }
    
    public static JPanel createKithenMainPanel()
    {
    	return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw sophisticated shadow with multiple layers
                g2.setColor(new Color(0, 0, 0, 8));
                g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 30, 30);
                
                g2.setColor(new Color(0, 0, 0, 12));
                g2.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 30, 30);
                
                g2.setColor(new Color(0, 0, 0, 6));
                g2.fillRoundRect(1, 1, getWidth() - 1, getHeight() - 1, 30, 30);
                
                // Draw main panel with gradient effect
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 30, 30);
                
                // Draw subtle inner highlight
                g2.setColor(new Color(255, 255, 255, 60));
                g2.drawRoundRect(1, 1, getWidth() - 5, getHeight() - 5, 28, 28);
            }
        };
    }
    
    public static JPanel createRoundedGreyPanel()
    {
    	return new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				// Draw subtle background
				g2.setColor(new Color(248, 248, 252));
				g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
			}
		};
    }
    
    public static JPanel createRoundedPanel(Color backGround, Color borderColor) {
		return new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				// Draw card background
				g2.setColor(backGround);
				g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
				
				// Draw subtle border
				g2.setColor(borderColor);
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
			}
		};
    }
    
    public static JPanel createGradientPanel(Color backGround, Color borderColor)
    {
    	return new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				// Gradient background
				GradientPaint gradient = new GradientPaint(
					//0, 0, new Color(255, 250, 245),
					0, 0, backGround,
//					0, getHeight(), new Color(248, 248, 252)
					0, getHeight(), borderColor
				);
				g2.setPaint(gradient);
				g2.fillRect(0, 0, getWidth(), getHeight());
			}
		};
    }
    
    public static JLabel createOrangeLabel(String text, int size)
    {
    	JLabel label = new JLabel(text);
    	label.setFont(new Font("SF Pro Text", Font.BOLD, size));
    	label.setForeground(KitchenTheme.WARM_ORANGE);
    	label.setAlignmentX(Component.CENTER_ALIGNMENT);
    	return label;
    }
    
    public static JScrollPane createScrollPanel()
    {
    	return new JScrollPane() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(248, 248, 252));
				g2.fillRect(0, 0, getWidth(), getHeight());
			}
		};
    }
    
    public static void styleScrollBar(JScrollBar scrollBar) {
        scrollBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(200, 200, 200);
                this.trackColor = new Color(240, 240, 240);
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

	public static JButton createStyledActionButton(String text) {
		JButton button = new JButton(text) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				if (getModel().isPressed()) {
					g2.setColor(new Color(KitchenTheme.WARM_ORANGE.getRed(), KitchenTheme.WARM_ORANGE.getGreen(),
							KitchenTheme.WARM_ORANGE.getBlue(), 200));
				} else if (getModel().isRollover()) {
					g2.setColor(new Color(KitchenTheme.WARM_ORANGE.getRed(), KitchenTheme.WARM_ORANGE.getGreen(),
							KitchenTheme.WARM_ORANGE.getBlue(), 150));
				} else {
					g2.setColor(new Color(KitchenTheme.WARM_ORANGE.getRed(), KitchenTheme.WARM_ORANGE.getGreen(),
							KitchenTheme.WARM_ORANGE.getBlue(), 100));
				}

				g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

				// Draw border
				g2.setColor(KitchenTheme.WARM_ORANGE);
				g2.setStroke(new BasicStroke(1.5f));
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

				super.paintComponent(g);
			}
		};

		button.setFont(new Font("SF Pro Text", Font.BOLD, 12));
		button.setForeground(Color.WHITE);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setPreferredSize(new Dimension(100, 35));

		return button;
	}
	
	public static JButton createPrimaryButton(String text) {
		JButton button = new JButton(text) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				if (getModel().isPressed()) {
					g2.setColor(KitchenTheme.DEEP_RED);
				} else if (getModel().isRollover()) {
					g2.setColor(new Color(0, 122, 255, 200));
				} else {
					g2.setColor(KitchenTheme.WARM_ORANGE);
				}

				g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
				super.paintComponent(g);
			}
		};
		button.setFont(new Font("SF Pro Text", Font.BOLD, 16));
		button.setForeground(Color.WHITE);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setPreferredSize(new Dimension(200, 40));
		button.setAlignmentX(Component.CENTER_ALIGNMENT);

		return button;
	}
	
	public static void styleModernSessionComboBox(JComboBox<String> comboBox) {
		comboBox.setFont(new Font("SF Pro Text", Font.PLAIN, 15));
		comboBox.setPreferredSize(new Dimension(380, 45));
		comboBox.setMaximumSize(new Dimension(380, 45));
		comboBox.setBackground(Color.WHITE);
		comboBox.setForeground(KitchenTheme.DARK_GRAY);
		comboBox.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
						BorderFactory.createEmptyBorder(10, 15, 10, 15)));
		comboBox.setFocusable(true);
	}
}

