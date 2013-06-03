package codeOrchestra.lcs.license;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Alexander Eliseyev
 */
public interface ExternalValidationService extends Remote {
  
  boolean areYouAlright() throws RemoteException;

}
