package utils;

import java.io.IOException;

/**
 * When dealing with <code>Consumer</code> instances that may throw exceptions,
 * we need to handle those exceptions appropriately. Since <code>Consumer</code>
 * is a functional interface that does not declare any checked exceptions, we
 * cannot directly use a <code>Consumer</code> that throws a checked exception
 * without wrapping it.
 */
@FunctionalInterface
public interface CheckedConsumer <T>{
    void accept(T t) throws IOException;
}
