import java.rmi.Naming;
import java.rmi.RemoteException;

public class RmiOddImpl extends java.rmi.server.UnicastRemoteObject implements RmiOdd {

  public RmiOddImpl() throws java.rmi.RemoteException {
    super();
  }

  public boolean isOdd(int n) throws java.rmi.RemoteException {
    try {
      RmiOdd1 odd1 = (RmiOdd1) Naming.lookup("//127.0.0.1:1099/RmiOddService1");
      return odd1.isOdd1(n);
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }

  }

}
