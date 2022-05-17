package br.com.southsystem.voto.client;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AssiciadoClient {

	private static Logger log = Logger.getLogger(AssiciadoClient.class);
	
	public static boolean requestValidateCpfWeb(String cpf) throws Exception {
		ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.OK);
		RestTemplate restTemplate = new RestTemplate();
		try {
			String url = "https://user-info.herokuapp.com/users/"+cpf;
			response = restTemplate.getForEntity(url, String.class);
		} catch (Exception e) {
			log.error(e);
		}
		System.out.println(response.getBody());
		return response.getStatusCodeValue() == 200? true : false;
	}
	
}
