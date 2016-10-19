package fr.feasil.kittens.main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import fr.feasil.kittens.game.Joueur;
import fr.feasil.kittens.game.client.JeuClient;
import fr.feasil.kittens.graphic.FenetreClient;


public class ClientLauncher {
	
	
	public static void main(String args[])
	{
		
		//Fichiers non externalisés
		try {
			System.getProperties().load(ClientLauncher.class.getResourceAsStream("/fr/feasil/kittens/main/version.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//-------------------
		
		//
		String version, subversion, revision;
		if ( System.getProperty(ClientLauncher.class.getCanonicalName() + ".VERSION") != null )
			version = System.getProperty(ClientLauncher.class.getCanonicalName() + ".VERSION");
		else version = "?";
		if ( System.getProperty(ClientLauncher.class.getCanonicalName() + ".SUBVERSION") != null )
			subversion = System.getProperty(ClientLauncher.class.getCanonicalName() + ".SUBVERSION");
		else subversion = "?";
		if ( System.getProperty(ClientLauncher.class.getCanonicalName() + ".REVISION") != null )
			revision = System.getProperty(ClientLauncher.class.getCanonicalName() + ".REVISION");
		else revision = "?";
		//
		
		final String title = "Exploding Mishes" + " v" + version + "." + subversion + "." + revision;
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("vokw983", "Fabien");
		map.put("vjya592", "Yannick");
		map.put("voex436", "Bertrand");
		map.put("xznx265", "Julien");
		
		String idrh = System.getProperty("user.name").toLowerCase();
		String nom = map.get(idrh);
		
		/*ArrayList<Joueur> joueurs = new ArrayList<Joueur>();
		joueurs.add(new Joueur("Fabien", "vokw983"));
		joueurs.add(new Joueur("Yannick", "vjya592"));
		joueurs.add(new Joueur("Bertrand", "voex436"));
		joueurs.add(new Joueur("Julien", "xznx265"));
		
		JeuServer server = new JeuServer(joueurs);*/
		
		
		Joueur joueur = new Joueur(nom, idrh);
		FenetreClient fen = new FenetreClient(title + " - " + nom);
		fen.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent evt) {
					if ( evt.getSource() instanceof FenetreClient )
						((FenetreClient) evt.getSource()).quitter();
					else
						System.exit(0);
				}
		});
		fen.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		
		JeuClient jc;
		try {
			jc = new JeuClient(joueur);
			fen.setJeuClient(jc);
		} catch (Exception e) {
			e.printStackTrace();
			if ( e.getMessage() != null )
				JOptionPane.showMessageDialog(fen, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
			else
				JOptionPane.showMessageDialog(fen, "Serveur introuvable", "Erreur", JOptionPane.ERROR_MESSAGE);
			fen.quitter();
		}
		
		
	}

}
