package fr.feasil.kittens.graphic.dialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import fr.feasil.kittens.cards.Carte;
import fr.feasil.kittens.graphic.CarteCellRenderer;

public class DialogSeeCards extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private JList listeCartes;
	private JScrollPane scrollCartes;
	
	private JButton btnOk;
	
	//private Joueur joueur;
	private Carte[] cartes;
	
	
	public DialogSeeCards(String title, ImageIcon icone, Carte[] cartes) 
	{
		super();
		//this.joueur = joueur;
		this.cartes = cartes;
		
		setTitle(title);
		setIconImage(icone.getImage());
		
		initComponents();
		addComponents();
		
		setMinimumSize(new Dimension(250, 300));
		setSize(getMinimumSize());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	

	private void initComponents()
	{
		listeCartes = new JList(cartes);
		listeCartes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		listeCartes.setCellRenderer(new CarteCellRenderer());
		scrollCartes = new JScrollPane(listeCartes);
		
		
		btnOk = new JButton("Terminer");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				actionOK();
			}
		});
		
		
		/*KeyAdapter escapeAdapter = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent evt) {
				if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
					actionOK();
			}
		};
		listeCartes.addKeyListener(escapeAdapter);
		btnOk.addKeyListener(escapeAdapter);
		scrollCartes.addKeyListener(escapeAdapter);*/
	}
	
	
	private void addComponents()
	{
		setLayout(new GridBagLayout());
		
		JPanel panTop = new JPanel(new GridBagLayout());
		{
			panTop.setBorder(new TitledBorder("Cartes"));
			GridBagConstraints cPan = new GridBagConstraints();
			cPan.gridx = 0; cPan.gridy = 0;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 1; cPan.weighty = 1;
			cPan.insets = new Insets(4, 4, 4, 4);
			panTop.add(scrollCartes, cPan);
		}
		
		JPanel panBot = new JPanel(new GridBagLayout());
		{
			GridBagConstraints cPan = new GridBagConstraints();
			cPan.gridx = 0; cPan.gridy = 0;
			cPan.gridwidth = 1; cPan.gridheight = 1;
			cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 0; cPan.weighty = 0;
			cPan.anchor = GridBagConstraints.NORTHWEST;
			cPan.insets = new Insets(4, 4, 4, 4);
			panBot.add(btnOk, cPan);
		}
		
		
		GridBagConstraints cPan = new GridBagConstraints();
		cPan.gridx = 0; cPan.gridy = 0;
		cPan.gridwidth = 1; cPan.gridheight = 1;
		cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 1; cPan.weighty = 1;
		cPan.insets = new Insets(4, 4, 4, 4);
		add(panTop, cPan);
		
		
		cPan.gridx = 0; cPan.gridy = 1;
		cPan.gridwidth = 1; cPan.gridheight = 1;
		cPan.fill = GridBagConstraints.HORIZONTAL; cPan.weightx = 0; cPan.weighty = 0;
		cPan.insets = new Insets(0, 4, 4, 4);
		add(panBot, cPan);
		
		
	}
	
	
	//-- ACTIONS ----------------------
	private void actionOK()
	{
		dispose();
	}
	//---------------------------------
	
}
