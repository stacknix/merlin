package io.stacknix.merlin.db.commons;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Utils {

    public static String[] @NotNull [] chunkArray(String @NotNull [] array, int chunkSize) {
        int chunkedSize = (int) Math.ceil((double) array.length / chunkSize);
        String[][] chunked = new String[chunkedSize][chunkSize];
        for (int index = 0; index < chunkedSize; index++) {
            String[] chunk = new String[chunkSize];
            System.arraycopy(array, index * chunkSize, chunk, 0, Math.min(chunkSize, array.length - index * chunkSize));
            chunked[index] = chunk;
        }
        return chunked;
    }

    @SuppressWarnings("unchecked")
    public static <T> T @NotNull [] mergeArray(T @NotNull [] array1, T @NotNull [] array2) {
        List<T> resultList = new ArrayList<>(array1.length + array2.length);
        Collections.addAll(resultList, array1);
        Collections.addAll(resultList, array2);
        T[] resultArray = (T[]) Array.newInstance(Objects.requireNonNull(array1.getClass().getComponentType()), 0);
        return resultList.toArray(resultArray);
    }

}
