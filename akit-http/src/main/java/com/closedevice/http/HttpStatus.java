package com.closedevice.http;

/**
 * @author liudongdong
 * @date 12.12
 */

public class HttpStatus {
  private int code;
  private String message;

  public HttpStatus(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
