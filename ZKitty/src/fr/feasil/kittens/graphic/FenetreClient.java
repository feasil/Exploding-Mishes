package fr.feasil.kittens.graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.feasil.kittens.cards.Carte;
import fr.feasil.kittens.cards.Combinaison;
import fr.feasil.kittens.cards.Exploding;
import fr.feasil.kittens.cards.Favor;
import fr.feasil.kittens.cards.Nope;
import fr.feasil.kittens.cards.Playable;
import fr.feasil.kittens.cards.SeeTheFuture;
import fr.feasil.kittens.cards.combinaison.CombinaisonCinqCartes;
import fr.feasil.kittens.cards.combinaison.CombinaisonDeuxCartes;
import fr.feasil.kittens.cards.combinaison.CombinaisonTroisCartes;
import fr.feasil.kittens.game.ActionDeJeu;
import fr.feasil.kittens.game.Joueur;
import fr.feasil.kittens.game.client.JeuClient;
import fr.feasil.kittens.game.server.ServerEventType;
import fr.feasil.kittens.graphic.dialog.DialogPickCard;
import fr.feasil.kittens.graphic.dialog.DialogPickCardType;
import fr.feasil.kittens.graphic.dialog.DialogPickPlayer;
import fr.feasil.kittens.graphic.dialog.DialogPickPosition;
import fr.feasil.kittens.graphic.dialog.DialogSeeCards;
import fr.feasil.utils.Utilitaire;


public class FenetreClient extends JFrame implements Observer
{
	private static final long serialVersionUID = 1L;
	
	//*-*-*-*-*-*-*-*-
	
	private JPanel panPlateau, panJ1, panJ2, panJ3, panJ4, panPioche;
	private JLabel lblJ1, lblJ2, lblJ3, lblJ4;
	private JList listJ1, listJ2, listJ3, listJ4;
	private JScrollPane scrollJ1, scrollJ2, scrollJ3, scrollJ4;
	private JButton btnPlayJ1, btnPlayJ2, btnPlayJ3, btnPlayJ4;
	private JButton btnNopeJ1, btnNopeJ2, btnNopeJ3, btnNopeJ4;
	private JButton btnPiocheJ1, btnPiocheJ2, btnPiocheJ3, btnPiocheJ4;
	
	
	//private JLabel lblCountCartes;
	private JButton btnCountCartes;
	//private JLabel lblLastCardPlayed;
	private JList listDefausse;
	
	private JLabel[] lbl;
	private JList[] list;
	private JScrollPane[] scroll;
	private JButton[] btnPlay;
	private JButton[] btnPioche;
	private JButton[] btnNope;
	private int indexJoueur;
	
	private JLabel statusBar;
	//------------------
	
	private JeuClient jeuClient;
	
	private DefausseListModel modelDefausse;
	
	
	public FenetreClient(String arg0)
		throws HeadlessException
	{
		super(arg0);pack();
		initFrame();
		initComponents();
		addComponents();
		
		
		setMinimumSize(new Dimension(400, 400));
		
		setVisible(true);
		
	}
	
	private void initFrame()
	{
		getContentPane().setLayout(new GridBagLayout());
		//setSize(500, 600);
		setSize(600, 600);
		//setExtendedState(this.getExtendedState() | MAXIMIZED_BOTH);
	}

