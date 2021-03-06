import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import exceptions.PortScanException;
import exceptions.PortScanExceptionCodes;
import models.output.ErrorResponse;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.db.DB;
import play.libs.F;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import repositories.cli.NmapPortScanRepository;
import repositories.cli.PortScanRepository;
import repositories.storage.DatabasePortScanStorageRepository;
import repositories.storage.PortScanStorageRepository;
import services.NmapPortScanService;
import services.PortScanService;

import java.sql.Connection;
import java.sql.SQLException;

import static play.mvc.Results.internalServerError;

/**
 * Created by Aaron on 1/1/2015.
 */
public class Global extends GlobalSettings
{
    private Injector injector;

    @Override
    public void onStart(Application application)
    {
        Logger.info("Port scanner demo is starting up");

    //  guice injection
        injector = Guice.createInjector(new AbstractModule()
        {
            @Override
            protected void configure()
            {
            //  services
                bind(PortScanService.class).to(NmapPortScanService.class);

            //  repositories
                bind(PortScanStorageRepository.class).to(DatabasePortScanStorageRepository.class);
                bind(PortScanRepository.class).to(NmapPortScanRepository.class);
            }
        });
    }

    @Override
    public void onStop(Application app)
    {
        Logger.info("Port scanner demo is shutting down...");

        PortScanStorageRepository storageRepository = injector.getInstance(PortScanStorageRepository.class);
        storageRepository.shutdown();
    }

    @Override
    public F.Promise<Result> onError(Http.RequestHeader request, Throwable t)
    {
        if(_isJson(request))
        {
            ErrorResponse errorResponse;

            if(t instanceof PortScanException)
            {
                errorResponse = _mapToResponse(t);
            }
            else if(t.getCause() instanceof PortScanException)
            {
                errorResponse = _mapToResponse(t.getCause());
            }
            else
            {
                errorResponse = injector.getInstance(ErrorResponse.class);
                errorResponse.setType(t.getCause().getClass().getSimpleName());
                errorResponse.setCode(PortScanExceptionCodes.unknown);
                errorResponse.setMessage(t.getMessage());
            }

            return F.Promise.<Result>pure
            (
                Results.internalServerError
                (
                    Json.toJson(errorResponse)
                )
            );
        }
        else
        {
            return F.Promise.<Result>pure
            (
                internalServerError(views.html.error.render(t))
            );
        }
    }

//  this allows guice binding in controllers
    @Override
    public <T> T getControllerInstance(Class<T> aClass) throws Exception {
        return injector.getInstance(aClass);
    }

    private ErrorResponse _mapToResponse(Throwable t)
    {
        PortScanException exception = (PortScanException) t;
        ErrorResponse errorResponse = injector.getInstance(ErrorResponse.class);
        errorResponse.setCode(exception.getExceptionCode());
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setType(t.getClass().getSimpleName());
        return errorResponse;
    }

    private boolean _isJson(Http.RequestHeader request)
    {
        return (request.getHeader("Content-Type")!= null && request.getHeader("Content-Type").contains("json")) || (request.getHeader("content-type")!= null && request.getHeader("content-type").contains("json"));
    }
}
