package math.linear.basic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class MathUtils {
    public static final int NOT_ASSIGNED = -1;
    /*
        Returns the index of the solution variable with the biggest fraction.
         */
    public static int getBiggestFractionIndex(List<BigDecimal> solution, int precision) {
        BigDecimal maxfraction = BigDecimal.ZERO;
        int index = NOT_ASSIGNED;
        for (int k = 1; k < solution.size(); k++) {
            BigDecimal value = solution.get(k).setScale(precision / 2, RoundingMode.HALF_UP);
            BigDecimal fraction = value.subtract(value.setScale(0, BigDecimal.ROUND_FLOOR));
            if (fraction.signum() > 0 && fraction.compareTo(maxfraction) > 0) {
                maxfraction = fraction;
                index = k;
            }

        }
        return index;
    }

    public static BigDecimal getFraction(BigDecimal value, int precision){
        return value.subtract(value.setScale(0, RoundingMode.FLOOR))
                .setScale(precision/2, RoundingMode.HALF_UP);
    }


    public static boolean isInteger(BigDecimal value, int precision){
        BigDecimal fraction = getFraction(value, precision);
        return fraction.signum() == 0;
    }
}