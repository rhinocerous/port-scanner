package controllers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.google.inject.Inject;
import exceptions.PortScanException;
import exceptions.PortScanExceptionCodes;
import helpers.JsonHelper;
import models.business.Host;
import models.business.Scan;
import org.apache.commons.lang3.StringUtils;
import play.libs.F;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import services.PortScanService;

import java.io.IOException;
import java.util.List;

/**
 * Created by Aaron on 1/1/2015.
 */
public class PortScan extends ApiController
{
    @Inject
    private PortScanService portScanService;

    @Inject
    private JsonHelper jsonHelper;

    @Inject
    private ObjectMapper mapper;

    @BodyParser.Of(BodyParser.Json.class)
    public F.Promise<Result> scan() throws PortScanException
    {
        final Http.RequestBody body = request().body();

        if(null == body || null == body.asJson())
            throw new PortScanException("Request body was empty. Request body must be in JSON format and include host:(string) or hosts:[(strings)...]", PortScanExceptionCodes.badRequest);

        JsonNode json = body.asJson();

        if(json.has("host") && !StringUtils.isEmpty(json.get("host").asText()))
        {
            return portScanService.scan(json.get("host").asText()).map
            (
                new F.Function<Host, Result>()
                {
                    public Result apply(Host host)
                    {
                        return ok(Json.toJson(host));
                    }
                }
            );
        }
        else if(json.has("hosts") && JsonNodeType.ARRAY == json.get("hosts").getNodeType() && json.get("hosts").size() > 0)
        {
            JsonNode list = json.get("hosts");

            List<String> hosts;

            try
            {
                hosts = mapper.readValue(json.get("hosts").toString(), new TypeReference<List<String>>() {});
            }
            catch (IOException e)
            {
                throw new PortScanException("unable to parse hosts from request json", e, PortScanExceptionCodes.badRequest);
            }

            return portScanService.scan(hosts).map
            (
                new F.Function<List<Host>, Result>()
                {
                    public Result apply(List<Host> host)
                    {
                        return ok(Json.toJson(host));
                    }
                }
            );
        }
        else
            throw new PortScanException("Request body must be in JSON format and include host:(string) or hosts:[(strings)...]", PortScanExceptionCodes.validationError);
    }

    public F.Promise<Result> history(String hostName, Integer count, Integer page) throws PortScanException
    {
        return portScanService.getHistoryByHostname(hostName, page, count).map
        (
            new F.Function<List<Scan>, Result>()
            {
                public Result apply(List<Scan> scans)
                {
                    return ok(Json.toJson(scans));
                }
            }
        );
    }
}
