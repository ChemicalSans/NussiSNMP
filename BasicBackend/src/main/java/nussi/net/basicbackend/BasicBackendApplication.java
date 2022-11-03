package nussi.net.basicbackend;

import nussi.net.pduControl.pdu.products.Avocent3009h;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class BasicBackendApplication {

	public static Avocent3009h pdu;

	public static void main(String[] args) throws IOException {
		pdu = new Avocent3009h("192.168.0.119", 161, "public_read");

		pdu.setOutletOnDelay(pdu.validOutletIDS() , 0);
		pdu.setOutletOffDelay(pdu.validOutletIDS(), 0);


		SpringApplication.run(BasicBackendApplication.class, args);
	}



}
