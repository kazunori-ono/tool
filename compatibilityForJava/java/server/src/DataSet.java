public class DataSet {
    private String data;

    public DataSet() {
        data = "No Name";
    }

    public DataSet(String data) {
        this.data = data;
    }

    public void setDame(String s) { data = s; }
    public String getDame() { return data; }
}