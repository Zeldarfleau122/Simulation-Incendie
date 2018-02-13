package Environnement;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Analyse d'une image rentree manuellement par l'utilisateur.
 * Transforme une image en un Terrain par discretisation et rapprochement aux CaseType implementees.
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 06/01/2018
 *
 * @version 0.0.1
 */
public class ImageCarte {
	private BufferedImage image ;				// Image a analyser
	
	/**
	 * Initialisation de l'image a partir d'un chemin absolu.
	 * 
	 * @param path Chemin absolu de l'image a analyser.
	 */
	public ImageCarte(String path) throws IOException {
		this.image = ImageIO.read(new File(path)) ;	
	}
	
	/**
	 * Convertie l'image en un Terrain. ( 1 case = 1 carre de 4 pixels )
	 */
	public void toTerrain() {
		int h = this.image.getHeight() / 4 ;
		int w = this.image.getWidth() / 4 ;
		
		Terrain.create(w,  h) ;
		
		for (int j = 0; j<h; j++) 								// Pour chaque case du terrain ...
			for (int i = 0; i<w; i++) 
				this.analyser(i, j) ;								// ... Analyser la case i, j du plateau.
	}
	
	/**
	 * Renvoie la couleur moyenne des couleurs indiquees. ( 4 )
	 * 
	 * @param couleurs Ensemble des couleurs a moyenner. ( 4 )
	 * 
	 * @return Renvoie la couleur moyenne de "couleurs"
	 */
	public int couleurMoyenne(int[] couleurs) {
		Color cTemp ;				// Couleur temporaire
		int r, g, b ;				// rouge, vert, bleu de la couleur moyenne
		
		r = b = g = 0 ;				// Au depart nuls
		
		for (int i=0; i<4; i++) {			// Pour l'ensemble des 4 couleurs indiquees.
			cTemp = new Color(couleurs[i]) ;		// Recuperer la couleur
			r += cTemp.getRed() ;					// Incrementer chaque couleur.
			g += cTemp.getGreen() ;
			b += cTemp.getBlue() ;
		}
			
		r /= 4 ;					// rouge moyen
		g /= 4 ;					// green moyen
		b /= 4 ;					// bleu moyen
		
		return new Color(r, g, b).getRGB() ;				// Renvoie de la couleur moyenne.
	}
	
	/**
	 * Renvoie le CaseType en fonction de la couleur moyenne indiquee.
	 * Cela par une comparaison a la couleur de chaque CaseType. ( utilisation d'une distance entre couleurs en fonction de chaque composante
	 *  rouge, bleu et vert. )
	 * 
	 * @param moyenne
	 * @return
	 */
	public CaractCaseType getCaseType(Color moyenne) {
		int dMin, dTemp ;				// Distance minimale et Distance Temporaire
		int dr, dg, db ;				// Distance associee au rouge, vert et bleu.
		CaractCaseType answer ;				// Reponse : CaractCaseType reponse associe a couleur la plus proche (dMin)
		Color cTemp ;					// Couleur temporaire
		CaractCaseType cctTemp ;		// Caract Case Type
		
		dMin = Integer.MAX_VALUE ;		// Inialisation de la distance Minimale.
		answer = null ;
		
		for (String ctName : CaseType.ensCase.keySet()) {
			cctTemp = CaseType.ensCase.get(ctName) ;
			
			if (cctTemp.getAnalysable()) {
				cTemp = cctTemp.getColor() ;				// Couleur de CaseType actuel.
				
				dr = moyenne.getRed() - cTemp.getRed() ;				// Calcul distance selon rouge
				dr *= dr ;
				dr = (int) Math.pow(dr, 0.5) ;
				
				dg = moyenne.getGreen() - cTemp.getGreen() ;			// Calcul distance selon vert
				dg *= dg ;
				dg = (int) Math.pow(dg, 0.5) ;
				
				db = moyenne.getBlue() - cTemp.getBlue() ;				// Calcul distace selon bleu
				db *= db ;
				db = (int) Math.pow(db, 0.5) ;
				
								// Distance total entre la couleur moyenne et la couleur du CaseType actuel
				dTemp = dr + dg + db ;
				if (dTemp < dMin) {				// Si plus faible que la distance minimale connu
					dMin = dTemp ;						// Remplacer la distance minimale connu
					answer = cctTemp ;						// Remplacer la couleur connu.
				}
			}
		}
		
		return answer ;				// Renvoie de la couleur trouvee.
	}
	
	/**
	 * Analyse de la case x, y du Terrain. Modifie son type a l'issu de cette fonction.
	 * 
	 * @param x Abscisse de la case a modifier.
	 * @param y Ordonnee de la case a modifier.
	 */
	public void analyser(int x, int y) {
		int[] couleurs ;				// RGB des 4 pixels constituant la futur case x, y du Terrain.
		
		couleurs = new int[4] ;
		
		for (int i=0; i < 2; i++) 								// Recuperation des couleurs des pixel (x,y) / (x+1,y)
			for (int j=0; j<2; j++) 								// (x,y+1) / (x+1, y+1)
				couleurs[i*2 + j] = this.image.getRGB(x*4+i, y*4+j) ;
		
		int moyenne ;
		
		moyenne = this.couleurMoyenne(couleurs) ;				// Recuperation de la couleur moyenne de l'ensemble.
		
		CaractCaseType ct = this.getCaseType(new Color(moyenne)) ;
		
		try {
			if (ct != null)
				Terrain.getInstance().getCase(x, y).changeType(ct);
		} catch (Exception e) {
		}
	}
}
