package codeOrchestra.lcs.license;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ExternalValidationServiceProvider implements ExternalValidationService {

    private ExternalValidationService service;

    public ExternalValidationServiceProvider() {
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry("localhost", 9037);
        } catch (RemoteException e) {
            throw new RuntimeException("Can't obtain the RMI registry", e);
        }

        try {
            service = (ExternalValidationService) registry.lookup("ExternalValidationService");
        } catch (Throwable t) {
            // ignore
        }
    }

    @Override
    public boolean areYouAlright() throws RemoteException {
        if (service == null) {
            return false;
        }
        return service.areYouAlright();
    }

}
