package net.nussi.jFrameClient.outlet;

import net.nussi.jFrameClient.Main;
import net.nussi.pduControl.pdu.OutletStatus;
import net.nussi.pduControl.pdu.PowerDistributionUnitManager;

import javax.swing.*;

public class Outlet extends JPanel {
    private static final PowerDistributionUnitManager manager = Main.manager;
    public final int outletID;

    private OutletStatus status;

    private final SwitchButton switchButton;
    private final LockButton lockButton;
    private final NameTextPanel nameTextPanel;

    public Outlet(int outletID) {
        this.outletID = outletID;
        this.status = OutletStatus.notSet;
        this.setVisible(true);

        this.switchButton = new SwitchButton(this);
        this.add(switchButton);

        this.lockButton = new LockButton(this);
        this.add(this.lockButton);

        this.nameTextPanel = new NameTextPanel(this);
        this.add(this.nameTextPanel);
    }


    public void updateOutletStatus() {
        this.status = manager.getOutletStatus(outletID);
    }

    private void updateColor() {
        this.lockButton.updateColor();
        this.switchButton.updateColor();

        this.updateUI();
    }

    private void updateText() {
        this.lockButton.updateText();
        this.switchButton.updateText();
    }

    public OutletStatus getStatus() {
        return status;
    }

    public void setStatus(OutletStatus status) {
        this.status = status;
        updateText();
        updateColor();
    }

}
