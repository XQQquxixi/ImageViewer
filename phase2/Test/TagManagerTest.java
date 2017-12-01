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

@SuppressWarnings("ResultOfMethodCallIgnored")
class TagManagerTest {

  /**
   * A TagManager.
   */
  private TagManager tagManger;

  /**
   * Delete the old serialized file for TagManager
   */
  @BeforeAll
  static void clearSer() {
    File ser = new File("tags.ser");
    if (ser.exists()) {
      ser.delete();
    }
  }

  /**
   * Assign tagManager with a new TagManager object, and clear it.
   *
   * @throws IOException if stream to TagManager's ser file cannot be written or closed
   * @throws ClassNotFoundException if the class path is not updated.
   */
  @BeforeEach
  void setUp() throws IOException, ClassNotFoundException {
    tagManger = new TagManager();
    TagManager.clear();
  }

  /**
   * Delete the tags.ser serialization file to remove history for the next test.
   */
  @AfterEach
  void tearDown() {
    File ser = new File("tags.ser");
    if (ser.exists()) {
      ser.delete();
    }
  }

  /**
   * Test returning the tagList of an empty TagManager.
   */
  @Test
  void testEmptyGetTagList() {
    assertTrue(TagManager.getTagList().isEmpty());
  }

  /**
   * Test adding one tag to the tagList of the TagManager.
   *
   * @throws IOException if stream to TagManager's ser file cannot be written or closed
   */
  @Test
  void testAddOneTag() throws IOException {
    TagManager.addTag("f4f");
    assertTrue(TagManager.getTagList().size() == 1
        && TagManager.getTagList().get(0).equals("f4f"));
  }

  /**
   * Test adding multiple tags to the tagList of the TagManager.
   * @throws IOException if stream to TagManager's ser file cannot be written or closed
   */
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

  /**
   * Test removing one tag from the tagList of the TagManager.
   * @throws IOException if stream to TagManager's ser file cannot be written or closed
   */
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

  /**
   * Test removing multiple tags from the tagList of the TagManager.
   * @throws IOException if stream to TagManager's ser file cannot be written or closed
   */
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

  /**
   * Test reading stored information from a serialized file and update the current
   * TagManager.
   * @throws IOException if stream to TagManager's ser file cannot be written or closed
   * @throws ClassNotFoundException if the class path is not updated
   */
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

  /**
   * Test saving the current TagManager information to a file.
   * @throws IOException if stream to TagManager's ser file cannot be written or closed
   */
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

