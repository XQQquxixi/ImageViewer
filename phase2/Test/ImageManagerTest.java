
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import Model.Image;
import Model.ImageManager;

import java.io.File;
import java.io.IOException;
import java.util.*;

class ImageManagerTest {

  private File f1, f2, f3, f4;
  private Image i1, i2, i3, i4;

  @BeforeAll
  static void clearSer() {
    File ser = new File("images.ser");
    if (ser.exists()) {
      ser.delete();
    }
  }

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
    ImageManager im = new ImageManager();
  }

  @AfterEach
  void tearDown() {
    File ser = new File("images.ser");
    if (ser.exists()) {
      ser.delete();
    }
  }

  @Test
  void addOnePic() throws IOException, ClassNotFoundException {
    ImageManager.add(i1);
    assertAll(
        () -> assertEquals(1, ImageManager.getImages().size()),
        () -> assertTrue(ImageManager.getImages().containsKey(f1)),
        () -> assertTrue(ImageManager.getImages().get(f1).equals(i1))
    );
  }

  @Test
  void addMultiplePic() throws IOException, ClassNotFoundException {
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

  @Test
  void testGetImages() throws IOException {
    ImageManager.add(i4);
    Map<File, Image> expected = new HashMap<>();
    expected.put(f4, i4);
    assertEquals(expected, ImageManager.getImages());
  }

  @Test
  void testReadFromFile() throws ClassNotFoundException, IOException {
    ImageManager.add(i1);
    ImageManager.add(i2);

    ImageManager.getImages().clear();
    ImageManager.readFromFile();
    assertAll(
        () -> assertEquals(i1, ImageManager.getImages().get(f1)),
        () -> assertEquals(i2, ImageManager.getImages().get(f2))
    );
  }

}
