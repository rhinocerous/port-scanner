package exceptions;

/**
 * Created by Aaron on 1/1/2015.
 */
public class PortScanStorageException extends PortScanException
{
    public PortScanStorageException(PortScanExceptionCodes exceptionCode) {
        super(exceptionCode);
    }

    public PortScanStorageException(String message, PortScanExceptionCodes exceptionCode) {
        super(message, exceptionCode);
    }

    public PortScanStorageException(String message, Throwable cause, PortScanExceptionCodes exceptionCode) {
        super(message, cause, exceptionCode);
    }

    public PortScanStorageException(Throwable cause, PortScanExceptionCodes exceptionCode) {
        super(cause, exceptionCode);
    }

    public PortScanStorageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, PortScanExceptionCodes exceptionCode) {
        super(message, cause, enableSuppression, writableStackTrace, exceptionCode);
    }
}
