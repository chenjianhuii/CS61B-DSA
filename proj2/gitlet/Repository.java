package gitlet;

import java.io.File;
import java.util.*;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

import static gitlet.Utils.*;
import gitlet.Commit;
import gitlet.Staging;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /**
     * INDEX is a map from UID to file name.
     */
    public static final File INDEX = join(GITLET_DIR, "index");

    /**
     * OBJ_FOLDER stores all the commit and copy of file.
     */
    public static final File OBJ_FOLDER = join(GITLET_DIR, "objects");
    public static final File COMMIT_FOLDER = join(GITLET_DIR, "commits");

    /**
     * BRANCH_FOLDER stores all the branches.
     */
    public static final File BRANCH_FOLDER = join(GITLET_DIR, "branches");

    /**
     * HEAD refers to the current commit.
     */
    public static final File HEAD = join(GITLET_DIR, "HEAD");

    private static Map<String, String> index;


    /* TODO: fill in the rest of this class. */
    public static void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        GITLET_DIR.mkdir();
        OBJ_FOLDER.mkdir();
        BRANCH_FOLDER.mkdir();
        COMMIT_FOLDER.mkdir();

        index = new ConcurrentSkipListMap<>();
        writeObject(INDEX, (Serializable) index);

        Commit initCommit = new Commit();
        writeCommit(initCommit);

        String uid = hashCommit(initCommit);
        writeContents(HEAD, uid);

        File masterBranch = join(BRANCH_FOLDER, "master");
        writeContents(masterBranch, uid);

        Staging s = new Staging();
        s.save();
    }

    public static void add(String fileName) {
        Map<String, String> index = readObject(INDEX, ConcurrentSkipListMap.class);
        File f = join(CWD, fileName);
        if (!f.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        String fuid = sha1(readContents(f));
        Staging s = new Staging();
        s.load();
        if (index.containsKey(fileName) && index.get(fileName).equals(fuid)) {
            if (s.contains(fileName)) {
                s.remove(fileName);
            }
            System.exit(0);
        }
        s.add(fileName);
        s.save();
    }

    public static void commit(String msg) {
        Commit cur = currentCommit();
        Commit newCommit = cur.clone();
        newCommit.message = msg;
        newCommit.parentRef = hashCommit(cur);
        Staging s = new Staging();
        s.load();
        if (s.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        Map<String, String> index = readObject(INDEX, ConcurrentSkipListMap.class);
        for (String f: s.addSet()) {
            String uid = hashFile(f);
            File file = uidToFile(uid);
            writeContents(file, readContentsAsString(join(CWD, f)));
            index.put(uid, f);
        }
        if (newCommit.fileRefs == null) {
            newCommit.fileRefs = new ConcurrentSkipListSet<>();
        }
        Set<String> fileRefs = newCommit.fileRefs;
        for (String f : fileRefs) {
            String fileName = index.get(f);
            if (s.contains(fileName)) {
                fileRefs.remove(f);
                fileRefs.add(hashFile(fileName));
                s.remove(fileName);
            }
            if (s.conatinsRM(fileName)) {
                fileRefs.remove(f);
            }
        }
        for (String f: s.addSet()) {
            fileRefs.add(hashFile(f));
        }
        writeCommit(newCommit);
        String uid = hashCommit(newCommit);
        String branch = findBranch();
        writeContents(HEAD, uid);
        writeContents(join(BRANCH_FOLDER, branch), uid);
        s.clear();
        s.save();
        writeObject(INDEX, (Serializable) index);
    }

    public static void status() {
        System.out.println("=== Branches ===");
        for (String b: plainFilenamesIn(BRANCH_FOLDER)) {
            if (b.equals(findBranch())) {
                System.out.print("*");
            }
            System.out.println(b);
        }
        System.out.println();
        Staging s = new Staging();
        s.load();
        System.out.println("=== Staged Files ===");
        printList(s.addSet());
        System.out.println("=== Removed Files ===");
        printList(s.rmSet());
        List<String> modification = new ArrayList<>();
        List<String> untracked = new ArrayList<>();
        Map<String, String> index = readObject(INDEX, ConcurrentSkipListMap.class);
        Commit cur = currentCommit();
        for (String f : plainFilenamesIn(CWD)) {
            if (!index.values().contains(f) && !s.contains(f)) {
                untracked.add(f);
            } else if (!s.contains(f) && !cur.fileRefs.contains(hashFile(f))) {
                modification.add(f);
            }
        }
        System.out.println("=== Modifications Not Staged For Commit ===");
//        printList(modification);
        System.out.println("=== Untracked Files ===");
//        printList(untracked);
    }

    public static void remove(String fileName) {
        Commit cur = currentCommit();
        Staging s = new Staging();
        s.load();
        if (!s.contains(fileName) && !cur.fileRefs.contains(hashFile(fileName))){
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        if (s.contains(fileName)) {
            s.remove(fileName);
        } else if (cur.fileRefs != null) {
            s.addRM(fileName);
            restrictedDelete(fileName);
        }
        s.save();
    }

    public static void log() {
        Commit cur = currentCommit();
        while (cur != null) {
            printCommit(cur);
            cur = readCommit(cur.parentRef);
        }
    }

    public static void globalLog() {
        for (String f: plainFilenamesIn(COMMIT_FOLDER)) {
            Commit c = readCommit(f);
            printCommit(c);
        }
    }

    public static void find(String msg) {
        boolean flag = false;
        for (String f: plainFilenamesIn(COMMIT_FOLDER)) {
            Commit c = readCommit(f);
            if (c.message.equals(msg)) {
                printCommit(c);
                flag = true;
            }
        }
        if (!flag) {
            System.out.println("Found no commit with that message.");
        }
    }


    /** helper functions */
    private static File uidToFile(String uid) {
        String folder = uid.substring(0, 2);
        File f = join(OBJ_FOLDER, folder);
        if (!f.exists()) {
            f.mkdir();
        }
        String filename = uid.substring(2);
        return join(f, filename);
    }

    private static String hashCommit(Commit c) {
        return sha1(serialize(c));
    }

    private static String hashFile(String fileName) {
        File f = join(CWD, fileName);
        return sha1(readContents(f));
    }

    private static void writeCommit(Commit c) {
        String uid = hashCommit(c);
        writeObject(join(COMMIT_FOLDER, uid), c);
    }

    private static Commit readCommit(String uid) {
        if (uid == null) {
            return null;
        }
        File f = join(COMMIT_FOLDER, uid);
        return readObject(f, Commit.class);
    }

    private static Commit currentCommit() {
        String head = readContentsAsString(HEAD);
        Commit cur = readCommit(head);
        return cur;
    }

    private static void printCommit(Commit cur) {
        System.out.println("commit " + hashCommit(cur));
        System.out.print(cur);
    }


    private static void printList(Iterable list) {
        for (Object s : list) {
            System.out.println(s);
        }
        System.out.println();
    }

    private static String findBranch() {
        for (String f: plainFilenamesIn(BRANCH_FOLDER)) {
            if (readContentsAsString(join(BRANCH_FOLDER, f)).equals(readContentsAsString(HEAD))) {
                return f;
            }
        }
        return null;
    }

}