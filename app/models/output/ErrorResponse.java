package models.output;

import exceptions.PortScanException;
import exceptions.PortScanExceptionCodes;
import models.BaseModel;

/**
 * Created by Aaron on 1/1/2015.
 */
public class ErrorResponse extends BaseModel
{
    protected boolean success = false;
    protected String message;
    protected PortScanExceptionCodes code;
    protected String type;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PortScanExceptionCodes getCode() {
        return code;
    }

    public void setCode(PortScanExceptionCodes code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
