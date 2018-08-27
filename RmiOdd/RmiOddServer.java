import java.rmi.Naming;

public class RmiOddServer {

  public RmiOddServer() {
    try {
      RmiOdd1 odd = new RmiOddImpl1();
      Naming.rebind("//127.0.0.1:1099/RmiOddService1", odd);
    } catch (Exception e) {
      System.out.println("Trouble: " + e);
    }
  }

  public static void main(String[] args) {
    new RmiOddServer();
  }

}
