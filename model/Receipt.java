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
    private boolean isGiven; // set true if receipt is given to patient
    private final TreeMap<Medicine, Integer> content;

    public Receipt(int id, int patientId, int staffId, Date givenDate, Date expireDate) {
        this.id = id;
        this.patientId = patientId;
        this.staffId = staffId;
        this.givenDate = givenDate;
        this.expireDate = expireDate;
        
        isGiven = false;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public Date getGivenDate() {
        return givenDate;
    }

    public void setGivenDate(Date givenDate) {
        this.givenDate = givenDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public boolean isGiven() {
        return isGiven;
    }

    public void setGiven(boolean isGiven) {
        this.isGiven = isGiven;
    }

    public TreeMap<Medicine, Integer> getContent() {
        return content;
    }

    public void giveReceipt() {
        isGiven = true;
    }

    public void setIsGiven(boolean isGiven) {
        this.isGiven = isGiven;
    }

    public boolean getIsGiven() {
        return isGiven;
    }
}
