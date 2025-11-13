package util;

import javax.swing.*;
import java.awt.event.ActionEvent;


//Az ESC lenyomásakor végrehajtandó művelet (kilépünk a teljes képernyőből egy előre meghatározott méretbe)
public class EscapeAction extends AbstractAction {
    private final JFrame frame;

    public EscapeAction(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ScreenUtil.toggleFullScreen(frame);
    }
}
