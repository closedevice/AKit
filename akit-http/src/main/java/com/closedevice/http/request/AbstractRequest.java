package com.closedevice.http.request;

import com.closedevice.http.HttpBody;
import com.closedevice.http.convert.DataConvertor;
import com.closedevice.http.listener.HttpListener;

/**
 * @author liudongdong
 * @date 12.12
 */

public abstract class AbstractRequest<T> {
  private static final String TAG = "AbstractRequest";

  private String uri;
  private HttpMethod method;
  private HttpListener httpListener;
  private CacheMode cacheMode;
  private int readTimeout = -1;
  private int connectTimeout = -1;
  private int maxRetryTimes = -1;

  private HttpBody httpBody;
  private String charSet;

  public AbstractRequest(String uri) {
    this.uri = uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  @SuppressWarnings("unchecked")
  public <S extends AbstractRequest<T>> S setMethod(HttpMethod method) {
    this.method = method;
    return (S) this;
  }

  public <S extends AbstractRequest<T>> S setListener(HttpListener listener) {
    this.httpListener = listener;
    return (S) this;
  }

  public HttpListener getListener() {
    return this.httpListener;
  }

  public CacheMode getCacheMode() {
    return cacheMode;
  }

  public boolean isCancelledOrInterrupted() {
    return false;
  }

  public int getMaxRetryTimes() {
    return maxRetryTimes;
  }

  public String realUrl() {
    return null;
  }

  public int getReadTimeout() {
    return readTimeout;
  }

  public int getConnectTimeout() {
    return connectTimeout;
  }

  public HttpMethod getMethod() {
    return method;
  }

  public HttpBody getHttpBody() {
    return httpBody;
  }

  public void cancel() {

  }

  public String getCharSet() {
    return charSet;
  }

  public DataConvertor getDataConvertor() {

  }
}
