package com.closedevice.http;

import android.util.Log;
import com.closedevice.http.convert.DataConvertor;
import com.closedevice.http.exception.ClientException;
import com.closedevice.http.exception.NetException;
import com.closedevice.http.exception.ServerException;
import com.closedevice.http.request.AbstractRequest;
import com.closedevice.http.request.HttpMethod;
import com.closedevice.http.response.WrapperResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liudongdong
 * @date 12.12
 */

public class DefaultHttpClient implements HttpClient {
  private static final String TAG = "DefaultHttpClient";

  @Override public void config(HttpConfig config) {
    //nothing to do
  }

  @Override public <T> void connect(AbstractRequest<T> request, WrapperResponse response)
      throws Exception {
    InputStream is = null;
    int maxRedirectTimes = request.getMaxRetryTimes();
    try {
      URL url = new URL(request.realUrl());
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setDoInput(true);
      connection.setUseCaches(false);
      connection.setReadTimeout(request.getReadTimeout());
      connection.setConnectTimeout(request.getConnectTimeout());

      try {
        //1.write data to server
        writeDataIfNeccessary(connection, request);
        //2.obtain input stream from server
        is = connection.getInputStream();
      } catch (SocketTimeoutException e) {
        throw e;
      } catch (InterruptedIOException e) {
        request.cancel();
      } catch (IOException e) {
        is = connection.getErrorStream();
      }

      if (is == null) {
        throw new NetException("Network is unreachable.");
      }

      //4.handle header in response
      ArrayList<NameValuePair> headers = new ArrayList<>();
      for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
        if (header != null) {
          List<String> values = header.getValue();
          for (String value : values) {
            NameValuePair nameValuePair = new NameValuePair(header.getKey(), value);
            headers.add(nameValuePair);
          }
        }
      }

      //5.hand response body property
      response.setContentLength(connection.getContentLength());
      response.setContentEncoding(connection.getContentEncoding());
      response.setContentType(connection.getContentType());

      //6.handle response body and code
      int statusCode = connection.getResponseCode();
      HttpStatus httpStatus = new HttpStatus(statusCode, connection.getResponseMessage());
      response.setHttpStatus(httpStatus);

      if (statusCode <= 299 || statusCode == 600) {
        String charSet = getCharsetByContentType(response.getContentType(), request.getCharSet());
        response.setCharset(charSet);
        int len = response.getContentLength();
        DataConvertor convertor = request.getDataConvertor();
        convertor.readIn(is, len, charSet);
        is = null;
        response.setReadedLength(convertor.getReadedLength());
      } else if (statusCode <= 399) {
        //300~399:file has moved,need handle redirect
        if (response.getRedirectTimes() < maxRedirectTimes) {
          //find new location from the response header
          String location = connection.getHeaderField("Location");
          if (location != null && location.length() > 0) {
            if (!location.toLowerCase().startsWith("http")) {
              URI uri = new URI(request.realUrl());
              URI redirect = new URI(uri.getScheme(), uri.getHost(), location, null);
              location = redirect.toString();
            }
            response.setRedirectTimes(response.getRedirectTimes() + 1);
            response.setUri(location);
            Log.d(TAG, "redirect to : " + location);
            if (request.getListener() != null) {
              request.getListener()
                  .callRedirect(request, maxRedirectTimes, response.getRedirectTimes());
            }
            connect(request, response);
            return;
          }
          throw new ServerException(httpStatus);
        } else {
          throw new ServerException("redirect too much");
        }
      } else if (statusCode <= 499) {
        throw new ClientException(httpStatus);
      } else if (statusCode <= 599) {
        throw new ServerException(httpStatus);
      }
    } finally {
      if (is != null) {
        is.close();
      }
    }
  }

  private String getCharsetByContentType(String contentType, String defaultCharset) {
    if (contentType != null) {
      String[] values = contentType.split(";");
      for (String value : values) {
        value = value.trim();
        if (value.toLowerCase().startsWith("charset=")) {
          return value.substring("charset=".length());
        }
      }
    }
    return defaultCharset == null ? com.closedevice.http.Charset.UTF_8 : defaultCharset;
  }

  private <T> void writeDataIfNeccessary(URLConnection connection, AbstractRequest<T> request)
      throws IOException {
    HttpMethod method = request.getMethod();
    if (method == HttpMethod.POST) {
      HttpBody httpBody = request.getHttpBody();
      if (httpBody != null) {
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", httpBody.getContentType());
        OutputStream os = connection.getOutputStream();
        httpBody.write(os);
        os.close();
      }
    }
  }
}
