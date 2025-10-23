//IDb.java

import java.rmi.*;
public interface IDb extends Remote
 {
   public String getData(String s,String db) throws RemoteException;
 }
