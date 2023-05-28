package model;

import model.enums.MedicineType;

import java.sql.Date;
import java.util.TreeMap;
import utils.List;

public class Receipt {
    private int id;
    private int patientId;
    private int staffId;
    private Date givenDate;
    private Date expireDate;
    private boolean isGiven;



    private final TreeMap<Medicine, ReceiptItem> content;

    public Receipt(int id, int patientId, int staffId, Date givenDate, Date expireDate) {
        this.id = id;
        this.patientId = patientId;
        this.staffId = staffId;
        this.givenDate = givenDate;
        this.expireDate = expireDate;
        
        isGiven = false;
        content = new TreeMap<>();
    }

    public void giveReceipt() {
        isGiven = true;
    }

    public Receipt(Receipt r) {
        this.content = (TreeMap<Medicine, ReceiptItem>) r.content.clone();
    }

    public void add(Medicine medicine, int amount) {
        ReceiptItem oldStock = content.getOrDefault(medicine, null);
        if(oldStock == null)
            oldStock = new ReceiptItem(medicine, 0);
        oldStock.amount += amount;

        content.put(medicine, oldStock);
    }

    public boolean decrease(Medicine medicine, int amount) {
        if (!content.containsKey(medicine) || content.get(medicine).amount < amount)
            return false;

        ReceiptItem oldStock = content.get(medicine);
        oldStock.amount -= amount;
        content.put(medicine, oldStock);

        return true;
    }

    public boolean remove(Medicine medicine) {
        if (!content.containsKey(medicine))
            return false;

        content.remove(medicine);
        return true;
    }

    public List<ReceiptItem> toList()
    {
        List<ReceiptItem> l = new List<>();
        content.forEach((k, v) -> l.add(v));

        return l;
    }

    public int getId() {
        return id;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getStaffId() {
        return staffId;
    }

    public Date getGivenDate() {
        return givenDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public boolean isGiven() {
        return isGiven;
    }

    public void setGiven(boolean isGiven) {
        this.isGiven = isGiven;
    }

    public static class ReceiptItem {
        Medicine med;
        int amount;

        public ReceiptItem(Medicine med, int amount) {
            this.med = med;
            this.amount = amount;
        }

        public String getName() {
            return med.getName();
        }

        public MedicineType getType() {
            return med.getType();
        }

        public int getId() {
            return med.getId();
        }

        public int getAmount() {
            return amount;
        }
    }
}