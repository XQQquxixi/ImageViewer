package Utility;
import java.io.*;

public class FileManager {

    public ObjectOutput saveToFile(String filePath) throws IOException {
        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        return  new ObjectOutputStream(buffer);
    }


    public ObjectInput readFromFile(String path) throws ClassNotFoundException, IOException {
        InputStream file = new FileInputStream(path);
        InputStream buffer = new BufferedInputStream(file);
        return new ObjectInputStream(buffer);
    }

}
