import java.io.*;

public class Routings {

  String interm;
  String pub;

  Routings(String i, String p) {
    this.interm = i;
    this.pub = p;
  }

  public String getInterm() {
    return this.interm;
  }

  public String getPub() {
    return this.pub;
  }

  public void setInterm(String i) {
    this.interm = i;
  }

  public void setPub(String p) {
    this.pub = p;
  }

}
