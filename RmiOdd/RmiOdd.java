import java.rmi.RemoteException;

public interface RmiOdd extends java.rmi.Remote {

  public boolean isOdd(int n) throws java.rmi.RemoteException;

}
