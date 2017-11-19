package Model;

import Utility.FileManager;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ImageManager {

  /* A map with file as key and its corresponding image as value. */
  private static Map<File, Image> images;
  /* A Logger. */
  private static final Logger logger = Logger.getLogger(Image.class.getName());
  /* A ConsoleHandler. */
  private static final Handler consoleHandler = new ConsoleHandler();
  private static final String path = "./images.ser";

  /**
   * An ImageManager with filePath.
   *
   * @throws ClassNotFoundException if the class path is not updated
   * @throws IOException if stream to file with filePath cannot be written or closed
   */
  public ImageManager() throws ClassNotFoundException, IOException {
    images = new HashMap<>();
    File file = new File(path);
    logger.setLevel(Level.ALL);
    consoleHandler.setLevel(Level.ALL);
    logger.addHandler(consoleHandler);
    if (file.exists()) {
      readFromFile();
    } else {
      if (file.createNewFile()) {
        logger.log(Level.FINE, path);
      }
    }
  }

  /**
   * Read from the file with path.
   *
   * @throws ClassNotFoundException if the class path is not updated
   */
  public static void readFromFile() throws ClassNotFoundException {
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
   *
   * @param record the Image that is about to be added
   */
  public static void add(Image record) {
    images.put(record.getFile(), record);
  }

  /**
   * Save the changes to the file with filePath.
   *
   * @throws IOException if stream to file with filePath cannot be written or closed
   */
  public static void saveToFile() throws IOException {

    FileManager fm = new FileManager();
    ObjectOutput output = fm.saveToFile(path);

    // serialize the Map
    output.writeObject(images);
    output.close();
  }

  /**
   * Returns a map of images in this ImageManager.
   *
   * @return a map of images
   */
  public static Map<File, Image> getList() {
    return images;
  }

  /**
   * Return the String representation of this ImageManager.
   *
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


  public static Image checkKey(File file) throws IOException {
    if (!images.containsKey(file)) {
      add(new Image(file));
      saveToFile();
    }
    return images.get(file);
  }

  public static void updateKey(File f1, Image i) throws IOException {
    images.remove(f1);
    add(i);
    saveToFile();
  }

  public static Image renameImage(String filePath, String newName) throws IOException {
    File file = new File(filePath);
    Image i = checkKey(file);

    i.setName(newName);

    updateKey(file, i);

    return i;
  }

  public static Image addTag(String filePath, String tag) throws IOException {
    File file = new File(filePath);
    Image i = checkKey(file);

    i.addTag(tag);

    updateKey(file, i);

    return i;
  }

  public static Image deleteTag(String filePath, String tag) throws IOException {
    File file = new File(filePath);
    Image i = checkKey(file);

    i.deleteTag(tag);

    updateKey(file, i);

    return i;
  }

  public static void move(String oldPath, String newPath) throws IOException {
    File file = new File(oldPath);
    Image i = checkKey(file);

    i.move(newPath);

    updateKey(file, i);
  }

  public static String getLog(String filePath) throws IOException, ClassNotFoundException {
    File file = new File(filePath);
    return checkKey(file).getLog();
  }

  public static ArrayList<String> getTags(String filepath) throws IOException {
    File file = new File(filepath);
    return checkKey(file).getCurrentTags();
  }

  public static ArrayList<String> getPastName(String filepath) {
    File file = new File(filepath);
      try {
          return checkKey(file).getPastNames();
      } catch (IOException e) {
          e.printStackTrace();
      }
      return null;
  }
}

