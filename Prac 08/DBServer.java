//DBServer.java

import java.rmi.*;
public class DBServer 
 {
  public static void main(String[] args) 
   {
    try
    {
     DBImpl di=new DBImpl();
     Naming.rebind("rmi://127.0.0.1/DBServer",di);
     System.out.println("Server Registered.");
    }
   catch (Exception e1)
    {
     e1.printStackTrace();
    }
  }
}
