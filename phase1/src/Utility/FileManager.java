package Utility;
import java.io.*;

public class FileManager {

    public ObjectOutput saveToFile(String filePath) throws IOException {
        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);
        return output;
    }


    public ObjectInput readFromFile(String path) throws ClassNotFoundException, IOException {
        InputStream file = new FileInputStream(path);
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream(buffer);
        return input;
    }

}
