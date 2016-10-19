package fr.feasil.kittens.cards;

public abstract class Cat extends Carte
{
	private static final long serialVersionUID = 1L;
	
	public Cat(int uniqueId) {
		super(uniqueId);
	}
	
	@Override
	public boolean isPlayable() {
		return false;
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
