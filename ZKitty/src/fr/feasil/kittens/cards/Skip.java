package fr.feasil.kittens.cards;

import javax.swing.ImageIcon;

import fr.feasil.utils.Utilitaire;

public class Skip extends Carte 
{
	private static final long serialVersionUID = 1L;
	
	public final static ImageIcon ICONE = Utilitaire.getImageIcon("skip_16_16.png");
	
	
	public Skip(int uniqueId) {
		super(uniqueId);
	}
	
	@Override
	public ImageIcon getIcon() {
		return ICONE;
	}

	
	@Override
	public boolean isPlayable() {
		return true;
	}
	@Override
	public boolean needTargetPlayer() {
		return false;
	}
	@Override
	public boolean needPickCardType() {
		return false;
	}
}
