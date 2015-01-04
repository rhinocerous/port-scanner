package repositories.storage;

import exceptions.PortScanStorageException;
import models.business.Host;
import models.business.Port;
import models.business.Scan;
import play.libs.F;

import java.util.List;

/**
 * Created by Aaron on 1/1/2015.
 */
public interface PortScanStorageRepository
{
    public void shutdown();

    public F.Promise<Integer> getHostIdByName(String hostName) throws PortScanStorageException;

    public F.Promise<Host> getHostById(Integer hostId) throws PortScanStorageException;

    public F.Promise<List<Scan>> getHistoryById(Integer hostId, Integer page, Integer count) throws PortScanStorageException;

    public F.Promise<List<Scan>> getHistoryByHostname(String hostname, Integer page, Integer count) throws PortScanStorageException;

    public F.Promise<Host> saveHost(Host host) throws PortScanStorageException;

    public F.Promise<List<Host>> saveHosts(List<Host> host) throws PortScanStorageException;

    public F.Promise<Scan> saveScan(Scan scan) throws PortScanStorageException;

    public F.Promise<List<Scan>> saveScans(List<Scan> scan) throws PortScanStorageException;

    public F.Promise<Port> savePort(Port port) throws PortScanStorageException;

    public F.Promise<List<Port>> savePorts(Integer scanId, List<Port> ports) throws PortScanStorageException;
}
