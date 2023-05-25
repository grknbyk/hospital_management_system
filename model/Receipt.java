package model;

import java.util.HashMap;

public class Receipt {
    HashMap<Medicine, Integer> content;

    public Receipt() {

    }

    public Receipt(HashMap<Medicine, Integer> content) {
        this.content = (HashMap<Medicine, Integer>) content.clone();
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
        return true;
    }

    public boolean remove(Medicine medicine) {
        if (!content.containsKey(medicine))
            return false;

        content.remove(medicine);
        return true;
    }
}
