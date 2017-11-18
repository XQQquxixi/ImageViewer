package Model;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageRenameObserver implements Observer {

  private Image image;
  private StringBuilder contents = new StringBuilder();
  private static final Logger logger =
      Logger.getLogger(ImageRenameObserver.class.getName());
  private static final Handler consoleHandler = new ConsoleHandler();


  public ImageRenameObserver(Image image) throws IOException, ClassNotFoundException {
    this.image = image;
    this.image.addObserver(this);
    logger.setLevel(Level.ALL);
    consoleHandler.setLevel(Level.ALL);
    logger.addHandler(consoleHandler);
  }

  public void add(Image image, String nameOld, String time) {
    String newLog = nameOld + "," + image.getName() + "," + time;
    contents.append(newLog);
    contents.append(System.lineSeparator());

    // LogObserver the addition of a student.
    logger.log(Level.FINE, "Added a new renaming " + image.toString());
  }

  public void update(Observable o, Object oldName) {
    Date now = new Date();
    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String date = dt.format(now);
    add((Image) o, (String) oldName, date);
  }


  public String getLogs() {
    return contents.toString();
  }

  public ArrayList<String> getListOfLog() {
    String logs = this.getLogs();
    return new ArrayList<>(Arrays.asList(logs.split(System.lineSeparator())));
  }

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
