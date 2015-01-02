package repositories.storage;

import exceptions.PortScanStorageException;
import models.business.Host;
import models.business.Scan;
import play.libs.F;

import java.util.List;

/**
 * Created by Aaron on 1/1/2015.
 */
public interface PortScanStorageRepository
{
    public F.Promise<Host> getHostById(Integer hostId) throws PortScanStorageException;

    public F.Promise<List<Scan>> getHistoryById(Integer hostId) throws PortScanStorageException;

    public F.Promise<List<Scan>> getHistoryByHostname(String hostname) throws PortScanStorageException;

    public F.Promise<Host> saveHost(Host host) throws PortScanStorageException;

    public F.Promise<List<Host>> saveHost(List<Host> host) throws PortScanStorageException;

    public F.Promise<Scan> saveScan(Scan scan) throws PortScanStorageException;

    public F.Promise<List<Scan>> saveScan(List<Scan> scan) throws PortScanStorageException;
}
