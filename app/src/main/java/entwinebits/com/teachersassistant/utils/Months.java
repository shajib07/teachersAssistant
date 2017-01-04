package entwinebits.com.teachersassistant.utils;

/**
 * Created by shajib on 1/4/2017.
 */
public enum Months {
    JAN(1), FEB(2), MAR(3), APR(4), MAY(5), JUN(6), JUL(7), AUG(8), SEP(9), OCT(10), NOV(11), DEC(12);

    int monthOrdinal = 0;

    Months(int ord) {
        this.monthOrdinal = ord;
    }

    public static Months get(int ord) {
        for (Months m : Months.values()) {
            if (m.monthOrdinal == ord) {
                return m;
            }
        }
        return null;
    }
}

