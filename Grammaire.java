package traitement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Vector;

public class Grammaire
{
    public static Relation requete(String base,String requete) throws Exception
    {
        Relation relation=null;
        String[] req=Grammaire.changeRequete(requete);
        switch (req[0]) 
        {
            case "SELECT":
                relation = Grammaire.select(base ,requete);
                return relation;
            case "INSERT":
                relation = Grammaire.insert(base,requete); 
                return relation;

            case "DROP":
                relation = Grammaire.drop(base, requete); 
                return relation;

            case "DELETE":
                relation = Grammaire.delete(base, requete); 
                return relation;

            case "UPDATE":
                relation = Grammaire.update(base,requete);
                return relation;

            case "CREATE":
                relation = Grammaire.create(requete);
                return relation;

            case "UNION":
                relation = Grammaire.union(base, requete);
                return relation;

            case "INTERSECTION":
                relation = Grammaire.intersection(base, requete);
                return relation;

            case "DIFFERENCE":
                relation = Grammaire.difference(base, requete);
                return relation;

            case "DIVISION":
                relation = Grammaire.division(base, requete);
                return relation;

            case "PRODUIT":
                relation = Grammaire.produitCartesien(base, requete);
                return relation;
            case "SHOW":
                if (req[1].equals("DATABASES")) {
                    relation = Grammaire.showDB(requete);
                }else{
                    relation = Grammaire.show(base,requete);
                }
                return relation;
        }
        throw new Exception("Verifier votre Requete");
    }
    public static String[] changeRequete(String requete)
    {
        String[] req=requete.split(" ");
        for (int i = 0; i < req.length; i++) 
        {
            req[i]=req[i].toUpperCase();
        }
        return req;
    }
    public static Relation select(String nomBase ,String requete) throws Exception
    {
        String[] req=Grammaire.changeRequete(requete);
        if (req[2].compareTo("FROM")!=0 && req.length==4) throw new Exception("Tokony SELECT <colonne> FROM <table>");
        if (req.length==8 && (req[2].compareTo("FROM")!=0 || req[4].compareTo("WHERE")!=0)) throw new Exception("Tokony SELECT <colonne> FROM <table> WHERE <colonne> = <value>"); 
        if (req.length==14 && (req[2].compareTo("FROM")!=0 || req[4].compareTo("WHERE")!=0 || req[8].compareTo("JOIN")!=0 || req[10].compareTo("ON")!=0 ) )  throw new Exception("Tokony SELECT <colonne> FROM <table> WHERE <colonne> = <value> JOIN <table2> ON <cTab1> = <cTab2>");
        
        Relation base=Relation.getRelationFromBase(nomBase,req[3]);
        
        if(req.length==4 && req[1].compareTo("*")==0) 
        {
            // Relation.afficherResultat(base);
            return base;    
        }
        if (req.length==4 && req[1].compareTo("*")!=0) 
        {
            String[] colonneSelectionner=req[1].split(",");
            Relation resultat = Relation.projection(base , colonneSelectionner);
            // Relation.afficherResultat(resultat);
            return resultat;
        }
        if(req.length==8 && req[1].compareTo("*")==0) 
        {
            Relation resultat = Relation.selection(base, req[5] , req[7], req[6]);
            // Relation.afficherResultat(resultat);
            return resultat;
        }
        if (req.length==8 && req[1].compareTo("*")!=0) 
        {
            String[] colonneSelectionner=req[1].split(",");
            Relation selection = Relation.selection(base, req[5] , req[7], req[6]);
            Relation resultat = Relation.projection(selection, colonneSelectionner);
            // Relation.afficherResultat(resultat);
            return resultat;
        }
        if (req.length==14 && req[1].compareTo("*")==0) 
        {
            Relation r1=Relation.getRelationFromBase(nomBase,req[3]);
            Relation r2=Relation.getRelationFromBase(nomBase, req[9]);
            String colonne1=req[11];
            String colonne2=req[13];
            Relation join = Relation.joiunture(r1, r2, colonne1, colonne2);
            // Relation.afficherResultat(join);
            return join;
        }
        if (req.length==14 && req[1].compareTo("*")!=0) 
        {
            Relation r1=Relation.getRelationFromBase(nomBase, req[3]);
            Relation r2=Relation.getRelationFromBase(nomBase, req[9]);
            String colonne1=req[11];
            String colonne2=req[13];
            Relation join = Relation.joiunture(r1, r2, colonne1, colonne2);
            String[] colonneSelectionner=req[1].split(",");
            Relation resultat = Relation.projection(join, colonneSelectionner);
            // Relation.afficherResultat(resultat);
            return resultat;
        }
        throw new Exception("Erreur");
    }
    public static Relation insert(String nomBase,String requete) throws Exception
    {
        String[] req=Grammaire.changeRequete(requete);
        if (req[0].compareTo("INSERT")!=0) throw new Exception("Erreur: INSERT INTO <table> <colonne1>,<colonne2>,<...> values <valeur1>,<valeur2>,<...>");
        if (req[1].compareTo("INTO")!=0) throw new Exception("Erreur: INSERT INTO <table> <colonne1>,<colonne2>,<...> values <valeur1>,<valeur2>,<...>");
        
        String[] colonne = req[3].split(",");
        String[] valeur = req[5].split(",");
        
        if (colonne.length < valeur.length) throw new Exception("Nombre de valeur > Nombre de colonne");
        if (colonne.length > valeur.length) throw new Exception("Nombre de valeur < Nombre de colonne");
        String nomFichier=req[2]+".bdd";

        FileWriter fichier=new FileWriter("Base/"+nomBase+"/"+nomFichier,true);
		BufferedWriter writter=new BufferedWriter(fichier);
		for (int i=0;i<colonne.length ;i++ ) 
		{
			writter.write(valeur[i]);
			writter.write(",");
		}	
		writter.newLine();
		writter.close();
        
        return Relation.getRelationFromBase(nomBase,req[2]);
    }
    public static Relation drop(String nomBase,String requete) throws Exception
    {
        String[] req=Grammaire.changeRequete(requete);
        if (req.length!=3) throw new Exception("Erreur : DROP <TABLE/BASE> <nom>");
        if (req[1].compareTo("TABLE")==0) 
        {
            File table=new File("Base/"+nomBase+"/"+req[2]+".bdd");
            table.delete();
            return Grammaire.show(nomBase, "Show Tables");
        }
        if (req[1].compareTo("BASE")==0) 
        {   
            File base=new File("Base/"+req[2]);
            String[] listFichier=(new File("Base/"+req[2])).list();
            for (int i = 0; i < listFichier.length; i++) 
            {
                File temp=new File("Base/"+req[2]+"/"+listFichier[i]);
                temp.delete();
            }
            base.delete();
            return Grammaire.showDB("Show Databases");
        }
        throw new Exception("Impossible de 'DROP' ");
    }
    public static Relation delete(String nomBase , String requete) throws Exception
    {   
        String[] req=Grammaire.changeRequete(requete);
        if (req.length==3 && req[1].compareTo("FROM")!=0) throw new Exception("DELETE FROM <TABLE>");
        if (req.length==7 && req[1].compareTo("FROM")!=0 && req[3].compareTo("WHERE")!=0) throw new Exception("DELETE FROM <TABLE> where <COLONNE> = <VALUE>");
        
        Relation relation=Relation.getRelationFromBase(nomBase, req[2]);
        
        if (req.length==3 && req[1].compareTo("FROM")==0) 
        {
            relation.getDonnees().removeAllElements();
        }
        if (req.length==7 && req[1].compareTo("FROM")==0 && req[3].compareTo("WHERE")==0) 
        {
            String colonne=req[4];
            String valeur=req[6];
            String egalite=req[5];

            int i_colonne=0;
            for (int i = 0; i < relation.getListe_colonne().size() ; i++) 
            {
                if ((String.valueOf(relation.getListe_colonne().get(i)).compareTo(colonne)==0))
                {
                    i_colonne=i;
                }
            }

            for (int j = 0; j < relation.getDonnees().size(); j++) 
            {
                Vector temp=(Vector)relation.getDonnees().get(j);
                
                if(egalite.compareTo("=")==0)
                {
                    if ((String.valueOf(temp.get(i_colonne))).compareTo(valeur)==0) 
                    {
                        relation.getDonnees().remove(j);
                        j=j-1;
                    }
                }
                if (egalite.compareTo("<")==0)
                {
                    double doubleDataCS=Double.parseDouble((String.valueOf(temp.get(i_colonne))));
                    double doubleColonneValue=Double.parseDouble(valeur);
                    if (doubleDataCS<doubleColonneValue) 
                    {
                        relation.getDonnees().remove(j);
                        j=j-1;
                    }
                }
                if (egalite.compareTo(">")==0) 
                {
                    double doubleDataCS=Double.parseDouble((String.valueOf(temp.get(i_colonne))));
                    double doubleColonneValue=Double.parseDouble(valeur);
                    if (doubleDataCS>doubleColonneValue) 
                    {
                        relation.getDonnees().remove(j);
                        j=j-1;
                    }
                }
                if (egalite.compareTo("<=")==0)
                {
                    double doubleDataCS=Double.parseDouble((String.valueOf(temp.get(i_colonne))));
                    double doubleColonneValue=Double.parseDouble(valeur);
                    if (doubleDataCS<=doubleColonneValue) 
                    {
                        relation.getDonnees().remove(j);
                        j=j-1;
                    }
                }
                if (egalite.compareTo(">=")==0) 
                {
                    double doubleDataCS=Double.parseDouble((String.valueOf(temp.get(i_colonne))));
                    double doubleColonneValue=Double.parseDouble(valeur);
                    if (doubleDataCS>=doubleColonneValue) 
                    {
                        relation.getDonnees().remove(j);
                        j=j-1;
                    }
                }
                if (egalite.compareTo("!=")==0) 
                {
                    if ((String.valueOf(temp.get(i_colonne))).compareTo(valeur)!=0)
                    {
                        relation.getDonnees().remove(j);
                        j=j-1;
                    }
                }
            }
        }
        

        File fichier=new File("Base/"+nomBase+"/"+req[2]);
        fichier.delete();
        relation.relationToFile(nomBase,req[2]);
        return relation;
    }   
    public static Relation update(String nomBase , String requete) throws Exception
    {
        String[] req=Grammaire.changeRequete(requete);
        if (req.length!=10 || req[2].compareTo("SET")!=0 || req[4].compareTo("VALUE")!=0 || req[6].compareTo("WHERE")!=0) throw new Exception("UPDATE <TABLE> SET <colonne1>,<colonne2>,<...> value <valeur1>,<valeur2>,<...> WHERE <colonne> = <value>");

        Relation relation=Relation.getRelationFromBase(nomBase, req[1]);

        String colonne_where=req[7];
        String egalite=req[8];
        String valeur_where=req[9];

        String[] liste_colonne=req[3].split(",");
        String[] liste_valeur=req[5].split(",");

        if (liste_colonne.length!=liste_valeur.length) throw new Exception("Nombre colonne != Nombre valeur");

        int[] indice_liste_colonne=new int[liste_colonne.length];
        for (int j = 0; j < liste_colonne.length; j++) 
        {
            for (int i = 0; i < relation.getListe_colonne().size(); i++) 
            {
                if((String.valueOf(relation.getListe_colonne().get(i))).compareTo(liste_colonne[j])==0) indice_liste_colonne[j]=i;
            }
        }

        int indice_colonne_where=0;
        for (int i = 0; i < relation.getListe_colonne().size() ; i++) 
        {
            if ((String.valueOf(relation.getListe_colonne().get(i))).compareTo(colonne_where)==0) 
            {
                indice_colonne_where=i;
            }
        }

        for (int j = 0; j < relation.getDonnees().size(); j++) 
        {   
            Vector temp=(Vector)relation.getDonnees().get(j);
            if(egalite.compareTo("=")==0)
            {
                if ((String.valueOf(temp.get(indice_colonne_where))).compareTo(valeur_where)==0) 
                {
                    for (int index = 0; index < indice_liste_colonne.length; index++) 
                    {
                        temp.set(indice_liste_colonne[index],liste_valeur[index]);
                    }
                }
            }
            if (egalite.compareTo("<")==0)
            {
                double doubleDataCS=Double.parseDouble((String.valueOf(temp.get(indice_colonne_where))));
                double doubleColonneValue=Double.parseDouble(valeur_where);
                if (doubleDataCS<doubleColonneValue) 
                {
                    for (int index = 0; index < indice_liste_colonne.length; index++) 
                    {
                        temp.set(indice_liste_colonne[index],liste_valeur[index]);
                    }
                }
            }
            if (egalite.compareTo(">")==0) 
            {
                double doubleDataCS=Double.parseDouble((String.valueOf(temp.get(indice_colonne_where))));
                double doubleColonneValue=Double.parseDouble(valeur_where);
                if (doubleDataCS>doubleColonneValue) 
                {
                    for (int index = 0; index < indice_liste_colonne.length; index++) 
                    {
                        temp.set(indice_liste_colonne[index],liste_valeur[index]);
                    }
                }
            }
            if (egalite.compareTo("<=")==0)
            {
                double doubleDataCS=Double.parseDouble((String.valueOf(temp.get(indice_colonne_where))));
                double doubleColonneValue=Double.parseDouble(valeur_where);
                if (doubleDataCS<=doubleColonneValue) 
                {
                    for (int index = 0; index < indice_liste_colonne.length; index++) 
                    {
                        temp.set(indice_liste_colonne[index],liste_valeur[index]);
                    }
                }
            }
            if (egalite.compareTo(">=")==0) 
            {
                double doubleDataCS=Double.parseDouble((String.valueOf(temp.get(indice_colonne_where))));
                double doubleColonneValue=Double.parseDouble(valeur_where);
                if (doubleDataCS>=doubleColonneValue) 
                {
                    for (int index = 0; index < indice_liste_colonne.length; index++) 
                    {
                        temp.set(indice_liste_colonne[index],liste_valeur[index]);
                    }
                }
            }
            if (egalite.compareTo("!=")==0) 
            {
                if ((String.valueOf(temp.get(indice_colonne_where))).compareTo(valeur_where)!=0)
                {
                    for (int index = 0; index < indice_liste_colonne.length; index++) 
                    {
                        temp.set(indice_liste_colonne[index],liste_valeur[index]);
                    }
                }
            }
        }

        File fichier=new File("Base/"+nomBase+"/"+req[1]);
        fichier.delete();
        relation.relationToFile(nomBase,req[1]);
        return relation;
    }
    public static Relation create(String requete) throws Exception
    {
        String[] req=Grammaire.changeRequete(requete);
        if (req.length==3 && req[1].compareTo("BASE")!=0) throw new Exception("CREATE BASE <NOM BASE>");
        if (req.length==3 && req[1].compareTo("BASE")==0) 
        {
            (new File("Base/"+req[2])).mkdir();
        }
        if (req.length==7 && (req[1].compareTo("TABLE")!=0 || req[3].compareTo(":")!=0 || req[5].compareTo("IN")!=0) ) throw new Exception("CREATE TABLE nomTable : c1,c2,c3,... IN nomBase");
        if (req.length==7 && (req[1].compareTo("TABLE")==0 || req[3].compareTo(":")==0 || req[5].compareTo("IN")==0) )
        {
            File dossierBase=new File("Base/"+req[6]);
            if(!dossierBase.exists()) throw new Exception("La base "+req[6]+" n'existe pas");
            File fichier = new File("Base/"+req[6]+"/"+req[2]+".bdd");
            fichier.createNewFile();

            String[] listeColonne=req[4].split(",");
            
            FileWriter fileWriter=new FileWriter(fichier);
            BufferedWriter writter=new BufferedWriter(fileWriter);
            for (int i = 0; i < listeColonne.length ; i++) 
            {
                if (i<listeColonne.length-1) 
                {
                    writter.write(listeColonne[i]);
                    writter.write(",");
                }else{
                    writter.write(listeColonne[i]);
                }	
            }
            writter.newLine();
            writter.close();
        }
        if (req[1].compareTo("BASE")==0) {
            return Grammaire.showDB("Show Databases");
        }else{
            return Relation.getRelationFromBase(req[6], req[2]);
        }
    }
    public static Relation union(String base , String requete) throws Exception
    {
        // Union de r1,r2,...
        String[] req = Grammaire.changeRequete(requete);
        if (req.length!=3 || !req[1].equals("DE")) throw new Exception("Verifier votre requete : Union de r1,r2,....");
        String[] nomRelation = req[2].split(",");
        Relation[] relation = new Relation[nomRelation.length];
        for (int i = 0; i < relation.length; i++) 
        {
            relation[i]=Relation.getRelationFromBase(base, nomRelation[i]);    
        }
        // Relation.afficherResultat(Relation.union(relation));
        return Relation.union(relation);
    }
    public static Relation intersection(String base , String requete) throws Exception
    {
        // Intersection de r1,r2,...
        String[] req = Grammaire.changeRequete(requete);
        if (req.length!=3 || !req[1].equals("DE")) throw new Exception("Verifier votre requete : Intersection de r1,r2,....");
        String[] nomRelation = req[2].split(",");
        Relation[] relation = new Relation[nomRelation.length];
        for (int i = 0; i < relation.length; i++) 
        {
            relation[i]=Relation.getRelationFromBase(base, nomRelation[i]);    
        }
        // Relation.afficherResultat(Relation.intersection(relation));
        return Relation.intersection(relation);
    }
    public static Relation difference(String base , String requete) throws Exception
    {
        // Difference de r1 et r2
        String[] req = Grammaire.changeRequete(requete);
        if (req.length!=5 || !req[1].equals("DE") || !req[3].equals("ET")) throw new Exception("Verifier votre requete : Difference de r1 et r2");
        Relation relation1 = Relation.getRelationFromBase(base, req[2]);
        Relation relation2 = Relation.getRelationFromBase(base, req[4]);
        // Relation.afficherResultat(Relation.difference(relation1,relation2));
        return Relation.difference(relation1,relation2);
    }
    public static Relation produitCartesien(String base , String requete) throws Exception
    {
        // Produit de r1 par r2
        String[] req = Grammaire.changeRequete(requete);
        if (req.length!=5 || !req[1].equals("DE") || !req[3].equals("PAR")) throw new Exception("Verifier votre requete : Produit de r1 par r2");
        Relation relation1 = Relation.getRelationFromBase(base, req[2]);
        Relation relation2 = Relation.getRelationFromBase(base, req[4]);
        // Relation.afficherResultat(Relation.produitCartesien(relation1, relation2));
        return Relation.produitCartesien(relation1, relation2);
    }
    public static Relation division(String base , String requete) throws Exception
    {
        // Division de r1 par r2
        String[] req = Grammaire.changeRequete(requete);
        if (req.length!=5 || !req[1].equals("DE") || !req[3].equals("PAR")) throw new Exception("Verifier votre requete : Division de r1 par r2");
        Relation relation1 = Relation.getRelationFromBase(base, req[2]);
        Relation relation2 = Relation.getRelationFromBase(base, req[4]);
        // Relation.afficherResultat(Relation.division(relation1, relation2));
        return Relation.division(relation1, relation2);
    }
    public static Relation show(String base , String requete) throws Exception
    {
        String[] req=Grammaire.changeRequete(requete);
        if (req.length!=2 || !req[1].equals("TABLES"))  throw new Exception("Verifier votre requete : Show Tables");
        String nom = "Les tables dans "+base;
        Vector liste_colonne = new Vector<String>();
        liste_colonne.add("Table Name");

        Vector donnee = new Vector<Vector>();

        File dossier = new File("Base/"+base);
        if (dossier.isDirectory()) 
        {
            String[] listTable = dossier.list();
            for (int i = 0; i < listTable.length; i++) 
            {
                Vector ligne = new Vector<String>();
                ligne.add(listTable[i]);
                donnee.add(ligne);   
            }
        }
        Relation relation = new Relation(nom,liste_colonne,donnee);
        // Relation.afficherResultat(relation);
        return relation;
    }
    public static Relation showDB(String requete) throws Exception
    {
        String[] req=Grammaire.changeRequete(requete);
        if (req.length!=2 || !req[1].equals("DATABASES"))  throw new Exception("Verifier votre requete : Show Databases");
        String nom = "ALL DATABASES";
        Vector liste_colonne = new Vector<String>();
        liste_colonne.add("DB NAME");

        Vector donnee = new Vector<Vector>();

        File dossier = new File("Base/");
        if (dossier.isDirectory()) 
        {
            String[] listTable = dossier.list();
            for (int i = 0; i < listTable.length; i++) 
            {
                Vector ligne = new Vector<String>();
                ligne.add(listTable[i]);
                donnee.add(ligne);   
            }
        }
        Relation relation = new Relation(nom,liste_colonne,donnee);
        // Relation.afficherResultat(relation);
        return relation;
    }
}

