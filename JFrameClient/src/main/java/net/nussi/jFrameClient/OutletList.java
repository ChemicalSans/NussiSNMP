package net.nussi.jFrameClient;

import javax.swing.*;
import java.util.ArrayList;

public class OutletList extends JPanel {

    public OutletList(ArrayList<Outlet> outlets) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setVisible(true);
        this.setSize(120,420);

        for(Outlet out : outlets) {
            this.add(out);
        }
    }

    public static OutletList fromIDs(int[] outletIDs) {
        ArrayList<Outlet> outlets = new ArrayList<>();
        for(int id : outletIDs) {
            outlets.add(new Outlet(id));
        }
        return new OutletList(outlets);
    }



}
