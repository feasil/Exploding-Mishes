package fr.feasil.kittens.game.server;

public enum ServerEventType 
{
	FIN_DE_TOUR ("finDeTour"),  
	CARTE_JOUEE_DEBUT ("carteJoueeDebut"), 
	CARTE_JOUEE_FIN ("carteJoueeFin"), 
	CARTE_PIOCHEE ("cartePiochee"), 
	JOUEUR_EXPLOSE ("joueurExplose"),
	GAME_READY ("gameReady"), 
	NOTIFY_EXCEPTION ("notifyException"), 
	SEETHEFUTURE ("seeTheFuture"), 
	FAVOR ("favor"), 
	FAVOR_FIN ("favorFin"), 
	COMBINAISONDEUXCARTES ("combinaisonDeuxCartes"), 
	COMBINAISONDEUXCARTES_FIN  ("combinaisonDeuxCartesFin"),
	COMBINAISONTROISCARTES ("combinaisonTroisCartes"), 
	COMBINAISONTROISCARTES_FIN ("combinaisonTroisCartesFin"), 
	COMBINAISONCINQCARTES ("combinaisonCinqCartes"), 
	REPOSITIONNEEXPLODING ("repositionneExploding"), 
	REPOSITIONNEEXPLODING_FIN ("repositionneExplodingFin");
	
	private String name = "";
	ServerEventType(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
}
