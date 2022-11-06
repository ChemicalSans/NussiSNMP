package nussi.net.basicbackend.rest.outlet;

import net.nussi.pduControl.pdu.OutletControlAction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static nussi.net.basicbackend.BasicBackendApplication.manager;

@RequestMapping("/outlet")
@RestController
public class OutletController {

    @GetMapping("/status/{id}")
    public ResponseEntity OutletStatus(
            @PathVariable int id
    ) {
        return ResponseEntity.ok(manager.getOutletStatus(id));
    }

    @PostMapping("/status/{id}")
    public ResponseEntity OutletControl(
            @PathVariable int id,
            @RequestParam("action") String actionValue
    ) {
        OutletControlAction action = null;
        if(actionValue.equals("powerSwitch")) {
            
            switch (manager.getOutletStatus(id)) {
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
            manager.doOutletAction(id, action);
        }

        manager.doOutletAction(id, action);
        return ResponseEntity.ok().build();
    }

}
