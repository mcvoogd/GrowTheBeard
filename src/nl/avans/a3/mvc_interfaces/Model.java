package nl.avans.a3.mvc_interfaces;

/**
 * Created by FlorisBob on 27-May-16.
 */
public interface Model {
    /**
     * this method is the first method to be called when initializing this model.
     */
    public void start();

    /**
     * this is the method that is called every time the model needs to be updated.
     */
    public void update();

    /**
     * this is the method that is called when the model closes.
     * use this method to close existing items in the model.
     */
    public void close();

    // TODO add some input method
}
