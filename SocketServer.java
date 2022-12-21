package socket;

import java.net.*;
import java.io.*;

public class SocketServer
{
  public static void main(String[] args) throws Exception 
  {
    try{
      ServerSocket server=new ServerSocket(8888);
      int counter=0;
      System.out.println("Server Started ....");
      
      while(true)
      {
        counter++;
        Socket serverClient=server.accept();
        System.out.println("Client Num:" + counter + " connecté");
        ServerClientThread sct = new ServerClientThread(serverClient,counter);
        sct.start();
      }
      
    }catch(Exception e){
      System.out.println(e);
    }
  }
}