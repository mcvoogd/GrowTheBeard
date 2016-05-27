package MVC_V2;

import MVC.game_models.MainMenu;

import javax.swing.*;
import java.awt.*;

/**
 * Created by FlorisBob on 27-May-16.
 */
public class ViewHandler implements ModelListener {
    private View view;
    private JFrame frame;

    public ViewHandler()
    {
        ModelHandler.instance.addListener(this);
        frame = new JFrame("Grow the porn");
        frame.setSize(new Dimension(600, 800));
        frame.setContentPane(new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                if (view != null) view.draw(g2, new Dimension(getWidth(), getHeight()));
            }
        });
    }

    @Override
    public void onModelEvent(ModelEvent event) {
        if (event instanceof NewModel)
        {
            if (view != null) view.close();
            view = selecedView(((NewModel)event).newModel);
            view.start();
        }
        else
        {
            view.onModelEvent(event);
        }
    }

    private static View selecedView(Model model)
    {
        // TODO add view selection
        return null;
    }
}
