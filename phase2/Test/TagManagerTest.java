import Model.Image;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import Model.TagManager;

import java.io.File;
import java.io.IOException;

class TagManagerTest {

  private TagManager tagManger;
  /* A Logger. */
  private static final Logger logger = Logger.getLogger(Image.class.getName());

  /* A ConsoleHandler. */
  private static final Handler consoleHandler = new ConsoleHandler();

  @BeforeAll
  static void clearSer() {
    logger.setLevel(Level.OFF);
    consoleHandler.setLevel(Level.OFF);
    logger.addHandler((consoleHandler));

    File ser = new File("tags.ser");
    if (ser.exists()) {
      if (!ser.delete()) {
        logger.log(Level.WARNING, "Deleting ser file failed!");
      }
    }
  }

  @BeforeEach
  void setUp() throws IOException, ClassNotFoundException {
    tagManger = new TagManager();
    TagManager.clear();
  }

  @AfterEach
  void tearDown() {
    File ser = new File("tags.ser");
    if (ser.exists()) {
      if (!ser.delete()) {
        logger.log(Level.WARNING, "Deleting ser file failed!");
      }
    }
  }

  @Test
  void testEmptyGetTagList() {
    assertTrue(TagManager.getTagList().isEmpty());
  }

  @Test
  void testAddOneTag() throws IOException {
    TagManager.addTag("f4f");
    assertTrue(TagManager.getTagList().size() == 1
        && TagManager.getTagList().get(0).equals("f4f"));
  }

  @Test
  void testAddMultipleTags() throws IOException {
    TagManager.addTag("Rick");
    TagManager.addTag("Morty");
    TagManager.addTag("100years");
    assertAll(
        () -> assertEquals(3, TagManager.getTagList().size()),
        () -> assertTrue(TagManager.getTagList().get(0).equals("Rick")),
        () -> assertTrue(TagManager.getTagList().get(1).equals("Morty")),
        () -> assertTrue(TagManager.getTagList().get(2).equals("100years"))
    );
  }

  @Test
  void testRemoveOneTag() throws IOException {
    TagManager.addTag("university");
    TagManager.addTag("toronto");
    TagManager.removeTag("university");
    assertAll(
        () -> assertEquals(1, TagManager.getTagList().size()),
        () -> assertTrue(TagManager.getTagList().get(0).equals("toronto"))
    );
  }

  @Test
  void testRemoveMultipleTags() throws IOException {
    TagManager.addTag("university");
    TagManager.addTag("of");
    TagManager.addTag("toronto");
    TagManager.removeTag("university");
    TagManager.removeTag("toronto");
    assertAll(
        () -> assertEquals(1, TagManager.getTagList().size()),
        () -> assertTrue(TagManager.getTagList().get(0).equals("of"))
    );
  }

  @Test
  void testReadFromFile() throws IOException, ClassNotFoundException {
    TagManager.addTag("a tag");
    TagManager.addTag("another tag");
    TagManager.addTag("the other tag");
    TagManager.clear();
    tagManger.readFromFile();
    assertAll(
        () -> assertEquals(3, TagManager.getTagList().size()),
        () -> assertTrue(TagManager.getTagList().get(0).equals("a tag")),
        () -> assertTrue(TagManager.getTagList().get(1).equals("another tag")),
        () -> assertTrue(TagManager.getTagList().get(2).equals("the other tag"))
    );
  }

  @Test
  void testSaveToFile() throws IOException {
    TagManager.addTag("wonder woman");
    TagManager.addTag("batman");
    TagManager.addTag("superman");
    TagManager.saveToFile();
    File ser = new File("tags.ser");
    assertTrue(ser.exists() && ser.isFile());
  }
}
