
import java.text.SimpleDateFormat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import Model.Image;
import Model.ImageManager;

import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
class ImageManagerTest {

  /**
   * An ImageManager.
   */
  private ImageManager im;
  /**
   * Files of images to test with.
   */
  private File f1, f2, f3, f4;
  /**
   * Images to test with with respect to Files f1, f2, f3, f4
   */
  private Image i1, i2, i3, i4;

  /**
   * Delete all serialized files to make sure every test starts with no history.
   */
  @BeforeAll
  static void clearSer() {
    File ser = new File("images.ser");
    if (ser.exists()) {
      ser.delete();
    }
    File log = new File("logs.ser");
    if (log.exists()) {
      log.delete();
    }
  }

  /**
   * Set up f1, f2, f3, f4 and i1, i2, i3, i4 for testing
   *
   * @throws IOException if stream to ImageManager's ser file cannot be written or closed
   * @throws ClassNotFoundException if the class path is not updated
   */
  @BeforeEach
  void setUp() throws IOException, ClassNotFoundException {
    f1 = new File("pics/a.jpg");
    f2 = new File("pics/b.jpg");
    f3 = new File("pics/c.jpg");
    f4 = new File("pics/d.jpg");
    i1 = new Image(f1);
    i2 = new Image(f2);
    i3 = new Image(f3);
    i4 = new Image(f4);
    im = new ImageManager();
  }

  /**
   * Remove the ImageManager serialized file to clear history.
   * Clear ImageManager's RenamingLog.
   *
   * @throws IOException if stream to RenamingLog's ser file cannot be written or closed
   */
  @AfterEach
  void tearDown() throws IOException {
    File ser = new File("images.ser");
    if (ser.exists()) {
      ser.delete();
    }
    ImageManager.getRl().clear();
  }

  /**
   * Test adding one Image to ImageManager.
   * @throws IOException if stream to ImageManager's ser file cannot be written or closed
   */
  @Test
  void addOneImage() throws IOException {
    ImageManager.add(i1);
    assertAll(
        () -> assertEquals(1, ImageManager.getImages().size()),
        () -> assertTrue(ImageManager.getImages().containsKey(f1)),
        () -> assertTrue(ImageManager.getImages().get(f1).equals(i1))
    );
  }


  /**
   * Test adding multiple images to ImageManager.
   * @throws IOException if stream to ImageManager's ser file cannot be written or closed
   */
  @Test
  void addMultipleImages() throws IOException {
    ImageManager.add(i1);
    ImageManager.add(i2);
    ImageManager.add(i3);
    assertAll(
        () -> assertEquals(3, ImageManager.getImages().size()),
        () -> assertTrue(ImageManager.getImages().get(f1).equals(i1)),
        () -> assertTrue(ImageManager.getImages().get(f2).equals(i2)),
        () -> assertTrue(ImageManager.getImages().get(f3).equals(i3))
    );
  }

  /**
   * Test getting all added Images.
   * @throws IOException if stream to ImageManager's ser file cannot be written or closed
   */
  @Test
  void testGetImages() throws IOException {
    ImageManager.add(i4);
    Map<File, Image> expected = new HashMap<>();
    expected.put(f4, i4);
    assertEquals(expected, ImageManager.getImages());
  }

  /**
   * Test reading and loading serialized file's information.
   * @throws ClassNotFoundException if the class path is not updated
   * @throws IOException if stream to ImageManager's ser file cannot be written or closed
   */
  @Test
  void testReadFromFile() throws ClassNotFoundException, IOException {
    ImageManager.add(i1);
    ImageManager.add(i2);

    ImageManager.getImages().clear();
    ImageManager.readFromFile();
    assertAll(
        () -> assertTrue(i1.equals(ImageManager.getImages().get(f1))),
        () -> assertTrue(i2.equals(ImageManager.getImages().get(f2)))
    );
  }

  /**
   * Test the String representation of an ImageManager.
   *
   * @throws IOException if stream to ImageManager's ser file cannot be written or closed
   */
  @Test
  void testToString() throws IOException {
    ImageManager.add(i1);
    ImageManager.add(i2);
    ImageManager.add(i3);
    String expected = "c.jpg" + System.lineSeparator() +
        "a.jpg" + System.lineSeparator() +
        "b.jpg" + System.lineSeparator();
    assertEquals(expected, im.toString());
  }

