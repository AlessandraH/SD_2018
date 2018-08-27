import java.net.*;
import java.io.*;
import java.util.*;

public class Intermediate {

  private String name;
  private int portS2I;
  private int portP2I;
  private int portI2S;
  private int portI2I;
  private List<Intermediate> neighborsIntermediate = new ArrayList<>();
  private List<Subscriptions> subscriptions = new ArrayList<>();
  private List<Routings> routings = new ArrayList<>();

  Intermediate(String name, int portS2I, int portP2I, int portI2S, int portI2I) {
    this.name = name;
    this.portS2I = portS2I;
    this.portP2I = portP2I;
    this.portI2S = portI2S;
    this.portI2I = portI2I;
  }

  public String getName() {
    return this.name;
  }

  public int getPortS2I() {
    return this.portS2I;
  }

  public int getPortP2I() {
    return this.portP2I;
  }

  public int getPortI2S() {
    return this.portI2S;
  }

  public int getPortI2I() {
    return this.portI2I;
  }

  public void receiveSubscription() {
    DatagramSocket socket = null;
    boolean containsS = false;
    boolean containsR = false;
    try {
      socket = new DatagramSocket(this.getPortS2I());
      byte[] msg = new byte[50];
      while(true) {
        for(int i = 0; i < msg.length; i++) msg[i] = '\0';
        DatagramPacket subs = new DatagramPacket(msg, msg.length);
        socket.receive(subs);

        String subscriber;
        if(subs.getPort() == 8000) subscriber = "s1";
        else if(subs.getPort() == 8001) subscriber = "s2";
        else subscriber = "s3";

        Subscriptions subAdd = new Subscriptions(subscriber, new String(subs.getData()));
        for(Subscriptions s : this.subscriptions) {
          if(s.getSub().equals(subAdd.getSub()) && s.getPub().equals(subAdd.getPub())) containsS = true;
        }
        if(!containsS) this.subscriptions.add(subAdd);

        Routings routAdd = new Routings(this.name, new String(subs.getData()));
        for(Intermediate i : this.neighborsIntermediate) {
          for(Routings r : i.routings) {
            if(r.getInterm().equals(routAdd.getInterm()) && r.getPub().equals(routAdd.getPub())) containsR = true;
          }
          if(!containsR) i.routings.add(routAdd);
        }

        // for(Subscriptions s : this.subscriptions) {
        //   System.out.println("From: " + this.name + " Sub: " + s.getSub() + ", Pub: " + s.getPub());
        // }
        // for(Routings r : this.routings) {
        //   System.out.println("From: " + this.name + " I: " + r.getInterm() + ", P: " + r.getPub());
        // }
        // System.out.println(" - - - - - - - - - - - ");
      }
    } catch(SocketException e) {
      System.out.println("Socket: " + e.getMessage());
    } catch(IOException e) {
      System.out.println("IO: " + e.getMessage());
    } finally {
      if(!socket.equals(null)) socket.close();
    }
  }

  public void receivePublish() {
    DatagramSocket socketP = null;
    DatagramSocket socketS = null;
    DatagramSocket socketI = null;
    boolean contains = false;
    try {
      socketP = new DatagramSocket(this.getPortP2I());
      byte[] msg = new byte[50];
      byte[] msg2 = new byte[50];
      while(true) {
        for(int i = 0; i < msg.length; i++) {
          msg[i] = '\0';
          msg2[i] = '\0';
        }
        DatagramPacket publ = new DatagramPacket(msg, msg.length);
        socketP.receive(publ);
        String msg1 = new String(publ.getData());

        int intermPort;
        for(Subscriptions s : this.subscriptions) {
          if(s.getPub().equals(msg1)) {
            if(s.getSub().equals("s1")) intermPort = 8003;
            else if(s.getSub().equals("s2")) intermPort = 8004;
            else intermPort = 8005;

            try {
              InetAddress host = InetAddress.getByName("localhost");
              socketS = new DatagramSocket(this.getPortI2S());
              msg2 = msg1.getBytes();
              DatagramPacket publ1 = new DatagramPacket(msg2, msg2.length, host, intermPort);
              socketS.send(publ1);
            } catch (SocketException e) {
              System.out.println("Socket: " + e.getMessage());
            } catch (IOException e) {
              System.out.println("IO: " + e.getMessage());
            } finally {
              if(!socketS.equals(null)) socketS.close();
            }

          }
        }

        for(Routings r : this.routings) {
          if(r.getPub().equals(msg1)) {
            if(r.getInterm().equals("i1")) intermPort = 7019;
            else if(r.getInterm().equals("i2")) intermPort = 7020;
            else intermPort = 7021;

            try {
              InetAddress host = InetAddress.getByName("localhost");
              socketI = new DatagramSocket();
              msg2 = msg1.getBytes();
              DatagramPacket publ2 = new DatagramPacket(msg2, msg2.length, host, intermPort);
              socketI.send(publ2);
            } catch (SocketException e) {
              System.out.println("Socket: " + e.getMessage());
            } catch (IOException e) {
              System.out.println("IO: " + e.getMessage());
            } finally {
              if(!socketI.equals(null)) socketI.close();
            }

          }
        }
      } // while(true) end
    } catch(SocketException e) {
      System.out.println("Socket: " + e.getMessage());
    } catch(IOException e) {
      System.out.println("IO: " + e.getMessage());
    } finally {
      if(!socketP.equals(null)) socketP.close();
    }
  }

