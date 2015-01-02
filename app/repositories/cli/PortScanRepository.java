package repositories.cli;

import exceptions.PortScanCliException;
import models.business.Host;
import models.business.Scan;
import play.libs.F;

import java.util.List;

/**
 * Created by Aaron on 1/1/2015.
 */
public interface PortScanRepository
{
    public F.Promise<Host> scan(String host) throws PortScanCliException;

    public F.Promise<List<Host>> scan(List<String> hosts) throws PortScanCliException;
}
