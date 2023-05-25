package model;

import utils.Tuple;

import java.util.TreeMap;
import utils.List;

public class Receipt {
    private final TreeMap<Medicine, Integer> content;

    public Receipt() {
        content = new TreeMap<>();
    }

    public Receipt(Receipt r) {
        this.content = (TreeMap<Medicine, Integer>) r.content.clone();
    }

    public void add(Medicine medicine, int amount) {
        int oldAmount = content.getOrDefault(medicine, 0);
        content.put(medicine, oldAmount + amount);
    }

    public boolean decrease(Medicine medicine, int amount) {
        if (!content.containsKey(medicine) || content.get(medicine) < amount)
            return false;

        int oldAmount = content.get(medicine);
        content.put(medicine, oldAmount - amount);

        if(content.get(medicine) == 0)
            content.remove(medicine);

        return true;
    }

    public boolean remove(Medicine medicine) {
        if (!content.containsKey(medicine))
            return false;

        content.remove(medicine);
        return true;
    }

    public List<Tuple<Medicine, Integer>> toList()
    {
        List<Tuple<Medicine, Integer>> l = new List<>();
        content.forEach((k, v) -> l.add(new Tuple<>(k, v)));

        return l;
    }
}
