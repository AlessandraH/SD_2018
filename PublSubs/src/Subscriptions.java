import java.io.*;

public class Subscriptions {

  String sub;
  String pub;

  Subscriptions(String s, String p) {
    this.sub = s;
    this.pub = p;
  }

  public String getSub() {
    return this.sub;
  }

  public String getPub() {
    return this.pub;
  }

  public void setSub(String s) {
    this.sub = s;
  }

  public void setPub(String p) {
    this.pub = p;
  }

}
