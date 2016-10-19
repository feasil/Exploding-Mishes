package fr.feasil.kittens.game;

import java.util.ArrayList;

public class ListeActionsDeJeu extends ArrayList<ActionDeJeu> 
{
	private static final long serialVersionUID = 1L;
	
	
	public ActionDeJeu getLast() {
		if ( size() == 0 )
			return null;
		return get(size()-1);
	}
	
	public int getActionsDeJeuWaitingCount() {
		int nb = 0;
		for ( ActionDeJeu a : this )
			if ( a.isWaiting() )
				nb++;
		return nb;
	}
	
	public ListeActionsDeJeu getActionsDeJeuWaiting() {
		ListeActionsDeJeu tmp = new ListeActionsDeJeu();
		for ( ActionDeJeu a : this )
			if ( a.isWaiting() )
				tmp.add(a);
		return tmp;
	}
	
	public int getActionsDeJeuEnCoursCount() {
		int nb = 0;
		for ( ActionDeJeu a : this )
			if ( a.isEnCours() )
				nb++;
		return nb;
	}
}
