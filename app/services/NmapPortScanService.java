package services;

import com.google.inject.Inject;
import exceptions.PortScanException;
import exceptions.PortScanExceptionCodes;
import exceptions.PortScanStorageException;
import helpers.UtilityHelper;
import models.business.Host;
import models.business.Scan;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.libs.F;
import repositories.cli.PortScanRepository;
import repositories.storage.PortScanStorageRepository;

import java.util.ArrayList;
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
    public F.Promise<Host> scan(final String hostName) throws PortScanException
    {
        List<String> domainCheck = UtilityHelper.extractDomains(hostName);

        String validatedHost = null;

        if(domainCheck.size() > 0)
        {
            Logger.info(String.format("found domain name [%s]", domainCheck.get(0)));

            validatedHost = domainCheck.get(0);
        }

        String ipCheck = UtilityHelper.extractIp(hostName);

        if(!StringUtils.isEmpty(ipCheck))
        {
            Logger.info(String.format("found IP address [%s]", ipCheck));

            validatedHost = ipCheck;
        }

        if(!StringUtils.isEmpty(validatedHost))
        {
            return scanRepository.scan(validatedHost).flatMap
            (
                new F.Function<Host, F.Promise<Host>>()
                {
                    public F.Promise<Host> apply(final Host host) throws PortScanStorageException
                    {
                        return storageRepository.getHostIdByName(hostName).flatMap
                        (
                            new F.Function<Integer, F.Promise<Host>>()
                            {
                                @Override
                                public F.Promise<Host> apply(Integer hostId) throws Throwable
                                {
                                    if(hostId > 0)
                                    {
                                        Logger.info(String.format("found host [%s] in database as id #%s", hostName, hostId));

                                        host.setId(hostId);
                                    }
                                    else
                                    {
                                        Logger.info(String.format("host [%s] not found in database", hostName));
                                    }

                                    return storageRepository.saveHost(host);
                                }
                            }
                        );
                    }
                }
            );
        }

        throw new PortScanException(String.format("invalid host [%s] - try scanning by domain name or IP address", hostName), PortScanExceptionCodes.badRequest);
    }

    @Override
    public F.Promise<List<Host>> scan(List<String> hostNames) throws PortScanException
    {
        List<F.Promise<Host>> promiseList = new ArrayList<>(hostNames.size());

        for(String host: hostNames)
        {
            promiseList.add(scan(host));
        }

        return F.Promise.sequence(promiseList);
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
