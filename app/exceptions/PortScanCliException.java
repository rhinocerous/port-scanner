package exceptions;

/**
 * Created by Aaron on 1/1/2015.
 */
public class PortScanCliException extends PortScanException
{
    public PortScanCliException(PortScanExceptionCodes exceptionCode) {
        super(exceptionCode);
    }

    public PortScanCliException(String message, PortScanExceptionCodes exceptionCode) {
        super(message, exceptionCode);
    }

    public PortScanCliException(String message, Throwable cause, PortScanExceptionCodes exceptionCode) {
        super(message, cause, exceptionCode);
    }

    public PortScanCliException(Throwable cause, PortScanExceptionCodes exceptionCode) {
        super(cause, exceptionCode);
    }

    public PortScanCliException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, PortScanExceptionCodes exceptionCode) {
        super(message, cause, enableSuppression, writableStackTrace, exceptionCode);
    }
}
