package info.androidhive.cardview;

/**
 * Created by Amine Liazidi on 31/10/16.
 */
public class Tshirt {
    private String name;
    private int thumbnail;

    public Tshirt(String name, int thumbnail) {
        this.name = name;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getThumbnail() {
        return thumbnail;
    }

}
