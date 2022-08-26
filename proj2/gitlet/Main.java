package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author CJH
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Repository.init();
                break;
            case "add":
                String fileName = args[1];
                Repository.add(fileName);
                break;
            case "status":
                Repository.status();
                break;
            case "commit":
                if (args.length == 1) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                String msg = args[1];
                Repository.commit(msg);
                break;
            case "rm":
                fileName = args[1];
                Repository.remove(fileName);
                break;
            case "log":
                Repository.log();
                break;
            case "global-log":
                Repository.globalLog();
                break;
            case "find":
                fileName = args[1];
                Repository.find(fileName);
                break;

        }
    }
}
