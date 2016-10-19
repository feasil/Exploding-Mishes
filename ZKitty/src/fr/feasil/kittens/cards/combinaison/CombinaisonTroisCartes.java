package fr.feasil.kittens.cards.combinaison;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import fr.feasil.kittens.cards.Carte;
import fr.feasil.kittens.cards.Combinaison;
import fr.feasil.utils.Utilitaire;

public class CombinaisonTroisCartes extends Combinaison
{
	private static final long serialVersionUID = 1L;
	
	public final static ImageIcon ICONE = Utilitaire.getImageIcon("three_16_16.png");
	
	private ArrayList<Carte> cartes;
	
	public CombinaisonTroisCartes(Carte c1, Carte c2, Carte c3) throws Exception
	{
		if ( c1 == null || c2 == null || c3 == null 
				|| c1.getType() != c2.getType() || c1.getType() != c3.getType() )
			throw new IllegalArgumentException("Les trois cartes doivent être identiques");
		
		cartes = new ArrayList<Carte>();
		cartes.add(c1);
		cartes.add(c2);
		cartes.add(c3);
	}
	
	@Override
	public Carte getCarte(int index) {
		return cartes.get(index);
	}
	@Override
	public int getNombreCartes() {
		return 3;
	}
	
	@Override
	public String toString() {
		return "Combinaison de trois cartes";
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
		return true;
	}
	
	
}
