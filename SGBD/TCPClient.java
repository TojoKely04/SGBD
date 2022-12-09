package socket;

import java.net.*;

import traitement.Relation;
import javax.swing.JOptionPane;

import affichage.Frame;
import traitement.Fonction;

import java.io.*;
public class TCPClient 
{
  public static void main(String[] args) throws Exception 
  {
    try{
      Socket socket=new Socket("192.168.43.77",8888);
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
      ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
      Frame frame = new Frame();

      Thread envoi = new Thread(new Runnable() {
        @Override
        public void run(){
          try{
            String requete;
            String base;
            while(true)
            {
              if (frame.getRequete()!=null && frame.getBase()!=null) 
              {
                objectOutputStream.writeUTF(frame.getRequete());
                objectOutputStream.writeUTF(frame.getBase());
                objectOutputStream.flush();
                frame.setRequete(null);
                frame.setBase(null);
                // frame.dispose();
              }else{
                System.out.print(" ");
              }
            }
          }catch(Exception e){
            e.printStackTrace();
          }
        }
      });
      envoi.start();


      Thread recevoir = new Thread(new Runnable() {
        @Override
        public void run()
        {
          try {
           while (true)     
           {
              Relation relation = null;
              if (objectInputStream.readObject() instanceof Exception) {
                  Exception exception = (Exception)objectInputStream.readObject();
                  JOptionPane.showMessageDialog(null,exception.getMessage());
              }else{
                relation=(Relation)objectInputStream.readObject(); 
                Frame resultat = new Frame(relation);
              }
           }
          } catch (Exception e) {
            e.printStackTrace();
          }
          
        }
      });
      recevoir.start();

    }catch(Exception e){
      e.printStackTrace();
    }
  }
}