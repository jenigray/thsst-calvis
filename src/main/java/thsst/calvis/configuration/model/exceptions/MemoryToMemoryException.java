package thsst.calvis.configuration.model.exceptions;

/**
 * Created by Goodwin Chua on 2/4/2016.
 */
public class MemoryToMemoryException extends Exception {

    public MemoryToMemoryException(String first, String second, int line) {
        super("No memory to memory: " + first + " " + second + " at line number: " + line);
    }

    public MemoryToMemoryException(String first, String second) {
        super("No memory to memory: " + first + " " + second);
    }

}
