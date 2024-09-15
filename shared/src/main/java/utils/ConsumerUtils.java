package utils;

import java.util.function.Consumer;

public class ConsumerUtils {
    public static <T> Consumer<T> wrapCheckedConsumer(CheckedConsumer<T> checkedConsumer) {
        return t -> {
            try {
                checkedConsumer.accept(t);
            } catch (Exception e) {
                System.err.println("Exception occurred: " + e.getMessage());
            }
        };
    }
}
