import java.io.*;
import java.util.*;

public class RenameObserver implements Observer {

    private Image image;
    private StringBuilder logs = new StringBuilder();
    private static final  String filePath = "./logs.txt";    // ser or txt?


    public RenameObserver(Image image) throws IOException, ClassNotFoundException {
        this.image = image;
        this.image.addObserver(this);


        File file = new File(filePath);
        if (file.exists()) {
            readFromFile(filePath);
        } else {
            file.createNewFile();
        }
    }

    public void readFromFile(String path) throws ClassNotFoundException {

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
        logs.append(nameOld + image.getName() + Long.toString(time));
        logs.append(System.lineSeparator());

        // RenameObserver the addition of a student.
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

    @Override
    public String toString() {
        String result = "";
        return result;
    }


    public void update(Observable o, Object oldName) {
        Date date = new Date();
        long time = date.getTime();

        try {
            readFromFile(filePath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        add((Image) o, (String) oldName, time);

        try {
            saveToFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
