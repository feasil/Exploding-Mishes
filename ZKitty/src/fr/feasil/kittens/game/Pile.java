package fr.feasil.kittens.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import fr.feasil.kittens.cards.Carte;

public class Pile implements Serializable
{
	private static final long serialVersionUID = 1L;

	private LinkedList<Carte> pileDeCartes;
	
	private List<PileEventListener> listeners = new ArrayList<PileEventListener>();
	
	public Pile() 
	{
		pileDeCartes = new LinkedList<Carte>();
	}
	
	public void ajouterCarte(Carte carte)
	{
		pileDeCartes.addLast(carte);
		carteAjoutee(pileDeCartes.size());
	}
	
	public void ajouterCarte(Carte carte, int positionFromTop)
	{
		if ( (getNombreCartes() - positionFromTop) < 0 )
			throw new IllegalArgumentException("Il ne reste que " + getNombreCartes() + " dans la pile");
		pileDeCartes.add((getNombreCartes() - positionFromTop), carte);
		carteAjoutee((getNombreCartes() - positionFromTop));
	}
	
	
	public Carte prendreLaPremiereCarte()
	{
		Carte carte = pileDeCartes.removeLast();
		carteRetiree(pileDeCartes.size()+1);
		return carte;
	}
	public void retirerLaCarte(Carte carte)
	{
		int i = pileDeCartes.indexOf(carte);
		pileDeCartes.remove(carte);
		carteRetiree(i);
	}

	public void melanger()
	{
		Collections.shuffle(pileDeCartes);
		pileMelangee();
	}
	
	
	public int getNombreCartes() 
	{
		return pileDeCartes.size();
	}
	
	/**
	 * Renvoie la carte ayant pour l'index donné (en partant de la fin)
	 * @param index
	 * @return
	 */
	public Carte voirLaCarte(int positionFromTop)
	{
		if ( (getNombreCartes() - 1 - positionFromTop) < 0 )
			throw new IllegalArgumentException("Il ne reste que " + getNombreCartes() + " dans la pile");
		return pileDeCartes.get((getNombreCartes() - 1 - positionFromTop));
	}
	
	
	
	
	public void addPileEventListener(PileEventListener l)
	{
		listeners.add(l);
	}
	public void removePileEventListener(PileEventListener l)
	{
		listeners.add(l);
	}
	
	private void carteAjoutee(int index)
	{
		for ( PileEventListener l : listeners )
			l.carteAjoutee(index);
	}
	private void carteRetiree(int index)
	{
		for ( PileEventListener l : listeners )
			l.carteRetiree(index);
	}
	private void pileMelangee()
	{
		for ( PileEventListener l : listeners )
			l.pileMelangee();
	}
}
