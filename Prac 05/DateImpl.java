// DateImpl.java
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class DateImpl extends UnicastRemoteObject implements IDate
{
  public DateImpl() throws RemoteException
  {}
  public String getDate()
  {
    Date d=new Date();
    return(d.toString());
  }
}
