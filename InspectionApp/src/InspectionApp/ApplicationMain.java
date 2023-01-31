package InspectionApp;

import java.util.HashMap;
import java.util.Map;

public class ApplicationMain {

	public static void main(String[] args) {
		
		// allowed example URI: visma-identity://sign?source=vismasign&documentid=105ab44
		// allowed example URI: visma-identity://confirm?source=netvisor&paymentnumber=102226
		// allowed example URI: visma-identity://login?source=severa
		
		// valid uri1
		URIInspection inspection1 = new URIInspection();
		String uri1 = "visma-identity://sign?source=vismasign&documentid=105ab44";
		Map<String, String> result1 = new HashMap<String, String>();
		result1 = inspection1.inspect(uri1);
		System.out.println(result1);
		
		//valid uri2
		URIInspection inspection2 = new URIInspection();
		String uri2 = "visma-identity://confirm?source=netvisor&paymentnumber=102226";
		Map<String, String> result2 = new HashMap<String, String>();
		result2 = inspection2.inspect(uri2);
		System.out.println(result2);
		
		// valid uri3
		URIInspection inspection3 = new URIInspection();
		String uri3 = "visma-identity://login?source=severa";
		Map<String, String> result3 = new HashMap<String, String>();
		result3 = inspection3.inspect(uri3);
		System.out.println(result3);
		
	}
	
	

}
