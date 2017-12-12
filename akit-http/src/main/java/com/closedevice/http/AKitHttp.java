package com.closedevice.http;

import android.content.Context;
import android.util.Log;
import com.closedevice.http.exception.ClientException;
import com.closedevice.http.exception.HttpException;
import com.closedevice.http.exception.NetException;
import com.closedevice.http.exception.ServerException;
import com.closedevice.http.listener.HttpListener;
import com.closedevice.http.request.AbstractRequest;
import com.closedevice.http.request.CacheMode;
import com.closedevice.http.response.Response;
import com.closedevice.http.response.WrapperResponse;

/**
 * @author liudongdong
 * @date 12.12
 */

public class AKitHttp {
  private static final String TAG = "AKitHttp";
  protected HttpConfig config;

  public AKitHttp(HttpConfig config) {
    initConfig(config);
  }

  protected void initConfig(HttpConfig config) {
    this.config = config;
    Log.d(TAG, config.toString());
  }

  public static HttpConfig build(Context context) {
    return new HttpConfig(context);
  }

  public final HttpConfig getConfig() {
    return config;
  }

  public <T> Response<T> execute(AbstractRequest<T> request) {
    final WrapperResponse<T> response = handleReqeust(request);
    HttpException httpException = null;
    HttpListener listener = request.getListener();

    try {
      if (listener != null) {
        listener.callStart(request);
      }
      if (request.getCacheMode() == CacheMode.CACHE_ONLY) {
        tryFuckCache(response);
        return response;
      } else if (request.getCacheMode() == CacheMode.CACHE_FIRST && tryFuckCache(response)) {
        return response;
      } else {
        tryConnectNetwork(request, response);
      }
    } catch (HttpException e) {
      e.printStackTrace();
      httpException = e;
      response.setException(httpException);
    } catch (Exception e) {
      httpException = new ClientException(e);
      response.setException(httpException);
    } finally {
      if (listener != null) {
        if (request.isCancelledOrInterrupted()) {
          // TODO: 12.12 liudongdong:handle request cancel
        } else if (httpException != null) {
          // TODO: 12.12 liudongdong:handle request failed
        } else {
          // TODO: 12.12 liudongdong:handle request success
        }
      }
    }
    return response;
  }

  private <T> void tryConnectNetwork(AbstractRequest<T> request, WrapperResponse<T> response)
      throws InterruptedException, NetException, ServerException, ClientException {
    try {
      if (!request.isCancelledOrInterrupted()) {
        tryDetectNetwork();
      }
      connectWithRetries(request, response);
    } finally {
      if (request.getCacheMode() == CacheMode.NET_FIRST
          && !response.isOk()
          && !request.isCancelledOrInterrupted()) {
        tryFuckCache(response);
      }
    }
  }

  private <T> void connectWithRetries(AbstractRequest<T> request, WrapperResponse response)
      throws ClientException, NetException, ServerException, InterruptedException {
    int times = 0;
    int maxRetryTimes = request.getMaxRetryTimes();
    while (times < maxRetryTimes) {
      try {
        config.httpClient.connect(request, response);
      } catch (ServerException | NetException e) {
        throw e;
      } catch (Exception e) {
        throw new ClientException(e);
      }
    }
  }

  private boolean tryFuckCache(WrapperResponse response) {
    // TODO: 12.12 liudongdong:query request result from disk cache
    return false;
  }

  private void tryDetectNetwork() {
    // TODO: 12.12 liudongdong:how to detect network status
  }

  private <T> WrapperResponse<T> handleReqeust(AbstractRequest<T> request) {
    // TODO: 12.12 config some request property
    return new WrapperResponse<>(request);
  }
}
