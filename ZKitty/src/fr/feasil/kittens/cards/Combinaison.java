package fr.feasil.kittens.cards;

import fr.feasil.kittens.cards.combinaison.CombinaisonCinqCartes;
import fr.feasil.kittens.cards.combinaison.CombinaisonDeuxCartes;
import fr.feasil.kittens.cards.combinaison.CombinaisonTroisCartes;


public abstract class Combinaison extends Playable 
{
	private static final long serialVersionUID = 1L;
	
	
	
	public static Combinaison getCombinaison(Carte[] cartes) throws Exception
	{
		if ( cartes.length == 2 )
			return new CombinaisonDeuxCartes(cartes[0], cartes[1]);
		if ( cartes.length == 3 )
			return new CombinaisonTroisCartes(cartes[0], cartes[1], cartes[2]);
		if ( cartes.length == 5 )
			return new CombinaisonCinqCartes(cartes[0], cartes[1], cartes[2], cartes[3], cartes[4]);
		
		throw new IllegalArgumentException("Combinaison impossible");
	}
	
}
