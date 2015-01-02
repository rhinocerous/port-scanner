package models.business;

import exceptions.PortScanException;
import models.BaseModel;

import java.net.InetAddress;
import java.util.List;

/**
 * Created by Aaron on 1/1/2015.
 */
public class Host extends BaseModel
{
    protected Integer id;
    protected InetAddress ip;
    protected String hostname;
    protected List<Scan> scans;

    public List<Scan> getScans() {
        return scans;
    }

    public void setScans(List<Scan> scans) {
        this.scans = scans;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
