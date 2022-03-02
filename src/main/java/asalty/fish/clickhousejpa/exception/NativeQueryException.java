package asalty.fish.clickhousejpa.exception;

/**
 * @author 13090
 * @version 1.0
 * @description: 手写 SQL 时的异常
 * @date 2022/3/2 15:10
 */

public class NativeQueryException extends Exception {

    public NativeQueryException(String message) {
        super(message);
    }

}
