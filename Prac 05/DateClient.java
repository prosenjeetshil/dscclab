// DateClient.java

import java.rmi.*;
public class DateClient
 {
   public static void main(String[] args)
    {
     try
      {
        String url="rmi://127.0.0.1/DServer";
        IDate intf=(IDate)Naming.lookup(url);
        // IDate intf=(IDate)Naming.lookup("rmi://127.0.0.1/DateServer");

        System.out.println("The Date On Server is: "+intf.getDate());
      }
     catch(Exception e)
      {
        e.printStackTrace();
      }
   }
}