  public void receiveIntermediate() {
    DatagramSocket socketR = null;
    DatagramSocket socketS = null;
    try {
      socketR = new DatagramSocket(this.getPortI2I());
      byte[] msg = new byte[50];
      while(true) {
        for(int i = 0; i < msg.length; i++) msg[i] = '\0';
        DatagramPacket publ = new DatagramPacket(msg, msg.length);
        socketR.receive(publ);
        String msg1 = new String(publ.getData());

        int port;
        for(Subscriptions s : this.subscriptions) {
          if(s.getPub().equals(msg1)) {
            if(s.getSub().equals("s1")) port = 8003;
            else if(s.getSub().equals("s2")) port = 8004;
            else port = 8005;

            try {
              InetAddress host = InetAddress.getByName("localhost");
              socketS = new DatagramSocket(this.getPortI2S());
              DatagramPacket publ1 = new DatagramPacket(msg, msg.length, host, port);
              socketS.send(publ1);
            } catch (SocketException e) {
              System.out.println("Socket: " + e.getMessage());
            } catch (IOException e) {
              System.out.println("IO: " + e.getMessage());
            } finally {
              if(!socketS.equals(null)) socketS.close();
            }

          }
        }
      } // while(true) end
    } catch (SocketException e) {
      System.out.println("Socket: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("IO: " + e.getMessage());
    } finally {
      if(!socketS.equals(null)) socketS.close();
    }
  }

  public static void main(String[] args) {
    Intermediate i1 = new Intermediate("i1", 7010, 7013, 7016, 7019);
    IntermediatePar ip1s = new IntermediatePar(i1, "recvSub");
    IntermediatePar ip1p = new IntermediatePar(i1, "recvPub");
    IntermediatePar ip1i = new IntermediatePar(i1, "recvInt");

    Intermediate i2 = new Intermediate("i2", 7011, 7014, 7017, 7020);
    IntermediatePar ip2s = new IntermediatePar(i2, "recvSub");
    IntermediatePar ip2p = new IntermediatePar(i2, "recvPub");
    IntermediatePar ip2i = new IntermediatePar(i2, "recvInt");

    Intermediate i3 = new Intermediate("i3", 7012, 7015, 7018, 7021);
    IntermediatePar ip3s = new IntermediatePar(i3, "recvSub");
    IntermediatePar ip3p = new IntermediatePar(i3, "recvPub");
    IntermediatePar ip3i = new IntermediatePar(i3, "recvInt");

    i1.neighborsIntermediate.add(i2);
    i2.neighborsIntermediate.add(i1);
    i2.neighborsIntermediate.add(i3);
    i3.neighborsIntermediate.add(i2);

    ip1s.start();
    ip2s.start();
    ip3s.start();

    ip1p.start();
    ip2p.start();
    ip3p.start();

    ip1i.start();
    ip2i.start();
    ip3i.start();

    try {
      ip1s.join();
      ip2s.join();
      ip3s.join();

      ip1p.join();
      ip2p.join();
      ip3p.join();

      ip1i.join();
      ip2i.join();
      ip3i.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}


class IntermediatePar extends Thread {

  private Intermediate i;
  private String task;

  IntermediatePar(Intermediate i, String task) {
    this.i = i;
    this.task = task;
  }

  public Intermediate getIntermediate() {
    return this.i;
  }

  public String getTask() {
    return this.task;
  }

  @Override
  public void run() {
    if(this.task.equals("recvSub")) {
      while(true)
        this.i.receiveSubscription();
    } else if(this.task.equals("recvPub")) {
      while(true)
        this.i.receivePublish();
    } else {
      while(true)
        this.i.receiveIntermediate();
    }
  }

}
