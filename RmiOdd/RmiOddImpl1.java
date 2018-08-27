import java.rmi.Naming;
import java.rmi.RemoteException;

public class RmiOddImpl1 extends java.rmi.server.UnicastRemoteObject implements RmiOdd1 {

  public RmiOddImpl1() throws java.rmi.RemoteException {
    super();
  }

  public boolean isOdd1(int n) throws java.rmi.RemoteException {
    return (n%2) == 1;
  }

}
