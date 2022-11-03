package nussi.net.basicbackend.rest.outlet;

import nussi.net.basicbackend.BasicBackendApplication;
import nussi.net.pduControl.pdu.OutletControlAction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/outlet")
@RestController
public class OutletController {

    @GetMapping("/status/{id}")
    public ResponseEntity OutletStatus(
            @PathVariable int id
    ) {
        return ResponseEntity.ok(BasicBackendApplication.pdu.getOutletStatus(id));
    }

    @PostMapping("/status/{id}")
    public ResponseEntity OutletControl(
            @PathVariable int id,
            @RequestParam("action") String actionValue
    ) {
        OutletControlAction action = null;
        if(actionValue.equals("powerSwitch")) {
            
            switch (BasicBackendApplication.pdu.getOutletStatus(id)) {
                case off:
                    action = OutletControlAction.powerOn;
                    break;
                case on:
                    action = OutletControlAction.powerOff;
                    break;
                default:
                    break;
            }
            
        } else {
            action = Enum.valueOf(OutletControlAction.class, actionValue);
        }

        if(action != null) {
            BasicBackendApplication.pdu.doOutletAction(id, action);
        }

        BasicBackendApplication.pdu.doOutletAction(id, action);
        return ResponseEntity.ok().build();
    }

}
