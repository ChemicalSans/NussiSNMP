package nussi.net.basicbackend;

import net.nussi.snmp.pdu.PowerDistributionUnit;
import net.nussi.snmp.pdu.PowerDistributionUnitManager;
import net.nussi.snmp.pdu.products.Avocent3009h;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class BasicBackendApplication {

	public static PowerDistributionUnitManager manager;

	public static void main(String[] args) throws Exception {
		ArrayList<PowerDistributionUnit> pdus = new ArrayList<>();
		pdus.add(new Avocent3009h("192.168.0.119", 161, "public_read"));
		pdus.add(new Avocent3009h("192.168.0.120", 161, "public_read"));
		manager = new PowerDistributionUnitManager(pdus);
		manager.setOutletOffDelay(manager.validOutletIDS(), 0);
		manager.setOutletOnDelay(manager.validOutletIDS(), 0);


		SpringApplication.run(BasicBackendApplication.class, args);
	}



}
