package androiddeviceid.emulk.it.androiddeviceid;

/**
 * Created by root on 18/04/17.
 */

public class IdObject {
    private String name;
    private String value;
    private String description;

    IdObject(String name, String value) {
        this.name = name;
        this.value = value;
    }

    IdObject(String name, String value, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
