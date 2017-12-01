package Model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
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

  /* A log of all changes. */
  private StringBuilder log = new StringBuilder();


  /* An ArrayList of observers. */
  private ArrayList<Observer> observers = new ArrayList<>();

  /**
   * A image associated with file.
   *
   * @param file the image's file
   */
  public Image(File file) throws IOException {
    this.name = file.getName();
    this.file = file;
    currentTags = new ArrayList<>();
    restoreTag(getName());
    logger.setLevel(Level.OFF);
    consoleHandler.setLevel(Level.OFF);
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
   * rename the image to a new name.
   *
   * @param name the new name for the image
   */
  void setName(String name){
    String oldName = this.name;
    String absolutePath = file.getAbsolutePath();
    String path = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
    File newFile = new File(path + File.separator + name + getExtension());
//    int i = 1;
//    while (newFile.exists()) {
//      path = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
//      newFile = new File(
//          path + File.separator + name + "(" + Integer.toString(i++) + ")" + getExtension());
//    }
    if (file.renameTo(newFile)) {
      logger.log(Level.FINE, "rename successfully");
    } else {
      logger.log(Level.WARNING, "File rename failed");
    }
    this.name = newFile.getName();
    this.file = newFile;
    updateLog(oldName);
    setChanged();
    notifyObservers(oldName);
  }

  /**
   * Notify the first observer of this image that it is changing its name.
   * @param oldName the previous name of this image
   */
  private void notifyObservers(String oldName) {
    observers.get(0).update(this, oldName);

  }

  /**
   * Add Observer to this image.
   * @param o the Observer to be added
   */
  public void addObserver(Observer o) {
    if (!observers.contains(o)) {
      observers.add(o);
    }
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
  void addTag(String tag) throws IOException {
    if (currentTags.contains(tag)) {
      logger.log(Level.WARNING, "This tag already exists!");
    } else {
      currentTags.add(tag);
      if (!getTagsFromName(getName()).contains(tag)) {
        this.setName(getName() + " @" + tag);
      }
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
  void deleteTag(String tag){
    if (currentTags.contains(tag)) {
      currentTags.remove(tag);
      int index = name.lastIndexOf(" @" + tag);
      int indexAfterTag = index + (" @" + tag).length();
      String namePart = getName();
      String newName;
      if (index == -1) {
        newName = this.getName();
      }
      else {
        if (indexAfterTag < namePart.length() - 1) {
          newName =
              getName().substring(0, index)
                  + getName().substring(indexAfterTag, getName().length());
        } else {
          newName = getName().substring(0, index);
        }
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
  void move(String newPath) throws IOException {
    Path oldPath = file.toPath();
    Files.move(oldPath, Paths.get(newPath));
    file = new File(newPath);
  }

  /**
   * Add an entry to the log corresponding to the name change from oldName. Each entry is a new line
   * and each item in an entry is comma separated.
   *
   * @param oldName the old name that was changed.
   */
  private void updateLog(String oldName) {
    Date now = new Date();
    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String date = dt.format(now);
    String entry = oldName + "," + name + "," + date;
    log.append(entry);
    log.append(System.lineSeparator());
  }

  /**
   * Return a String version of this Image's log.
   *
   * @return a String version of this Image's log.
   */
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
    if (logs.isEmpty()) {
      return new ArrayList<>();
    } else {
      return new ArrayList<>(Arrays.asList(logs.split(System.lineSeparator())));
    }
  }

  /**
   * Return a list of past name the associated Image image had.
   *
   * @return An ArrayList of past names.
   */
  ArrayList<String> getPastNames() {
    ArrayList<String> listOfLog = getListOfLog();
    ArrayList<String> pastNames = new ArrayList<>();
    if (listOfLog.size() > 0) {
      for (String log : listOfLog) {
        String oldFullName = log.split(",")[0];
        String oldName = oldFullName.substring(0, oldFullName.lastIndexOf(getExtension()));
        if (!pastNames.contains(oldName) && !oldName.equals(name.substring(0, name.lastIndexOf(".")))) {
          pastNames.add(oldName);
        }
      }
    }
    Collections.reverse(pastNames);
    return pastNames;
  }

  /**
   * Return the tag part of the file name.
   *
   * @return the tag part of the file name
   */
  public String getTagPartOfName() {
    int i = getName().indexOf(" @");
    if(i != (-1)) {
      return getName().substring(i, getName().length());
    } else {
      return "";
    }
  }

  /**
   * Return an ArrayList of tags extracted from name
   *
   * @return An ArrayList of tags
   */
  private ArrayList<String> getTagsFromName(String name) {
    ArrayList<String> result = new ArrayList<>(Arrays.asList(name.split(" @")));
    result.remove(0);
    return result;
  }


  /**
   * Update this image's current tag list with tags shown in its name.
   * @param name the name of this image
   * @throws IOException if stream to file with newPath cannot be written or closed
   */
  public void restoreTag(String name) throws IOException {
    ArrayList<String> tags = getTagsFromName(name);
    currentTags.clear();
    for (String tag: tags) {
      addTag(tag);
    }
  }

  /**
   * Returns if two images have the same tags, name, file location and log.
   *
   * @param other the other Image to compare with.
   * @return true iff two images are the same by their name, tags, file and log. false otherwise.
   */
  public boolean equals(Object other) {
    return (other instanceof Image)
        && (this.currentTags.equals(((Image) other).currentTags))
        && (this.name.equals(((Image) other).name))
        && (this.file.equals(((Image) other).file))
        && (this.log.toString().equals(((Image) other).log.toString()));
  }
}
