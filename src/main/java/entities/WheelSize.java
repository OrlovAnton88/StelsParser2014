package entities;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by antonorlov on 06/12/14.
 */
public enum WheelSize {

    TWELWE("12"), FOURTEEN("14"), SIXTEEN("16"), EIGHTTEEN("18"), TWENTY("20"), TWENTYFOUR("24"),
    TWENTYSIX("26"), TWENTYSEVENANDHALF("27.5"), TWENTYEIGTH("28"), TWENTYNINE("29");


    private static final Map<String, WheelSize> map = new HashMap<String, WheelSize>();

    static {
        for (WheelSize d : WheelSize.values())
            map.put(d.getSize(), d);
    }

    private String size;

    private  WheelSize(String s) {
        size = s;
    }

    public static WheelSize getSizeByValue(String value) {
        return map.get(value);
    }

    public String getSize() {
        return size;
    }
}
