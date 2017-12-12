package com.closedevice.http.listener;

import com.closedevice.http.request.AbstractRequest;
import com.closedevice.http.response.Response;

/**
 * @author liudongdong
 * @date 12.12
 */

public abstract class HttpListener {

  public void onStart(AbstractRequest request) {

  }

  public void onFailed() {

  }

  public void onSuccess(Response response) {

  }

  public final void callStart(AbstractRequest request) {

  }

  public <T> void callRedirect(AbstractRequest<T> request, int maxRedirectTimes,
      int redirectTimes) {

  }
}
