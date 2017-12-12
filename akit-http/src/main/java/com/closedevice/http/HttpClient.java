package com.closedevice.http;

import com.closedevice.http.request.AbstractRequest;
import com.closedevice.http.response.WrapperResponse;

/**
 * @author liudongdong
 * @date 12.12
 */

public interface HttpClient {
  void config(HttpConfig config);

  <T> void connect(AbstractRequest<T> request, WrapperResponse response) throws Exception;
}
