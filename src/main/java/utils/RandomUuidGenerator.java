package utils;

import java.util.UUID;

/**
 * Created by rishabhshukla on 05/01/18.
 */
public class RandomUuidGenerator implements UuidGenerator {
    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }
}
