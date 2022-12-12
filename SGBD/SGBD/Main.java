package affichage;

import java.io.File;
import java.util.Scanner;
import java.util.Vector;

import affichage.Frame;
import traitement.*;

public class Main 
{
    public static void main(String[] args) throws Exception
    {
        // new Frame();
        // try 
        // {
            // String requete="SELECT * FROM Andrana";
            // String requete="SELECT NOM,CLASSE FROM Andrana";
            // String requete="SELECT * FROM Andrana WHERE NOM = Rakoto";
            // String requete="SELECT NOM,PRENOM,NUMERO FROM Andrana WHERE Numero < 3";
            // String requete="SELECT * FROM Membre WHERE NOM = RAKOTO JOIN Vente ON ID = ID_MEMBRE";
            // String requete="SELECT ID,ACHAT FROM Membre WHERE NOM = RAKOTO JOIN Vente ON ID = ID_MEMBRE";
            // String requete="INSERT INTO Andrana Nom,Prenom,CLASSE,NUMERO values Randrianasolo,Tojonirina,S2,ETU002043";
            // String requete="Division de d1 par d2";
            // String requete="Union de Andrana,Andrana1";
            // String requete="Intersection de Andrana,Andrana1";
            // String requete="Difference de Andrana et Andrana1";
            // String requete="Produit de Membre par Vente";
            // String requete="Show tables";

            // Relation base=Relation.getRelationFromBase("Andrana","Andrana");
            // String[] colonneSelectionner=new String[2];
            // colonneSelectionner[0]="NOM";
            // colonneSelectionner[1]="PRENOM";
            // Relation.afficherResultat(Relation.projection(base,colonneSelectionner)); 

            // String requete="DROP BASE Division";

            // String requete="DELETE FROM ANDRANA";
            // String requete="UPDATE ANDRANA SET nom,classe value KELY,S5 WHERE numero < 123";

            // String requete="CREATE BASE TESTE";
            // String requete="CREATE TABLE TESTE : az,er,ty,ui,op IN TESTE";

        //     Relation.afficherResultat(Grammaire.requete("Andrana", requete));
        // } 
        // catch (Exception e) 
        // {
        //     new Frame(e);
        //     System.out.println(e.getMessage());
        // }
        // Relation[] r=new Relation[2];
        // r[0]=Relation.getRelationFromBase("Andrana","Andrana");
        // r[1]=Relation.getRelationFromBase("Andrana","Andrana1");
        // Relation.afficherResultat(Relation.union(r));

        // Relation difference=Relation.difference(Relation.getRelationFromBase("Andrana", "Andrana") , Relation.getRelationFromBase("Andrana", "Andrana1"));
        // Relation.afficherResultat(difference);
        
        // Relation produitCartesien=Relation.produitCartesien(Relation.getRelationFromBase("Vente", "Membre"),Relation.getRelationFromBase("Vente", "Vente"));
        // Relation.afficherResultat(produitCartesien);

        // Relation division=Relation.division(Relation.getRelationFromBase("Division", "d1"), Relation.getRelationFromBase("Division", "d2"));
        // Relation.afficherResultat(division);

        // Relation division=Relation.division(produitCartesien, Relation.getRelationFromBase("Vente", "Vente"));
        // Relation.afficherResultat(division);
    }
      
}