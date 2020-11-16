package dk.dd.rabbit;

public class RecipientList {
    String[] names;

    public RecipientList(String[] names) {
        this.names = names;
    }

    public RecipientList() {
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }
}
