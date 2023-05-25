package utils;

import java.util.Arrays;

public class List<T> implements ListInterface<T> {
    Object[] arr;
    int len;
    final static int DEFAULT_SIZE = 10;

    public List() {
        arr = new Object[DEFAULT_SIZE];
        len = 0;
    }

    @Override
    public void add(T newEntry) {
        if (len == arr.length)
            resize();

        arr[len] = newEntry;
        len++;
    }

    @Override
    public void add(int newPosition, T newEntry) {
        if (len == arr.length)
            resize();

        System.arraycopy(arr, newPosition, arr, newPosition + 1, len - newPosition);
        arr[newPosition] = newEntry;
        len++;
    }

    @Override
    public T remove(int givenPosition) {
        if (givenPosition >= len)
            return null;

        T ret = (T) arr[givenPosition];
        if (givenPosition + 1 != arr.length)
            System.arraycopy(arr, givenPosition + 1, arr, givenPosition, len - givenPosition - 1);
        arr[--len] = null;

        return ret;
    }

    @Override
    public void clear() {
        arr = new Object[DEFAULT_SIZE];
    }

    @Override
    public T replace(int givenPosition, T newEntry) {
        if (givenPosition >= len)
            return null;

        T ret = (T) arr[givenPosition];
        arr[givenPosition] = newEntry;

        return ret;
    }

    @Override
    public T getEntry(int givenPosition) {
        return (T) arr[givenPosition];
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(arr, len);
    }

    @Override
    public boolean contains(T anEntry) {
        for (Object t : arr) {
            if (anEntry.equals(t))
                return true;
        }

        return false;
    }

    @Override
    public int getLength() {
        return len;
    }

    @Override
    public boolean isEmpty() {
        return len == 0;
    }

    private void resize() {
        Object[] newArr = new Object[arr.length * 2];
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        arr = newArr;
    }
}
