package model;

public class InHouse extends Part {
    private int machineId;

    /**
     * Constructor Function. Uses Part Super Class Constructor as InHouse is a subclass of Part.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }


    /**
     *
     * @param machineId is the value used to set this.machineId.
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     *
     * @return machineId.
     */
    public int getMachineId() {
        return machineId;
    }
}
