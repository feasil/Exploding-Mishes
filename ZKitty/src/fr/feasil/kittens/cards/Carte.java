package fr.feasil.kittens.cards;


public abstract class Carte extends Playable implements Comparable<Carte>
{
	private static final long serialVersionUID = 1L;
	
	// Identifiant unique de la carte
	private final int uniqueId;
	
	public Carte(int uniqueId)
	{
		this.uniqueId = uniqueId;
	}
	
	public int getUniqueId() {
		return uniqueId;
	}
	
	
	@Override
	public Carte getCarte(int index) {
		return this;
	}
	@Override
	public int getNombreCartes() {
		return 1;
	}
	
	
	@SuppressWarnings("unchecked")
	public Class<Carte> getType()
	{
		return (Class<Carte>) this.getClass();
	}
	
	
	
	@Override
	public boolean equals(Object obj) 
	{
		if ( !(obj instanceof Carte) )
			return false;
		return getType().equals(((Carte) obj).getType()) 
					&& getUniqueId() == ((Carte) obj).getUniqueId();
	}
	
	@Override
	public String toString() {
		return getType().getSimpleName() + " (" + getUniqueId() + ")";
	}
	
	@Override
	public int compareTo(Carte o) {
		if ( o == null )
			return 1;
		if ( !getType().equals(o.getType()) )
			return getType().getSimpleName().compareTo(o.getType().getSimpleName());
		else 
			return getUniqueId() - o.getUniqueId();
	}
}
