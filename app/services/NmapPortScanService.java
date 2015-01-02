package services;

import com.google.inject.Inject;
import exceptions.PortScanException;
import exceptions.PortScanExceptionCodes;
import helpers.UtilityHelper;
import models.business.Host;
import models.business.Scan;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
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
        List<String> domainCheck = UtilityHelper.extractDomains(hostName);

        if(domainCheck.size() > 0)
        {
            Logger.info(String.format("found domain name [%s]", domainCheck.get(0)));

            return scanRepository.scan(domainCheck.get(0));
        }

        String ipCheck = UtilityHelper.extractIp(hostName);

        if(!StringUtils.isEmpty(ipCheck))
        {
            Logger.info(String.format("found IP address [%s]", ipCheck));

            return scanRepository.scan(ipCheck);
        }

        throw new PortScanException(String.format("invalid host [%s] - try scanning by domain name or IP address", hostName), PortScanExceptionCodes.badRequest);
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
