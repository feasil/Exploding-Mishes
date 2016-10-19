package fr.feasil.kittens.cards;

import javax.swing.ImageIcon;

import fr.feasil.utils.Utilitaire;

public class Favor extends Carte 
{
	private static final long serialVersionUID = 1L;
	
	public final static ImageIcon ICONE = Utilitaire.getImageIcon("favor_16_16.png");
	
	
	public Favor(int uniqueId) {
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
		return true;
	}
	@Override
	public boolean needPickCardType() {
		return false;
	}
}
