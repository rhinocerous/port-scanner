package services;

import com.google.inject.Inject;
import exceptions.PortScanException;
import models.business.Host;
import models.business.Scan;
import play.libs.F;
import repositories.cli.PortScanRepository;
import repositories.storage.PortScanStorageRepository;

import java.util.List;

/**
 * Created by Aaron on 1/1/2015.
 */
public class NmapPortScanService implements PortScanService
{
    @Inject
    protected PortScanRepository scanRepository;

    @Inject
    protected PortScanStorageRepository storageRepository;

    @Override
    public F.Promise<Host> scan(String hostName) throws PortScanException
    {
        return scanRepository.scan(hostName);
    }

    @Override
    public F.Promise<List<Host>> scan(List<String> hostNames) throws PortScanException {
        return null;
    }

    @Override
    public F.Promise<List<Scan>> getHistoryByHostname(String hostname) throws PortScanException {
        return null;
    }

    @Override
    public F.Promise<List<Scan>> getHistoryById(Integer hostId) throws PortScanException {
        return null;
    }
}
