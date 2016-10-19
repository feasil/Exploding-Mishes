package fr.feasil.kittens.cards.combinaison;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import fr.feasil.kittens.cards.Carte;
import fr.feasil.kittens.cards.Combinaison;
import fr.feasil.utils.Utilitaire;

public class CombinaisonDeuxCartes extends Combinaison
{
	private static final long serialVersionUID = 1L;
	
	public final static ImageIcon ICONE = Utilitaire.getImageIcon("two_16_16.png");
	
	private ArrayList<Carte> cartes;
	
	public CombinaisonDeuxCartes(Carte c1, Carte c2) throws Exception
	{
		if ( c1 == null || c2 == null || c1.getType() != c2.getType() )
			throw new IllegalArgumentException("Les deux cartes doivent être identiques");
		
		cartes = new ArrayList<Carte>();
		cartes.add(c1);
		cartes.add(c2);
	}
	
	@Override
	public Carte getCarte(int index) {
		return cartes.get(index);
	}
	@Override
	public int getNombreCartes() {
		return 2;
	}
	
	@Override
	public String toString() {
		return "Combinaison de deux cartes";
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