  /**
   * Test method checkKey returns the corresponding Image in images
   * when there is no image in images.
   *
   * @throws IOException if stream to ImageManager's ser file cannot be written or closed
   */
  @Test
  void testCheckKeyWithNoKey() throws IOException {
    Image image = ImageManager.checkKey(f1);
    assertAll(
        () -> assertTrue(ImageManager.getImages().containsKey(f1)),
        () -> assertEquals(i1, image)
    );
  }

  /**
   * Test method checkKey returns the corresponding Image in images
   * when images is not empty.
   *
   * @throws IOException if stream to ImageManager's ser file cannot be written or closed
   */
  @Test
  void testCheckKeyWithKeys() throws IOException {
    ImageManager.add(i1);
    ImageManager.add(i2);
    Image image = ImageManager.checkKey(f2);
    assertAll(
        () -> assertTrue(ImageManager.getImages().containsKey(f2)),
        () -> assertEquals(i2, image)
    );
  }

  /**
   * Test method updateKey updates the key of the image in images to the new File.
   * @throws IOException if stream to ImageManager's ser file cannot be written or closed
   */
  @Test
  void testUpdateKey() throws IOException {
    ImageManager.add(i1);
    ImageManager.add(i2);
    File f5 = new File("e.jpg");
    Image i5 = new Image(f5);
    ImageManager.updateKey(f1, i5);
    assertAll(
        () -> assertFalse(ImageManager.getImages().containsKey(f1)),
        () -> assertEquals(i2, ImageManager.getImages().get(f2)),
        () -> assertEquals(i5, ImageManager.getImages().get(f5))
    );
  }

  /**
   * Test renaming an Image.
   *
   * @throws IOException if stream to ImageManager's ser file cannot be written or closed
   */
  @Test
  void testRenameImage() throws IOException {
    ImageManager.add(i1);
    ImageManager.add(i2);
    Image I1 = ImageManager.renameImage("pics/a.jpg", "1");
    File F1 = new File("pics/1.jpg");
    assertAll(
        () -> assertFalse(f1.exists() && f1.isFile()),
        () -> assertTrue(F1.exists() && F1.isFile()),
        () -> assertFalse(ImageManager.getImages().containsKey(f1)),
        () -> assertEquals(I1, ImageManager.getImages().get(I1.getFile())),
        () -> assertTrue(ImageManager.getImages().containsKey(f2))
    );
    F1.renameTo(f1);
  }

  /**
   * Test adding one tag to one image in images won't affect others.
   *
   * @throws IOException if stream to ImageManager's ser file cannot be written or closed
   */
  @Test
  void testAddOneTagToOneImage() throws IOException {
    ImageManager.add(i1);
    ImageManager.add(i2);
    Image I1 = ImageManager.addTag("pics/a.jpg", "a");
    File F1 = new File("pics/a @a.jpg");
    assertAll(
        () -> assertFalse(f1.exists() && f1.isFile()),
        () -> assertTrue(F1.exists() && F1.isFile()),
        () -> assertTrue(I1.getCurrentTags().contains("a")),
        () -> assertEquals(1, I1.getCurrentTags().size()),
        () -> assertEquals(0, i2.getCurrentTags().size())
    );
    F1.renameTo(f1);
  }

  /**
   * Test adding multiple tags to one image won't affect other images.
   *
   * @throws IOException if stream to ImageManager's ser file cannot be written or closed
   */
  @Test
  void testAddMultipleTagsToOneImage() throws IOException {
    ImageManager.add(i1);
    ImageManager.add(i2);
    Image I1 = ImageManager.addTag(f1.getPath(), "a");
    Image I2 = ImageManager.addTag(I1.getFile().getPath(), "b");
    assertAll(
        () -> assertFalse(f1.exists() && f1.isFile()),
        () -> assertTrue(I2.getFile().exists() && I2.getFile().isFile()),
        () -> assertTrue(I2.getCurrentTags().contains("a")),
        () -> assertTrue(I2.getCurrentTags().contains("b")),
        () -> assertEquals(2, I2.getCurrentTags().size()),
        () -> assertEquals(0, i2.getCurrentTags().size())
    );
    I2.getFile().renameTo(f1);
  }

