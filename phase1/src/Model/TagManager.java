package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TagManager {

  public static ArrayList<String> tagList;
  private static String filePath = "./tm.ser";
  private static final Logger logger =
          Logger.getLogger(TagManager.class.getName());
  private static final Handler consoleHandler = new ConsoleHandler();

  public TagManager() throws ClassNotFoundException, IOException {
    tagList = new ArrayList<>();
    logger.setLevel(Level.ALL);
    consoleHandler.setLevel(Level.ALL);
    logger.addHandler(consoleHandler);

    File file = new File(filePath);
    if (file.exists()) {
      readFromFile(filePath);
    } else {
      if(file.createNewFile()) {
        logger.log(Level.FINE, "created tag.ser");
      }
    }
  }

  public ArrayList<String> getTagList() {
    return new ArrayList<>(tagList);
  }

  public static void addTag(String tag) throws IOException {
    if (!tagList.contains(tag)) {
      tagList.add(tag);
      saveToFile(filePath);
    } else {
      logger.log(Level.WARNING, "This tag already existed.");
    }

  }

  public static void removeTag(String tag) throws IOException {
    if (tagList.contains(tag)) {
      tagList.remove(tag);
      saveToFile(filePath);
    } else {
      logger.log(Level.WARNING,"No such tag.");
    }

  }

  public static void saveToFile(String filePath) throws IOException {

    OutputStream file = new FileOutputStream(filePath);
    OutputStream buffer = new BufferedOutputStream(file);
    ObjectOutput output = new ObjectOutputStream(buffer);

    // serialize the Map
    output.writeObject(tagList);
    output.close();
  }

  public static void readFromFile(String path) throws ClassNotFoundException {

    try {
      InputStream file = new FileInputStream(path);
      InputStream buffer = new BufferedInputStream(file);
      ObjectInput input = new ObjectInputStream(buffer);

      tagList = (ArrayList<String>) input.readObject();
      input.close();
    } catch (IOException ex) {
      logger.log(Level.WARNING,"Cannot find the file.");
    }
  }
}
