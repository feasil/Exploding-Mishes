package fr.feasil.kittens.graphic;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

import fr.feasil.utils.Utilitaire;



public class CarteAveugleCellRenderer extends DefaultListCellRenderer 
{
	private static final long serialVersionUID = 1L;
	
	private static final ImageIcon ICONE = Utilitaire.getImageIcon("card_16_16.png");
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
	{
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
		this.setIcon(ICONE);
		if ( index == 0 )
			this.setText(Integer.toString(index+1) + "�re carte");
		else
			this.setText(Integer.toString(index+1) + "�me carte");
		
		return this;
	}
	
	/*
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		int width, height = (int) this.getFontMetrics(this.getFont()).getHeight();
		double clipWidth = g2d.getClipBounds().getWidth();
		
		if (getText().startsWith("<colored>")) {
			String text = getText();
			setText("");
			super.paint(g);
			
			Color savedColor = g2d.getColor();
			
			String textFormatted = text.substring(9, text.length()-10);
			int totalWidth = 2;
			for ( String txt : textFormatted.split("�") )
			{
				if ( txt.startsWith("color=#") )
				{
					g2d.setColor(Color.decode(txt.substring(6)));
				}
				else
				{
					width = (int) this.getFontMetrics(this.getFont()).stringWidth(txt);
					g2d.drawString(txt, totalWidth, height-4);
					totalWidth += width;
				}
			}
			
			if ( totalWidth > clipWidth )
			{
				width = (int) this.getFontMetrics(this.getFont()).stringWidth("...");
				g2d.setColor(getBackground());
				g2d.fillRect((int)(clipWidth-width)-1, 1, width+1, height-3);
				g2d.setColor(savedColor);
				g2d.drawString("...", (int) (clipWidth-width), height-4);
			}
			g2d.setColor(savedColor);
			setText(text);
		}
		else {
			super.paint(g);
		}
	}*/
}
