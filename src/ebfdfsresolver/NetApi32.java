package ebfdfsresolver;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;

/**
 * @author Yovoslav Ivanov
 */
public interface NetApi32 extends StdCallLibrary {

    NetApi32 INSTANCE = (NetApi32) Native.loadLibrary("Netapi32", NetApi32.class);

    public static class DFS_INFO_3 extends Structure {

        public WString EntryPath;
        public WString Comment;
        public int State;
        public int NumberOfStorages;
        public DFS_STORAGE_INFO.ByReference[] Storage;

        public DFS_INFO_3(Pointer m) {
            DFS_STORAGE_INFO.ByReference ir = new DFS_STORAGE_INFO.ByReference();
            Storage = (DFS_STORAGE_INFO.ByReference[]) ir.toArray(1);
            useMemory(m);
            read();
        }
    }

    public static class DFS_STORAGE_INFO extends Structure {

        public static class ByReference extends DFS_STORAGE_INFO implements Structure.ByReference {
        }
        public int State;
        public WString ServerName;
        public WString ShareName;
    }

    int NetDfsGetInfo(WString DfsEntryPath, WString ServerName, WString ShareName, int level, PointerByReference Buffer);
    int NetDfsGetClientInfo(WString DfsEntryPath, WString ServerName, WString ShareName, int level, PointerByReference Buffer);
    int NetDfsEnum(WString DfsName, int Level, int PrefMaxLen, PointerByReference Buffer, IntByReference EntriesRead, IntByReference ResumeHandle);
    int NetShareGetInfo(WString ServerName, WString ShareName, int Level, PointerByReference Bufer);
}