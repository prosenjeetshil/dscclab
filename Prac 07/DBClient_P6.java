//DBClient.java for Practical 6 (Using Only one .accdb file)(same for mysql odbc connection)

import java.rmi.*;
import java.io.*;

public class DBClient_P6 {
  public static void main(String[] args) {
    String sql = "", res = "";
    try 
    {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("\n*** Books Table ***");
      sql = "select * from Book";
      String url = "rmi://127.0.0.1/DBServer";
      IDb id = (IDb) Naming.lookup(url);
      res = id.getData(sql, "LibraryDB");
      System.out.println("\n-----------------------------------------------------------");
      System.out.print(res);
      System.out.println("-----------------------------------------------------------");
    } // end of try
    catch (Exception e) 
    {
      e.printStackTrace();
    }
  }
}