package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageManager {

    private Map<File, Image>images;
    private static final Logger logger =
            Logger.getLogger(Image.class.getName());
    private static final Handler consoleHandler = new ConsoleHandler();


    public ImageManager(String filePath) throws ClassNotFoundException, IOException {
        images = new HashMap<>();
        File file = new File(filePath);
        logger.setLevel(Level.ALL);
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);
        if (file.exists()) {
            readFromFile(filePath);
        } else {
            file.createNewFile();
        }
    }

    public void readFromFile(String path) throws ClassNotFoundException {

        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            //deserialize the Map
            images = (Map<File, Image>) input.readObject();
            input.close();
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Cannot read from images.ser");
        }
    }

    public void add(Image record) {
        images.put(record.getFile(), record);

    }

    public void saveToFile(String filePath) throws IOException {

        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        // serialize the Map
        output.writeObject(images);
        output.close();
    }

    public Map<File, Image> getList() {
        return images;
    }

    @Override
    public String toString() {
        String result = "";
        for (Image i : images.values()) {
            result += i.toString() + "\n";
        }
        return result;
    }

}


