package me.rrf.utm.rest;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import me.jmll.utm.service.FileService;

@Controller
@RequestMapping("api/v1/file")
public class FileRest {
	
	@Autowired
	FileService fileService;
	
	@RequestMapping(method = RequestMethod.OPTIONS)
	public ResponseEntity<?> showOptions() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Allow", "OPTIONS,GET,POST,DELETE");
		
		Map<String, String> methods = new Hashtable<>(4);
		methods.put("POST", "Uploads specified file in parameter 'path'");
		methods.put("OPTIONS", "Resource documentation");
		methods.put("GET", "Downloads specified file in parameter 'path'");
		methods.put("DELETE", "Deletes specified file in parameter 'path'");
		
		Map<String, Map> body = Collections.singletonMap("methods", methods);
		
		return new ResponseEntity<>(body, headers, HttpStatus.OK);
	}

}
;