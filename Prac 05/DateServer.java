// DateServer.java

import java.rmi.*;

public class DateServer
 {
  public static void main(String[] args)
   {
     try
      {
       DateImpl di=new DateImpl();
       Naming.rebind("DServer",di);

       System.out.println("Date Server is Ready");
      }
     catch(Exception e)
      {
       e.printStackTrace();
      }
  }
}