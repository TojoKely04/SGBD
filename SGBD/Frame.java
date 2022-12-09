package affichage;

import javax.swing.*;
import traitement.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.table.*;

public class Frame extends JFrame implements MouseListener
{
    JTextField TextFieldRequete = new JTextField();
    JTextField textFieldBase = new JTextField();
    JButton valider;
    String base;
    String requete;
    
    // Pour les resultats apres requete
    public Frame(Relation relation)
    {
        String[] entete = new String[relation.getListe_colonne().size()];
        for (int i = 0; i < entete.length; i++) 
        {
            entete[i]=String.valueOf(relation.getListe_colonne().get(i));    
        }
        String[][] data = new String[relation.getDonnees().size()][relation.getListe_colonne().size()];
        for(int i=0 ; i<relation.getDonnees().size() ; i++)
        {
            Vector ligne = (Vector)relation.getDonnees().get(i);
            for(int j=0;j<ligne.size();j++)
            {
                data[i][j]=String.valueOf(ligne.get(j));
            }
        }
        // pour le placement des entetes
        DefaultTableModel tableModel = new DefaultTableModel(data,entete){
            @Override
            public boolean isCellEditable(int row , int col) // on ne peut plus modifie les elements du tableau
            {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        
        JScrollPane scrollPane=new JScrollPane();
        scrollPane.setViewportView(table);
        scrollPane.setBounds(2, 0, 600, 360);
        this.add(scrollPane);
        this.setLayout(null);
        this.setSize(620,400);
        this.setVisible(true);
        this.setTitle(relation.getNom());
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    // Pour les requetes
    public Frame()
    {
        JLabel labelRequete=new JLabel("Votre Requete :");
        labelRequete.setBounds(10, 10, 200, 20);

        this.TextFieldRequete = new JTextField();
        this.TextFieldRequete.setBounds(10,30,300,40);

        this.valider = new JButton("Ok");
        this.valider.setBounds(10,170,50,40);
        this.valider.addMouseListener(this);

        JLabel labelBase = new JLabel("Choisir une Base de Donnee :");
        labelBase.setBounds(10,80,200,40);

        this.textFieldBase=new JTextField();
        this.textFieldBase.setBounds(10,120,300,40);
           
        JButton quitter = new JButton("Quitter");
        quitter.setBounds(10, 300, 100, 40);
        quitter.addMouseListener(this);

        this.add(quitter);
        this.add(labelRequete);
        this.add(valider);
        this.add(labelBase);
        this.add(TextFieldRequete);
        this.add(textFieldBase);
        this.setLayout(null);
        this.setSize(400,400);
        this.setVisible(true);
        this.setTitle("Requete");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    public void mouseClicked(MouseEvent e)
    {
        if (e.getSource() instanceof JButton) 
        {
            JButton temp = (JButton)e.getSource(); 
            if (temp.getText().equals("Quitter")) 
            {
                this.dispose();
            }else{
                this.requete = this.TextFieldRequete.getText();
                this.base = this.textFieldBase.getText();
                // System.out.println("Votre Requete :"+this.requete);
                // if(this.textFieldBase.getText().isEmpty())
                // {
                //     JOptionPane.showMessageDialog(null,"Mila mapiditra Base");
                //     return;
                // }
            }
        }
        // this.revalidate();
    }
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}

    public JTextField getTextFieldRequete() {
        return TextFieldRequete;
    }

    public void setTextFieldRequete(JTextField textFieldRequete) {
        TextFieldRequete = textFieldRequete;
    }

    public JButton getValider() {
        return valider;
    }

    public void setValider(JButton valider) {
        this.valider = valider;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getRequete() {
        return requete;
    }

    public void setRequete(String requete) {
        this.requete = requete;
    }
}
