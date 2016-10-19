package fr.feasil.kittens.cards.cats;

import javax.swing.ImageIcon;

import fr.feasil.kittens.cards.Cat;
import fr.feasil.utils.Utilitaire;

public class CatRayan extends Cat 
{
	private static final long serialVersionUID = 1L;
	
	public final static ImageIcon ICONE = Utilitaire.getImageIcon("cat4_16_16.png");
	
	
	public CatRayan(int uniqueId) {
		super(uniqueId);
	}
	
	@Override
	public ImageIcon getIcon() {
		return ICONE;
	}
	
	
}