  /**
   * Test adding multiple tags to different images.
   *
   * @throws IOException if stream to ImageManager's ser file cannot be written or closed
   */
  @Test
  void testAddMultipleTagsToImages() throws IOException {
    ImageManager.add(i1);
    ImageManager.add(i2);
    ImageManager.add(i3);
    Image I1 = ImageManager.addTag("pics/a.jpg", "a");
    Image I2 = ImageManager.addTag("pics/b.jpg", "b");
    File F1 = new File("pics/a @a.jpg");
    File F2 = new File("pics/b @b.jpg");
    assertAll(
        () -> assertFalse(f1.exists() && f1.isFile()),
        () -> assertFalse(f2.exists() && f2.isFile()),
        () -> assertTrue(F1.exists() && F1.isFile()),
        () -> assertTrue(F2.exists() && F2.isFile()),
        () -> assertTrue(I1.getCurrentTags().contains("a")),
        () -> assertTrue(I2.getCurrentTags().contains("b")),
        () -> assertEquals(1, I1.getCurrentTags().size()),
        () -> assertEquals(1, I2.getCurrentTags().size()),
        () -> assertEquals(0, i3.getCurrentTags().size())
    );
    F1.renameTo(f1);
    F2.renameTo(f2);
  }

  /**
   * Test deleting one tag in one image.
   *
   * @throws IOException if stream to ImageManager's ser file cannot be written or closed
   */
  @Test
  void testDeleteOneTagInOneImage() throws IOException {
    ImageManager.add(i1);
    Image I1 = ImageManager.addTag("pics/a.jpg", "a");
    Image I2 = ImageManager.addTag(I1.getFile().getPath(), "b");
    Image Ia = ImageManager.deleteTag(I2.getFile().getPath(), "b");
    File F1 = new File("pics/a @a.jpg");
    assertAll(
        () -> assertFalse(f1.exists() && f1.isFile()),
        () -> assertTrue(F1.exists() && F1.isFile()),
        () -> assertTrue(Ia.getCurrentTags().contains("a")),
        () -> assertEquals(1, Ia.getCurrentTags().size())
    );
    F1.renameTo(f1);
  }

  /**
   * Test deleting multiple tags in one image.
   *
   * @throws IOException if stream to ImageManager's ser file cannot be written or closed
   */
  @Test
  void testDeleteTagsInOneImage() throws IOException {
    ImageManager.add(i1);
    Image I1 = ImageManager.addTag("pics/a.jpg", "a");
    Image I2 = ImageManager.addTag(I1.getFile().getPath(), "b");
    Image I3 = ImageManager.deleteTag(I2.getFile().getPath(), "a");
    Image Ia = ImageManager.deleteTag(I3.getFile().getPath(), "b");
    assertAll(
        () -> assertTrue(f1.exists() && f1.isFile()),
        () -> assertEquals(0, Ia.getCurrentTags().size())
    );
  }

  /**
   * Test moving one image to another directory.
   *
   * @throws IOException if stream to ImageManager's ser file cannot be written or closed
   */
  @Test
  void testMoveOneImage() throws IOException {
    ImageManager.add(i1);
    ImageManager.add(i2);
    ImageManager.move("pics/a.jpg", "pics/move here/a.jpg");
    File newFile = new File("pics/move here/a.jpg");
    assertAll(
        () -> assertFalse(f1.exists() && f1.isFile()),
        () -> assertTrue(newFile.exists() && newFile.isFile()),
        () -> assertTrue(f2.exists() && f2.isFile())
    );
    ImageManager.move("pics/move here/a.jpg", "pics/a.jpg");
  }

  /**
   * Test moving multiple images in images.
   * @throws IOException if stream to ImageManager's ser file cannot be written or closed
   */
  @Test
  void testMoveMultipleImages() throws IOException {
    ImageManager.add(i1);
    ImageManager.add(i2);
    ImageManager.move("pics/a.jpg", "pics/move here/a.jpg");
    ImageManager.move("pics/b.jpg", "pics/move here/b.jpg");
    File newF1 = new File("pics/move here/a.jpg");
    File newF2 = new File("pics/move here/b.jpg");
    assertAll(
        () -> assertFalse(f1.exists() && f1.isFile()),
        () -> assertFalse(f2.exists() && f2.isFile()),
        () -> assertTrue(newF1.exists() && newF1.isFile()),
        () -> assertTrue(newF2.exists() && newF2.isFile())
    );
    ImageManager.move("pics/move here/a.jpg", "pics/a.jpg");
    ImageManager.move("pics/move here/b.jpg", "pics/b.jpg");
  }

