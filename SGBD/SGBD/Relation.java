package traitement;

import java.util.Vector;
import java.io.*;
import affichage.*;

public class Relation implements Serializable
{
    String nom;
    Vector liste_colonne;
    Vector donnees;

    public Relation(String nom, Vector liste_colonne, Vector donnees) {
        this.nom = nom;
        this.liste_colonne = liste_colonne;
        this.donnees = donnees;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public Vector getListe_colonne() {
        return liste_colonne;
    }
    public void setListe_colonne(Vector liste_colonne) {
        this.liste_colonne = liste_colonne;
    }
    public Vector getDonnees() {
        return donnees;
    }
    public void setDonnees(Vector donnees) {
        this.donnees = donnees;
    }

    public static Relation union(Relation[] relations)
    {
        String nom="Union de ";
        for (int i = 0; i < relations.length; i++) 
        {
            nom=nom+relations[i].getNom()+" ";
        }

        Vector liste_colonne=relations[0].getListe_colonne();

        Vector donnees=new Vector<Vector>();
        for (int i = 0; i < relations.length; i++) 
        {
            Vector v=(Vector)relations[i].getDonnees();
            for (int j = 0; j < v.size(); j++) 
            {
                Vector data=(Vector)v.get(j);
                if (!donnees.contains(data)) donnees.add(data); 
            }
        }
        
        Relation r_finale=new Relation(nom, liste_colonne, donnees);
        return r_finale;
    }
    public static Relation selection(Relation relation , String colonneSelectionner , String colonneValue , String egalite)
    {
        String nom="Selection de "+relation.getNom();

        Vector liste_colonne=relation.getListe_colonne();

        Vector donnees=new Vector<Vector>();
        int index_CS=relation.getListe_colonne().indexOf(colonneSelectionner);
        for (int i = 0; i < relation.getDonnees().size() ; i++) 
        {
            Vector data=(Vector)relation.getDonnees().get(i);
            String dataCS=String.valueOf(data.get(index_CS));
            if(egalite.compareTo("=")==0)
            {
                if (dataCS.compareTo(colonneValue)==0)
                {
                    donnees.add(data);
                }
            }
            if (egalite.compareTo("<")==0)
            {
                double doubleDataCS=Double.parseDouble(dataCS);
                double doubleColonneValue=Double.parseDouble(colonneValue);
                if (doubleDataCS<doubleColonneValue) 
                {
                    donnees.add(data);
                }
            }
            if (egalite.compareTo(">")==0) 
            {
                double doubleDataCS=Double.parseDouble(dataCS);
                double doubleColonneValue=Double.parseDouble(colonneValue);
                if (doubleDataCS>doubleColonneValue) 
                {
                    donnees.add(data);
                }
            }
            if (egalite.compareTo("<=")==0)
            {
                double doubleDataCS=Double.parseDouble(dataCS);
                double doubleColonneValue=Double.parseDouble(colonneValue);
                if (doubleDataCS<=doubleColonneValue) 
                {
                    donnees.add(data);
                }
            }
            if (egalite.compareTo(">=")==0) 
            {
                double doubleDataCS=Double.parseDouble(dataCS);
                double doubleColonneValue=Double.parseDouble(colonneValue);
                if (doubleDataCS>=doubleColonneValue) 
                {
                    donnees.add(data);
                }
            }
            if (egalite.compareTo("!=")==0) 
            {
                if (dataCS.compareTo(colonneValue)!=0)
                {
                    donnees.add(data);
                }
            }
        }

        Relation r_finale=new Relation(nom, liste_colonne, donnees);
        return r_finale;
    }

    public static Relation projection(Relation relation , String[] colonneSelectionner) throws Exception
    {
        String nom=relation.getNom();

        int[] index_CS=new int[colonneSelectionner.length];

        Vector liste_colonne=new Vector<String>();
        for (int i = 0; i < colonneSelectionner.length; i++) 
        {
            if (relation.getListe_colonne().contains(colonneSelectionner[i])) 
            {
                index_CS[i]=relation.getListe_colonne().indexOf(colonneSelectionner[i]);
                liste_colonne.add(colonneSelectionner[i]);
            }else{
                throw new Exception("La colonne "+colonneSelectionner[i]+" n' existe pas");
            }   
        }

        Vector donnees=new Vector<Vector>();
        for (int i = 0; i < relation.getDonnees().size() ; i++) 
        {
            Vector data=(Vector)relation.getDonnees().get(i);
            Vector data_finale=new Vector<String>();
            for (int j = 0; j < index_CS.length ; j++) 
            {
                data_finale.add(data.get(index_CS[j]));
            }
            if(!donnees.contains(data_finale))
            {
                donnees.add(data_finale);
            }
        }

        Relation r_finale=new Relation(nom, liste_colonne, donnees); 
        return r_finale;
    }
    public static Relation intersection(Relation[] relations)
    {
        String nom="Intersection de ";
        for (int i = 0; i < relations.length; i++) 
        {
            nom=nom+relations[i].getNom()+" ";
        }

        Vector liste_colonne=relations[0].getListe_colonne();

        Vector donnees=new Vector<Vector>();
            Vector d_r=new Vector<Vector>();
            d_r=relations[0].getDonnees();
            for (int i = 1; i < relations.length; i++) 
            {
                Vector temp=new Vector<Vector>();
                Vector v=relations[i].getDonnees();
                for (int j = 0; j < v.size() ; j++) 
                {
                    if (d_r.contains((Vector)v.get(j)))
                    {
                        temp.add(v.get(j));
                    }
                }
                d_r=temp;
            }
        donnees=d_r;
        Relation r_finale=new Relation(nom, liste_colonne, donnees);
        return r_finale;
    }
    public static Relation joiunture(Relation r1 , Relation r2 , String colonne1 , String colonne2)
    {
        String nom = "Jointure de "+r1.getNom()+" et de "+r2.getNom();

        Vector liste_colonne=new Vector<String>();
        for (int i = 0; i < r1.getListe_colonne().size(); i++) 
        {
            liste_colonne.add(r1.getListe_colonne().get(i));
        }
        for (int i = 0; i < r2.getListe_colonne().size(); i++) 
        {
            liste_colonne.add(r2.getListe_colonne().get(i));
        }

        Vector donnees=new Vector<Vector>();
        int indexC1=r1.getListe_colonne().indexOf(colonne1);
        int indexC2=r2.getListe_colonne().indexOf(colonne2);
        for (int i = 0; i < r1.getDonnees().size() ; i++) 
        {
            Vector dr1=(Vector)r1.getDonnees().get(i);
            String dataR1=String.valueOf(dr1.get(indexC1)).toUpperCase();

            for (int j = 0; j < r2.getDonnees().size() ; j++) 
            {
                Vector dr2=(Vector)r2.getDonnees().get(j);
                String dataR2=String.valueOf(dr2.get(indexC2)).toUpperCase();

                if (dataR1.compareTo(dataR2)==0) 
                {
                    Vector temp=new Vector<String>();
                    for (int k2 = 0; k2 < r1.getListe_colonne().size(); k2++) 
                    {
                        temp.add(String.valueOf( dr1.get(k2) ));
                    }
                    for (int k3 = 0; k3 < r2.getListe_colonne().size(); k3++) 
                    {
                        temp.add(String.valueOf( dr2.get(k3) ));  
                    }
                    donnees.add(temp);
                }
            }
            
        }

        Relation r_finale=new Relation(nom, liste_colonne, donnees);
        return r_finale;
    }
    public static Relation difference(Relation r1 , Relation r2)
    {
        String nom=r1.getNom()+" - "+r2.getNom();

        Vector liste_colonne=r1.getListe_colonne();

        Vector donnees=new Vector<Vector>();
        for (int i = 0; i < r1.getDonnees().size(); i++) 
        {
            if (!r2.getDonnees().contains( (Vector)r1.getDonnees().get(i) )) 
            {
                donnees.add((Vector)r1.getDonnees().get(i)); 
            }
        }
        Relation r_finale=new Relation(nom, liste_colonne, donnees);
        return r_finale;
    }

    public static Relation produitCartesien(Relation r1 , Relation r2)
    {
        String nom=r1.getNom()+" X "+r2.getNom();
        Vector liste_colonne=r1.getListe_colonne();
        for (int i = 0; i < r2.getListe_colonne().size() ; i++) 
        {
            liste_colonne.add(String.valueOf(r2.getListe_colonne().get(i)));
        }
        Vector donnees=new Vector<Vector>();
        for (int i = 0; i < r2.getDonnees().size(); i++) 
        {
            Vector temp_r2=(Vector)r2.getDonnees().get(i);
            String[] donnee_temp_r2=Relation.objectToString(temp_r2.toArray());
            for (int j = 0; j < r1.getDonnees().size(); j++) 
            {
                Vector temp_donnees=new Vector<String>();
                Vector temp_r1=(Vector)r1.getDonnees().get(j);
                String[] donnee_temp_r1=Relation.objectToString(temp_r1.toArray());
                for (int k = 0; k < donnee_temp_r1.length; k++) 
                {
                    temp_donnees.add(donnee_temp_r1[k]);
                }
                for (int k2 = 0; k2 < donnee_temp_r2.length; k2++) 
                {
                    temp_donnees.add(donnee_temp_r2[k2]);
                }
                donnees.add(temp_donnees);
            }

        }
        Relation r_finale=new Relation(nom, liste_colonne, donnees);
        return r_finale;
    }

    public static Relation division(Relation r1 ,Relation r2)
    {
        int[] indice_colonne_r2_in_r1=new int[r2.getListe_colonne().size()];
        int i_indice_colonne_r2_in_r1=0;

        String nom=r1.getNom()+" / "+r2.getNom();
        Vector liste_colonne=new Vector<String>();
        for (int i = 0; i < r1.getListe_colonne().size() ; i++) 
        {
            String temp=String.valueOf(r1.getListe_colonne().get(i));
            boolean verification=true;
            for (int j = 0; j <r2.getListe_colonne().size() ; j++) 
            {
                String temp2=String.valueOf(r2.getListe_colonne().get(j));
                if (temp.compareTo(temp2)==0) 
                {
                    verification=false;
                }
            }
            if (verification==true) liste_colonne.add(temp);
            if (verification==false)
            {
                indice_colonne_r2_in_r1[i_indice_colonne_r2_in_r1]=i;
                i_indice_colonne_r2_in_r1+=1;
            }
        }

        Vector donnees=new Vector<Vector>();
        Relation[] temp_relations=new Relation[r2.getDonnees().size()];
        

        for (int index = 0; index < r2.getDonnees().size(); index++) 
        {
            Vector temp_r2=(Vector)r2.getDonnees().get(index);
            String[] tab_r2=Relation.objectToString(temp_r2.toArray());
            
            Vector temp_donnees=new Vector<Vector>();

            for (int i = 0; i < r1.getDonnees().size(); i++) 
            {
                Vector temp_r1=(Vector)r1.getDonnees().get(i);
                if (Relation.vectorContains(temp_r1, tab_r2 , indice_colonne_r2_in_r1))
                {  
                    Vector v=new Vector<String>();
                    for (int j = 0; j < r1.getListe_colonne().size()-r2.getListe_colonne().size() ; j++) 
                    {
                        v.add(String.valueOf(temp_r1.get(j)));
                    }
                    temp_donnees.add(v);
                }
            }
            temp_relations[index]=new Relation("temp"+index, liste_colonne, temp_donnees);
        }
        
        
        donnees=(Relation.intersection(temp_relations)).getDonnees();
        Relation r_finale=new Relation(nom, liste_colonne, donnees);
        return r_finale;
    }

    public static void afficherResultat(Relation r)
    {
        new Frame(r);
        for (int i = 0; i < r.getListe_colonne().size() ; i++) 
        {
            if (i < r.getListe_colonne().size()-1) {
                System.out.print(r.getListe_colonne().get(i) + " , ");   
            }else{
                System.out.print(r.getListe_colonne().get(i));   
            }
        }
        System.out.println(" ");
        for (int i = 0; i < r.getDonnees().size() ; i++) 
        {
            Vector d=(Vector)r.getDonnees().get(i);
            for (int j = 0; j < d.size(); j++) 
            {
                if (j < d.size()-1) 
                {
                    System.out.print(d.get(j)+" , ");  
                }else{
                    System.out.print(d.get(j));
                }
            }
            System.out.println(" ");
        }
    }
    public static Relation getRelationFromBase(String base,String nom) throws Exception
    {
        Vector liste_colonne=new Vector<String>();
        Vector donnees=new Vector<Vector>();
        Relation r=new Relation(nom, liste_colonne, donnees);
        // try
        // {
            BufferedReader reader=new BufferedReader(new FileReader("Base/"+base+"/"+nom+".bdd"));
            for(int i=0 ; reader.ready() ; i++)
            {
                if (i==0) 
                {
                    String[] colonne=reader.readLine().split(",");
                    for (int j = 0; j < colonne.length; j++) 
                    {
                        liste_colonne.add(colonne[j]);    
                    }
                }else{
                    String[] allData=reader.readLine().split(",");
                    Vector data=new Vector<String>();
                    for (int j = 0; j < allData.length; j++) 
                    {
                        data.add(allData[j]);
                    }
                    donnees.add(data);
                }
            }
        // }catch(Exception e)  
        // {
        //     if (e instanceof FileNotFoundException) 
        //     {
        //         new Frame(e);
        //         System.out.println("La table "+nom+" n'existe pas dans la base "+base+", ou la base "+base+" n'existe pas");
        //     }else{
        //         System.out.println(e.getMessage());
        //     }
        // }
        return r;
    }
    public static String[] objectToString(Object[] tabObj)
    {
        String[] tabString=new String[tabObj.length];
        for (int i = 0; i < tabString.length; i++) 
        {
            tabString[i]=String.valueOf(tabObj[i]);
        }
        return tabString;
    }
    public static boolean vectorContains(Vector vector, String[] tab , int[] indice)
    {
        int count=0;
        Vector temp=new Vector<String>();
        for (int j = 0 ; j < indice.length; j++) 
        {
            temp.add(String.valueOf(vector.get(indice[j])));
        }
        for (int i =0; i < tab.length; i++) 
        {
            if (temp.contains(tab[i]))
            {
                count+=1;
            }
        }
        if (count==tab.length) 
        {
            return true;
        }
        return false;
    }
    public void relationToFile(String nomBase ,String nomFichier)
    {
        File fichier=new File("Base/"+nomBase+"/"+nomFichier+".bdd");
        try {
            if (!(new File("Base/"+nomBase)).exists()) 
            {
                (new File("Base/"+nomBase)).mkdir();
            }
            if (!fichier.exists()) 
            {
                fichier.createNewFile();
            }
            FileWriter fileWriter=new FileWriter(fichier);
            BufferedWriter writter=new BufferedWriter(fileWriter);
            for (int i=0;i<this.getListe_colonne().size() ;i++ ) 
            {
                if (i<this.getListe_colonne().size()-1) 
                {
                    writter.write(String.valueOf(this.getListe_colonne().get(i)));
                    writter.write(",");
                }else{
                    writter.write(String.valueOf(this.getListe_colonne().get(i)));
                }
                
            }	
            writter.newLine();
            for (int i = 0; i < this.getDonnees().size(); i++) 
            {
                Vector temp=(Vector)this.getDonnees().get(i);
                for (int index = 0; index < temp.size(); index++) 
                {
                    if (index< temp.size()-1) 
                    {
                        writter.write(String.valueOf(temp.get(index)));
                        writter.write(",");
                    }else{
                        writter.write(String.valueOf(temp.get(index)));
                    }
                    
                }
                writter.newLine();
            }
            writter.close();
        } catch (Exception e) { System.out.println(e.getMessage()); } 
    }
}