	private void initComponents()
	{
		lblJ1 = new JLabel();
		lblJ2 = new JLabel();
		lblJ3 = new JLabel();
		lblJ4 = new JLabel();
		
		listJ1 = new JList(); listJ1.setCellRenderer(new CarteAveugleCellRenderer());
		listJ2 = new JList(); listJ2.setCellRenderer(new CarteAveugleCellRenderer());
		listJ3 = new JList(); listJ3.setCellRenderer(new CarteAveugleCellRenderer());
		listJ4 = new JList(); listJ4.setCellRenderer(new CarteAveugleCellRenderer());
		scrollJ1 = new JScrollPane(listJ1);
		scrollJ2 = new JScrollPane(listJ2);
		scrollJ3 = new JScrollPane(listJ3);
		scrollJ4 = new JScrollPane(listJ4);
		
		btnPlayJ1 = new JButton("Jouer");
		btnPiocheJ1 = new JButton("Piocher");
		btnNopeJ1 = new JButton("Nope !");
		btnPlayJ2 = new JButton("Jouer");
		btnPiocheJ2 = new JButton("Piocher");
		btnNopeJ2 = new JButton("Nope !");
		btnPlayJ3= new JButton("Jouer");
		btnPiocheJ3 = new JButton("Piocher");
		btnNopeJ3 = new JButton("Nope !");
		btnPlayJ4 = new JButton("Jouer");
		btnPiocheJ4 = new JButton("Piocher");
		btnNopeJ4 = new JButton("Nope !");
		
		lbl = new JLabel[]{lblJ1, lblJ2, lblJ3, lblJ4};
		list = new JList[]{listJ1, listJ2, listJ3, listJ4};
		scroll = new JScrollPane[]{scrollJ1, scrollJ2, scrollJ3, scrollJ4};
		btnPlay = new JButton[]{btnPlayJ1, btnPlayJ2, btnPlayJ3, btnPlayJ4};
		btnPioche = new JButton[]{btnPiocheJ1, btnPiocheJ2, btnPiocheJ3, btnPiocheJ4};
		btnNope = new JButton[]{btnNopeJ1, btnNopeJ2, btnNopeJ3, btnNopeJ4};
		
		ActionListener alPlay = new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) 
			{
				((JButton)e.getSource()).setEnabled(false);
				
				try {
					int idxJoueur = Integer.parseInt(e.getActionCommand());
					if ( list[idxJoueur].getSelectedValue() != null )
					{
						Joueur j = jeuClient.getJoueur();
						Playable c; 
						if ( list[idxJoueur].getSelectedValues().length == 1 )
						{//Une seule carte sélectionnée
							c = (Carte) list[idxJoueur].getSelectedValue();
						}
						else 
						{//Plusieurs cartes sélectionnées
							Carte[] cartes = new Carte[list[idxJoueur].getSelectedValues().length];
							for ( int i = 0 ; i < cartes.length ; i++ )
								cartes[i] = (Carte) list[idxJoueur].getSelectedValues()[i];
							c = Combinaison.getCombinaison(cartes);
						}
						Joueur cible = null;
						if ( c.needTargetPlayer() )
						{//Si une cible est nécessaire
							List<Joueur> joueurs = jeuClient.getJoueursEnJeu();
							joueurs.remove(j);
							Joueur[] tmp = new Joueur[joueurs.size()];
							for ( int i = 0 ; i < tmp.length ; i++ )
								tmp[i] = joueurs.get(i);
							cible = choisirUnJoueur(c.toString() + " to ?", c.getIcon(), tmp);
							if ( cible == null )
								return;
						}
						Class<Carte> typeDeCarte = null;
						if ( c.needPickCardType() )
						{
							typeDeCarte = choisirUnTypeDeCarte("Choisir un type de carte", c.getIcon(), true, false);
							if ( typeDeCarte == null )
								return;
						}
						
						jeuClient.jouer(j, c, cible, typeDeCarte);
					}
					list[idxJoueur].setSelectedValue(null, false);
				} catch(Exception exc) {
					JOptionPane.showMessageDialog(FenetreClient.this, exc.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
				}
				
				
				((JButton)e.getSource()).setEnabled(true);
			}
		};
		ActionListener alPioche = new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) 
			{
				((JButton)e.getSource()).setEnabled(false);
				
				jeuClient.piocher(jeuClient.getJoueur());
				
				((JButton)e.getSource()).setEnabled(true);
			}
		};
		ActionListener alNope = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				try {
					//int idxJoueur = Integer.parseInt(e.getActionCommand());
					Joueur j = jeuClient.getJoueur();
					Playable c = getFirstNope();
					Joueur cible = null;
					if ( c != null )
						jeuClient.jouer(j, c, cible, null);
						
					list[indexJoueur].setSelectedValue(null, false);
				} catch(Exception exc) {
					JOptionPane.showMessageDialog(FenetreClient.this, exc.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		};
		
		btnPlayJ1.setActionCommand("0");
		btnPlayJ1.addActionListener(alPlay);
		btnPlayJ2.setActionCommand("1");
		btnPlayJ2.addActionListener(alPlay);
		btnPlayJ3.setActionCommand("2");
		btnPlayJ3.addActionListener(alPlay);
		btnPlayJ4.setActionCommand("3");
		btnPlayJ4.addActionListener(alPlay);
		
		btnPiocheJ1.setActionCommand("0");
		btnPiocheJ1.addActionListener(alPioche);
		btnPiocheJ2.setActionCommand("1");
		btnPiocheJ2.addActionListener(alPioche);
		btnPiocheJ3.setActionCommand("2");
		btnPiocheJ3.addActionListener(alPioche);
		btnPiocheJ4.setActionCommand("3");
		btnPiocheJ4.addActionListener(alPioche);
		
		btnNopeJ1.setActionCommand("0");
		btnNopeJ1.addActionListener(alNope);
		btnNopeJ2.setActionCommand("1");
		btnNopeJ2.addActionListener(alNope);
		btnNopeJ3.setActionCommand("2");
		btnNopeJ3.addActionListener(alNope);
		btnNopeJ4.setActionCommand("3");
		btnNopeJ4.addActionListener(alNope);
		
		ListSelectionListener listSelect = new ListSelectionListener() {
			int id;
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if ( arg0.getSource() == listJ1 )
					id = 0;
				else if ( arg0.getSource() == listJ2 )
					id = 1;
				else if ( arg0.getSource() == listJ3 )
					id = 2;
				else if ( arg0.getSource() == listJ4 )
					id = 3;
				else id = 0;
				btnPlay[id].setEnabled(list[id].getSelectedIndex()!=-1);
				
			}
		};
		listJ1.addListSelectionListener(listSelect);
		listJ2.addListSelectionListener(listSelect);
		listJ3.addListSelectionListener(listSelect);
		listJ4.addListSelectionListener(listSelect);
		
		//lblLastCardPlayed = new JLabel();
		//lblCountCartes = new JLabel();
		btnCountCartes = new JButton(Utilitaire.getImageIcon("card_16_16.png"));
		listDefausse = new JList();
		listDefausse.setCellRenderer(new CarteCellRenderer());
		
		statusBar = new JLabel(" ");statusBar.setIcon(Utilitaire.getImageIcon("bomb_16_16.png"));
	}
	
	private void addComponents()
	{
		GridBagConstraints c = new GridBagConstraints();
		
		panPlateau = new JPanel(new GridBagLayout());
		
		panJ1 = new JPanel(new GridBagLayout());
		{
			GridBagConstraints cPan = new GridBagConstraints();
			cPan.gridx = 0; cPan.gridy = 0;
			cPan.gridwidth = 3; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 10; cPan.weighty = 0;
			cPan.insets = new Insets(4, 4, 4, 4);
			panJ1.add(lblJ1, cPan);
			
			cPan.gridx = 0; cPan.gridy = 1;
			cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 10; cPan.weighty = 10;
			panJ1.add(scrollJ1, cPan);
			
			cPan.gridx = 0; cPan.gridy = 2;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 5; cPan.weighty = 0;
			panJ1.add(btnPlayJ1, cPan);
			
			cPan.gridx = 1; cPan.gridy = 2;
			panJ1.add(btnPiocheJ1, cPan);
			
			cPan.gridx = 2; cPan.gridy = 2;
			panJ1.add(btnNopeJ1, cPan);
		}
		c.gridx = 1; c.gridy = 0;
		c.gridwidth = 1; c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH; c.weightx = 10; c.weighty = 10;
		c.insets = new Insets(4, 4, 4, 4);
		panPlateau.add(panJ1, c);
		
		
		panJ2 = new JPanel(new GridBagLayout());
		{
			GridBagConstraints cPan = new GridBagConstraints();
			cPan.gridx = 0; cPan.gridy = 0;
			cPan.gridwidth = 3; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 10; cPan.weighty = 0;
			cPan.insets = new Insets(4, 4, 4, 4);
			panJ2.add(lblJ2, cPan);
			
			cPan.gridx = 0; cPan.gridy = 1;
			cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 10; cPan.weighty = 10;
			panJ2.add(scrollJ2, cPan);
			
			cPan.gridx = 0; cPan.gridy = 2;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 5; cPan.weighty = 0;
			panJ2.add(btnPlayJ2, cPan);
			
			cPan.gridx = 1; cPan.gridy = 2;
			panJ2.add(btnPiocheJ2, cPan);
			
			cPan.gridx = 2; cPan.gridy = 2;
			panJ2.add(btnNopeJ2, cPan);
		}
		c.gridx = 2; c.gridy = 1;
		c.gridwidth = 1; c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH; c.weightx = 10; c.weighty = 10;
		c.insets = new Insets(4, 4, 4, 4);
		panPlateau.add(panJ2, c);
		
		
		panJ3 = new JPanel(new GridBagLayout());
		{
			GridBagConstraints cPan = new GridBagConstraints();
			cPan.gridx = 0; cPan.gridy = 0;
			cPan.gridwidth = 3; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 10; cPan.weighty = 0;
			cPan.insets = new Insets(4, 4, 4, 4);
			panJ3.add(lblJ3, cPan);
			
			cPan.gridx = 0; cPan.gridy = 1;
			cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 10; cPan.weighty = 10;
			panJ3.add(scrollJ3, cPan);
			
			cPan.gridx = 0; cPan.gridy = 2;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 5; cPan.weighty = 0;
			panJ3.add(btnPlayJ3, cPan);
			
			cPan.gridx = 1; cPan.gridy = 2;
			panJ3.add(btnPiocheJ3, cPan);
			
			cPan.gridx = 2; cPan.gridy = 2;
			panJ3.add(btnNopeJ3, cPan);
		}
		c.gridx = 1; c.gridy = 2;
		c.gridwidth = 1; c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH; c.weightx = 10; c.weighty = 10;
		c.insets = new Insets(4, 4, 4, 4);
		panPlateau.add(panJ3, c);
		
		
		panJ4 = new JPanel(new GridBagLayout());
		{
			GridBagConstraints cPan = new GridBagConstraints();
			cPan.gridx = 0; cPan.gridy = 0;
			cPan.gridwidth = 3; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 10; cPan.weighty = 0;
			cPan.insets = new Insets(4, 4, 4, 4);
			panJ4.add(lblJ4, cPan);
			
			cPan.gridx = 0; cPan.gridy = 1;
			cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 10; cPan.weighty = 10;
			panJ4.add(scrollJ4, cPan);
			
			cPan.gridx = 0; cPan.gridy = 2;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 5; cPan.weighty = 0;
			panJ4.add(btnPlayJ4, cPan);
			
			cPan.gridx = 1; cPan.gridy = 2;
			panJ4.add(btnPiocheJ4, cPan);
			
			cPan.gridx = 2; cPan.gridy = 2;
			panJ4.add(btnNopeJ4, cPan);
		}
		c.gridx = 0; c.gridy = 1;
		c.gridwidth = 1; c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH; c.weightx = 10; c.weighty = 10;
		c.insets = new Insets(4, 4, 4, 4);
		panPlateau.add(panJ4, c);
		
		
		panPioche = new JPanel(new GridBagLayout());
		{
			GridBagConstraints cPan = new GridBagConstraints();
			cPan.gridx = 0; cPan.gridy = 0;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 10; cPan.weighty = 0;
			cPan.insets = new Insets(4, 4, 4, 4);
			//panPioche.add(new JLabel("PIOCHE !!"), cPan);
			
			/*cPan.gridx = 0; cPan.gridy = 1;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 10; cPan.weighty = 10;
			cPan.insets = new Insets(4, 4, 4, 4);
			panPioche.add(lblLastCardPlayed, cPan);*/
			
			cPan.gridx = 0; cPan.gridy = 0;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 10; cPan.weighty = 10;
			cPan.insets = new Insets(4, 4, 4, 4);
			//panPioche.add(lblCountCartes, cPan);
			panPioche.add(btnCountCartes, cPan);
			
			cPan.gridx = 1; cPan.gridy = 0;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 10; cPan.weighty = 10;
			cPan.insets = new Insets(4, 4, 4, 4);
			panPioche.add(new JScrollPane(listDefausse), cPan);
			
			
		}
		c.gridx = 1; c.gridy = 1;
		c.gridwidth = 1; c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH; c.weightx = 10; c.weighty = 10;
		c.insets = new Insets(4, 4, 4, 4);
		panPlateau.add(panPioche, c);
		
		
		
		
		
		
		c = new GridBagConstraints();
		c.gridwidth = 1;
		c.gridx = 0; c.gridy = 0;
		c.fill = GridBagConstraints.BOTH; c.weightx = 10; c.weighty = 10;
		c.insets = new Insets(4, 4, 2, 4);
		getContentPane().add(panPlateau, c);
		
		/*c.gridy = 1;
		c.fill = GridBagConstraints.BOTH; c.weightx = 10; c.weighty = 10;
		getContentPane().add(splitPane, c);
		*/
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 10; c.weighty = 0;
		getContentPane().add(statusBar, c);
	}
	
	
	
	
	public void setJeuClient(JeuClient jeuClient) 
	{
		this.jeuClient = jeuClient;
		if ( jeuClient != null )
		{
			if ( jeuClient.getNombreJoueurs() != 4 )
				;//throw new IllegalArgumentException("Pour le moment c'est pour 4 joueurs !");
			
			for ( int i = 0 ; i < btnPioche.length/*jeuClient.getJoueurs().size()*/ ; i++ )
			{
				btnPioche[i].setVisible(false);
				btnPlay[i].setVisible(false);
				btnNope[i].setVisible(false);
				scroll[i].setVisible(false);
			}
			
			indexJoueur = jeuClient.getJoueurs().indexOf(jeuClient.getJoueur());
			//btnPioche[indexJoueur].setVisible(true);
			btnCountCartes.setActionCommand(btnPioche[indexJoueur].getActionCommand());
			for ( ActionListener l : btnPioche[indexJoueur].getActionListeners() )
				btnCountCartes.addActionListener(l);
			btnPioche[indexJoueur] = btnCountCartes;
			
			btnPlay[indexJoueur].setVisible(true);
			btnNope[indexJoueur].setVisible(true);
			list[indexJoueur].setCellRenderer(new CarteCellRenderer());
			scroll[indexJoueur].setVisible(true);
			
			
			modelDefausse = new DefausseListModel(jeuClient.getDefausse());
			listDefausse.setModel(modelDefausse);
			
			
			actualiserJoueurs();
			jeuClient.addObserver(this);
		}
	}
	
	
	
	private void actualiserJoueurs() 
	{
		for ( int i = 0 ; i < jeuClient.getNombreJoueurs() ; i++ )
		{
			lbl[i].setText(jeuClient.getJoueurs().get(i).getNom() + " (" + jeuClient.getJoueurs().get(i).getMain().size() + ")");
			lbl[i].setForeground(Color.BLACK);
			if ( !jeuClient.getJoueurs().get(i).isEnJeu() )
				lbl[i].setForeground(Color.RED);
			list[i].setListData(new Vector<Carte>(jeuClient.getJoueurs().get(i).getMain()));
			
			btnPlay[i].setEnabled(false);
			btnPioche[i].setEnabled(false);
			btnNope[i].setEnabled(jeuClient.getJoueurs().get(i).hasNope());
		}
		
		int idJoueurActuel = jeuClient.getJoueurs().indexOf(jeuClient.getJoueurActuel());
		if ( jeuClient.isAttack() )
			lbl[idJoueurActuel].setText(lbl[idJoueurActuel].getText() + " x2");
		lbl[idJoueurActuel].setForeground(Color.BLUE);
		btnPlay[idJoueurActuel].setEnabled(list[idJoueurActuel].getSelectedIndex()!=-1);
		btnPioche[idJoueurActuel].setEnabled(true);
		
		//lblCountCartes.setText("Pioche : " + jeuClient.getNombrePioche() + " / Defausse : " + jeuClient.getNombreDefausse());
		btnCountCartes.setText("Pioche : " + jeuClient.getNombrePioche());// + " / Defausse : " + jeuClient.getNombreDefausse());
	}
	
	
	

	/**
	 * Quitte l'application
	 */
	public void quitter()
	{
		System.exit(0);
	}
	
	
	
	
	
	
	
	@SuppressWarnings({ "unchecked" })
	public void update(Observable o, final Object arg)
	{
		
		if ( arg instanceof Object[] 
				&& ((Object[]) arg)[0] instanceof String )
		{//{"action", JoueurConcerné, ...}
			
			new Thread() {
				public void run() {
					
					String action = (String) ((Object[]) arg)[0];
					if ( "seeTheFuture".equals(action) )
					{
						ActionDeJeu actionDeJeu = (ActionDeJeu) ((Object[]) arg)[1];
						Carte[] cartes = (Carte[]) ((Object[]) arg)[2];
						voirCartes(actionDeJeu.getJoueur().getNom() + " :: See The Future", SeeTheFuture.ICONE, cartes);
					}
					else if ( "favor".equals(action) )
					{//Pour répondre à un favor
						ActionDeJeu actionDeJeu = (ActionDeJeu) ((Object[]) arg)[1];
						Carte[] cartes = (Carte[]) ((Object[]) arg)[2];
						
						Playable nope = getFirstNope();
						
						DialogPickCard dial = choisirUneCarte(actionDeJeu.getCible().getNom() + " :: Favor to " + actionDeJeu.getJoueur().getNom(), Favor.ICONE, cartes, false, false, (nope!=null));
						while ( dial.getCarte() == null && !dial.isNoped() )
						{
							JOptionPane.showMessageDialog(FenetreClient.this, "Vous devez donner une carte à " + actionDeJeu.getJoueur().getNom(), "Attention", JOptionPane.WARNING_MESSAGE);
							dial = choisirUneCarte(actionDeJeu.getCible().getNom() + " :: Favor to " + actionDeJeu.getJoueur().getNom(), Favor.ICONE, cartes, false, false, (nope!=null));
						}
						if ( (nope!=null) && dial.isNoped() )
						{
							jeuClient.jouer(jeuClient.getJoueur(), nope, null, null);
							jeuClient.finishFavor(actionDeJeu, null);
						}
						else
							jeuClient.finishFavor(actionDeJeu, dial.getCarte());
					}
					else if ( "favorFin".equals(action) )
					{
						ActionDeJeu actionDeJeu = (ActionDeJeu) ((Object[]) arg)[1];
						Carte carte =  (Carte) ((Object[]) arg)[2];
						
						JOptionPane.showMessageDialog(FenetreClient.this, actionDeJeu.getCible().getNom() + " t'a donné : " + carte.toString(), "Information", JOptionPane.INFORMATION_MESSAGE);
					}
					else if ( "combinaisonDeuxCartes".equals(action) )
					{
						ActionDeJeu actionDeJeu = (ActionDeJeu) ((Object[]) arg)[1];
						//TODO ne pas envoyer les cartes mais juste le nombre de cartes
						Carte[] cartes =  (Carte[]) ((Object[]) arg)[2];
						
						Carte carte = choisirUneCarte(actionDeJeu.getJoueur().getNom() + " :: Fais ton choix dans la main de " + actionDeJeu.getCible().getNom(), CombinaisonDeuxCartes.ICONE, cartes, false, true, false).getCarte();
						while (carte == null)
						{
							JOptionPane.showMessageDialog(FenetreClient.this, "Vous devez choisir une carte dans la main de " + actionDeJeu.getCible().getNom(), "Attention", JOptionPane.WARNING_MESSAGE);
							carte = choisirUneCarte(actionDeJeu.getJoueur().getNom() + " :: Fais ton choix dans la main de " + actionDeJeu.getCible().getNom(), CombinaisonDeuxCartes.ICONE, cartes, false, true, false).getCarte();
						}
						jeuClient.finishCombinaisonDeuxCartes(actionDeJeu, carte);
						
					}
					else if ( "combinaisonDeuxCartesDTC".equals(action) )
					{
						ActionDeJeu actionDeJeu = (ActionDeJeu) ((Object[]) arg)[1];
						
						JOptionPane.showMessageDialog(FenetreClient.this, actionDeJeu.getJoueur().getNom() + " pioche dans ton jeu...", "Attention", JOptionPane.WARNING_MESSAGE);
					}
					else if ( "combinaisonDeuxCartesFin".equals(action) )
					{
						ActionDeJeu actionDeJeu = (ActionDeJeu) ((Object[]) arg)[1];
						Carte carte =  (Carte) ((Object[]) arg)[2];
						
						JOptionPane.showMessageDialog(FenetreClient.this, "Tu as pris à " + actionDeJeu.getCible().getNom() + " : " + carte.toString(), "Information", JOptionPane.INFORMATION_MESSAGE);
					}
					else if ( "combinaisonDeuxCartesFinDTC".equals(action) )
					{
						ActionDeJeu actionDeJeu = (ActionDeJeu) ((Object[]) arg)[1];
						Carte carte =  (Carte) ((Object[]) arg)[2];
						
						JOptionPane.showMessageDialog(FenetreClient.this, actionDeJeu.getJoueur().getNom() + " t'a piqué : " + carte.toString(), "Information", JOptionPane.INFORMATION_MESSAGE);
					}
					else if ( "combinaisonTroisCartes".equals(action) )
					{
						ActionDeJeu actionDeJeu = (ActionDeJeu) ((Object[]) arg)[1];
						
						List<Carte> mainType = new ArrayList<Carte>();
						for ( Carte c : actionDeJeu.getCible().getMain() )
							if ( actionDeJeu.getTypeDeCarte().isInstance(c) )
								mainType.add(c);
						
						Carte[] cartes = new Carte[mainType.size()];
						for ( int i = 0 ; i < cartes.length ; i++ )
							cartes[i] = mainType.get(i);
						
						if ( cartes.length == 0 )
						{
							JOptionPane.showMessageDialog(FenetreClient.this, "Lucky boy, " + actionDeJeu.getJoueur().getNom() + " vous demande un " + actionDeJeu.getTypeDeCarte().getSimpleName() + " alors que vous n'en avez pas !", "Jour de chance", JOptionPane.INFORMATION_MESSAGE);
							jeuClient.finishCombinaisonTroisCartes(actionDeJeu, null);
						}
						else
						{
							Playable nope = getFirstNope();
							
							DialogPickCard dial = choisirUneCarte(actionDeJeu.getCible().getNom() + ":: Combinaison 3 to " + actionDeJeu.getJoueur().getNom(), CombinaisonTroisCartes.ICONE, cartes, false, false, nope != null);
							while ( dial.getCarte() == null && !dial.isNoped() )
							{
								JOptionPane.showMessageDialog(FenetreClient.this, "Vous devez donner une carte de type " + actionDeJeu.getTypeDeCarte().getSimpleName() + " à " + actionDeJeu.getJoueur().getNom(), "Attention", JOptionPane.WARNING_MESSAGE);
								dial = choisirUneCarte(actionDeJeu.getCible().getNom() + ":: Combinaison 3 to " + actionDeJeu.getJoueur().getNom(), CombinaisonTroisCartes.ICONE, cartes, false, false, nope != null);
							}
							
							if ( (nope!=null) && dial.isNoped() )
							{
								jeuClient.jouer(jeuClient.getJoueur(), nope, null, null);
								jeuClient.finishFavor(actionDeJeu, null);
							}
							else
								jeuClient.finishCombinaisonTroisCartes(actionDeJeu, dial.getCarte());
						}
						
					}
					else if ( "combinaisonTroisCartesFin".equals(action) )
					{
						ActionDeJeu actionDeJeu = (ActionDeJeu) ((Object[]) arg)[1];
						Carte carte = (Carte) ((Object[]) arg)[2];
						if ( carte == null )
							JOptionPane.showMessageDialog(FenetreClient.this, "Vous avez demandé un " + actionDeJeu.getTypeDeCarte().getSimpleName() + " à " + actionDeJeu.getCible().getNom() + " et il n'en avait pas !", "Pwned !", JOptionPane.INFORMATION_MESSAGE);
						else
							JOptionPane.showMessageDialog(FenetreClient.this, actionDeJeu.getCible().getNom() + " vous a donné un " + actionDeJeu.getTypeDeCarte().getSimpleName() + " : " + carte.toString() + " !", "Information", JOptionPane.INFORMATION_MESSAGE);
					}
					else if ( "combinaisonCinqCartes".equals(action) )
					{
						ActionDeJeu actionDeJeu = (ActionDeJeu) ((Object[]) arg)[1];
						Carte[] cartes = (Carte[]) ((Object[]) arg)[2];;
						
						Carte carte = choisirUneCarte(actionDeJeu.getJoueur().getNom() + " :: Fais ton choix dans la défausse", CombinaisonCinqCartes.ICONE, cartes, false, false, false).getCarte();
						while (carte == null)
						{
							JOptionPane.showMessageDialog(FenetreClient.this, "Vous devez choisir une carte dans la défausse", "Attention", JOptionPane.WARNING_MESSAGE);
							carte = choisirUneCarte(actionDeJeu.getJoueur().getNom() + " :: Fais ton choix dans la défausse", CombinaisonCinqCartes.ICONE, cartes, false, false, false).getCarte();
						}
						jeuClient.finishCombinaisonCinqCartes(actionDeJeu, carte);
					}
					else if ( "exploding".equals(action) )
					{//TODO améliorer la fenêtre d'exploding
						Joueur joueur = (Joueur) ((Object[]) arg)[1];
						int pioche = (Integer) ((Object[]) arg)[2];
						Carte exploding = (Carte) ((Object[]) arg)[3];
						
						
						Integer position = choisirUnePosition(joueur.getNom() + " :: Put Exploding", Exploding.ICONE, pioche, false);
						while (position == null)
						{
							JOptionPane.showMessageDialog(FenetreClient.this, "Vous devez repositionner l'exploding ", "Attention", JOptionPane.WARNING_MESSAGE);
							position = choisirUnePosition(joueur.getNom() + " :: Put Exploding", Exploding.ICONE, pioche, false);
						}
						jeuClient.finishExploding(joueur, exploding, position.intValue());
					}
					else if ( "explodingFin".equals(action) )
					{
						Joueur joueur = (Joueur) ((Object[]) arg)[1];
						Carte exploding = (Carte) ((Object[]) arg)[2];
						
						JOptionPane.showMessageDialog(FenetreClient.this, joueur.getNom() + " a repositionné l'exploding : " + exploding.toString(), "Information", JOptionPane.INFORMATION_MESSAGE);
					}
					
				}
			}.start();
		}
		else if ( arg instanceof Object[] 
				&& ((Object[]) arg)[0] instanceof ServerEventType )
		{
			Joueur j, cible;
			Playable c;
			Class<Carte> typeDeCarte;
			boolean hasSucceded, canDefuse;
			Exception ex;
			switch ( (ServerEventType) ((Object[]) arg)[0] )
			{
			case FIN_DE_TOUR : 
				actualiserJoueurs();
				j = (Joueur) ((Object[]) arg)[1];
				if ( j.equals(jeuClient.getJoueur()) )
					JOptionPane.showMessageDialog(this, "A toi de jouer !");
				break;
			case CARTE_JOUEE_DEBUT : 
				j = (Joueur) ((Object[]) arg)[1];
				c = (Playable) ((Object[]) arg)[2];
				cible = (Joueur) ((Object[]) arg)[3];
				typeDeCarte = ((Class<Carte>) ((Object[]) arg)[4]);
				
				statusBar.setText(j.getNom() + " joue : " + c + (cible==null?"":" et cible " + cible.getNom()) + (typeDeCarte==null?"":" et demande un " + typeDeCarte.getSimpleName()));
				modelDefausse.setDefausse(jeuClient.getDefausse());
				actualiserJoueurs();
				break;
			case CARTE_JOUEE_FIN : 
				j = (Joueur) ((Object[]) arg)[1];
				c = (Playable) ((Object[]) arg)[2];
				cible = (Joueur) ((Object[]) arg)[3];
				typeDeCarte = (Class<Carte>) ((Object[]) arg)[4];
				hasSucceded = (Boolean) ((Object[]) arg)[5];
				
				statusBar.setText(j.getNom() + " a joué : " + c + (cible==null?"":" et a ciblé " + cible.getNom()) + (typeDeCarte==null?"":" et demande un " + typeDeCarte.getSimpleName()) + (hasSucceded?" // Réussi !":"// Raté..."));
				modelDefausse.setDefausse(jeuClient.getDefausse());
				actualiserJoueurs();
				break;
			case CARTE_PIOCHEE : 
				j = (Joueur) ((Object[]) arg)[1];
				
				statusBar.setText(j.getNom() + " a pioché");
				actualiserJoueurs();
				break;
			case JOUEUR_EXPLOSE : 
				j = (Joueur) ((Object[]) arg)[1];
				canDefuse = (Boolean) ((Object[]) arg)[2];
				Carte exploding = (Carte) ((Object[]) arg)[3];
				
				if ( canDefuse )
					statusBar.setText(j.getNom() + " a explosé avec defuse : " + exploding.toString());
				else
					statusBar.setText(j.getNom() + " a explosé sans defuse : " + exploding.toString());
				modelDefausse.setDefausse(jeuClient.getDefausse());
				actualiserJoueurs();
				break;
			case GAME_READY : 
				j = (Joueur) ((Object[]) arg)[1];
				
				actualiserJoueurs();
				break;
			case NOTIFY_EXCEPTION : 
				j = (Joueur) ((Object[]) arg)[1];
				ex = (Exception) ((Object[]) arg)[2];
				
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
				break;
			default :
				break;
			}
		}
	}
	
	
	private Playable getFirstNope() {
		Playable c = null; 
		
		for ( int i = 0 ; i < list[indexJoueur].getModel().getSize() ; i++  )
			if ( list[indexJoueur].getModel().getElementAt(i) instanceof Nope )
			{
				c = (Playable) list[indexJoueur].getModel().getElementAt(i);
				break;
			}
		return c;
	}
	
	
	private void voirCartes(String title, ImageIcon icone, Carte[] cartes)
	{
		DialogSeeCards d = new DialogSeeCards(title, icone, cartes);
		d.setModal(true);
		d.setLocationRelativeTo(this);
		d.setVisible(true);
	}
	
	
	private Joueur choisirUnJoueur(String title, ImageIcon icone, Joueur[] joueurs)
	{
		DialogPickPlayer d = new DialogPickPlayer(title, icone, joueurs);
		d.setModal(true);
		d.setLocationRelativeTo(this);
		d.setVisible(true);
		
		return d.getJoueur();
	}
	
	private DialogPickCard choisirUneCarte(String title, ImageIcon icone, Carte[] Cartes, boolean isCancellable, boolean isAveugle, boolean isNopable)
	{
		DialogPickCard d = new DialogPickCard(title, icone, Cartes, isCancellable, isAveugle, isNopable);
		d.setModal(true);
		d.setLocationRelativeTo(this);
		d.setVisible(true);
		
		return d;
	}
	
	private Integer choisirUnePosition(String title, ImageIcon icone, int taille, boolean isCancellable)
	{
		DialogPickPosition d = new DialogPickPosition(title, icone, taille, isCancellable);
		d.setModal(true);
		d.setLocationRelativeTo(this);
		d.setVisible(true);
		
		return d.getPosition();
	}
	
	private Class<Carte> choisirUnTypeDeCarte(String title, ImageIcon icone, boolean isCancellable, boolean includeExploding)
	{
		DialogPickCardType d = new DialogPickCardType(title, icone, isCancellable, includeExploding);
		d.setModal(true);
		d.setLocationRelativeTo(this);
		d.setVisible(true);
		
		return d.getTypeDeCarte();
	}
	
	
	
	
	
	
	
	
	
//	private void refreshStatusBar()
//	{
//		//statusBar.setText("T9 : " + T9as.getSelectedT9as().getName() + " / Base de données : " + Database.getSelectedDatabase().getName());
//	}
	
	
	
	

	
	
}