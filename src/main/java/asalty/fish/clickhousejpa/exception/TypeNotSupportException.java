package asalty.fish.clickhousejpa.exception;

/**
 * @author 13090
 * @version 1.0
 * @description: 类型不支持时的异常
 * @date 2022/3/1 13:07
 */

public class TypeNotSupportException extends Exception {

    String message;

    public TypeNotSupportException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
