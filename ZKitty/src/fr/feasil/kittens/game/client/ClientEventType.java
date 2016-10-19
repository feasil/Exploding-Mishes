package fr.feasil.kittens.game.client;

public enum ClientEventType 
{
	JOUER ("jouer"), 
	JOUER_AFTER_WAIT ("jouerAfterWait"), 
	PIOCHER ("piocher"), 
	FINISH_FAVOR ("finishFavor"), 
	FINISH_EXPLODING ("finishExploding"), 
	FINISH_COMBINAISON_DEUX_CARTES ("finishCombinaisonDeuxCartes"), 
	FINISH_COMBINAISON_TROIS_CARTES ("finishCombinaisonTroisCartes"), 
	FINISH_COMBINAISON_CINQ_CARTES ("finishCombinaisonCinqCartes");
	
	private String name = "";
	ClientEventType(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
}
