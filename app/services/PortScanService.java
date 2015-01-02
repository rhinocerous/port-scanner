package services;

import exceptions.PortScanException;
import models.business.Host;
import models.business.Scan;
import play.libs.F;

import java.util.List;

/**
 * Created by Aaron on 1/1/2015.
 */
public interface PortScanService
{
    public F.Promise<Host> scan(String hostName) throws PortScanException;

    public F.Promise<List<Host>> scan(List<String> hostNames) throws PortScanException;

    public F.Promise<List<Scan>> getHistoryByHostname(String hostname) throws PortScanException;

    public F.Promise<List<Scan>> getHistoryById(Integer hostId) throws PortScanException;
}
