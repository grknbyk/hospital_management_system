package model;

import java.util.ArrayList;
import java.util.List;

import model.enums.MedicineType;
import utils.Pair;

import java.util.TreeMap;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableObjectValue;

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
        oldStock.stock.set(oldStock.stock.get() + amount);

        inventory.put(medicine, oldStock);
    }

    public void setStock(Medicine medicine, int amount) {
        SupplyItem oldStock = inventory.getOrDefault(medicine, null);
        if (oldStock == null)
            oldStock = new SupplyItem(medicine, 0);
        oldStock.stock.set(amount);

        inventory.put(medicine, oldStock);
    }

    /**
     * @param medicine Medicine that will be consumed.
     * @param amount   Amount of medicine that will be consumed.
     * @return False if there is less amount of medicine in inventory than the
     * amount.
     */
    public boolean consumeStock(Medicine medicine, int amount) {
        if (!inventory.containsKey(medicine) || inventory.get(medicine).stock.get() < amount)
            return false;

        SupplyItem oldStock = inventory.get(medicine);
        oldStock.stock.set((oldStock.stock.get()- amount));
        inventory.put(medicine, oldStock);

        return true;
    }

    public List<SupplyItem> toList() {
        List<SupplyItem> l = new ArrayList<>();
        inventory.forEach((k, v) -> l.add(v));

        return l;
    }

    public static class SupplyItem {
        ObjectProperty<Medicine> med = new SimpleObjectProperty<>();
        SimpleIntegerProperty stock = new SimpleIntegerProperty();

        public SupplyItem(Medicine med, int stock) {
            this.med.set(med);
            this.stock.set(stock);
        }

        public String getName() {
            return med.get().getName();
        }

        public MedicineType getType() {
            return med.get().getType();
        }

        public int getId() {
            return med.get().getId();
        }

        public int getStock() {
            return stock.get();
        }

        public Medicine getMedicine() {
            return med.get();
        }
    }
}
