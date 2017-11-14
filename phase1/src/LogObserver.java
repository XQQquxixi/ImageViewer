import java.io.*;
import java.util.*;

public class LogObserver implements Observer {

    private Image image;
    private StringBuilder logs = new StringBuilder();
    private File file;


    public LogObserver(Image image) throws IOException, ClassNotFoundException {
        this.image = image;
        this.image.addObserver(this);

        String absolutePath = image.getFile().getAbsolutePath();
        String filePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator)) + File.separator + "log_" + image.getName() + ".ser";
        this.file = new File(filePath);

        if (file.exists()) {
            readFromFile(filePath);
        } else {
            file.createNewFile();
        }
    }

    public void readFromFile(String filePath) throws ClassNotFoundException {

        try {
            InputStream file = new FileInputStream(filePath);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);


            logs = (StringBuilder) input.readObject();
            input.close();
        } catch (IOException ex) {
            System.out.println("Cannot read from input.");
        }
    }

    public void add(Image image, String nameOld, long time) {
        String newLog = nameOld + " " + image.getName() + " " + Long.toString(time);
        logs.append(newLog);
        logs.append(System.lineSeparator());

        // LogObserver the addition of a student.
        System.out.println("Added a new renaming " + image.toString());
    }

    /**
     * Writes the students to file at filePath.
     * @param filePath the file to write the records to
     * @throws IOException
     */
    public void saveToFile(String filePath) throws IOException {

        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        // serialize the Map
        output.writeObject(logs);
        output.close();
    }


    public void update(Observable o, Object oldName) {
        Date date = new Date();
        long time = date.getTime();

        // add new log entry
        if (!((Image) o).getName().equals(oldName)) {
            add((Image) o, (String) oldName, time);
            try {
                saveToFile(file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // update log name
        String absolutePath = ((Image) o).getFile().getAbsolutePath();
        String fileName = ((Image) o).getName();
        File newFile = new File(absolutePath.substring(0, absolutePath.lastIndexOf(File.separator)) + File.separator + "log_" + fileName + ".ser");
        file.renameTo(newFile);
        file = newFile;

    }


    public StringBuilder getLogs() {
        return logs;
    }
}
