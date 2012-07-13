package ebfdfsresolver;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.sun.jna.WString;
import com.sun.jna.ptr.PointerByReference;

/**
 *
 * @author Yovoslav Ivanov
 */
public class EBFDfsResolver {
    
    public static String getSmbUrl(String dfsUNCPath) throws EBFDfsResolverException {
        String uncPath = null;
        
        try {
            uncPath = resolveDfsPathAsUNC(dfsUNCPath);
        } catch(EBFDfsResolverException dfsResolverException) {
            throw dfsResolverException;
        }
        
        String smbPath = uncPath.replaceFirst("\\\\\\\\", "smb://");
        smbPath = smbPath.replaceAll("\\\\", "/");
        
        return smbPath;
    }
    
    private static String resolveDfsPathAsUNC(String dfsPath) throws EBFDfsResolverException {
        PointerByReference buffer = new PointerByReference();
        int result = NetApi32.INSTANCE.NetDfsGetInfo(new WString(dfsPath), null, null, 3, buffer);
        
        String resolvedDfsPath = null;
        
        if (result == 0) {
            NetApi32.DFS_INFO_3 dfsInfo = new NetApi32.DFS_INFO_3(buffer.getValue());
            resolvedDfsPath = "\\\\" + dfsInfo.Storage[0].ServerName.toString() + "\\" + dfsInfo.Storage[0].ShareName.toString();
        } else {
            throw new EBFDfsResolverException("A Microsoft Windows system error occured: " + result);
        }
        
        return resolvedDfsPath;
    }
}
