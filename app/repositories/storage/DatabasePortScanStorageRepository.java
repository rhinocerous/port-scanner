package repositories.storage;

import exceptions.PortScanExceptionCodes;
import exceptions.PortScanStorageException;
import models.business.Host;
import models.business.Port;
import models.business.Scan;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import play.Logger;
import play.db.DB;
import play.libs.F;

import java.net.InetAddress;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron on 1/1/2015.
 */
public class DatabasePortScanStorageRepository implements PortScanStorageRepository
{
    private Connection dbConnection;

    public DatabasePortScanStorageRepository() {
        dbConnection = DB.getConnection();
    }

    @Override
    public void shutdown() {
        try {
            dbConnection.close();
        } catch (SQLException e) {
            Logger.error("error while shutting down mysql connection", e);
        }
    }

    @Override
    public F.Promise<Integer> getHostIdByName(final String hostName) throws PortScanStorageException
    {
        return F.Promise.promise
        (
            new F.Function0<Integer>()
            {
                public Integer apply() throws PortScanStorageException
                {
                    try
                    {
                        PreparedStatement query = dbConnection.prepareStatement("SELECT id FROM hosts WHERE hostname=?");
                        query.setString(1, hostName);

                        ResultSet result = query.executeQuery();

                        while (result.next())
                            return result.getInt("id");

                        return 0;
                    }
                    catch (SQLException e)
                    {
                        throw new PortScanStorageException(String.format("id lookup failed for host %s", hostName), e, PortScanExceptionCodes.databaseException);
                    }
                }
            }
        );
    }

    @Override
    public F.Promise<Host> getHostById(final Integer hostId) throws PortScanStorageException
    {
        try
        {
            PreparedStatement query = dbConnection.prepareStatement("SELECT id, hostname, ip, created, lastScan FROM hosts WHERE id=?");
            query.setInt(1, hostId);

            ResultSet result = query.executeQuery();

            final Host host = new Host();

        //  there is only one record
            while (result.next())
            {
                host.setId(result.getInt("id"));
                host.setCreated(new DateTime(result.getTimestamp("created")));
                host.setLastScan(new DateTime(result.getTimestamp("lastScan")));
                host.setHostname(result.getString("hostname"));
                host.setIp(InetAddress.getByName(result.getString("ip")));
            }

        //  decorate host with scan history
            return getHistoryById(hostId, 1, 2).map
            (
                new F.Function<List<Scan>, Host>()
                {
                    @Override
                    public Host apply(List<Scan> scans) throws Throwable
                    {
                        host.setScans(scans);

                        return host;
                    }
                }
            );
        }
        catch (Exception e)
        {
            throw new PortScanStorageException(String.format("host by id lookup failed for id #%s", hostId), e, PortScanExceptionCodes.databaseException);
        }
    }

    @Override
    public F.Promise<List<Scan>> getHistoryById(final Integer hostId, final Integer page, final Integer count) throws PortScanStorageException
    {

        try
        {
            PreparedStatement query = dbConnection.prepareStatement("SELECT id, hostId, created, inactivePorts, validHost FROM scans WHERE hostId=? ORDER BY created DESC LIMIT ? OFFSET ?");
            query.setInt(1, hostId);
            query.setInt(2, count);
            query.setInt(3, (page - 1) * count);

            ResultSet result = query.executeQuery();

            final List<Scan> scanList = new ArrayList<>();
            List<Integer> scanIdList = new ArrayList<>();

            while (result.next())
            {
                Scan scan = new Scan();
                scan.setId(result.getInt("id"));
                scan.setCreated(new DateTime(result.getTimestamp("created")));
                scan.setHostId(result.getInt("hostId"));
                scan.setValidHost(result.getString("validHost"));
                scan.setInactivePortCount(result.getInt("inactivePorts"));
                scanList.add(scan);

                scanIdList.add(scan.getId());
            }

        //  decorate scans with ports
            return getPortsByScanIds(scanIdList).map
            (
                new F.Function<List<Port>, List<Scan>>()
                {
                    @Override
                    public List<Scan> apply(List<Port> ports) throws Throwable
                    {
                        for(Scan scan : scanList)
                        {
                            for(Port port : ports)
                            {
                                if(port.getScanId() == scan.getId())
                                {
                                    scan.addPort(port);
                                }
                            }
                        }

                        return scanList;
                    }
                }
            );
        }
        catch (Exception e)
        {
            throw new PortScanStorageException(String.format("scans by host id lookup failed for host #%s", hostId), e, PortScanExceptionCodes.databaseException);
        }
    }

