package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

import static gitlet.Utils.*;

public class Staging implements Serializable {

    public static final File STAGING_AREA = join(Repository.GITLET_DIR, "staging");
    public static final File STAGIND_FOLDER = join(Repository.GITLET_DIR, "stages");


    /** value = 0 for adding, value = 1 for removing. */
    private  Map<String, Integer> set;

    public Staging() {
        set = new ConcurrentSkipListMap<>();
        if (!STAGIND_FOLDER.exists()) {
            STAGIND_FOLDER.mkdir();
        }
    }

    public void save() {
        writeObject(STAGING_AREA, (Serializable) set);
    }

    public void load() {
        set = readObject(STAGING_AREA, ConcurrentSkipListMap.class);
    }

    public void add(String fileName) {
        set.put(fileName, 0);
        restrictedDelete(fileName);
        writeContents(join(STAGIND_FOLDER, fileName), readContents(join(Repository.CWD, fileName)));
    }

    public void addRM(String fileName) {
        set.remove(fileName, 0);
        set.put(fileName, 1);
    }

    public void remove(String fileName) {
        set.remove(fileName, 0);
        restrictedDelete(join(STAGIND_FOLDER, fileName));
    }
    public boolean contains(String fileName) {
        if (!set.containsKey(fileName)) {
            return false;
        }
        return set.get(fileName) == 0;
    }

    public boolean conatinsRM(String fileName) {
        if (!set.containsKey(fileName)) {
            return false;
        }
        return set.get(fileName) == 1;
    }

    public void clear() {
        set.clear();
        for (String f: plainFilenamesIn(STAGIND_FOLDER)) {
            restrictedDelete(f);
        }
    }

    public List<String> addSet() {
        List<String> l = new ArrayList<>();
        for (Map.Entry<String, Integer> e: set.entrySet()) {
            if (e.getValue() == 0) {
                l.add(e.getKey());
            }
        }
        return l;
    }

    public List<String> rmSet() {
        List<String> l = new ArrayList<>();
        for (Map.Entry<String, Integer> e: set.entrySet()) {
            if (e.getValue() == 1) {
                l.add(e.getKey());
            }
        }
        return l;
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }
}
