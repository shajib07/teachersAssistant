package entwinebits.com.teachersassistant.utils;

/**
 * Created by shajib on 1/4/2017.
 */
public enum Days {
    SAT(1), SUN(2), MON(3), TUE(4), WED(5), THU(6), FRI(7);

    int dayOrdinal = 0;

    Days(int ord) {
        this.dayOrdinal = ord;
    }

    public static Days get(int ord) {
        for (Days d : Days.values()) {
            if (d.dayOrdinal == ord) {
                return d;
            }
        }
        return null;
    }
}

