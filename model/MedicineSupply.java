package model;

import java.util.ArrayList;
import java.util.List;

import model.enums.MedicineType;
import utils.Pair;

import java.util.TreeMap;

public class MedicineSupply {
    private TreeMap<Medicine, SupplyItem> inventory = new TreeMap<>();
    private static MedicineSupply instance = new MedicineSupply();

    private MedicineSupply() {

    }

    public static MedicineSupply getInstance() {
        return instance;
    }

    public int getStock(Medicine medicine) {
        return inventory.get(medicine).getStock();
    }

    public TreeMap<Medicine, SupplyItem> getInventory() {
        return inventory;
    }

    public boolean checkStock(Medicine medicine) {
        return inventory.get(medicine).getStock() == 0;
    }

    public void supplyStock(Medicine medicine, int amount) {
        SupplyItem oldStock = inventory.getOrDefault(medicine, null);
        if (oldStock == null)
            oldStock = new SupplyItem(medicine, 0);
        oldStock.stock += amount;

        inventory.put(medicine, oldStock);
    }

    public void setStock(Medicine medicine, int amount) {
        SupplyItem oldStock = inventory.getOrDefault(medicine, null);
        if (oldStock == null)
            oldStock = new SupplyItem(medicine, 0);
        oldStock.stock = amount;

        inventory.put(medicine, oldStock);
    }

    /**
     * @param medicine Medicine that will be consumed.
     * @param amount   Amount of medicine that will be consumed.
     * @return False if there is less amount of medicine in inventory than the
     * amount.
     */
    public boolean consumeStock(Medicine medicine, int amount) {
        if (!inventory.containsKey(medicine) || inventory.get(medicine).stock < amount)
            return false;

        SupplyItem oldStock = inventory.get(medicine);
        oldStock.stock -= amount;
        inventory.put(medicine, oldStock);

        return true;
    }

    public List<SupplyItem> toList() {
        List<SupplyItem> l = new ArrayList<>();
        inventory.forEach((k, v) -> l.add(v));

        return l;
    }

    public static class SupplyItem {
        private Medicine med;
        private int stock;

        public SupplyItem(Medicine med, int stock) {
            this.med = med;
            this.stock = stock;
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

        public int getStock() {
            return stock;
        }

        public Medicine getMedicine() {
            return med;
        }
    }
}
