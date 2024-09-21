package edivad.extrastorage.items.item;

public enum ItemStorageType
{
    TIER_5(256),
    TIER_6(1024),
    TIER_7(4096),
    TIER_8(1_6384),
    TIER_9(6_5536),
    TIER_10(26_2144),
    TIER_11(104_8576),
    TIER_12(214_7483),
    TIER_13(-1);

    private final String name;
    private final int capacity;

    ItemStorageType(int capacity) {
        if (capacity == -1) {
            this.name = "infinite";
            this.capacity = -1;
        } else {
            if (capacity < 1_0000) {
                this.name = capacity + "k";
            } else {
                this.name = capacity / 1000 + "m";
            }
            this.capacity = capacity * 1000;
        }
    }

    public String getName() {
        return this.name;
    }

    public int getCapacity() {
        return this.capacity;
    }
}
