package asalty.fish.clickhousejpa.exception;

/**
 * @author 13090
 * @version 1.0
 * @description: 建表异常
 * @date 2022/3/1 16:05
 */

public class TableCreateException extends Exception {
    public TableCreateException(String message) {
        super(message);
    }
}