    @Override
    public F.Promise<List<Port>> getPortsByScanIds(final List<Integer> scanIds) throws PortScanStorageException
    {
        return F.Promise.promise
        (
            new F.Function0<List<Port>>()
            {
                public List<Port> apply() throws PortScanStorageException
                {
                    try
                    {
                    //  this is a dirty hack used to generate an IN clause because it turns out jdbc doesn't support passing in array type
                    //  as a param so you can't really do IN queries effectively: http://www.javaranch.com/journal/200510/Journal200510.jsp#a2
                    //  this is lame. if I had more time I would like to use a different db connection library that lets us select more efficiently.
                        List<String> binders = new ArrayList<>(scanIds.size());

                        for (Integer scanId : scanIds)
                            binders.add("?");

                        PreparedStatement query = dbConnection.prepareStatement(String.format("SELECT id, port, scanId, protocol, state, service FROM ports WHERE scanId IN (%s)", StringUtils.join(binders, ",")));

                        int position = 1;

                        for (Integer scanId : scanIds)
                        {
                            query.setInt(position, scanId);

                            position++;
                        }

                        ResultSet result = query.executeQuery();

                        List<Port> portList = new ArrayList<>();

                        while (result.next())
                        {
                            Port port = new Port();
                            port.setId(result.getInt("id"));
                            port.setScanId(result.getInt("scanId"));
                            port.setProtocol(result.getString("protocol"));
                            port.setState(result.getString("state"));
                            port.setService(result.getString("service"));
                            port.setPort(result.getInt("port"));
                            portList.add(port);
                        }

                        return portList;
                    }
                    catch (Exception e)
                    {
                        throw new PortScanStorageException("ports by scan ids query failed", e, PortScanExceptionCodes.databaseException);
                    }
                }
            }
        );
    }

    @Override
    public F.Promise<List<Scan>> getHistoryByHostname(String hostname, Integer page, Integer count) throws PortScanStorageException {
        return null;
    }

    @Override
    public F.Promise<Host> saveHost(Host host) throws PortScanStorageException
    {
        F.Promise<Host> promise;

        if(null == host.getId())
        {
            promise = _insertHost(host);
        }
        else
        {
            promise = _updateHost(host);
        }

        return promise.flatMap
        (
            new F.Function<Host, F.Promise<Host>>()
            {
                public F.Promise<Host> apply(final Host host) throws PortScanStorageException {
                    if (host.getScans().size() > 0) {
                        final Scan scan = host.getScans().get(0);

                        scan.setHostId(host.getId());

                    //  do this one as blocking call since we need to make sure scanId is returned
                        saveScan(scan).get(30000);
                    }

                    return getHostById(host.getId());
                }
            }
        );
    }

