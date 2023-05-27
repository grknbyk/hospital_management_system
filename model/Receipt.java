package model;

import utils.Pair;

import java.sql.Date;
import java.util.TreeMap;
import utils.List;

public class Receipt {
    private int id;
    private int patientId;
    private int staffId;
    private Date givenDate;
    private Date expireDate;
    private final TreeMap<Medicine, Integer> content;

    public Receipt(int id, int patientId, int staffId, Date givenDate, Date expireDate) {
        this.id = id;
        this.patientId = patientId;
        this.staffId = staffId;
        this.givenDate = givenDate;
        this.expireDate = expireDate;

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

    public List<Pair<Medicine, Integer>> toList()
    {
        List<Pair<Medicine, Integer>> l = new List<>();
        content.forEach((k, v) -> l.add(new Pair<>(k, v)));

        return l;
    }
}
