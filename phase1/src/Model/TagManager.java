package Model;

import Utility.FileManager;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Tag Manager.
 */
public class TagManager {

  /**
   * The list of all existing tags.
   */
  public static ArrayList<String> tagList = new ArrayList<>();
  /**
   * The string path of the serialized file of this TagManager.
   */
  private static String filePath = "./tm.ser";
  /**
   * A Logger.
   */
  private static final Logger logger =
      Logger.getLogger(TagManager.class.getName());
  /**
   * A Console Handler.
   */
  private static final Handler consoleHandler = new ConsoleHandler();

  /**
   * Initialize a TagManager and creates a serialized file.
   */
  public TagManager() throws ClassNotFoundException, IOException {
    logger.setLevel(Level.ALL);
    consoleHandler.setLevel(Level.ALL);
    logger.addHandler(consoleHandler);

    File file = new File(filePath);
    if (file.exists()) {
      readFromFile(filePath);
    } else {
      if (file.createNewFile()) {
        logger.log(Level.FINE, "created tag.ser");
      }
    }
  }

  /**
   * Return a copy of tagList.
   *
   * @return a copy of tagList.
   */
  public static ArrayList<String> getTagList() {
    return new ArrayList<>(tagList);
  }

  /**
   * Add a tag into tagList then update the serialized file if it does not exist in it. Otherwise
   * add a WARNING log.
   *
   * @param tag the added tag
   */
  public static void addTag(String tag) throws IOException {
    if (!tagList.contains(tag)) {
      tagList.add(tag);
      saveToFile(filePath);
    } else {
      logger.log(Level.WARNING, "This tag already existed.");
    }

  }

  /**
   * Remove a tag from tagList then update the serialized file if it exists in it. Otherwise add a
   * WARNING log.
   *
   * @param tag the tag to delete
   */
  public static void removeTag(String tag) throws IOException {
    if (tagList.contains(tag)) {
      tagList.remove(tag);
      saveToFile(filePath);
    } else {
      logger.log(Level.WARNING, "No such tag.");
    }

  }

  /**
   * Save this TagManager to a serialized file to filePath.
   *
   * @param filePath the path to save the file.
   */
  public static void saveToFile(String filePath) throws IOException {

    FileManager fm = new FileManager();
    ObjectOutput output = fm.saveToFile(filePath);
    // serialize the Map
    output.writeObject(tagList);
    output.close();
  }
  /**
   * Read a TagManager from the serialized file with path and update this TagManger's tagList with
   * the read TagManager's.
   *
   * @param path the path of this file to read.
   */
  public void readFromFile(String path) throws ClassNotFoundException {
    FileManager fm = new FileManager();
    try {
      ObjectInput input = fm.readFromFile(path);
      tagList = (ArrayList<String>) input.readObject();
      input.close();
    } catch (IOException ex) {
      System.out.println("Cannot read from tags.ser");
    }
  }
}
