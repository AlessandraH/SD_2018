import java.net.*;
import java.io.*;

public class Subscribers {

  private String name;
  private int portS2I;
  private int portI2S;

  Subscribers(String name, int portS2I, int portI2S) {
    this.name = name;
    this.portS2I = portS2I;
    this.portI2S = portI2S;
  }

  public String getName() {
    return this.name;
  }

  public int getPortS2I() {
    return this.portS2I;
  }

  public int getPortI2S() {
    return this.portI2S;
  }

  public void subscribe(String s) {
    int intermPort;
    byte[] msg = new byte[50];
    DatagramSocket socket = null;

    if(this.getName().equals("s3")) {
      intermPort = 7012;
    } else { // if(this.getName().equals("s1")||this.getName().equals("s2"))
      intermPort = 7011;
    }

    System.out.println("Sub: " + this.getName() + " subscribed on " + s);
    try {
      InetAddress host = InetAddress.getByName("localhost");
      socket = new DatagramSocket(this.getPortS2I());
      msg = s.getBytes();
      DatagramPacket subs = new DatagramPacket(msg, msg.length, host, intermPort);
      socket.send(subs);
    } catch (SocketException e) {
      System.out.println("Socket: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("IO: " + e.getMessage());
    } finally {
      if(!socket.equals(null)) socket.close();
    }
  }

  public void receivePublish() {
    DatagramSocket socket = null;

    try {
      socket = new DatagramSocket(this.getPortI2S());
      byte[] msg = new byte[50];
      while(true) {
        for(int i = 0; i < msg.length; i++) msg[i] = '\0';
        DatagramPacket publ = new DatagramPacket(msg, msg.length);
        socket.receive(publ);

        String pub = new String(publ.getData());
        System.out.println("Subscriber: " + this.name +
                            " received: " + pub);
      }
    } catch (SocketException e) {
      System.out.println("Socket: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("IO: " + e.getMessage());
    } finally {
      if(!socket.equals(null)) socket.close();
    }
  }

  public static void main(String[] args) {
    Subscribers s1 = new Subscribers("s1", 8000, 8003);
    SubscribersPar sp1 = new SubscribersPar(s1);

    Subscribers s2 = new Subscribers("s2", 8001, 8004);
    SubscribersPar sp2 = new SubscribersPar(s2);

    Subscribers s3 = new Subscribers("s3", 8002, 8005);
    SubscribersPar sp3 = new SubscribersPar(s3);

    s1.subscribe("X");
    s2.subscribe("Y");
    s3.subscribe("X");
    s1.subscribe("Y");

    sp1.start();
    sp2.start();
    sp3.start();

    try {
      sp1.join();
      sp2.join();
      sp3.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}


class SubscribersPar extends Thread {

  private Subscribers s;
  private String task;

  SubscribersPar(Subscribers s) {
    this.s = s;
  }

  public Subscribers getSubscribers() {
    return this.s;
  }

  @Override
  public void run() {
    while(true)
      this.s.receivePublish();
  }

}
