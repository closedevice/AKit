package com.closedevice.http.request;

/**
 * @author liudongdong
 * @date 12.12
 */

public enum HttpMethod {
  GET("GET"), POST("POST");

  private String methodName;

  HttpMethod(String methodName) {
    this.methodName = methodName;
  }

  public String getMethodName() {
    return methodName;
  }
}
