package Utility;
import java.io.*;

public class FileManager {

    /**
     * Returns ObjectOutput of of this filePath
     * Code adapted from Paul's slides
     * http://www.teach.cs.toronto.edu/~csc207h/fall/lectures.shtml
     * @param filePath: the path about to be  returned as ObjectOutput
     * @return ObjectOutput of this filePath
     * @throws IOException if stream to file with filePath cannot be written or closed
     */
    public ObjectOutput saveToFile(String filePath) throws IOException {
        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        return  new ObjectOutputStream(buffer);
    }


    /**
     * Returns ObjectInput of of this path
     * Code adapted from Paul's slides
     * http://www.teach.cs.toronto.edu/~csc207h/fall/lectures.shtml
     * @param path the path about to be  returned as ObjectOutput
     * @return ObjectInput of this filePath
     * @throws ClassNotFoundException if the class path is not updated
     * @throws IOException if stream to file with path cannot be written or closed
     */
    public ObjectInput readFromFile(String path) throws ClassNotFoundException, IOException {
        InputStream file = new FileInputStream(path);
        InputStream buffer = new BufferedInputStream(file);
        return new ObjectInputStream(buffer);
    }

}
