import Model.Image;
import Model.ImageManager;
import Model.TagManager;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Model.Similarity.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimilarityTest {

    /* Initial String of test images. */
    private static String f1, f2, f3, f4;
    /* prefix of the path to the test images. */
    private String pre;
    /* A Logger. */
    private static final Logger logger = Logger.getLogger(Image.class.getName());
    /* A ConsoleHandler. */
    private static final Handler consoleHandler = new ConsoleHandler();

    /**
     * Before all test classes start, store all serializable files elsewhere.
     */
    @BeforeAll
    static void clearSer() {
        logger.setLevel(Level.OFF);
        consoleHandler.setLevel(Level.OFF);
        logger.addHandler((consoleHandler));
        //f1 = "pics/a.jpg";
        //String a = new File(f1).getAbsolutePath();
        //String pre = a.substring(0, a.lastIndexOf(File.separator) + 1);

        File ser = new File("./images.ser");
        if (ser.exists()) {
            if (!ser.renameTo(new File("./tempIm.ser"))) {
                logger.log(Level.WARNING, "Moving ser file failed!");
            }
        }

        File ser2 = new File("./tags.ser");
        if (ser2.exists()) {
            if (!ser2.renameTo(new File("./tempTags.ser"))) {
                logger.log(Level.WARNING, "Moving ser file failed!");
            }
        }

        File ser3 = new File("./logs.ser");
        if (ser3.exists()) {
            if (!ser2.renameTo(new File("./tempLogs.ser"))) {
                logger.log(Level.WARNING, "Moving ser file failed!");
            }
        }
    }

    /**
     * Before each class starts, assign file path and initialize ImageManager and TagManager.
     * @throws IOException if the class path is not updated
     * @throws IOException if stream to file with filePath cannot be written or closed
     */
    @BeforeEach
    void setUp() throws IOException, ClassNotFoundException {
        f1 = "pics/a.jpg";
        f2 = "pics/b.jpg";
        f3 = "pics/c.jpg";
        f4 = "pics/d.jpg";
        String a = new File(f1).getAbsolutePath();
        pre = a.substring(0, a.lastIndexOf(File.separator) + 1);
        ImageManager im = new ImageManager();
    }

    /**
     * Test cosine value between same vectors.
     */
    @Test
    void testCosineSimilarityWithSameVectors(){
        Vector a = new Vector();
        a.add(1);
        a.add(1);
        a.add(0);
        Vector b = new Vector();
        b.add(1);
        b.add(1);
        b.add(0);
        double n = cosineSimilarity(a, b);
        assertEquals(0.9999999999999998,n);
    }

    /**
     * Test cosine vector of between Zero vectors.
     */
    @Test
    void testCosineSimilarityWithZeroVectors() {
        Vector a = new Vector();
        a.add(0);
        a.add(0);
        a.add(0);
        Vector b = new Vector();
        b.add(0);
        b.add(0);
        b.add(0);
        double n = cosineSimilarity(a, b);
        assertEquals(0.0, n);
    }

    /**
     * Test cosine value between two different vectors.
     */
    @Test
    void testCosineSimilarityWithDifferentVectors() {
        Vector a = new Vector();
        a.add(1);
        a.add(0);
        a.add(1);
        Vector b = new Vector();
        b.add(1);
        b.add(0);
        b.add(0);
        double n = cosineSimilarity(a, b);
        assertEquals(0.7071067811865475, n);
    }

    /**
     * Test if an image can be reduced to a vector properly according to its tags.
     * @throws IOException if the class path is not updated
     * @throws IOException if stream to file with filePath cannot be written or closed
     */
    @Test
    void testGetVector() throws IOException, ClassNotFoundException {
        String f11 = pre + "a @hello.jpg";
        String f12 = pre + "a @hello @world.jpg";
        String f13 = pre + "a @hello @world @cs.jpg";
        String f41 = pre + "d @is.jpg";
        ImageManager.addTag(f1, "hello");
        ImageManager.addTag(f11, "world");
        ImageManager.addTag(f12, "cs");
        ImageManager.addTag(f4, "is");
        Vector v = getVector(new Image(new File(f13)));
        Vector v2 = new Vector();
        v2.add(1);
        v2.add(1);
        v2.add(1);
        v2.add(0);
        assertEquals(v2,v);
        ImageManager.renameImage(f41, "d");
        ImageManager.renameImage(f13, "a");

    }


    /**
     * Test if it returns b when a and b have same tags and c has different tags.
     * @throws IOException if the class path is not updated
     */
    @Test
    void testGetSimilarImagesHasSameTagsAndDifferentTags() throws IOException {
        String f11 = pre + "a @csc.jpg";
        String f12 = pre + "a @csc @207.jpg";
        String f21 = pre + "b @csc.jpg";
        String f22 = pre + "b @csc @207.jpg";
        String f31 = pre + "c @mat.jpg";
        ImageManager.addTag(f1, "csc");
        ImageManager.addTag(f11, "207");
        ImageManager.addTag(f2, "csc");
        ImageManager.addTag(f21, "207");
        ImageManager.addTag(f3, "mat");
        ArrayList<Image> re = getSimilarImages(f12);
        assertEquals("b @csc @207",re.get(0).getName());
        ImageManager.renameImage(f12, "a");
        ImageManager.renameImage(f22, "b");
        ImageManager.renameImage(f31, "c");

    }


    /**
     * Test if it returns b when a and b have some common tags and c has no tag.
     * @throws IOException if the class path is not updated
     */

    @Test
    void testGetSimilarImagesHasNoTagsAndPartlySameTags() throws IOException {
        String f11 = pre + "a @csc.jpg";
        String f12 = pre + "a @csc @207.jpg";
        String f21 = pre + "b @csc.jpg";
        ImageManager.addTag(f1, "csc");
        ImageManager.addTag(f11, "207");
        ImageManager.addTag(f2, "csc");
        ImageManager.checkKey(new File(f3));
        ArrayList<Image> re = getSimilarImages(f12);
        assertEquals("b @csc",re.get(0).getName());
        assertEquals("c",re.get(1).getName());
        ImageManager.renameImage(f12, "a");
        ImageManager.renameImage(f21, "b");
    }


    /**
     * Test if it returns b when b has more tags than a and c has different tags.
     * @throws IOException if the class path is not updated
     */

    @Test
    void testGetSimilarImagesHasMoreTagsAndDifferentTags() throws IOException {
        String f11 = pre + "a @csc.jpg";
        String f21 = pre + "b @csc.jpg";
        String f22 = pre + "b @csc @207.jpg";
        String f31 = pre + "c @mat.jpg";
        ImageManager.addTag(f1, "csc");
        ImageManager.addTag(f2, "csc");
        ImageManager.addTag(f21, "207");
        ImageManager.addTag(f3, "mat");
        ArrayList<Image> re = getSimilarImages(f11);
        assertEquals("b @csc @207",re.get(0).getName());
        ImageManager.renameImage(f11, "a");
        ImageManager.renameImage(f22, "b");
        ImageManager.renameImage(f31, "c");
    }

    /**
     * Test if it returns b when b has less tags than a and c has different tags.
     * @throws IOException if the class path is not updated
     */
    @Test
    void testGetSimilarImagesHasLessTagsAndDifferentTags() throws IOException {
        String f11 = pre + "a @csc.jpg";
        String f12 = pre + "a @csc @207.jpg";
        String f21 = pre + "b @csc.jpg";
        String f31 = pre + "c @mat.jpg";
        ImageManager.addTag(f1, "csc");
        ImageManager.addTag(f11, "207");
        ImageManager.addTag(f2, "csc");
        ImageManager.addTag(f3, "mat");
        ArrayList<Image> re = getSimilarImages(f12);
        assertEquals("b @csc",re.get(0).getName());
        ImageManager.renameImage(f12, "a");
        ImageManager.renameImage(f21, "b");
        ImageManager.renameImage(f31, "c");
    }


    /**
     * Test if it returns b when a and b has same tags and c has less tags.
     * @throws IOException if the class path is not updated
     */
    @Test
    void testGetSimilarImagesHasLessTagsAndSameTags() throws IOException {
        String f11 = pre + "a @csc.jpg";
        String f12 = pre + "a @csc @207.jpg";
        String f21 = pre + "b @207.jpg";
        String f22 = pre + "b @207 @csc.jpg";
        String f31 = pre + "c @207.jpg";
        ImageManager.addTag(f1, "csc");
        ImageManager.addTag(f11, "207");
        ImageManager.addTag(f2, "207");
        ImageManager.addTag(f21, "csc");
        ImageManager.addTag(f3, "207");
        ArrayList<Image> re = getSimilarImages(f12);
        assertEquals("b @207 @csc",re.get(0).getName());
        ImageManager.renameImage(f12, "a");
        ImageManager.renameImage(f22, "b");
        ImageManager.renameImage(f31, "c");
    }


    /**
     * Test if it returns d when a and d has same tags and c has more tags.
     * @throws IOException if the class path is not updated
     */
    @Test
    void testGetSimilarImagesHasMoreTagsAndSameTags() throws IOException {
        String f11 = pre + "a @csc.jpg";
        String f41 = pre + "d @csc.jpg";
        String f31 = pre + "c @csc.jpg";
        String f32 = pre + "c @csc @207.jpg";
        ImageManager.addTag(f1, "csc");
        ImageManager.addTag(f4, "csc");
        ImageManager.addTag(f3, "csc");
        ImageManager.addTag(f31, "207");
        ArrayList<Image> re = getSimilarImages(f11);
        assertEquals("d @csc",re.get(0).getName());
        ImageManager.renameImage(f11, "a");
        ImageManager.renameImage(f41, "d");
        ImageManager.renameImage(f32, "c");
    }


    /**
     * Test if the return size is exactly one less than the images size.
     * @throws IOException if the class path is not updated
     */
    @Test
    void testGetSimilarImagesReturnSize() throws IOException {
        String f11 = pre + "a @csc.jpg";
        String f21 = pre + "b @csc.jpg";
        String f31 = pre + "c @csc.jpg";
        String f32 = pre + "c @csc @207.jpg";
        ImageManager.addTag(f1, "csc");
        ImageManager.addTag(f2, "csc");
        ImageManager.addTag(f3, "csc");
        ImageManager.addTag(f31, "207");
        ArrayList<Image> re = getSimilarImages(f11);
        assertEquals(2,re.size());
        ImageManager.renameImage(f11, "a");
        ImageManager.renameImage(f21, "b");
        ImageManager.renameImage(f32, "c");
    }



    /**
     * Test if a hash map with Image as key and Double as value can be sorted based on values in ascending order.
     * @throws IOException if the class path is not updated
     */
    @Test
    void testSortByComparatorWithDifferentDoubles() throws IOException {
        HashMap<Image, Double> unsortedMap = new HashMap<>();
        unsortedMap.put(new Image(new File("a.jpg")), 0.9);
        unsortedMap.put(new Image(new File("b.jpg")), 0.4);
        unsortedMap.put(new Image(new File("c.jpg")), 0.2);
        unsortedMap.put(new Image(new File("d.jpg")), 0.6);
      Map<Image, Double> sorted = getSorted(unsortedMap);
        ArrayList<Double> result = new ArrayList<>();
        for (Map.Entry<Image,Double> entry : sorted.entrySet()) {
            result.add(entry.getValue());
        }
        assertEquals(new ArrayList<>(Arrays.asList(0.9, 0.6, 0.4, 0.2)), result);
    }

    /**
     * Test if the map with some same values can still be sorted properly.
     * @throws IOException if the class path is not updated
     */
    @Test
    void testSortByComparatorWithSomeSameDoubles() throws IOException {
        HashMap<Image, Double> unsortedMap = new HashMap<>();
        unsortedMap.put(new Image(new File("a.jpg")), 0.9);
        unsortedMap.put(new Image(new File("b.jpg")), 0.4);
        unsortedMap.put(new Image(new File("c.jpg")), 0.2);
        unsortedMap.put(new Image(new File("d.jpg")), 0.9);
      Map<Image, Double> sorted = getSorted(unsortedMap);
        ArrayList<Double> result = new ArrayList<>();
        for (Map.Entry<Image,Double> entry : sorted.entrySet()) {
            result.add(entry.getValue());
        }
        assertEquals(new ArrayList<>(Arrays.asList(0.9, 0.9, 0.4, 0.2)), result);
    }


    /**
     * Test if a map can be converted to an Array of its keys.
     * @throws IOException if the class path is not updated
     */
    @Test
    void testMapToArray() throws IOException {
        Map<Image, Double> map = new HashMap<>();
        Image i1 = new Image(new File("a.jpg"));
        Image i2 = new Image(new File("b.jpg"));
        Image i3 = new Image(new File("c.jpg"));
        Image i4 = new Image(new File("d.jpg"));
        map.put(i1, 0.9);
        map.put(i2, 0.4);
        map.put(i3, 0.2);
        map.put(i4, 0.9);
        ArrayList<Image> re = mapToArray(map);
        assertTrue(re.contains(i1));
        assertTrue(re.contains(i2));
        assertTrue(re.contains(i3));
        assertTrue(re.contains(i4));
    }

    /**
     * After each classes being tested, delete images.ser and tags.ser and clear tagList.
     */
    @AfterEach
    void tearDown() {
        File ser = new File("images.ser");
        if (ser.exists()) {
            if(!ser.delete()) {
                logger.log(Level.WARNING, "j");
            }
        }
        File ser2 = new File("tags.ser");
        if (ser2.exists()) {
            if(!ser2.delete()) {
                logger.log(Level.WARNING, "j");
            }
        }
        TagManager.clear();
    }

    /**
     * After all tests are done, restore original tags.ser and images.ser.
     * @throws IOException if the class path is not updated
     * @throws IOException if stream to file with filePath cannot be written or closed
     */
    @AfterAll
    static void restoreSer() throws IOException, ClassNotFoundException {
        logger.setLevel(Level.OFF);
        consoleHandler.setLevel(Level.OFF);
        logger.addHandler((consoleHandler));

        File ser = new File("./tempIm.ser");
        if (ser.exists()) {
            if (!ser.renameTo(new File("./images.ser"))) {
                logger.log(Level.WARNING, "Restoring ser file failed!");
            }
        }

        File ser2 = new File("./tempTags.ser");
        if (ser2.exists()) {
            if (!ser2.renameTo(new File("./tags.ser"))) {
                logger.log(Level.WARNING, "Restoring ser file failed!");
            }
        }

        File ser3 = new File("./tempLogs.ser");
        if (ser3.exists()) {
            if (!ser2.renameTo(new File("./logs.ser"))) {
                logger.log(Level.WARNING, "Restoring ser file failed!");
            }
        }
    }


}
