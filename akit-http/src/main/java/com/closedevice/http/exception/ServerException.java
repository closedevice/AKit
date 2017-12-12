package com.closedevice.http.exception;

import com.closedevice.http.HttpStatus;

/**
 * @author liudongdong
 * @date 12.12
 */

public class ServerException extends HttpException {

  public ServerException(HttpStatus httpStatus) {

  }

  public ServerException(String message) {

  }
}
