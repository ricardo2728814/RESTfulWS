package me.rrf.utm.rest;

import java.util.Hashtable;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import me.jmll.utm.model.OptionsDoc;
import me.jmll.utm.service.NotificationService;

@RestController
@RequestMapping("api/v1/notify")
public class NotificationRest {
	private static final Logger logger = LogManager.getLogger();
	
	@Autowired
	NotificationService notificationService;
	
	@RequestMapping(method = RequestMethod.OPTIONS)
	@ResponseBody
	public ResponseEntity<?> showOptions() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Allow", "OPTIONS,GET,POST");

		Map<HttpMethod, String> methods = new Hashtable<>(4);
		methods.put(HttpMethod.POST, "Uploads specified file in parameter 'path'");
		methods.put(HttpMethod.OPTIONS, "Resource documentation");
		methods.put(HttpMethod.GET, "Downloads specified file in parameter 'path'");

		OptionsDoc bdy = new OptionsDoc();
		bdy.setMethods(methods);
		return new ResponseEntity<>(bdy, headers, HttpStatus.OK);
	}
	
}
