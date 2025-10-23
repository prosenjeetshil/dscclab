//DBClient.java for Practical 6 (Using Only one .accdb file)(same for mysql odbc connection)

import java.rmi.*;
import java.io.*;

public class DBClient_P7 {
  public static void main(String[] args) {
    String sql = "", res = "";
    try 
    {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("\n*** Bill Table ***");
      sql = "select * from Bill";
      String url = "rmi://127.0.0.1/DBServer";
      IDb id = (IDb) Naming.lookup(url);
      res = id.getData(sql, "MtnlDB");
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