package org.pojo.tester.field.collection;

import java.util.Arrays;
import java.util.stream.Stream;

public class StreamValueChanger extends CollectionFieldValueChanger<Stream<?>> {

    @Override
    public boolean areDifferentValues(final Stream<?> sourceValue, final Stream<?> targetValue) {
        if (sourceValue == targetValue) {
            return false;
        }
        if (sourceValue == null || targetValue == null) {
            return true;
        } else {
            final Object[] sourceValuesArray = sourceValue.toArray();
            final Object[] targetValuesArray = targetValue.toArray();
            return !Arrays.deepEquals(sourceValuesArray, targetValuesArray);
        }
    }

    @Override
    protected Stream<?> increaseValue(final Stream<?> value, final Class<?> type) {
        return value != null
               ? null
               : Stream.empty();
    }
}
