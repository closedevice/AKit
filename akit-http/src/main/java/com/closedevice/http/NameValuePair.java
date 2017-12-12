package com.closedevice.http;

/**
 * @author liudongdong
 * @date 12.12
 */

public class NameValuePair {
  private String name;
  private String value;

  public NameValuePair(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override public String toString() {
    return "NameValuePair{" + "name='" + name + '\'' + ", value='" + value + '\'' + '}';
  }
}
