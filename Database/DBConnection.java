package Database;

import java.sql.*;

import Environnement.CaseType;
import Environnement.FeuilleRes;

/**
 * Connection et discussion avec la base de donnee ( MySql via JDBC )
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 09/01/2018
 *
 * @version 0.0.1
 */
public class DBConnection {
	private Object[][] resRecherche ;						// Resultat d'une recherche dans la base de donnee.
	
	/**
	 * Connection a la base de donnee
	 * 
	 * @return Renvoie la connection etablie avec la base de donnee.
	 * 
	 * @throws SQLException Probleme lors de l'execution de la requete SQL ( connection )
	 */
	private static Connection connectToDB()  throws SQLException {
		DriverManager.getConnection("jdbc:mysql://localhost:3306?useSSL=false", "user", "password");
		try {
			Connection res = DriverManager.getConnection("jdbc:mysql://localhost:3306/simulationIncendie?useSSL=false", "user", "password");
			Statement st = res.createStatement() ;
			String req = "SHOW TABLES FROM simulationincendie LIKE 'casetype';";
			
			ResultSet ans = st.executeQuery(req) ;
			
			if (!ans.next()) {								// Si abs table casetype 
				initializeTableCaseType() ;							// la creer et l'initialiser
			}
			
			req = "SHOW TABLES FROM simulationincendie LIKE 'resultats';" ;
			ans = st.executeQuery(req) ;
			if (!ans.next()) {								// Si bas table resultats
				initializeTableResultats() ;						// La creer.
			}
			
			return res ;
		} catch (SQLException e) {
			DBConnection.initializeDatabase() ;
			
			return DriverManager.getConnection("jdbc:mysql://localhost:3306/simulationIncendie?useSSL=false", "user", "password");
		}
	}
	
	/**
	 * Initialisation de la base de donnee si abs. ( nom : "simulationincendie" )
	 * 
	 * @throws SQLException Erreur SQl lors de la connection ou envoie des requetes.
	 */
	public static void initializeDatabase() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306?useSSL=false", "user", "password");
		Statement create = conn.createStatement() ;
		String req ;
		
		req = "CREATE DATABASE `simulationincendie` /*!40100 DEFAULT CHARACTER SET utf8 */;";
		create.execute(req) ;
		
