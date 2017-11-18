package Model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Observable;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Image extends Observable implements Serializable {

  /**
   * The name part of file's name.
   */
  private String name;


  /**
   * This image file.
   */
  private File file;

  /**
   * An ArrayList of all the tags of this image.
   */
  private ArrayList<String> currentTags;


  private static final Logger logger =
      Logger.getLogger(Image.class.getName());
  private static final Handler consoleHandler = new ConsoleHandler();


  public Image(File file) {
    this.name = file.getName();
    this.file = file;
    currentTags = new ArrayList<>();
    logger.setLevel(Level.ALL);
    consoleHandler.setLevel(Level.ALL);
    logger.addHandler((consoleHandler));
  }


  public String getName() {
    return name.substring(0, name.lastIndexOf("."));

  }

  public File getFile() {
    return file;
  }

  public String getExtension() {
    String absolutePath = file.getAbsolutePath();
    return absolutePath.substring(absolutePath.lastIndexOf("."), absolutePath.length() - 1);
  }

  public ArrayList<String> getCurrentTags() {
    return currentTags;
  }

  public void setName(String name) {
    String oldName = this.name;
    String absolutePath = file.getAbsolutePath();
    String path = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
    File newFile = new File(path + File.separator + name + getExtension());
    int i = 1;
    while (newFile.exists()) {
      path = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
      newFile = new File(path + File.separator + name + Integer.toString(i++));
    }
    if (file.renameTo(newFile)) {
      logger.log(Level.FINE, "rename successfully");
    } else {
      logger.log(Level.WARNING, "File rename failed");
    }
    this.name = name;
    this.file = newFile;
    setChanged();
    notifyObservers(oldName);
  }


  public String toString() {
    return name;
  }

  public void addTag(String tag) throws IOException {
    if (currentTags.contains(tag)) {
      System.out.println("This tag is already in here!");
    } else {
      currentTags.add(tag);
      this.setName(this.name + " @" + tag);
      if (!TagManager.tagList.contains(tag)) {
        TagManager.addTag(tag);
      }
    }
  }

  public void deleteTag(String tag) {
    if (currentTags.contains(tag)) {
      currentTags.remove(tag);
      int index = name.lastIndexOf(" @" + tag);
      String newName = name.substring(0, index) +
          name.substring(index + (" @" + tag).length() + 1, name.length());
      this.setName(newName);
    } else {
      logger.log(Level.WARNING, "No such tag in this photo!");
    }
  }

  public void move(String newPath) throws IOException {
    Path oldPath = file.toPath();
    Files.move(oldPath, Paths.get(newPath));
    file = new File(newPath);
    String serPath =
        oldPath.toString().substring(0, oldPath.toString().lastIndexOf(File.separator) + 1)
            + name + ".ser";
    Files.delete(Paths.get(serPath));
  }
}