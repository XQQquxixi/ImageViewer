package Model;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An Observer for renaming Image.
 */
public class ImageRenameObserver implements Observer, Serializable {

  /**
   * The Image this observer is recording renaming logs of.
   */
  private Image image;
  /**
   * The log of re-namings done. Each line composes of the old name, the new name, and the
   * timestamp, all separated by comma. Each entry separated by a linebreak.
   */
  private StringBuilder contents = new StringBuilder();
  /**
   * A Logger.
   */
  private static final Logger logger =
      Logger.getLogger(ImageRenameObserver.class.getName());
  /**
   * A Console Handler.
   */
  private static final Handler consoleHandler = new ConsoleHandler();


  /**
   * Initialize an ImageRenameObserver with an association with an Image object.
   *
   * @param image the Image associated.
   */
  public ImageRenameObserver(Image image) {
    this.image = image;
    this.image.addObserver(this);
    logger.setLevel(Level.ALL);
    consoleHandler.setLevel(Level.ALL);
    logger.addHandler(consoleHandler);
  }

  /**
   * Add an entry of image's renaming in the contents log with nameOld as the old name and time as
   * the time modified.
   *
   * @param image this image that was renamed.
   * @param nameOld the previous name before renaming.
   * @param time the time when the renaming occurred.
   */
  public void add(Image image, String nameOld, String time) {
    String newLog = nameOld + "," + image.getName() + "," + time;
    contents.append(newLog);
    contents.append(System.lineSeparator());

    logger.log(Level.FINE, "Added a new renaming " + image.toString());
  }

  /**
   * This method is called whenever the observed object is changed.
   *
   * @param image the observable image that is changed
   * @param oldName the old name before renaming.
   */
  @Override
  public void update(Observable image, Object oldName) {
    Date now = new Date();
    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String date = dt.format(now);
    add((Image) image, (String) oldName, date);
  }


  /**
   * Return the String version of the log stored in contents.
   *
   * @return the log recorded so far in String.
   */
  public String getLogs() {
    return contents.toString();
  }

  /**
   * Return an ArrayList of the log where each item is an entry in the log.
   *
   * @return An ArrayList of entries in log.
   */
  private ArrayList<String> getListOfLog() {
    String logs = this.getLogs();
    if (!logs.isEmpty()) {
      return new ArrayList<>(Arrays.asList(logs.split(System.lineSeparator())));
    } else {
      return new ArrayList<>();
    }
  }

  /**
   * Return a list of past name the associated Image image had.
   *
   * @return An ArrayList of past names.
   */
  public ArrayList<String> getPastNames() {
    ArrayList<String> listOfLog = getListOfLog();
    if (listOfLog.isEmpty()) {
      return new ArrayList<>();
    } else {
      listOfLog.remove(0);
      ArrayList<String> pastNames = new ArrayList<>();
      for (String log : listOfLog) {
        pastNames.add(log.split(",")[0]);
      }
      return pastNames;
    }
  }
}
