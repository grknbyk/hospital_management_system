package model;

import java.util.HashMap;

public class MedicineSupply {
    private HashMap<Medicine, Integer> inventory;
    private static final MedicineSupply instance = new MedicineSupply();

    private MedicineSupply() {

    }

    public MedicineSupply getInstance() {
        return instance;
    }

    public HashMap<Medicine, Integer> getInventory() {
        return (HashMap<Medicine, Integer>) inventory.clone();
    }

    public int getStock(Medicine medicine) {
        return inventory.get(medicine);
    }

    public boolean checkStock(Medicine medicine) {
        return inventory.get(medicine) == 0;
    }

    public void supplyStock(Medicine medicine, int amount) {
        int oldStock = inventory.getOrDefault(medicine, 0);
        inventory.put(medicine, oldStock + amount);
    }

    /**
     * @param medicine Medicine that will be consumed.
     * @param amount   Amount of medicine that will be consumed.
     * @return False if there is less amount of medicine in inventory than the amount.
     */
    public boolean consumeStock(Medicine medicine, int amount) {
        if (!inventory.containsKey(medicine) || inventory.get(medicine) < amount)
            return false;

        int oldStock = inventory.get(medicine);
        inventory.put(medicine, oldStock - amount);

        return true;
    }
}
