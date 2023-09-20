package TienIch;

public class Widget {
    private String name;
    private int iconResId;
    private Class<?> destinationClass;
    public Widget(String name, int iconResId, Class<?> destinationClass) {
        this.name = name;
        this.iconResId = iconResId;
        this.destinationClass = destinationClass;
    }

    public String getName() {
        return name;
    }

    public int getIconResId() {
        return iconResId;
    }

    public Class<?> getDestinationClass() {
        return destinationClass;
    }
}
