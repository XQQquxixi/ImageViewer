package Model;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageManager {

  /* A map with file as key and its corresponding image as value. */
  private Map<File, Image> images;
  /* A Looger. */
  private static final Logger logger = Logger.getLogger(Image.class.getName());
  /* A ConsoleHandler. */
  private static final Handler consoleHandler = new ConsoleHandler();

    /**
     * An ImageManager with filePath.
     * @param filePath the path where information is stored
     * @throws ClassNotFoundException if the class path is not updated
     * @throws IOException if stream to file with filePath cannot be written or closed
     */
  public ImageManager(String filePath) throws ClassNotFoundException, IOException {
    images = new HashMap<>();
    File file = new File(filePath);
    logger.setLevel(Level.ALL);
    consoleHandler.setLevel(Level.ALL);
    logger.addHandler(consoleHandler);
    if (file.exists()) {
      readFromFile(filePath);
    } else {
      if (file.createNewFile()) {
        logger.log(Level.FINE, "created images.ser");
      }
      ;
    }
  }

    /**
     * Read from the file with path.
     * @param path the path of the file to be read
     * @throws ClassNotFoundException if the class path is not updated
     */

  private void readFromFile(String path) throws ClassNotFoundException {

    try {
      InputStream file = new FileInputStream(path);
      InputStream buffer = new BufferedInputStream(file);
      ObjectInput input = new ObjectInputStream(buffer);

      // deserialize the Map
      images = (Map<File, Image>) input.readObject();
      input.close();
    } catch (IOException ex) {
      logger.log(Level.WARNING, "Cannot read from images.ser");
    }
  }

    /**
     * Add an image record to images.
     * @param record the Image that is about to be added
     */
  public void add(Image record) {
    images.put(record.getFile(), record);
  }

    /**
     * Save the changes to the file with filePath.
     * @param filePath the path of the file that is about to be changed
     * @throws IOException if stream to file with filePath cannot be written or closed
     */
  public void saveToFile(String filePath) throws IOException {

    OutputStream file = new FileOutputStream(filePath);
    OutputStream buffer = new BufferedOutputStream(file);
    ObjectOutput output = new ObjectOutputStream(buffer);

    // serialize the Map
    output.writeObject(images);
    output.close();
  }

    /**
     * Returns a map of images in this ImageManager.
     * @return a map of images
     */
  public Map<File, Image> getList() {
    return images;
  }

    /**
     * Return the String representation of this ImageManager.
     * @return the String representation of this ImageManager
     */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (Image i : images.values()) {
      result.append(i.toString());
      result.append(System.lineSeparator());
    }
    return result.toString();
  }
}
