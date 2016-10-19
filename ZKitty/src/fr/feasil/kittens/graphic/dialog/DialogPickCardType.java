package fr.feasil.kittens.graphic.dialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import fr.feasil.kittens.cards.Attack;
import fr.feasil.kittens.cards.Carte;
import fr.feasil.kittens.cards.Defuse;
import fr.feasil.kittens.cards.Exploding;
import fr.feasil.kittens.cards.Favor;
import fr.feasil.kittens.cards.Nope;
import fr.feasil.kittens.cards.SeeTheFuture;
import fr.feasil.kittens.cards.Shuffle;
import fr.feasil.kittens.cards.Skip;
import fr.feasil.kittens.cards.cats.CatKarima;
import fr.feasil.kittens.cards.cats.CatMaya;
import fr.feasil.kittens.cards.cats.CatMish;
import fr.feasil.kittens.cards.cats.CatRayan;
import fr.feasil.kittens.cards.cats.CatSofia;
import fr.feasil.kittens.graphic.CarteTypeCellRenderer;

public class DialogPickCardType extends JDialog
{
	private static final long serialVersionUID = 1L;
	private static final Class<?>[] CARTES_NOEXPLODING = {Defuse.class, Attack.class, Favor.class, Nope.class, SeeTheFuture.class, Shuffle.class, Skip.class, CatKarima.class, CatMaya.class, CatMish.class, CatRayan.class, CatSofia.class};
	private static final Class<?>[] CARTES = {Attack.class, Defuse.class, Exploding.class, Favor.class, Nope.class, SeeTheFuture.class, Shuffle.class, Skip.class, CatKarima.class, CatMaya.class, CatMish.class, CatRayan.class, CatSofia.class};
	
	private JList listeTypesCartes;
	private JScrollPane scrollTypesCartes;
	
	private JButton btnOk, btnCancel;
	
	private boolean isCancellable;
	private boolean includeExploding;
	
	private boolean isCanceled = true;
	private Class<Carte> typeDeCarte = null;
	
	public DialogPickCardType(String title, ImageIcon icone, boolean isCancellable, boolean includeExploding) 
	{
		super();
		this.isCancellable = isCancellable;
		this.includeExploding = includeExploding;
		
		setTitle(title);
		setIconImage(icone.getImage());
		
		initComponents();
		addComponents();
		
		setMinimumSize(new Dimension(250, 400));
		setSize(getMinimumSize());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
	}
	
	private void initComponents()
	{
		if ( includeExploding )
			listeTypesCartes = new JList(CARTES);
		else
			listeTypesCartes = new JList(CARTES_NOEXPLODING);
		listeTypesCartes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listeTypesCartes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if ( SwingUtilities.isLeftMouseButton(evt) 
						&& evt.getClickCount() == 2 )
				{
					actionOK();
				}
			}
		});
		listeTypesCartes.setCellRenderer(new CarteTypeCellRenderer());
		
		scrollTypesCartes = new JScrollPane(listeTypesCartes);
		
		
		
		btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				actionOK();
			}
		});
		btnCancel = new JButton("Annuler");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				actionCancel();
			}
		});
		
		
		/*KeyAdapter escapeAdapter = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent evt) {
				if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
					actionCancel();
			}
		};
		
		listeJoueurs.addKeyListener(escapeAdapter);
		btnOk.addKeyListener(escapeAdapter);
		btnCancel.addKeyListener(escapeAdapter);
		scrollJoueurs.addKeyListener(escapeAdapter);*/
		
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
			panTop.add(scrollTypesCartes, cPan);
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
			
			if ( isCancellable )
			{
				cPan.gridx = 1;
				cPan.fill = GridBagConstraints.BOTH; cPan.weightx = 0; cPan.weighty = 0;
				panBot.add(btnCancel, cPan);
			}
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
	@SuppressWarnings("unchecked")
	private void actionOK()
	{
		if ( listeTypesCartes.getSelectedIndex() == -1 )
		{
			JOptionPane.showMessageDialog(DialogPickCardType.this, "Vous n'avez sélectionné aucune type de carte !", "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		typeDeCarte = (Class<Carte>)  listeTypesCartes.getSelectedValue();
		
		isCanceled = false;
		dispose();
	}
	private void actionCancel()
	{
		isCanceled = true;
		dispose();
	}
	public boolean isCanceled() {
		return isCanceled;
	}
	public Class<Carte> getTypeDeCarte() 
	{
		if ( isCanceled )
			return null;
		
		return typeDeCarte;
	}
	//---------------------------------
	
}
