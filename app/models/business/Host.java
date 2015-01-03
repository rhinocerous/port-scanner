package models.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import exceptions.PortScanException;
import models.BaseModel;
import org.joda.time.DateTime;

import java.net.InetAddress;
import java.util.ArrayList;
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
    protected DateTime created;
    protected DateTime lastScan;

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

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    @JsonProperty("lastScan")
    public String getLastScanString()
    {
        return (null == lastScan) ? null : lastScan.toString();
    }

    @JsonIgnore
    public DateTime getLastScan() {
        return lastScan;
    }

    public void setLastScan(DateTime lastScan) {
        this.lastScan = lastScan;
    }

    public void addScan(Scan scan)
    {
        if(null == scans)
            scans = new ArrayList<>();

        scans.add(scan);
    }
}
