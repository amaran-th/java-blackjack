package techcourse.icf.mission;

public interface SimpleList<T> {

    static <T> SimpleList<T> fromArrayToList(T[] arrays) {
        SimpleList<T> result = new SimpleArrayList<T>();
        for (T value : arrays) {
            result.add(value);
        }
        return result;
    }

    static <T extends Number> Double sum(SimpleList<T> values) {
        Double sum = Double.valueOf(0);
        for (int i = 0; i < values.size(); i++) {
            sum += values.get(i).doubleValue();
        }
        return sum;
    }

    static <T extends Number> SimpleList<T> filterNegative(SimpleList<T> rawValues) {
        SimpleList<T> values = new SimpleArrayList<>();
        for (int i = 0; i < rawValues.size(); i++) {
            if (rawValues.get(i).doubleValue() >= (double) 0) {
                values.add(rawValues.get(i));
            }
        }
        return values;
    }

    static <T> void copy(SimpleList<? extends T> fromList, SimpleList<T> toList) {
        for (int i = 0; i < fromList.size(); i++) {
            toList.add(0, fromList.get(i));
        }
    }

    boolean add(T value);

    void add(int index, T value);

    T set(int index, T value);

    T get(int index);

    boolean contains(T value);

    int indexOf(T value);

    int size();

    boolean isEmpty();

    boolean remove(T value);

    T remove(int index);

    void clear();

}
