package Model;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Similarity {

    public static double cosineSimilarity(Vector<Integer> vectorA, Vector<Integer> vectorB) {
        double dotProduct = 0;
        double normA = 0;
        double normB = 0;
        for (int i = 0; i < vectorA.size(); i++) {
            dotProduct += vectorA.get(i) * vectorB.get(i);
            normA += Math.pow(vectorA.get(i), 2);
            normB += Math.pow(vectorB.get(i), 2);
        }
        try{
            return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
        } catch (ArithmeticException e){
            System.out.println("no recommendation");
        }
        return 0.0;
    }

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

    public static ArrayList<Image> getSimilarImages(String path) {
        HashMap<Image, Double> similar = new HashMap<>();
        Map<File, Image> images = ImageManager.getImages();
        Vector v = getVector(images.get(new File(path)));
        images.remove(new File(path));
        for (Image i: images.values()) {
            double n = cosineSimilarity(v, getVector(i));
            similar.put(i, n);
        }
        Map sorted = getSorted(similar);
        return mapToArray(sorted);
    }



    public static Map<Image, Double> getSorted(HashMap<Image, Double> unsortedMap) {
        Map<Image, Double> sortedMap = sortByComparator(unsortedMap);
        return sortedMap;
    }



    private static Map<Image, Double> sortByComparator(HashMap<Image, Double> unsortedMap) {

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

    public static ArrayList<Image> mapToArray(Map<Image,Double> map) {
        ArrayList<Image> result = new ArrayList<>();
        for (Map.Entry<Image,Double> entry : map.entrySet()) {
            result.add(entry.getKey());
        }
        return result;
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {

        ImageManager im = new ImageManager();
        TagManager tm = new TagManager();
        //im.addTag("/Users/QiqiXu/phase2/group_0473/phase2/src/./C @csc.jpg", "258");
        System.out.println(im);
        System.out.println(tm);
        String p = "/Users/QiqiXu/phase2/group_0473/phase2/src/./C @csc @258.jpg";
        //Image i = new Image(new File(p));
        ArrayList<Image> result = getSimilarImages(p);
        //System.out.println(tm);
        System.out.println(result);








    }
}
