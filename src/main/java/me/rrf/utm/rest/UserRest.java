package me.rrf.utm.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import me.jmll.utm.form.UserForm;
import me.jmll.utm.model.Link;
import me.jmll.utm.model.User;
import me.jmll.utm.model.UserLinkListResource;
import me.jmll.utm.model.UserResource;
import me.jmll.utm.rest.exception.ResourceNotFoundException;
import me.jmll.utm.service.UserService;

/**
 * 
 * Se utilizó como base el archivo de la actividad.
 *
 */

@RestController
@RequestMapping("api/v1/user")
public class UserRest {
	@Autowired
	UserService userService;

	@RequestMapping(method = RequestMethod.GET, produces = { "application/json", "text/json" })
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> getUsersJSON() {
		ServletUriComponentsBuilder sucBuilder = ServletUriComponentsBuilder.fromCurrentServletMapping();
		List<Link> links = new ArrayList<Link>();
		links.add(new Link(sucBuilder.path("/api/v1/").build().toString(), "api"));
		links.add(new Link(sucBuilder.path("user/").build().toString(), "self"));
		List<Link> userLinks = new ArrayList<Link>();
		for (User u : userService.getUsers()) {
			String path = ServletUriComponentsBuilder.fromCurrentServletMapping()
					.path("/api/v1/user/" + u.getUsername()).build().toString();
			userLinks.add(new Link(path, u.getUsername()));
		}

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("_links", links);
		response.put("data", userLinks);
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, produces = { "application/xml", "text/xml" })
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public UserLinkListResource getUsersXML() {
		ServletUriComponentsBuilder sucBuilder = ServletUriComponentsBuilder.fromCurrentServletMapping();
		List<Link> links = new ArrayList<Link>();
		links.add(new Link(sucBuilder.path("/api/v1/").build().toString(), "api"));
		links.add(new Link(sucBuilder.path("user/").build().toString(), "self"));
		List<Link> userLinks = new ArrayList<Link>();
		for (User u : userService.getUsers()) {
			String path = ServletUriComponentsBuilder.fromCurrentServletMapping()
					.path("/api/v1/user/" + u.getUsername()).build().toString();
			userLinks.add(new Link(path, u.getUsername()));
		}

		UserLinkListResource userLinksResource = new UserLinkListResource();
		userLinksResource.setLinks(links);
		userLinksResource.setUserLinks(userLinks);
		return userLinksResource;
	}

	@RequestMapping(value = "{username}", method = RequestMethod.GET, produces = { "application/json", "text/json" })
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> getUserJSON(@PathVariable("username") String username) {
		User user = this.userService.getUser(username);
		if (user == null)
			throw new ResourceNotFoundException("User was not found");
		ServletUriComponentsBuilder sucBuilder = ServletUriComponentsBuilder.fromCurrentServletMapping();
		List<Link> links = new ArrayList<Link>();
		links.add(new Link(sucBuilder.path("/api/v1/").build().toString(), "api"));
		links.add(new Link(sucBuilder.path("user/"+user.getUsername()).build().toString(), "self"));
		
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("_links", links);
		response.put("data", user);
		return response;
	}

	@RequestMapping(value = "{username}", method = RequestMethod.GET, produces = { "application/xml", "text/xml" })
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public UserResource getUserXML(@PathVariable("username") String username) {
		User user = this.userService.getUser(username);
		if (user == null)
			throw new ResourceNotFoundException("User was not found");
		ServletUriComponentsBuilder sucBuilder = ServletUriComponentsBuilder.fromCurrentServletMapping();
		List<Link> links = new ArrayList<Link>();
		links.add(new Link(sucBuilder.path("/api/v1/").build().toString(), "api"));
		links.add(new Link(sucBuilder.path("user/"+user.getUsername()).build().toString(), "self"));
		UserResource resource = new UserResource();
		resource.setLinks(links);
		resource.setUser(user);
		return resource;
	}

	@RequestMapping(value = "{username}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable("username") String username) {
		if (this.userService.getUser(username) == null)
			throw new ResourceNotFoundException("User was not found");
		this.userService.deleteUser(username);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<User> create(@RequestBody UserForm form) {
		User newUser = this.userService.createUser(form.getUsername(), form.getPassword(), form.getFullName());

		String uri = ServletUriComponentsBuilder.fromCurrentServletMapping().path("/api/v1/user/{username}")
				.buildAndExpand(newUser.getUsername()).toString();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", uri);

		return new ResponseEntity<>(newUser, headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "{username}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@PathVariable("username") String username, @RequestBody UserForm form) {
		User user = this.userService.getUser(username);
		if (user == null)
			throw new ResourceNotFoundException("User was not found");
		user.setFullName(form.getFullName());
		user.setPassword(form.getPassword());
		user.setUsername(form.getUsername());
		this.userService.updateUser(user);
	}

	@RequestMapping(method = RequestMethod.OPTIONS)
	public ResponseEntity<Void> userIndex() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Allow", "OPTIONS,HEAD,GET,POST");
		return new ResponseEntity<>(null, headers, HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "{username}", method = RequestMethod.OPTIONS)
	public ResponseEntity<Void> userOptions(@PathVariable("username") String username) {
		if (this.userService.getUser(username) == null)
			throw new ResourceNotFoundException("User was not found");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Allow", "OPTIONS,HEAD,GET,PUT,DELETE");
		return new ResponseEntity<>(null, headers, HttpStatus.NO_CONTENT);
	}
}