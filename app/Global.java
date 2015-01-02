import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import play.Application;
import play.GlobalSettings;
import repositories.cli.NmapPortScanRepository;
import repositories.cli.PortScanRepository;
import repositories.storage.DatabasePortScanStorageRepository;
import repositories.storage.PortScanStorageRepository;
import services.NmapPortScanService;
import services.PortScanService;

/**
 * Created by Aaron on 1/1/2015.
 */
public class Global extends GlobalSettings
{
    private Injector injector;

    @Override
    public void onStart(Application application)
    {
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
    public <T> T getControllerInstance(Class<T> aClass) throws Exception {
        return injector.getInstance(aClass);
    }
}
