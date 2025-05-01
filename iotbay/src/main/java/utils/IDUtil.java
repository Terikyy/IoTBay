package utils;

public class IDUtil {
    public static Integer generateID() {
        return (int) (Math.random() * 1000000);
    }
}
