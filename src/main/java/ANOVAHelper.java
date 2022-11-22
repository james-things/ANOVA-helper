import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.FileReader;
import java.text.DecimalFormat;

import static java.lang.System.out;

public class ANOVAHelper {
    static float k = 2;  // K of study (# groups)
    private static final DecimalFormat trimDigits = new DecimalFormat("#.##");


    public static void main(String[] args) throws Exception {
        Object dataReader = new JSONParser().parse(new FileReader("data/values.json"));
        JSONObject dataJSON = (JSONObject) dataReader;
        Object[] dataKeys = dataJSON.keySet().toArray();

        for (Object dataKey : dataKeys) {
            JSONObject questionSet = (JSONObject) dataJSON.get(dataKey);
            //out.println(oo.toString());
            Object question = questionSet.get("question");
            Object values1 = questionSet.get("values1");
            Object values2 = questionSet.get("values1");
            out.println(values1);
            out.println(values2);
            out.println(question);
            out.println();
        }

    }

    public static void processSet(JSONObject researchSet) {
        float[] numbersA = {5, 8, 11, 9, 12, 6, 8, 7, 13, 11};  // set B
        float[] numbersB = {5, 8, 11, 9, 12, 6, 8, 7, 13, 11};  // set B
        float nA = numbersA.length;  // n of Set A (# participants)
        float nB = numbersB.length;  // n of Set B (# participants)
        float N = nA + nB;  // N of study (# total participation)

        float meanA = getMean(numbersA);  // get A mean
        float meanB = getMean(numbersB);  // get B mean
        float totalMean = getMean(new float[]{meanA, meanB});  // get study mean

        // print study details
        out.print("A: ");
        for (float f : numbersA) out.print((int) f + " ");
        out.print("\nB: ");
        for (float f : numbersB) out.print((int) f + " ");
        out.println("\nn of A: " + nA);
        out.println("n of B: " + nB);
        out.println("N of Study: " + N);
        out.println("mean of A: " + meanA);
        out.println("mean of B: " + meanB);
        out.println("total mean: " + totalMean);
        out.println();

        // print ANOVA table
        printANOVATable(meanA, meanB, totalMean, numbersA, numbersB, N);
    }

    // helper method written to perform one-way ANOVA and print the results
    public static void printANOVATable(float meanA, float meanB, float totalMean, float[] numbersA, float[] numbersB, float N) {
        int counterA = 0;
        int counterB = 0;
        float ssFactor = 0;
        float ssError = 0;
        float ssTotal = 0;

        out.println("Set A Header\tYij\t Mean Yi\tMean Y ");
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
            out.printf("Set A Time %s: \t %s\t %s\t %s\t %s\t %s\t %s\t %s\t %s\t %s%n",
                    counterA, number, meanA, totalMean, trimDigits.format(meanA - totalMean),
                    trimDigits.format(number - totalMean), trimDigits.format(number - meanA),
                    trimDigits.format(ssfValue), trimDigits.format(sseValue), trimDigits.format(sstValue));
        }

        out.println("\nSet B Header\tYij\t Mean Yi\tMean Y ");
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
            out.printf("Set B Time %s: \t %s\t %s\t %s\t %s\t %s\t %s\t %s\t %s\t %s%n",
                    counterB, number, meanB, totalMean, trimDigits.format(meanB - totalMean),
                    trimDigits.format(number - totalMean), trimDigits.format(number - meanB),
                    trimDigits.format(ssfValue), trimDigits.format(sseValue), trimDigits.format(sstValue));
        }

        // calculate degrees of freedom
        float dFFactor = k - 1;
        float dFError = N - k;
        float dfTotal = N - 1;

        // output sum of squares and degrees of freedom
        out.printf("%nSums of Squares:    \t SS(Factor): %s\t SS(Error): %s\t SS(Total): %s",
                trimDigits.format(ssFactor),trimDigits.format(ssError),trimDigits.format(ssTotal));
        out.printf("%nDegrees of Freedom: \t dF(Factor): %s\t dF(Error): %s\t dF(Total): %s",
                trimDigits.format(dFFactor), trimDigits.format(dFError), trimDigits.format(dfTotal));

        // calculate mean squares and f ratio
        float meanSqrFactor = ssFactor / dFFactor;
        float meanSqrError = ssError / dFError;
        float fRatio = meanSqrFactor / meanSqrError;

        // output mean squares and f ratio
        out.printf("%nMean Sqares/F Ratio:\t mSq(Factor): %s\t mSq(Error): %s\t F Ratio: %s",
                trimDigits.format(meanSqrFactor),trimDigits.format(meanSqrError),trimDigits.format(fRatio));

    }

    // helper method to return the mean of a float array
    public static float getMean(float[] numberList) {
        float divisor = numberList.length; // get count of digits
        float total = 0;
        for (float number : numberList) total += number; // sum the array
        return total / divisor; // return the mean
    }
}
