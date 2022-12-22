package socket;

import java.net.*;
import traitement.Relation;
import javax.swing.JOptionPane;
import affichage.Frame;
import java.io.*;

public class SocketClient 
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
            while(true)
            {
                try {
                  objectOutputStream.writeUTF(frame.getRequete());
                  objectOutputStream.writeUTF(frame.getBase());
                  objectOutputStream.flush();

                  System.out.println("Requete :"+frame.getRequete());
                  System.out.println("Base :"+frame.getBase());

                  frame.setRequete(null);
                  frame.setBase(null);
                  // frame.dispose();
                } catch (Exception e) {
                  //TODO: handle exception
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
             try {
              Object object = objectInputStream.readObject();
              if (object instanceof Exception)
              {
                  Exception exception = (Exception)object;
                  JOptionPane.showMessageDialog(null,exception.getMessage());
              }
              if(object instanceof Relation)
              {
                Relation relation=(Relation)object; 
                Relation.afficherResultat(relation);
                // Frame resultat = new Frame(relation);
              }
             } catch (Exception e) {
               //TODO: handle exception
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