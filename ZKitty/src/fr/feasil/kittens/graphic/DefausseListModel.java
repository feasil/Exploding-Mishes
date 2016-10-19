package fr.feasil.kittens.graphic;

import javax.swing.DefaultListModel;

import fr.feasil.kittens.game.Pile;
import fr.feasil.kittens.game.PileEventListener;


public class DefausseListModel extends DefaultListModel implements PileEventListener
{
	private static final long serialVersionUID = 1L;
	
	private Pile defausse;
	
	public DefausseListModel(Pile defausse) 
	{
		this.defausse = defausse;
		defausse.addPileEventListener(this);
	}
	
	public void setDefausse(Pile defausse) {
		if ( this.defausse != null )
			this.defausse.removePileEventListener(this);
		this.defausse = defausse;
		this.defausse.addPileEventListener(this);
		fireContentsChanged(this, 0, getSize());
	}
	
	@Override
	public Object getElementAt(int index) 
	{
		return defausse.voirLaCarte(index);
	}
	@Override
	public int getSize() 
	{
		return defausse.getNombreCartes();
	}


	@Override
	public void carteAjoutee(int index) {
		fireIntervalAdded(this, index, index);
	}
	@Override
	public void carteRetiree(int index) {
		fireIntervalRemoved(this, index, index);
	}
	@Override
	public void pileMelangee() {
		fireContentsChanged(this, 0, getSize());
	}
}