    @Override
    public F.Promise<Scan> saveScan(final Scan scan) throws PortScanStorageException
    {
        return F.Promise.promise
        (
            new F.Function0<Scan>()
            {
                public Scan apply() throws PortScanStorageException
                {
                    Timestamp now = new Timestamp(DateTime.now(DateTimeZone.UTC).getMillis()) ;

                    try
                    {
                        PreparedStatement query = dbConnection.prepareStatement("INSERT INTO scans (hostId, validHost, inactivePorts, created) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                        query.setInt(1, scan.getHostId());
                        query.setString(2, scan.getValidHost());
                        query.setInt(3, scan.getInactivePortCount());
                        query.setTimestamp(4, now);

                        query.executeUpdate();

                        ResultSet rs = query.getGeneratedKeys();

                        if(rs.next())
                        {
                            scan.setId(rs.getInt(1));
                        }
                    }
                    catch (SQLException e)
                    {
                        throw new PortScanStorageException("scan insert failed", e, PortScanExceptionCodes.databaseException);
                    }

                    if(scan.getPorts().size() > 0)
                    {
                        savePorts(scan.getId(), scan.getPorts()).map
                        (
                            new F.Function<List<Port>, Scan>()
                            {
                                @Override
                                public Scan apply(List<Port> ports) throws Throwable
                                {
                                    scan.setPorts(ports);

                                    return scan;
                                }
                            }
                        );
                    }

                    return scan;
                }
            }
        );
    }

    @Override
    public F.Promise<List<Host>> saveHosts(List<Host> host) throws PortScanStorageException {
        return null;
    }

    @Override
    public F.Promise<List<Scan>> saveScans(List<Scan> scan) throws PortScanStorageException {
        return null;
    }

    @Override
    public F.Promise<Port> savePort(final Port port) throws PortScanStorageException
    {
        return F.Promise.promise
        (
            new F.Function0<Port>()
            {
                public Port apply() throws PortScanStorageException
                {
                    try
                    {
                        PreparedStatement query = dbConnection.prepareStatement("INSERT INTO ports (scanId, port, protocol, state, service) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                        query.setInt(1, port.getScanId());
                        query.setInt(2, port.getPort());
                        query.setString(3, port.getProtocol());
                        query.setString(4, port.getState());
                        query.setString(5, port.getService());

                        query.executeUpdate();

                        ResultSet rs = query.getGeneratedKeys();

                        if(rs.next())
                        {
                            port.setId(rs.getInt(1));
                        }
                    }
                    catch (SQLException e)
                    {
                        throw new PortScanStorageException("port insert failed", e, PortScanExceptionCodes.databaseException);
                    }

                    return port;
                }
            }
        );
    }

    @Override
    public F.Promise<List<Port>> savePorts(Integer scanId, List<Port> ports) throws PortScanStorageException
    {
        List<F.Promise<Port>> promiseList = new ArrayList<>();

        for (Port port : ports)
        {
            port.setScanId(scanId);

            promiseList.add(savePort(port));
        }

        return F.Promise.sequence(promiseList);
    }

    private F.Promise<Host> _updateHost(final Host host) throws PortScanStorageException
    {
        return F.Promise.promise
        (
            new F.Function0<Host>()
            {
                public Host apply() throws PortScanStorageException
                {
                    Timestamp now = new Timestamp(DateTime.now(DateTimeZone.UTC).getMillis()) ;

                    try
                    {
                        PreparedStatement query = dbConnection.prepareStatement("UPDATE hosts SET ip=?, lastScan=? WHERE id=?");

                        query.setString(1, host.getIp().toString().replace("/", ""));
                        query.setTimestamp(2, now);
                        query.setInt(3, host.getId());

                        int rows = query.executeUpdate();

                        host.setLastScan(new DateTime(now));
                    }
                    catch (SQLException e)
                    {
                        throw new PortScanStorageException("host update failed", e, PortScanExceptionCodes.databaseException);
                    }

                    return host;
                }
            }
        );
    }

    private F.Promise<Host> _insertHost(final Host host) throws PortScanStorageException
    {
        return F.Promise.promise
        (
            new F.Function0<Host>()
            {
                public Host apply() throws PortScanStorageException
                {
                    Timestamp now = new Timestamp(DateTime.now(DateTimeZone.UTC).getMillis()) ;

                    try
                    {
                        PreparedStatement query = dbConnection.prepareStatement("INSERT INTO hosts (hostname, ip, created, lastScan) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                        query.setString(1, host.getHostname());
                        query.setString(2, host.getIp().toString().replace("/", ""));
                        query.setTimestamp(3, now);
                        query.setTimestamp(4, now);

                        query.executeUpdate();

                        ResultSet rs = query.getGeneratedKeys();

                        if(rs.next())
                        {
                            host.setId(rs.getInt(1));
                        }
                    }
                    catch (SQLException e)
                    {
                        throw new PortScanStorageException("host insert failed", e, PortScanExceptionCodes.databaseException);
                    }

                    return host;
                }
            }
        );
    }
}
