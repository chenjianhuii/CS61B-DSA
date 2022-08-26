package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.Set;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable, Cloneable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    public String message;

    public Date time;

    public Set<String> fileRefs;

    public String parentRef;

    public Commit() {
        message = "initial commit";
        parentRef = null;
        fileRefs = null;
        time = new Date(0);
    }

    @Override
    public Commit clone() {
        Commit c = new Commit();
        c.time = new Date();
        c.parentRef = this.parentRef;
        c.fileRefs = this.fileRefs;
        c.message = this.message;
        return c;
    }

    @Override
    public String toString() {
        String msg = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy").format(time);
        return "Date: " + msg + "\n" + message + "\n\n";
    }

}
