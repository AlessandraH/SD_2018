import java.net.*;
import java.io.*;

public class Publishers {

  private String name;
  private int port;

  Publishers(String name, int port) {
    this.name = name;
    this.port = port;
  }

  public String getName() {
    return this.name;
  }

  public int getPort() {
    return this.port;
  }

  public void publish(String s) {
    int intermPort;
    byte[] msg = new byte[50];
    DatagramSocket socket = null;

    if(this.getName().equals("p1")) intermPort = 7013;
    else intermPort = 7015;

    System.out.println("Pub: " + this.getName() + " publishing " + s);
    try {
      InetAddress host = InetAddress.getByName("localhost");
      socket = new DatagramSocket(this.getPort());
      msg = s.getBytes();
      DatagramPacket publ = new DatagramPacket(msg, msg.length, host, intermPort);
      socket.send(publ);
    } catch (SocketException e) {
      System.out.println("Socket: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("IO: " + e.getMessage());
    } finally {
      if(!socket.equals(null)) socket.close();
    }
  }

  public static void main(String[] args) {
    Publishers p1 = new Publishers("p1", 8020);
    Publishers p2 = new Publishers("p2", 8021);

    p1.publish("Y");
    p2.publish("X");
  }

}
