import Model.Image;
import Model.ImageManager;
import Model.TagManager;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static Model.Similarity.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimilarityTest {

    @Test
    public void testCosineSimilarityWithSameVectors(){
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

    @Test
    public void testCosineSimilarityWithZeroVectors() {               // ?????????????????????????
        Vector a = new Vector();
        a.add(0);
        a.add(0);
        a.add(0);
        Vector b = new Vector();
        b.add(0);
        b.add(0);
        b.add(0);
        double n = cosineSimilarity(a, b);
        assertEquals("NaN",n);
    }

    @Test
    public void testCosineSimilarityWithDifferentVectors() {
        Vector a = new Vector();
        a.add(1);
        a.add(0);
        a.add(1);
        Vector b = new Vector();
        b.add(1);
        b.add(0);
        b.add(0);
        double n = cosineSimilarity(a, b);
        assertEquals(0.7071067811865475,n);
    }

    @Test
    public void testGetVector() throws IOException, ClassNotFoundException {        // how to deal with tm and im?
        TagManager tm = new TagManager();
        ImageManager im = new ImageManager();
        tm.addTag("hello");
        tm.addTag("world");
        tm.addTag("csc");
        tm.addTag("207");
        String p = "/Users/QiqiXu/phase2/group_0473/phase2/src/./A.jpg";
        String p2 = "/Users/QiqiXu/phase2/group_0473/phase2/src/./A @hello.jpg";
        Image i = new Image(new File(p));
        im.addTag(p, "hello");
        im.addTag(p2, "world");
        Vector v = getVector(i);
        Vector v2 = new Vector();
        v2.add(1);
        v2.add(1);
        v2.add(0);
        v2.add(0);
        assertEquals(v2,v);
    }

    @Test
    public void testGetSimilarImagesHasSameTagsAndDifferentTags() throws IOException {      //how to deal with tm and im?

    }

    @Test
    public void testGetSimilarImagesHasMoreTagsAndDifferentTags() throws IOException {      //how to deal with tm and im?

    }

    @Test
    public void testGetSimilarImagesHasLessTagsAndDifferentTags() throws IOException {      //how to deal with tm and im?

    }

    @Test
    public void testGetSimilarImagesHasLessTagsAndSameTags() throws IOException {      //how to deal with tm and im?

    }

    @Test
    public void testGetSimilarImagesHasMoreTagsAndSameTags() throws IOException {      //how to deal with tm and im?

    }

    @Test
    public void testGetSimilarImagesReturnSize() throws IOException {      //how to deal with tm and im?

    }


    @Test
    public void testSortByComparatorWithDifferentDoubles() throws IOException {
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

    @Test
    public void testSortByComparatorWithSomeSameDoubles() throws IOException {
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


    @Test
    public void testMapToArray() throws IOException {
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
        assertEquals(i1, re.get(0));
    }


}
