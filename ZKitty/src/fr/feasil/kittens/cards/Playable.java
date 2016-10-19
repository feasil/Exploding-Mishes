package fr.feasil.kittens.cards;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import javax.swing.ImageIcon;

public abstract class Playable implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	
	private static HashMap<Class<Playable>, Playable> icones = new HashMap<Class<Playable>, Playable>();
	
	public abstract int getNombreCartes();
	public abstract Carte getCarte(int index);
	
	public abstract boolean isPlayable();
	public abstract boolean needTargetPlayer();
	public abstract boolean needPickCardType();
	
	public abstract ImageIcon getIcon();
	
	public static ImageIcon getIcon(Class<Playable> type)
	{
		if ( !icones.containsKey(type) )
			try {
				icones.put(type, type.getDeclaredConstructor(int.class).newInstance(-1));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		return icones.get(type).getIcon();
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if ( !(obj instanceof Playable) )
			return false;
		Playable p = (Playable) obj;
		if ( getNombreCartes() != p.getNombreCartes() )
			return false;
		
		boolean test;
		for ( int i = 0 ; i < getNombreCartes() ; i++ )
		{
			test = false;
			for ( int j = 0 ; j < p.getNombreCartes() ; j++ )
				if ( getCarte(i).equals(p.getCarte(j)) )
				{
					test = true;
					break;
				}
			if ( !test )
				return false;
		}
		
		return true;
	}
	
	
	
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append(getCarte(0));
		for ( int i = 1 ; i < getNombreCartes() ; i++ )
		{
			sb.append(", ");
			sb.append(getCarte(i));
		}
		sb.append("]");
		return sb.toString();
	}
}
