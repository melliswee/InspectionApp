package InspectionApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class URIInspection {
	
	private String request; // original request
	private String path; // path split from request
	private String scheme; // scheme split from request
	private List<String> parameters; // parameter(s) split from request
	private static final String BASE_SCHEME = "visma-identity:"; // scheme that is enforced, request scheme is compared to BASE_SCHEME
	private List<String> validPaths; // allowed paths, request path is compared against this list of allowed paths
	
	public URIInspection() {
		
		this.request = null;
		this.path = null;
		this.scheme = null;
		this.parameters = null;
		this.validPaths = new ArrayList<String>();
		this.parameters = new ArrayList<String>();
		validPaths.add("login");
		validPaths.add("sign");
		validPaths.add("confirm");
	}
	
	// allowed example URI: visma-identity://sign?source=vismasign&documentid=105ab44
	// allowed example URI: visma-identity://confirm?source=netvisor&paymentnumber=102226
	// allowed example URI: visma-identity://login?source=severa
	
	public Map<String, String> inspect(String URIRequest) {
		
		this.validPaths = new ArrayList<String>();
		validPaths.add("login");
		validPaths.add("sign");
		validPaths.add("confirm");
		this.request = URIRequest;
		boolean splitResult = split();
		
		boolean isValidUri = false;
		if (splitResult) {
			isValidUri = compare();
		}
		
		// if URI is acceptable, form an answer
		Map<String, String> answer = new HashMap<String, String>();
		if (isValidUri) {
			
			answer.put("path", this.path);
			
			// for each parameter, split name and value of parameter
			for (String param : this.parameters) {
				String[] paramSplit = param.split("=", 2);
				answer.put(paramSplit[0], paramSplit[1]);
			}			
		}
		
		return answer;
	}
	
	private boolean split() {
		
		String Uri = this.request;
		
		try {
			String[] parts = Uri.split("//", 2); // separates scheme and path+parameters
			String pathAndParameters = parts[1];
			String[] rest = pathAndParameters.split("\\?", 2); // separates path and parameters
			
			this.scheme = parts[0];
			this.path = rest[0];
			
			// there can be 1 or 2 parameters depending on path requirements
			String allParameters = rest[1];
			String[] paramParts = allParameters.split("&", 2);
			
			// add all parameters to the list 
			for (int i = 0; i < paramParts.length; i++) {
				this.parameters.add(paramParts[i]);
			}
			
			return true; // URI was split successfully and is generally in correct form 
		} catch (Exception e) {
			System.out.println("URI was something unexpected");
			return false;
		}
	}
	
	// Verifies that scheme and path are expected. If yes, verify that parameters are of the right type
	private boolean compare() {
		
		boolean success = false;
		try {
			if (this.scheme.equals(BASE_SCHEME) && this.validPaths.contains(this.path)) {
				switch (this.path) {
					case "login":
						String[] nameAndValue = this.parameters.get(0).split("=", 2);
						if (nameAndValue[1] instanceof java.lang.String) {
							success = true; // login-path has valid parameter
							break;
						}
					case "sign":
						// split source-parameter
						String[] sourceSplit = this.parameters.get(0).split("=", 2);
						// split documentid-parameter
						String[] docSplit = this.parameters.get(1).split("=", 2);
						if (sourceSplit[1] instanceof java.lang.String && docSplit[1] instanceof java.lang.String) {
							success = true; // sign-path has valid parameters
							break;
						}
					case "confirm":
						// split source-parameter
						String[] cSourceSplit = this.parameters.get(0).split("=", 2);
						//split paymentnumber-parameter
						String[] paySplit = this.parameters.get(1).split("=", 2);
						if (cSourceSplit[1] instanceof java.lang.String) {
							try {
								Integer.parseInt(paySplit[1]);
								success = true;
								break;
							} catch (NumberFormatException nfe) {
								success = false;
								break;
							}
						}
				}					
			} else {
				success = false;
			}
		} catch (Exception e) {
			success = false;
		} finally {
			return success;
		}
	}

}

