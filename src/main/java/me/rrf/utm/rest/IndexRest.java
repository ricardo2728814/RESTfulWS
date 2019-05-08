package me.rrf.utm.rest;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import me.jmll.utm.model.Link;
import me.jmll.utm.model.Resource;

@RestController
@RequestMapping("api/v1/")
public class IndexRest {

	@RequestMapping(method = RequestMethod.GET, produces = { "application/json", "text/json" })
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> indexJson() {
		ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentServletMapping();
		List<Link> links = new ArrayList<Link>();
		String self = builder.path("/api/v1/").build().toString();
		links.add(new Link(self, "self"));
		links.add(new Link(self+"user", "user"));
		links.add(new Link(self+"directory", "directory"));
		links.add(new Link(self+"notify", "notify"));
		links.add(new Link(self+"file", "file"));
		Map<String, Object> response = new Hashtable<>(2);
		response.put("_links", links);
		response.put("version", "1");
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, produces = { "application/xml", "text/xml" })
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Resource indexXml() {
		ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentServletMapping();
		Resource resource = new Resource();
		String self = builder.path("/api/v1/").build().toString();
		resource.addLink(new Link(self, "self"));
		resource.addLink(new Link(self+"user", "user"));
		resource.addLink(new Link(self+"directory", "directory"));
		resource.addLink(new Link(self+"notify", "notify"));
		resource.addLink(new Link(self+"file", "file"));
		return resource;
	}
}
