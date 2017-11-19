package Model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Observable;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Image extends Observable implements Serializable {

  /* The name of the image file with extension. */
  private String name;

  /* This image file.  */
  private File file;

  /* An ArrayList of all the tags of this image. */
  private ArrayList<String> currentTags;


  /* A Logger. */
  private static final Logger logger = Logger.getLogger(Image.class.getName());

  /* A ConsoleHandler. */
  private static final Handler consoleHandler = new ConsoleHandler();

  public StringBuilder log = new StringBuilder();

  /**
   * A image associated with file.
   *
   * @param file the image's file
   */
  public Image(File file) {
    this.name = file.getName();
    this.file = file;
    currentTags = new ArrayList<>();
    logger.setLevel(Level.ALL);
    consoleHandler.setLevel(Level.ALL);
    logger.addHandler((consoleHandler));
  }


  /**
   * Returns this image's name without file extension.
   *
   * @return this image's name
   */
  public String getName() {
    return name.substring(0, name.lastIndexOf("."));
  }

  /**
   * Returns this image's associated file.
   *
   * @return this image's file.
   */
  public File getFile() {
    return file;
  }

  /**
   * Returns the file extension associated with this image.
   *
   * @return the file extension
   */
  public String getExtension() {
    String absolutePath = file.getAbsolutePath();
    return absolutePath.substring(absolutePath.lastIndexOf("."), absolutePath.length());
  }

  /**
   * Returns a list of tags this image has.
   *
   * @return the list of tags this image has
   */
  public ArrayList<String> getCurrentTags() {
    return currentTags;
  }

  /**
   * Rename the image to a new name.
   *
   * @param name the new name for the image
   */
  public void setName(String name) {
    String oldName = this.name;
    String absolutePath = file.getAbsolutePath();
    String path = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
    File newFile = new File(path + File.separator + name + getExtension());
    int i = 1;
    while (newFile.exists()) {
      path = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
      newFile = new File(path + File.separator + name + "(" +Integer.toString(i++) +")" + getExtension());
    }
    if (file.renameTo(newFile)) {
      logger.log(Level.FINE, "rename successfully");
    } else {
      logger.log(Level.WARNING, "File rename failed");
    }
    this.name = name + getExtension();
    this.file = newFile;
    updateLog(oldName);
  }

  /**
   * Returns the String representation of this image.
   *
   * @return the name of this image with extension
   */
  public String toString() {
    return name;
  }

  /**
   * Add a new tag to this image.
   *
   * @param tag the new tag to be added
   * @throws IOException if tags.ser is no longer available
   */
  public void addTag(String tag) throws IOException {
    if (currentTags.contains(tag)) {
      logger.log(Level.WARNING, "This tag already exists!");
    } else {
      currentTags.add(tag);
      this.setName(getName() + " @" + tag);
      if (!TagManager.tagList.contains(tag)) {
        TagManager.addTag(tag);
      }
    }
  }

  /**
   * Delete a tag from this image.
   *
   * @param tag the tag about to delete
   */
  public void deleteTag(String tag) {
    if (currentTags.contains(tag)) {
      currentTags.remove(tag);
      int index = name.lastIndexOf(" @" + tag);
      int indexAfterTag = index + (" @" + tag).length() + 1;
      String namePart = getName();
      String newName;
      if (indexAfterTag < namePart.length()) {
        newName =
            getName().substring(0, index)
                + getName().substring(index + (" @" + tag).length() + 1, getName().length());
      } else {
        newName = getName().substring(0, index);
      }
      this.setName(newName);
    } else {
      logger.log(Level.WARNING, "No such tag in this photo!");
    }
  }

  /**
   * Move this file.
   *
   * @param newPath the destination the file is about to move to
   * @throws IOException if stream to file with newPath cannot be written or closed
   */
  public void move(String newPath) throws IOException {
    Path oldPath = file.toPath();
    Files.move(oldPath, Paths.get(newPath));
    file = new File(newPath);
  }

  public void updateLog(String oldName) {
    Date now = new Date();
    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String date = dt.format(now);
    String entry = oldName + "," + name +"," + date;
    log.append(entry);
    log.append(System.lineSeparator());
  }

  public String getLog() {
    return log.toString();
  }

  /**
   * Return an ArrayList of the log where each item is an entry in the log.
   *
   * @return An ArrayList of entries in log.
   */
  private ArrayList<String> getListOfLog() {
    String logs = this.getLog();
    return new ArrayList<>(Arrays.asList(logs.split(System.lineSeparator())));
  }

  /**
   * Return a list of past name the associated Image image had.
   *
   * @return An ArrayList of past names.
   */
  public ArrayList<String> getPastNames() {
    ArrayList<String> listOfLog = getListOfLog();
    listOfLog.remove(0);
    ArrayList<String> pastNames = new ArrayList<>();
    for (String log : listOfLog) {
      pastNames.add(log.split(",")[0]);
    }
    return pastNames;
  }

}