		initializeTableCaseType() ;
		initializeTableResultats() ;
	}
	
	/**
	 * Creation et initialisation de la table casetype.
	 * 
	 * @throws SQLException Erreur SQl lors de la connection ou envoie des requetes.
	 */
	public static void initializeTableCaseType() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/simulationIncendie?useSSL=false", "user", "password");
		Statement create = conn.createStatement() ;
		
		String req = "CREATE TABLE casetype (" + 
				"  `idCaseType` int NOT NULL AUTO_INCREMENT," + 
				"  `Nom` varchar(45) NOT NULL," + 
				"  `Access` tinyint(4) DEFAULT NULL," + 
				"  `Inflammable` tinyint(4) DEFAULT NULL," + 
				"  `Couleur` int(32) DEFAULT NULL," + 
				"  `RecordStat` tinyint(4) DEFAULT NULL," + 
				"  `Analysable` tinyint(4) DEFAULT NULL," + 
				"  `ProbBruler` double DEFAULT NULL," + 
				"  PRIMARY KEY (`idCaseType`)" + 
				") ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;" + 
				"" ;
		create.execute(req) ;
		
		ajoutCaseType("Arbre", "1", "1", "-13483478", "1", "1", "0.2") ;
		ajoutCaseType("Eau", "0", "0", "-15057603", "0", "1", "0") ;
		ajoutCaseType("Habitation", "1", "1", "-4808288", "1", "1", "0.1") ;
		ajoutCaseType("Cendre", "1", "0", "-6381922", "0", "0", "0") ;
		ajoutCaseType("Neutre", "1", "1", "-1", "0", "0", "0.2") ;
	}
	
	/**
	 * Creation de la table "resultats".
	 * 
	 * @throws SQLException Erreur SQl lors de la connection ou envoie des requetes.
	 */
	public static void initializeTableResultats() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/simulationIncendie?useSSL=false", "user", "password");
		Statement create = conn.createStatement() ;
		
		String req = "CREATE TABLE resultats (\r\n" + 
				"  `idResultats` int(11) NOT NULL AUTO_INCREMENT,\r\n" + 
				"  `NomSimulation` varchar(45) NOT NULL,\r\n" + 
				"  `FichierSource` varchar(100) DEFAULT NULL,\r\n" + 
				"  `FeuEteint` int(11) DEFAULT NULL,\r\n" + 
				"  `PompiersMort` int(11) DEFAULT NULL,\r\n" + 
				"  `HabitationsBrulees` int(11) DEFAULT NULL,\r\n" + 
				"  PRIMARY KEY (`idResultats`)\r\n" + 
				") ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;\r\n" + 
				"" ;
		create.execute(req) ;
	}
	
	/**
	 * Enregistrement des statistiques d'un terrain dans la base de donnee.
	 * 
	 * @param f Ensemble des stat. a enregistrer.
	 * 
	 * @throws SQLException Probleme lors de l'execution de la requete SQL. ( connection et/ou erreur syntaxique )
	 */
	public static void enregistrer(FeuilleRes f) throws SQLException {
		Connection conn = connectToDB() ;
		Statement test = conn.createStatement() ;
	      
	    String req = "INSERT INTO Resultats(NomSimulation, FichierSource, FeuEteint, PompiersMort, HabitationsBrulees) VALUES (" ;
	    req += "'" + f.getNomSimulation() + "'" ;
	    req += ",'"+f.getFichierSource()+"'" ;
	    req += f.getFeuEteint() ? ",'1'": ",'0'" ;
	    req += ",'"+f.getPompierMort() + "'";
	    req += ",'"+f.getHabitationsBrulees()+"');" ;
	    test.execute(req) ;
	}
	
														
	/**
	 * Enregistrement des statistiques d'un terrain dans la base de donnee.
	 * 
	 * @param f Ensemble des stat. a enregistrer.
	 * 
	 * @throws SQLException Probleme lors de l'execution de la requete SQL. ( connection et/ou erreur syntaxique )
	 */
	public static void enregistrerCaseType(Object[] Caract) throws SQLException {
		CaseType.creationCaseType() ;
		Connection conn = connectToDB() ;
		Statement test = conn.createStatement() ;
	      
	    String req = "INSERT INTO CaseType(Nom, Accessible, Inflammable, Couleur, RecordStat, Analysable, ProbBruler) VALUES (" ;
	    req += "'" + Caract[0] + "'" ;
	    req += ",'"+ Caract[1] +"'" ;
	    req += ",'" + Caract[2] + "'" ;
	    req += ",'"+ Caract[3] + "'";
	    req += ",'"+ Caract[4] + "'";
	    req += ",'"+ Caract[5] + "'";
	    req += ",'"+ Caract[6] + "'";
	    req += ",'"+ Caract[7] +"');" ;
	    test.execute(req) ;
	}
	
	/**
	 * Recuperation et affichage de l'ensemble des enregistrements de la table Resultats.
	 * 
	 * @throws SQLException Erreur lors de la connection a la base de donnee ou lors de la requete.
	 */
	public void recupererStat() throws SQLException {
		Connection conn = connectToDB() ;
		Statement demande = conn.createStatement() ;
		
		String req = "SELECT COUNT(*) FROM Resultats;" ;
		ResultSet res = demande.executeQuery(req) ;
		
		if (res.next()) {
			this.resRecherche = new Object[res.getInt(1)][5] ;
		}
		req = "SELECT NomSimulation, FichierSource, FeuEteint, PompiersMort, HabitationsBrulees FROM Resultats;" ;
		res = demande.executeQuery(req) ;
		
		int numEnregistrement = 0 ;
		while (res.next()) {
			this.resRecherche[numEnregistrement][0] = res.getString(1) ;
			this.resRecherche[numEnregistrement][1] = res.getString(2) ;
			this.resRecherche[numEnregistrement][2] = res.getInt(3) ;
			this.resRecherche[numEnregistrement][3] = res.getInt(4) ;
			this.resRecherche[numEnregistrement][4] = res.getInt(5) ;
			
			numEnregistrement++ ;
		}
	}
	
	/**
	 * Recuperation de l'ensemble des types de case repertorie dans la base de donnee.
	 * 
	 * @return Renvoie un tableau de type "CaractCaseType".
	 * 
	 * @throws SQLException Erreur SQl lors de la connection ou envoie des requetes.
	 */
	public Object[][] recupererEnsCaseType() throws SQLException {
		Connection conn = connectToDB() ;

		Statement demande = conn.createStatement() ;
		
		String req = "SELECT COUNT(*) FROM CaseType;" ;
		ResultSet res = demande.executeQuery(req) ;
		
		if (res.next()) {
			this.resRecherche = new Object[res.getInt(1)][7] ;
		}
		req = "SELECT Nom, Access, Inflammable, Couleur, RecordStat, Analysable, ProbBruler FROM CaseType;" ;
		res = demande.executeQuery(req) ;
		
		int numEnregistrement = 0 ;
		while (res.next()) {
			this.resRecherche[numEnregistrement][0] = res.getString(1) ;
			this.resRecherche[numEnregistrement][1] = res.getBoolean(2) ;
			this.resRecherche[numEnregistrement][2] = res.getBoolean(3) ;
			this.resRecherche[numEnregistrement][3] = res.getInt(4) ;
			this.resRecherche[numEnregistrement][4] = res.getBoolean(5) ;
			this.resRecherche[numEnregistrement][5] = res.getBoolean(6) ;
			this.resRecherche[numEnregistrement][6] = res.getDouble(7) ;
			
			numEnregistrement++ ;
		}
		
		return this.resRecherche ;
	}
	
	/**
	 * Ajoute un enregistrement a la table "casetype".
	 * 
	 * @param nom Nom du type de la case
	 * @param accessible Indique si elle est accessible au pompier.
	 * @param inflammable Indique si la case est inflammable
	 * @param couleur Couleur de la case.
	 * @param recordStat Indique si suivie statistique de la case ou non.
	 * @param analysable Indique si ce type de case est utilise dans l'analyse d'image. ( = faux le plus souvent sauf pour types de base )
	 * @param probBruler Probabilite que le feu se propage sur cette case.
	 * 
	 * @throws SQLException Erreur SQl lors de la connection ou envoie des requetes.
	 */
	public static void ajoutCaseType(String nom, String accessible, String inflammable, String couleur, String recordStat, String analysable, String probBruler) throws SQLException {
		Connection conn = connectToDB() ;
		Statement state = conn.createStatement() ;
	      
		String req = "INSERT INTO casetype(Nom, Access, Inflammable, Couleur, RecordStat, Analysable, ProbBruler) VALUES (" ;
		req += "'" + nom + "'," ;
		req += accessible + ",";
		req += inflammable + ",";
		req += couleur + "," ;
		req += recordStat + ",";
		req += analysable + ",";
		req += probBruler + ") ;" ;
		
		state.execute(req) ;
	}
	
	/**
	 * Modifie un type de case dans la base de donnee.
	 * 
	 * @param nom Nom de la case a modifier.
	 * @param accessible Indique si case accessible ou non.
	 * @param inflammable Indique si inflammable ou non.
	 * @param couleur Couleur de la case
	 * @param recordStat Indique si suivie statistique ou non de la case.
	 * @param analysable Indique si analysable ou non. ( pour l'analyse d'image )
	 * @param probBruler Probabilite d'etre brulee.
	 * 
	 * @throws SQLException erreur SQL si probleme de connection ou autre.
	 */
	public static void modifierCase(String nom, String accessible, String inflammable, String couleur, String recordStat, String analysable, String probBruler) throws SQLException {
		Connection conn = connectToDB() ;				// Connection
		Statement state = conn.createStatement() ;
	      
		String req = "UPDATE casetype SET " ;
		req += "Access =" ;
		req += accessible + ", Inflammable =";
		req += inflammable + ", Couleur = ";
		req += couleur + ", RecordStat = " ;
		req += recordStat + ", Analysable = ";
		req += analysable + ", ProbBruler =";
		req += probBruler + " WHERE nom = '" + nom + "' ;" ;
		
		state.execute(req) ;
	}
	
	/**
	 * Suppression d'un type de case dans la base de donnee.
	 * 
	 * @param nom Nom de la case a supprimer.
	 * 
	 * @throws SQLException Erreur SQL si probleme de connection ou autre.
	 */
	public static void supprimerCaseType(String nom) throws SQLException {
		Connection conn = connectToDB() ;				// Connection
		Statement state = conn.createStatement() ;
		
		String req = "DELETE FROM casetype WHERE Nom = " + "'" + nom + "' ;" ;
		state.execute(req) ;
	}
	
	/**
	 * Renvoie les resultats obtenue a partir de cette connection.
	 * Verification de la fiabilite des donnees a la charge de l'appelant. ( ex taille tableau. )
	 * 
	 * @return Renvoie le tableau des resultats.
	 */
	public Object[][] getResRecherche() {
		return this.resRecherche ;
	}
}
