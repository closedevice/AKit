package com.closedevice.http.exception;

import com.closedevice.http.HttpStatus;

/**
 * @author liudongdong
 * @date 12.12
 */

public class ClientException extends HttpException {

  public ClientException(Exception e) {
    super();
  }

  public ClientException(HttpStatus httpStatus) {

  }
}
