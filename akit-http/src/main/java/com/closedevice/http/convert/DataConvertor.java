package com.closedevice.http.convert;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author liudongdong
 * @date 12.12
 */

public abstract class DataConvertor<T> {
  private long readLength;
  private T data;

  public T readIn(InputStream is, int len, String charSet) throws IOException {
    if (is != null) {
      try {
        data = convert(is, len, charSet);
      } finally {
        is.close();
      }
    }
    return data;
  }

  public T getData() {
    return data;
  }

  public abstract T convert(InputStream is, int len, String charSet);

  public long getReadedLength() {
    return this.readLength;
  }
}
