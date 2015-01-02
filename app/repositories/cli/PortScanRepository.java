package repositories.cli;

import exceptions.PortScanCliException;
import models.business.Scan;
import play.libs.F;

import java.util.List;

/**
 * Created by Aaron on 1/1/2015.
 */
public interface PortScanRepository
{
    public F.Promise<Scan> scan(String host) throws PortScanCliException;

    public F.Promise<List<Scan>> scan(List<String> hosts) throws PortScanCliException;
}
