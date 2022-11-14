import static java.lang.System.out;

public class MainApp {
    static float[] numbersA = {12, 9, 11, 17, 21, 16, 23, 14, 15, 24};
    static float[] numbersB = {5, 8, 11, 9, 12, 6, 8, 7, 13, 11};
    static float nA = numbersA.length;
    static float nB = numbersB.length;
    static float N = nA + nB;
    static float k = 2;

    public static void main(String[] args) {
        float meanA = getMean(numbersA);
        float meanB = getMean(numbersB);
        float totalMean = getMean(new float[]{meanA, meanB});

        out.print("A: ");
        for (float f : numbersA) out.print((int) f + " ");

        out.print("\nB: ");
        for (float f : numbersB) out.print((int) f + " ");

        out.println("\nk of A: " + nA);
        out.println("k of B: " + nB);
        out.println("N of Study: " + N);
        out.println("mean of A: " + meanA);
        out.println("mean of B: " + meanB);
        out.println("total mean: " + totalMean);
        out.println();
        printCalculation(meanA, meanB, totalMean);
    }

    public static void printCalculation(float meanA, float meanB, float totalMean) {
        int counterA = 0;
        int counterB = 0;
        float ssFactor = 0;
        float ssError = 0;
        float ssTotal = 0;

        out.println("Set A Header\t Yij\t M,Yi\t M,EY");
        for (float number : numbersA) {
            float ssfValue = (float) Math.pow((meanA - totalMean), 2);
            float sseValue = (float) Math.pow(number - meanA, 2);
            float sstValue = (float) Math.pow(number - totalMean, 2);

            ssFactor += ssfValue;
            ssError += sseValue;
            ssTotal += sstValue;

            counterA += 1;
            out.printf("Set A Time %s: \t %s\t %s\t %s\t %s\t %s\t %s\t %s\t %s\t %s%n",
                    counterA, number, meanA, totalMean, meanA - totalMean, number - totalMean,
                    number - meanA, ssfValue, sseValue, sstValue);
        }

        out.println("\nSet B Header\t Yij\t M,Yi\t M,EY");
        for (float number : numbersB) {
            float ssfValue = (float) Math.pow(meanB - totalMean, 2);
            float sseValue = (float) Math.pow(number - meanB, 2);
            float sstValue = (float) Math.pow(number - totalMean, 2);

            ssFactor += ssfValue;
            ssError += sseValue;
            ssTotal += sstValue;

            counterB += 1;
            out.printf("Set B Time %s: \t %s\t %s\t %s\t %s\t %s\t %s\t %s\t %s\t %s%n",
                    counterB, number, meanB, totalMean, meanB - totalMean, number - totalMean,
                    number - meanB, ssfValue, sseValue, sstValue);
        }

        float dFFactor = k - 1;
        float dFError = N - k;
        float dfTotal = N - 1;

        out.printf("%nSums of Squares:    \t SS(Factor): %s\t SS(Error): %s\t SS(Total): %s",ssFactor,ssError,ssTotal);
        out.printf("%nDegrees of Freedom: \t dF(Factor): %s\t dF(Error): %s\t dF(Total): %s", dFFactor, dFError, dfTotal);

        float meanSqrFactor = ssFactor / dFFactor;
        float meanSqrError = ssError / dFError;
        float fRatio = meanSqrFactor / meanSqrError;

        out.printf("%nMean Sqares/F Ratio:\t mSq(Factor): %s\t mSq(Error): %s\t F Ratio: %s",meanSqrFactor,meanSqrError,fRatio);

    }

    public static float getMean(float[] numberList) {
        float divisor = numberList.length;
        float total = 0;
        for (float number : numberList) total += number;
        return total / divisor;
    }
}
