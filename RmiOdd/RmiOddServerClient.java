import java.rmi.Naming;

public class RmiOddServerClient {

  public RmiOddServerClient() {
    try {
      RmiOdd odd = new RmiOddImpl();
      Naming.rebind("//127.0.0.1:1099/RmiOddService", odd);
    } catch (Exception e) {
      System.out.println("Trouble: " + e);
    }
  }

  public static void main(String[] args) {
    new RmiOddServerClient();
  }

}
