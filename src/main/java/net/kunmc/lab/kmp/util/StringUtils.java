package net.kunmc.lab.kmp.util;

public class StringUtils {
    public static String getTimeNotationPercentage(long current, long all) {
        StringBuffer bff = new StringBuffer();
        long sec = Math.round((float) all / 1000f);
        long min = sec / 60;
        long hr = min / 60;
        bff.append(getTimeNotation(current, hr != 0)).append("/").append(getTimeNotation(all));
        return bff.toString();
    }

    public static String getTimeNotation(long time) {
        long sec = Math.round((float) time / 1000f);
        long min = sec / 60;
        long hr = min / 60;
        return getTimeNotation(time, hr != 0);
    }

    public static String getTimeNotation(long time, boolean horus) {

        long sec = Math.round((float) time / 1000f);
        long min = sec / 60;
        long hr = min / 60;

        long minz = min - (hr * 60);
        long secz = sec - (min * 60);

        StringBuffer biff = new StringBuffer();
        if (horus) {
            biff.append(zeroNumber(Math.toIntExact(hr))).append(":").append(zeroNumber(Math.toIntExact(minz)));
        } else {
            biff.append(zeroNumber(Math.toIntExact(min)));
        }
        biff.append(":").append(zeroNumber(Math.toIntExact(secz)));
        return biff.toString();
    }

    private static String zeroNumber(int num) {
        String numst = String.valueOf(num);

        if (numst.length() == 1) {
            StringBuffer bff = new StringBuffer();
            bff.append(0).append(num);
            return bff.toString();
        }

        return numst;
    }
}
