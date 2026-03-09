package exceptions;


public class RepositoryException extends BaseException {

    public static final int HTTP_CODE = 500;

    public static final int CODE = 5000;

    public RepositoryException(String message, Throwable cause) {
        super(message);
    }

    @Override
    public int getHttpCode() {
        return HTTP_CODE;
    }

    @Override
    public int getCode() {
        return CODE;
    }
}


