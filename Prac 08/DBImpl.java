//DBImpl.java

import java.rmi.*;
import java.rmi.server.*;
import java.sql.*;

public class DBImpl extends UnicastRemoteObject implements IDb
  {
    String str,str1;
    public DBImpl() throws RemoteException
    {}

    public String getData(String sql,String dsn)
    {
      String URL="jdbc:odbc:"+ dsn; 
      try
      {
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        Connection con=DriverManager.getConnection(URL);
        Statement s=con.createStatement();
        ResultSet rs=s.executeQuery(sql);
        ResultSetMetaData rsmd=rs.getMetaData();
        str="";
	str1="";

        for(int i=1;i<=rsmd.getColumnCount();i++)
	{
          str1=str1+rsmd.getColumnName(i) + "\t";
	}
	System.out.println();

        while(rs.next())
        {
          for(int i=1;i<=rsmd.getColumnCount();i++)
            {
              str=str+rs.getString(i)+"\t";
            }
          str=str+"\n";
        }
     }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  return (str1+"\n" +str);
  }
}
