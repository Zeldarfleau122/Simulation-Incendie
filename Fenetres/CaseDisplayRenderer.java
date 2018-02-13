package Fenetres;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import Environnement.CaseType;

/**
 * Affichage des types de case dans une liste : [nom] [couleurType]
 * cf. FenetreAdministration
 * 
 * @author @author ALLAM Jonathan - <thomasallam@outlook.fr> - 02/01/2018
 *
 * @version 0.0.1
 *
 * @see FenetreAdministration
 */
@SuppressWarnings("serial")
public class CaseDisplayRenderer extends JPanel implements ListCellRenderer {
	private String nom ;
	
	/**
	 * Obtention des caracteristiques associee a la case et adaptation de la case associe en fonction des caract.
	 */
	@Override
	public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus) {
		this.nom = (String) value ;
		
										// Changer couleur de fond selon si selectionnee ou non.
		if (isSelected) {			
			this.setBackground(list.getSelectionBackground());
			this.setForeground((list.getSelectionForeground()));
		} else {
			this.setBackground(list.getBackground());
			this.setForeground((list.getForeground()));
		}
			
		return this ;
	}
	
	/**
	 * Dimension d'une case dans la liste.
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(120,15) ;
	}
	
	/**
	 * Dessin du nom de la case ainsi que d'un carre contenant sa couleur.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g ;
		
		g2.drawString(this.nom, 10, 10);
		
		Color cCase = CaseType.ensCase.get(this.nom).getColor() ;
		Rectangle2D r = new Rectangle2D.Double(70, 1, 10, 10) ;
		
		g2.draw(r);
		g2.setPaint(cCase);
		g2.fill(r);
		g2.setPaint(Color.BLACK);
	}
}
