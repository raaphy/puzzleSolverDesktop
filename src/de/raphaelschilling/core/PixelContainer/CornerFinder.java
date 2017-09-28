package de.raphaelschilling.core.PixelContainer;

import java.util.ArrayList;
import java.util.Arrays;

public class CornerFinder {
    public static long count = 0;

    public static int[] findCorners(ArrayList<BorderPixel> borderList, int matrixWidth, int matrixHeight) {
        float[] accuracyBorder = new float[borderList.size()];
        for (int i = 0; i < borderList.size(); i++) {
            accuracyBorder[i] = calcCorner(i, borderList, matrixWidth, matrixHeight);
        }
        int cornerAmount = 4;
        int[] result = new int[cornerAmount];
        for (int i = 0; i < cornerAmount; i++) {
            float lowestError = Float.MAX_VALUE;
            int id = -1;
            for (int j = 0; j < accuracyBorder.length; j++) {
                if (accuracyBorder[j] <= lowestError) {
                    id = j;
                    lowestError = accuracyBorder[j];
                }
            }
            result[i] = id;
            borderList.get(id).accuracy = 1;
            accuracyBorder[id] = Float.MAX_VALUE;
            int k = id - accuracyBorder.length / 8;
            for (; k < id + accuracyBorder.length / 8; k++) {
                accuracyBorder[Math.floorMod(k, accuracyBorder.length)] = Float.MAX_VALUE;
            }
        }
        Arrays.sort(result);
        return result;
    }

    private static float calcCorner(int borderIndex, ArrayList<BorderPixel> borderList, int matrixWidth, int matrixHeight) {
        int widthHalf = matrixWidth / 2;
        int heightHalf = matrixHeight / 2;
        int cornerDepth = borderList.size() / 32;
        final int x = borderList.get(borderIndex).x;
        final int y = borderList.get(borderIndex).y;
        float angularBefore = angularBetweenBorderListEntrys(borderIndex, borderIndex - cornerDepth, borderList);
        float angularAfter = angularBetweenBorderListEntrys(borderIndex, borderIndex + cornerDepth, borderList);
        float cornerAngular = (float) Math.abs(angularDifference(angularBefore, angularAfter) - Math.PI / 2);
        if (cornerAngular > Math.PI / 8) {
            return Float.MAX_VALUE;
        }
        float positionAngular = calcAngularOfVector(x - widthHalf, y - heightHalf);
        float beforeAngularDrift = angularDifference((float) (positionAngular - 3f / 4f * Math.PI), angularBefore);
        float afterAngularDrift = angularDifference((float) (positionAngular + 3f / 4f * Math.PI), angularAfter);
        if (beforeAngularDrift > Math.PI / 4 || afterAngularDrift > Math.PI / 4) {
            return Float.MAX_VALUE;
        }
        float error = 0;
        for (int i = -1; i > -cornerDepth; i--) {
            float angular = angularBetweenBorderListEntrys(borderIndex, borderIndex + i, borderList);
            error += Math.abs(angularDifference(angular, angularBefore));

        }
        //  if(error/edgeDepth>0.2)
        //      return Float.MAX_VALUE;
        for (int i = 1; i < cornerDepth; i++) {
            float angular = angularBetweenBorderListEntrys(borderIndex, borderIndex + i, borderList);
            error += Math.abs(angularDifference(angular, angularAfter));

        }
        // if(error/edgeDepth>0.4)
        //     return Float.MAX_VALUE;
        return ((error / cornerDepth) + cornerAngular + beforeAngularDrift + afterAngularDrift);
    }

    private static float angularBetweenBorderListEntrys(int index1, int index2, ArrayList<BorderPixel> borderList) {
        int x = borderList.get(Math.floorMod(index2, borderList.size())).x - borderList.get(Math.floorMod(index1, borderList.size())).x;
        int y = borderList.get(Math.floorMod(index2, borderList.size())).y - borderList.get(Math.floorMod(index1, borderList.size())).y;
        return calcAngularOfVector(x, y);
    }

    private static float angularDifference(float angular, float angularBefore) {
        if (Math.abs(angular - angularBefore) > Math.PI) {
            return (float) Math.abs(2 * Math.PI - Math.abs(angular - angularBefore));
        } else {
            return Math.abs(angular - angularBefore);
        }
    }

    private static float calcAngularOfVector(int x, int y) {
        count++;
        return (float) Math.atan2(y, x);
    }


}
