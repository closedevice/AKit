package com.closedevice.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

/**
 * @author liudongdong-os
 * @date 2018/3/16
 */

public class ShellUtil {

    private static final String LINE_SEP = System.getProperty("line.separator");

    public static void exec(final String command) {
        execCmd(new String[]{command}, false, false);
    }

    public static Future execCmd(final String[] commands,
                                 final boolean isRoot,
                                 final boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new Future(result, null, null);
        }
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(isRoot ? "su" : "sh");
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) continue;
                os.write(command.getBytes());
                os.writeBytes(LINE_SEP);
                os.flush();
            }
            os.writeBytes("exit" + LINE_SEP);
            os.flush();
            result = process.waitFor();
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream(),
                        "UTF-8"));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream(),
                        "UTF-8"));
                String line;
                if ((line = successResult.readLine()) != null) {
                    successMsg.append(line);
                    while ((line = successResult.readLine()) != null) {
                        successMsg.append(LINE_SEP).append(line);
                    }
                }
                if ((line = errorResult.readLine()) != null) {
                    errorMsg.append(line);
                    while ((line = errorResult.readLine()) != null) {
                        errorMsg.append(LINE_SEP).append(line);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // FIXME: 2018/3/16 liudongdong:修改为正式工具类
            //            CloseUtils.closeIO(os, successResult, errorResult);
            if (process != null) {
                process.destroy();
            }
        }
        return new Future(result, successMsg != null ? successMsg.toString() : null, errorMsg != null ? errorMsg.toString() : null);
    }

    public static class Future {
        int code;
        String msg;
        String errorMsg;

        public Future(int code, String msg, String errorMsg) {
            this.code = code;
            this.msg = msg;
            this.errorMsg = errorMsg;
        }
    }
}
