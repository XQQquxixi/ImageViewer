package Model;

import Utility.FileManager;

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
  /* A Logger. */
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
    }
  }

    /**
     * Read from the file with path.
     * @param path the path of the file to be read
     * @throws ClassNotFoundException if the class path is not updated
     */
    public void readFromFile(String path) throws ClassNotFoundException {
      FileManager fm = new FileManager();
      try {
        ObjectInput input = fm.readFromFile(path);

        // deserialize the Map
        images = (HashMap<File, Image>) input.readObject();
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

      FileManager fm = new FileManager();
      ObjectOutput output = fm.saveToFile(filePath);

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


  public Image checkKey(File file) throws IOException {
    if(!images.containsKey(file)) {
      this.add(new Image(file));
      saveToFile("./images.ser");
    }
    return images.get(file);
  }

  public void updateKey(File f1, Image i) throws IOException {
    images.remove(f1);
    this.add(i);
    this.saveToFile("./images.ser");
  }

  public void renameImage(String filePath, String newName) throws IOException {
    File file = new File(filePath);
    Image i = checkKey(file);

    i.setName(newName);

    updateKey(file, i);
  }

  public void addTag(String filePath, String tag) throws IOException {
    File file = new File(filePath);
    Image i = checkKey(file);

    i.addTag(tag);

    updateKey(file, i);
  }

  public void deleteTag(String filePath, String tag) throws IOException {
    File file = new File(filePath);
    Image i = checkKey(file);

    i.deleteTag(tag);

    updateKey(file, i);

  }

  public void move(String oldPath, String newPath) throws IOException {
    File file = new File(oldPath);
    Image i = checkKey(file);

    i.move(newPath);

    updateKey(file, i);
  }

  public String getLog(String filePath) throws IOException, ClassNotFoundException {
    File file = new File(filePath);
    return checkKey(file).getLog();
  }
}
