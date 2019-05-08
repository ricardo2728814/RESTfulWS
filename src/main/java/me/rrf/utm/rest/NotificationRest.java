package me.rrf.utm.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import me.jmll.utm.model.OptionsDoc;
import me.jmll.utm.model.Link;
import me.jmll.utm.model.Notification;
import me.jmll.utm.model.NotificationLinkListResource;
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
		methods.put(HttpMethod.POST, "Submits notifications to send");
		methods.put(HttpMethod.OPTIONS, "Resource documentation");
		methods.put(HttpMethod.GET, "Lists notifications submitted");

		OptionsDoc bdy = new OptionsDoc();
		bdy.setMethods(methods);
		return new ResponseEntity<>(bdy, headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, "text/json" })
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> getNotificationsJSON() {
		ServletUriComponentsBuilder sucBuilder = ServletUriComponentsBuilder.fromCurrentServletMapping();
		List<Link> links = new ArrayList<Link>();
		links.add(new Link(sucBuilder.path("/api/v1/").build().toString(), "api"));
		links.add(new Link(sucBuilder.path("notify/").build().toString(), "self"));

		Map<String, Object> response = new Hashtable<>();
		response.put("_links", links);
		response.put("data", notificationService.getNotifications());
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_XHTML_XML_VALUE, MediaType.TEXT_XML_VALUE })
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public NotificationLinkListResource getNotificationsXML() {
		ServletUriComponentsBuilder sucBuilder = ServletUriComponentsBuilder.fromCurrentServletMapping();
		List<Link> links = new ArrayList<Link>();
		links.add(new Link(sucBuilder.path("/api/v1/").build().toString(), "api"));
		links.add(new Link(sucBuilder.path("notify/").build().toString(), "self"));

		NotificationLinkListResource response = new NotificationLinkListResource();
		response.setLinks(links);
		response.setNotifications(notificationService.getNotifications());
		return response;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> notify(@RequestParam String subject, @RequestParam String message,
			@RequestParam(value = "toAddress") String toAddressRaw,
			@RequestParam(value = "ccAddress", defaultValue = "", required = false) String ccAddressRaw) {

		List<String> toAddress = Arrays.asList(toAddressRaw.split(";"));
		List<String> ccAddress = ccAddressRaw.length() == 0 ? Collections.emptyList()
				: Arrays.asList(ccAddressRaw.split(";"));
		Notification n = notificationService.notify(subject, message, toAddress, ccAddress);

		return new ResponseEntity<>(null, null,
				n.getStatus() == "ERROR" ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.ACCEPTED);
	}
}
