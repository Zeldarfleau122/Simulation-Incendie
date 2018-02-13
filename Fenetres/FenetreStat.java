package Fenetres;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Comportemental.StatActeur;
import Environnement.StatCase;
import Environnement.Terrain;
import Exceptions.FenetreErreurFatale;
import Exceptions.NoTerrain;

/**
 * Fenetre montrant les informations sur la simulation terminee. ( cf. FeuilleRes )
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 06/01/2018
 *
 * @version 0.0.1
 * 
 * @see FeuilleRes
 */
@SuppressWarnings("serial")
public class FenetreStat extends JFrame {
	private JLabel infoGene ;				// Informations generales sur la simulation ( nom + nbr Tour )
	private JButton retour ;
	
	/**
	 * Constructeur par defaut.
	 */
	public FenetreStat() {
		this.setTitle("Fenetre resultats");							// Caracteristiques de la fenetre
		this.setBounds(100, 100, 500, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
		
		try {
			this.infoGene = new JLabel() ;								// Initialisation des informations generales ( Label )
			this.infoGene.setText(Terrain.getInstance().stat.toString());
			
			JPanel p = new JPanel() ;									// Panneau du haut contenant les informations generales.
			
			p.add(this.infoGene) ;
			
			this.add(p, BorderLayout.NORTH) ;								// Mise en forme du panneau du haut. ( informations generales )
			this.add(Box.createHorizontalStrut(50), BorderLayout.WEST);
			this.add(Box.createHorizontalStrut(50), BorderLayout.EAST);
			
			JPanel pCenter = new JPanel() ;								// Panneau central contenant informations sur chaque Case/Acteur.
			JPanel pTemp ;												// Panneau temporaire : information sur un element particulier ( mise en forme des infos )
			JLabel titre, chpCrea, chpMort ;							// Ensemble des infos relative a l'elt actuel. ( Nom / nbrCrea / nbrMort ) sous forme de texte.
			pCenter.setLayout(new GridLayout(0,2, 20, 20));
			pCenter.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			for (StatCase sc : Terrain.getInstance().stat.ensStatCase.values()) {				// Pour chaque acteur suivit pendant la simulation ...
				pTemp = new JPanel() ;																	// Creer un nouveau panneau
				pTemp.setLayout(new BoxLayout(pTemp, BoxLayout.PAGE_AXIS));								// Ajout d'un element ligne par ligne dans ce panneau.
				
				titre = new JLabel(sc.getCaseType(), JLabel.CENTER) ;
				chpCrea = new JLabel() ;
				chpMort = new JLabel() ;
				
				chpCrea.setText("Nombre de creation : " + sc.getNbrCreation());
				chpMort.setText("Nombre de destruction : " + sc.getNbrDetruite());
				
				pTemp.add(titre) ;
				pTemp.add(chpCrea) ;
				pTemp.add(chpMort) ;
				
				pTemp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				pCenter.add(pTemp) ;
			}
			
			for (StatActeur sa : Terrain.getInstance().stat.ensStatActeur.values()) {			// Pour chaque Case suivit pendant la simulation
				pTemp = new JPanel() ;																	// Creer un nouveau panneau
				pTemp.setLayout(new BoxLayout(pTemp, BoxLayout.PAGE_AXIS));								// Ajout d'un element ligne par ligne dans ce panneau.
				
				titre = new JLabel(sa.getActeurType(), JLabel.CENTER) ;
				chpCrea = new JLabel() ;
				chpMort = new JLabel() ;
				
				chpCrea.setText("Nombre de creation : " + sa.getNbrCreation());
				chpMort.setText("Nombre de mort : " + sa.getNbrMort());
				
				pTemp.add(titre) ;
				pTemp.add(chpCrea) ;
				pTemp.add(chpMort) ;
				
				pTemp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				pCenter.add(pTemp) ;			
			}
			
			pCenter.add(Box.createHorizontalStrut(20));								// Delimination en bas.
			
			this.add(pCenter, BorderLayout.CENTER);
			
			JPanel pSouth = new JPanel() ;
			
			this.retour = new JButton("Retour Menu") ;
			this.retour.addActionListener(new RetourAction());
			
			pSouth.add(this.retour) ;
			this.add(pSouth, BorderLayout.SOUTH);
			
			this.setVisible(true);
		} catch (NoTerrain e) {
			new FenetreErreurFatale(e.toString()) ;
		}
	}
	
	/**
	 * Action associee au bouton "retour". Effectue un reset du terrain avant de retourner au menu.
	 */
	private class RetourAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dispose() ;						// Quitter cette fenetre.
			
			Terrain.reset();				// Reset du terrain
			
			new FenetreMenu() ;				// Passage au menu.
		}
	}
}
