package exceptions;

/**
 * Created by Aaron on 1/1/2015.
 */
public class PortScanException extends Exception
{
    protected PortScanExceptionCodes exceptionCode;

    public PortScanException(PortScanExceptionCodes exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public PortScanException(String message, PortScanExceptionCodes exceptionCode) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public PortScanException(String message, Throwable cause, PortScanExceptionCodes exceptionCode) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
    }

    public PortScanException(Throwable cause, PortScanExceptionCodes exceptionCode) {
        super(cause);
        this.exceptionCode = exceptionCode;
    }

    public PortScanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, PortScanExceptionCodes exceptionCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.exceptionCode = exceptionCode;
    }
}
