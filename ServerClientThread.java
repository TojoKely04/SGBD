package socket;

import java.net.*;
import traitement.*;
import java.io.*;

public class ServerClientThread extends Thread 
{
    Socket serverClient;
    int clientNo;

    ServerClientThread(Socket inSocket,int counter)
    {
      this.serverClient = inSocket;
      this.clientNo=counter;
    }

    public void run()
    {
      try{
        ObjectInputStream objectInputStream = new ObjectInputStream(this.serverClient.getInputStream());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(this.serverClient.getOutputStream());
        
        String requete ="";
        String base="";
        while (true) 
        {
          
          if((requete=objectInputStream.readUTF())!=null && (base=objectInputStream.readUTF())!=null)
          {
            System.out.println("Client -"+clientNo+" :");
            System.out.println(requete);
            System.out.println(base);
            try {
              Relation relation = Grammaire.requete(base, requete);
              objectOutputStream.writeObject(relation);
            } catch (Exception e) {
              objectOutputStream.writeObject(e);
            }
            objectOutputStream.flush();

            requete=null;
            base=null;
          }
        }
        
      }catch(Exception ex){
        // System.out.println(ex);
      }finally{
        System.out.println("Client -" + clientNo + " exit!! ");
      }
    }
}