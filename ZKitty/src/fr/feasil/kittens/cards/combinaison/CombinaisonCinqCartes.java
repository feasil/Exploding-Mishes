package fr.feasil.kittens.cards.combinaison;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import fr.feasil.kittens.cards.Carte;
import fr.feasil.kittens.cards.Combinaison;
import fr.feasil.utils.Utilitaire;

public class CombinaisonCinqCartes extends Combinaison
{
	private static final long serialVersionUID = 1L;
	
	public final static ImageIcon ICONE = Utilitaire.getImageIcon("five_16_16.png");
	
	private ArrayList<Carte> cartes;
	
	public CombinaisonCinqCartes(Carte c1, Carte c2, Carte c3, Carte c4, Carte c5) throws Exception
	{
		if ( c1 == null || c2 == null || c3 == null || c4 == null || c5 == null
				|| c1.getType() == c2.getType() 
				|| c1.getType() == c3.getType()
				|| c1.getType() == c4.getType()
				|| c1.getType() == c5.getType()
				|| c2.getType() == c3.getType()
				|| c2.getType() == c4.getType()
				|| c2.getType() == c5.getType()
				|| c3.getType() == c4.getType()
				|| c3.getType() == c5.getType()
				|| c4.getType() == c5.getType() )
			throw new IllegalArgumentException("Les cinq cartes doivent être différentes");
		
		cartes = new ArrayList<Carte>();
		cartes.add(c1);
		cartes.add(c2);
		cartes.add(c3);
		cartes.add(c4);
		cartes.add(c5);
	}
	
	@Override
	public Carte getCarte(int index) {
		return cartes.get(index);
	}
	@Override
	public int getNombreCartes() {
		return 5;
	}
	
	@Override
	public String toString() {
		return "Combinaison de cinq cartes";
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
