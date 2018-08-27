import java.rmi.Naming;

public class RmiOddClient {

  public static void main(String[] args) {
    try {
      RmiOdd odd = (RmiOdd) Naming.lookup("//127.0.0.1:1099/RmiOddService");
      System.out.println(odd.isOdd(40));
      System.out.println(odd.isOdd(3));
      System.out.println(odd.isOdd(100));
      System.out.println(odd.isOdd(89));
      System.out.println(odd.isOdd(54));
    } catch (Exception e) {
      System.out.println(e);
    }
  }

}
