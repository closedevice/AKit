package com.closedevice.http;

import android.content.Context;

/**
 * @author liudongdong
 * @date 12.12
 */

public class HttpConfig {
  private Context context;
  private int connectTimeout;
  private int socketTimeout;
  public HttpClient httpClient;

  public HttpConfig(Context context) {
    if (context != null) {
      this.context = context.getApplicationContext();
    }
  }

  public AKitHttp create() {
    return new AKitHttp(this);
  }

  public HttpConfig setConnectTimeout(int connectTimeOut) {
    this.connectTimeout = connectTimeOut;
    return this;
  }

  public HttpConfig setSocketTimeout(int socketTimeout) {
    this.socketTimeout = socketTimeout;
    return this;
  }
}
