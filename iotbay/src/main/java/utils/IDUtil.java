package utils;

public class IDUtil {
    public static Integer generateID() {
        return (int) (Math.random() * Integer.MAX_VALUE);
    }
}
