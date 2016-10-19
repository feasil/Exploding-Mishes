package fr.feasil.kittens.main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import fr.feasil.kittens.game.Joueur;
import fr.feasil.kittens.game.server.JeuServer;
import fr.feasil.kittens.graphic.FenetreClient;
import fr.feasil.kittens.graphic.FenetreServer;


public class ServerLauncher {
	
	
	public static void main(String args[])
	{
		
		//Fichiers non externalisés
		try {
			System.getProperties().load(ServerLauncher.class.getResourceAsStream("/fr/feasil/kittens/main/version.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//-------------------
		
		//
		String version, subversion, revision;
		if ( System.getProperty(ServerLauncher.class.getCanonicalName() + ".VERSION") != null )
			version = System.getProperty(ServerLauncher.class.getCanonicalName() + ".VERSION");
		else version = "?";
		if ( System.getProperty(ServerLauncher.class.getCanonicalName() + ".SUBVERSION") != null )
			subversion = System.getProperty(ServerLauncher.class.getCanonicalName() + ".SUBVERSION");
		else subversion = "?";
		if ( System.getProperty(ServerLauncher.class.getCanonicalName() + ".REVISION") != null )
			revision = System.getProperty(ServerLauncher.class.getCanonicalName() + ".REVISION");
		else revision = "?";
		//
		
		String title = "Exploding Mishes server";
		FenetreServer fen = new FenetreServer(title + " v" + version + "." + subversion + "." + revision);
		fen.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent evt) {
					if ( evt.getSource() instanceof FenetreClient )
						((FenetreServer) evt.getSource()).quitter();
					else
						System.exit(0);
				}
		});
		fen.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		ArrayList<Joueur> joueurs = new ArrayList<Joueur>();
		joueurs.add(new Joueur("Fabien", "vokw983"));
		joueurs.add(new Joueur("Yannick", "vjya592"));
		joueurs.add(new Joueur("Bertrand", "voex436"));
		joueurs.add(new Joueur("Julien", "xznx265"));
		
		try {
			JeuServer server = new JeuServer(joueurs);
			fen.setJeuServer(server);
		} catch (Exception e) {
			//e.printStackTrace();
			JOptionPane.showMessageDialog(fen, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
			fen.quitter();
		}
		
		
	}

}