  /**
   * Test if getLog can return each image's individual log in images.
   * @throws IOException if stream to ImageManager's ser file cannot be written or closed
   */
  @Test
  void testGetLog() throws IOException {
    ImageManager.add(i1);
    ImageManager.add(i2);
    Image I1 = ImageManager.addTag("pics/a.jpg", "a");
    Date now = new Date();
    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String date = dt.format(now);
    assertEquals("a.jpg,a @a.jpg," + date + "\n",
        ImageManager.getLog(I1.getFile().getPath()));
  }

  /**
   * Test getTags can return an ArrayList of tags of each image.
   * @throws IOException if stream to ImageManager's ser file cannot be written or closed
   */
  @Test
  void testGetTags() throws IOException {
    ImageManager.add(i1);
    ImageManager.add(i2);
    ImageManager.addTag("pics/a.jpg", "a");
    ArrayList<String> tags = ImageManager.getTags("pics/a @a.jpg");
    ArrayList<String> expected = new ArrayList<>();
    expected.add("a");
    assertEquals(expected, tags);
    ImageManager.deleteTag("pics/a @a.jpg", "a");
  }

  /**
   * Test getPastNames returns an ArrayList of past names.
   * @throws IOException if stream to ImageManager's ser file cannot be written or closed
   */
  @Test
  void testGetPastNames() throws IOException {
    ImageManager.add(i1);
    Image I1 = ImageManager.addTag(i1.getFile().getPath(), "a");
    Image I2 = ImageManager.addTag(I1.getFile().getPath(), "b");
    Image I3 = ImageManager.deleteTag(I2.getFile().getPath(), "a");
    ArrayList<String> pastNames = ImageManager.getPastName(I3.getFile().getPath());
    ArrayList<String> expected = new ArrayList<>();
    expected.add("a @a @b");
    expected.add("a @a");
    expected.add("a");
    assertEquals(expected, pastNames);
    I3.getFile().renameTo(f1);
  }

  /**
   * Test getLogs returns a log of all changes ever done.
   * @throws IOException if stream to ImageManager's ser file cannot be written or closed
   */
  @Test
  void testGetLogs() throws IOException {
    ImageManager.add(i1);
    ImageManager.add(i2);
    Image I1 = ImageManager.addTag(i1.getFile().getPath(), "a");
    Image I2 = ImageManager.addTag(i2.getFile().getPath(), "b");

    Date now = new Date();
    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String date = dt.format(now);
    String expected = "a.jpg, a @a.jpg, " + date + "\n"
        + "b.jpg, b @b.jpg, " + date + "\n";

    assertEquals(expected, ImageManager.getLogs());
    I1.getFile().renameTo(f1);
    I2.getFile().renameTo(f2);
  }

  /**
   * Test getImagesWithSameTags returns an ArrayList of images that has the
   * exact same set of tags.
   * @throws IOException if stream to ImageManager's ser file cannot be written or closed
   */
  @Test
  void testGetImagesWithSameTags() throws IOException {
    ImageManager.add(i1);
    ImageManager.add(i2);
    ImageManager.add(i3);
    Image I1 = ImageManager.addTag(i1.getFile().getPath(), "tag 1");
    Image I1_ = ImageManager.addTag(I1.getFile().getPath(), "tag 2");
    Image I2 = ImageManager.addTag(i2.getFile().getPath(), "tag 1");
    Image I2_ = ImageManager.addTag(I2.getFile().getPath(), "tag 2");
    ArrayList<String> tags = new ArrayList<>();
    tags.add("tag 1");
    tags.add("tag 2");
    ArrayList<String> result = ImageManager.getImagesWithSameTags(tags);
    ArrayList<String> expected = new ArrayList<>();
    expected.add(I1_.getName());
    expected.add(I2_.getName());
    assertEquals(expected, result);
    I1_.getFile().renameTo(f1);
    I2_.getFile().renameTo(f2);
  }
}


