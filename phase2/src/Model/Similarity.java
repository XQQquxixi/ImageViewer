package Model;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Similarity {

    /**
     * Returns the cosine value between two vectors.
     * @param vectorA a vector
     * @param vectorB a vector with same length as vectorA
     * @return the cosine value between two vectors.\
     */
    public static double cosineSimilarity(Vector<Integer> vectorA, Vector<Integer> vectorB) {
        double dotProduct = 0;
        double normA = 0;
        double normB = 0;
        for (int i = 0; i < vectorA.size(); i++) {
            dotProduct += vectorA.get(i) * vectorB.get(i);
            normA += Math.pow(vectorA.get(i), 2);
            normB += Math.pow(vectorB.get(i), 2);
        }
        // assign the cosine value between zero vector and the other vector
        // with 0.0 for purpose of comparing in this context
        if(normA == 0|normB == 0) {
            return 0.0;
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    /**
     * Return the vector representation of an image.
     * @param image the image of imterest
     * @return the vector representation of an image
     */
    public static Vector<Integer> getVector(Image image) {
        ArrayList<String> benchmark = TagManager.tagList;
        int size = benchmark.size();
        Vector<Integer> vector = new Vector<>();
        for(int i = 0; i < size; i++) {
           if (image.getCurrentTags().contains(benchmark.get(i))) {
               vector.add(1);
            } else {
               vector.add(0);
           }
        }
        return vector;
    }

    /**
     * Return an ArrayList of images sorted in a way where first one is most related to selected image whose file path
     * is path, and last one is least related.
     * @param path the file path of selected image
     * @return an sorted ArrayList of images
     */
    public static ArrayList<Image> getSimilarImages(String path) {
        HashMap<Image, Double> similar = new HashMap<>();
        Map<File, Image> images = new HashMap<>(ImageManager.getImages());
        Vector v = getVector(images.get(new File(path)));
        images.remove(new File(path));
        for (Image i: images.values()) {
            double n = cosineSimilarity(v, getVector(i));
            similar.put(i, n);
        }
        Map sorted = getSorted(similar);
        return mapToArray(sorted);
    }


    /**
     * Return a sorted map based on values of unsortedMap in ascending order.
     * @param unsortedMap the map to be sorted
     * @return a sorted map
     */
    public static Map<Image, Double> getSorted(HashMap<Image, Double> unsortedMap) {
        List<Map.Entry<Image, Double>> list = new LinkedList<Map.Entry<Image, Double>>(unsortedMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<Image, Double>>()
        {
            public int compare(Map.Entry<Image, Double> o1,
                               Map.Entry<Image, Double> o2)
            {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<Image, Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Image, Double> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }


    /**
     * Return an ArrayList of the key of the map.
     * @param map the map to be converted to ArrayList
     * @return an ArrayList of the key of the map
     */
    public static ArrayList<Image> mapToArray(Map<Image,Double> map) {
        ArrayList<Image> result = new ArrayList<>();
        for (Map.Entry<Image,Double> entry : map.entrySet()) {
            result.add(entry.getKey());
        }
        return result;
    }
}
