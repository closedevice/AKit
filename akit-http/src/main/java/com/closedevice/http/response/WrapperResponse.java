package com.closedevice.http.response;

import com.closedevice.http.HttpStatus;
import com.closedevice.http.NameValuePair;
import com.closedevice.http.exception.HttpException;
import com.closedevice.http.request.AbstractRequest;
import java.util.ArrayList;

/**
 * @author liudongdong
 * @date 12.12
 */

public class WrapperResponse<T> implements Response<T> {
  private final AbstractRequest<T> request;
  private HttpException exception;
  private HttpStatus httpStatus;
  private int contentLength;
  private String contentEncoding;
  private String contentType;
  private int redirectTimes;
  private ArrayList<NameValuePair> headers;
  private String uri;
  private String charSet;
  private long readedLength;

  public WrapperResponse(AbstractRequest<T> request) {
    this.request = request;
  }

  public void setException(HttpException exception) {
    this.exception = exception;
  }

  public void setHttpStatus(HttpStatus status) {
    this.httpStatus = status;
  }

  public void setContentLength(int contentLength) {
    this.contentLength = contentLength;
  }

  public void setContentEncoding(String contentEncoding) {
    this.contentEncoding = contentEncoding;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public int getRedirectTimes() {
    return redirectTimes;
  }

  public void setRedirectTimes(int redirectTimes) {
    this.redirectTimes = redirectTimes;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public String getContentType() {
    return contentType;
  }

  public void setCharset(String charSet) {
    if (charSet != null) {
      this.charSet = charSet;
    }
  }

  public ArrayList<NameValuePair> getHeaders() {
    return headers;
  }

  public void setHeaders(ArrayList<NameValuePair> headers) {
    this.headers = headers;
  }

  public int getContentLength() {
    return contentLength;
  }

  public void setReadedLength(long readedLength) {
    this.readedLength = readedLength;
  }

  public long getReadedLength() {
    return readedLength;
  }

  public boolean isOk() {
    return false;
  }

  public <R extends AbstractRequest<T>> R getRequest() {
    return (R) request;
  }
}
