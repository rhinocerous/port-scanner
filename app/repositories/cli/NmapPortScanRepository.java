package repositories.cli;

import exceptions.PortScanCliException;
import exceptions.PortScanExceptionCodes;
import helpers.UtilityHelper;
import models.business.Host;
import models.business.Port;
import models.business.Scan;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.libs.F;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.List;

/**
 * Created by Aaron on 1/1/2015.
 *
 * C:\Users\Aaron\Documents\Projects\demo>nmap -p1-1000 boldride.com

 Starting Nmap 6.47 ( http://nmap.org ) at 2015-01-01 17:57 Pacific Standard Time

 Nmap scan report for boldride.com (54.243.97.45)
 Host is up (0.088s latency).
 rDNS record for 54.243.97.45: ec2-54-243-97-45.compute-1.amazonaws.com
 Not shown: 995 filtered ports
 PORT    STATE  SERVICE
 25/tcp  closed smtp
 80/tcp  open   http
 443/tcp closed https
 465/tcp closed smtps
 587/tcp closed submission

 Nmap done: 1 IP address (1 host up) scanned in 8.59 seconds

 *
 */
public class NmapPortScanRepository implements PortScanRepository
{
    private static final String CMD = "nmap -p1-1000 %s";

    @Override
    public F.Promise<Host> scan(final String hostString) throws PortScanCliException
    {
        try
        {
            return F.Promise.promise
            (
                new F.Function0<Host>()
                {
                    public Host apply() throws IOException, InterruptedException
                    {
                        Host host = new Host();
                        host.setHostname(hostString);

                        Scan scan = new Scan();

                        String cmd = String.format(CMD, hostString);

                        Logger.info("execute command " + cmd);

                        Process p= Runtime.getRuntime().exec(cmd);
                        p.waitFor();
                        BufferedReader reader=new BufferedReader(
                                new InputStreamReader(p.getInputStream())
                        );

                        String line;

                        boolean portLines = false;

                        while((line = reader.readLine()) != null)
                        {
                            if(line.contains("Nmap scan report for"))
                            {
                                String validHost = UtilityHelper.extractDomains(line).get(0);

                                Logger.info(String.format(">>>>\tfound valid hostname [%s]", validHost));

                                scan.setValidHost(validHost);

                                String ip = UtilityHelper.extractIp(line);

                                host.setIp(InetAddress.getByName(ip));

                                Logger.info(String.format(">>>>\tfound valid IP [%s]", ip));
                            }
                            else if(line.contains("Not shown:"))
                            {
                                Integer inactivePorts = UtilityHelper.extractIntegers(line).get(0);

                                scan.setInactivePortCount(inactivePorts);

                                Logger.info(String.format(">>>>\tfound [%s] inactive ports", inactivePorts));
                            }
                            else if (line.equals("PORT    STATE  SERVICE"))
                            {
                                portLines = true;
                            }
                            else if(StringUtils.isEmpty(line))
                            {
                                portLines = false;
                            }
                            else if(portLines)
                            {
                                String[] segments = line.trim().replaceAll(" +", " ").split(" ");

                                if(segments.length >= 3)
                                {
                                    Logger.info(String.format(">>>>\tfound port %s -> %s -> %s", segments[0], segments[1], segments[2]));

                                    String[] protocol = segments[0].split("/");

                                    Port port = new Port();
                                    port.setPort(Integer.valueOf(protocol[0]));
                                    port.setProtocol(protocol[1]);
                                    port.setService(segments[2]);
                                    port.setState(segments[1]);

                                    scan.addPort(port);
                                }
                            }
//                            Logger.info(line);
                        }

                        host.addScan(scan);

                        return host;
                    }
                }
            );
        }
        catch (Exception e)
        {
            throw  new PortScanCliException(e,PortScanExceptionCodes.scanFailure);
        }
    }

    @Override
    public F.Promise<List<Host>> scan(List<String> hosts) throws PortScanCliException {
        return null;
    }
}
