import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import static java.lang.System.out;

public class ANOVAHelper {
    static float k = 2;  // K of study (# groups)
    private static final DecimalFormat trimDigits = new DecimalFormat("#.##");


    public static void main(String[] args) throws Exception {
        Object dataReader = new JSONParser().parse(new FileReader("data/values.json"));
        JSONObject dataJSON = (JSONObject) dataReader;
        Object[] dataKeys = dataJSON.keySet().toArray();

        out.println("Study Summarized Results - Group 1");

        for (Object dataKey : dataKeys) {
            JSONObject questionSet = (JSONObject) dataJSON.get(dataKey);
            String question = questionSet.get("question").toString();
            JSONArray values1 = (JSONArray) questionSet.get("values1");
            JSONArray values2 = (JSONArray) questionSet.get("values2");

            Iterator<Long> iterator1 = values1.iterator();
            Iterator<Long> iterator2 = values2.iterator();
            ArrayList<Float> list1 = new ArrayList<>();
            ArrayList<Float> list2 = new ArrayList<>();
            while(iterator1.hasNext()) list1.add(iterator1.next().floatValue());
            while(iterator2.hasNext()) list2.add(iterator2.next().floatValue());

            Float[] numbers1 = list1.toArray(new Float[0]);  // set A
            Float[] numbers2 = list2.toArray(new Float[0]);  // set B

            out.println("==================================================================================================");
            out.println("Research Question: " + question);
            out.println("==================================================================================================");
            out.println("Responses, Session 1: " + values1);
            out.println("Responses, Session 2: " + values2);
            out.println("------------------------------------------------");
            aNOVAProcessSet(numbers1, numbers2);
        }

        out.println("Study Summarized Results - End");
    }

    public static void aNOVAProcessSet(Float[] numbersA, Float[] numbersB) {
        float nA = numbersA.length;  // n of Set A (# participants)
        float nB = numbersB.length;  // n of Set B (# participants)
        float N = nA + nB;  // N of study (# total participation)

        float meanA = getMean(numbersA);  // get A mean
        float meanB = getMean(numbersB);  // get B mean
        float totalMean = getMean(new Float[]{meanA, meanB});  // get study mean

        out.println("ANOVA Calculation:");
        // print question details
        out.print("G1: ");
        for (float f : numbersA) out.print((int) f + " ");
        out.print("\nG2: ");
        for (float f : numbersB) out.print((int) f + " ");
        out.println("\nn of G1: " + nA + "\t\tn of G2: " + nB + "\t\tN of Study: " + N);
        out.println("mean of G1: " + meanA + "\t\tmean of G2: " + meanB + "\t\ttotal mean: " + totalMean);

        // print ANOVA table
        printANOVATable(meanA, meanB, totalMean, numbersA, numbersB, N);
    }

    // helper method written to perform one-way ANOVA and print the results
    public static void printANOVATable(float meanA, float meanB, float totalMean, Float[] numbersA, Float[] numbersB, float N) {
        int counterA = 0;
        int counterB = 0;
        float ssFactor = 0;
        float ssError = 0;
        float ssTotal = 0;

        out.println("------------------------------------------------");
        out.print("Set G1 Header \t\t Yij \t Mean Yi \t Mean Y ");
        for (float number : numbersA) {
            // initialize sum of squares data
            float ssfValue = (float) Math.pow((meanA - totalMean), 2);
            float sseValue = (float) Math.pow(number - meanA, 2);
            float sstValue = (float) Math.pow(number - totalMean, 2);

            // begin calculating sum of squares
            ssFactor += ssfValue;
            ssError += sseValue;
            ssTotal += sstValue;

            counterA += 1;
            out.printf("%nSet G1 Time %2s: \t %s\t %s\t %s\t %s\t %s\t %s\t %s\t %s\t %s",
                    counterA, number, meanA, totalMean, trimDigits.format(meanA - totalMean),
                    trimDigits.format(number - totalMean), trimDigits.format(number - meanA),
                    trimDigits.format(ssfValue), trimDigits.format(sseValue), trimDigits.format(sstValue));
        }
        out.println("\n------------------------------------------------");
        out.print("Set G2 Header \t\t Yij \t Mean Yi \t Mean Y ");
        for (float number : numbersB) {
            // initialize sum of squares data
            float ssfValue = (float) Math.pow(meanB - totalMean, 2);
            float sseValue = (float) Math.pow(number - meanB, 2);
            float sstValue = (float) Math.pow(number - totalMean, 2);

            // finish calculating sum of squares
            ssFactor += ssfValue;
            ssError += sseValue;
            ssTotal += sstValue;

            counterB += 1;
            out.printf("%nSet G2 Time %2s: \t %s\t %s\t %s\t %s\t %s\t %s\t %s\t %s\t %s",
                    counterB, number, meanB, totalMean, trimDigits.format(meanB - totalMean),
                    trimDigits.format(number - totalMean), trimDigits.format(number - meanB),
                    trimDigits.format(ssfValue), trimDigits.format(sseValue), trimDigits.format(sstValue));
        }

        // calculate degrees of freedom
        float dFFactor = k - 1;
        float dFError = N - k;
        float dfTotal = N - 1;

        // output sum of squares and degrees of freedom
        out.println("\n------------------------------------------------");
        out.printf("Sums of Squares:    \t SS(Factor): %s\t SS(Error): %s\t SS(Total): %s",
                trimDigits.format(ssFactor),trimDigits.format(ssError),trimDigits.format(ssTotal));
        out.printf("%nDegrees of Freedom: \t dF(Factor): %s\t dF(Error): %s\t dF(Total): %s",
                trimDigits.format(dFFactor), trimDigits.format(dFError), trimDigits.format(dfTotal));

        // calculate mean squares and f ratio
        float meanSqrFactor = ssFactor / dFFactor;
        float meanSqrError = ssError / dFError;
        float fRatio = meanSqrFactor / meanSqrError;

        // output mean squares and f ratio
        out.println("\n------------------------------------------------");
        out.printf("Mean Sqares/F Ratio:\t mSq(Factor): %s\t mSq(Error): %s\t F Ratio: %s",
                trimDigits.format(meanSqrFactor),trimDigits.format(meanSqrError),trimDigits.format(fRatio));

        out.println("\n==================================================================================================\n");
    }

    // helper method to return the mean of a float array
    public static float getMean(Float[] numberList) {
        float divisor = numberList.length; // get count of digits
        float total = 0;
        for (float number : numberList) total += number; // sum the array
        return total / divisor; // return the mean
    }
}
