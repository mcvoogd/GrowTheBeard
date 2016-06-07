package nl.avans.a3.mvc_interfaces;

public interface Model {
    /**
     * this method is the first method to be called when initializing this model.
     */
    void start();

    /**
     * this is the method that is called every time the model needs to be updated.
     */
    void update();

    /**
     * this is the method that is called when the model closes.
     * use this method to close existing items in the model.
     */
    void close();

    // TODO add some input method
}
