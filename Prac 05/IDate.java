// IDate.java
import java.rmi.*;
public interface IDate extends Remote
  {
      String getDate() throws RemoteException;
  }