package me.rrf.utm.rest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import me.jmll.utm.model.File;
import me.jmll.utm.model.FileLinkListResource;
import me.jmll.utm.model.Link;
import me.jmll.utm.model.OptionsDoc;
import me.jmll.utm.rest.exception.ResourceNotFoundException;
import me.jmll.utm.service.FileService;

@RestController
@RequestMapping("api/v1/directory")
public class DirectoryRest {
	@Autowired
	FileService fileService;

	@RequestMapping(method = RequestMethod.OPTIONS)
	@ResponseBody
	public ResponseEntity<?> showOptions() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Allow", "OPTIONS,GET");
		Map<HttpMethod, String> methods = new Hashtable<>(4);
		methods.put(HttpMethod.OPTIONS, "Resource documentation");
		methods.put(HttpMethod.GET, "Lists the specified directory contents in parameter 'dir'");
		OptionsDoc bdy = new OptionsDoc();
		bdy.setMethods(methods);
		return new ResponseEntity<>(bdy, headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> getFilesJSON(@RequestParam(value = "dir") String dir) {
		ServletUriComponentsBuilder sucBuilder = ServletUriComponentsBuilder.fromCurrentServletMapping();
		List<Link> links = new ArrayList<Link>();
		links.add(new Link(sucBuilder.path("/api/v1/").build().toString(), "api"));
		links.add(new Link(sucBuilder.path("directory/").build().toString(), "self"));
		Path reqDir = Paths.get(dir); // Path to requested directory
		List<Path> paths = new ArrayList<Path>();
		if (!Files.exists(reqDir)) {
			throw new ResourceNotFoundException(dir + " not found");
		}
		List<File> files = new ArrayList<File>();
		paths = fileService.walkDir(reqDir, paths);
		for (Path p : paths) {
			String fileName = p.getFileName().toString();
			String path = reqDir.toAbsolutePath().toString().replaceAll("\\\\", "/");
			String fullPath = p.toAbsolutePath().toString().replaceAll("\\\\", "/");
			String href = ServletUriComponentsBuilder.fromCurrentServletMapping()
					.path("/api/v1/file/?path=" + p.toAbsolutePath()).build().toString().replaceAll("\\\\", "/");
			files.add(new File(fileName, path, fullPath, String.valueOf(p.toFile().length()),
					new Link(href, "download")));
		}

		Map<String, Object> res = new Hashtable<>(2);
		res.put("_links", links);
		res.put("data", files);
		return res;
	}

	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_XML_VALUE,
			MediaType.APPLICATION_XHTML_XML_VALUE })
	@ResponseBody
	public FileLinkListResource getFilesXML(@RequestParam(value = "dir") String dir) {
		ServletUriComponentsBuilder sucBuilder = ServletUriComponentsBuilder.fromCurrentServletMapping();
		List<Link> links = new ArrayList<Link>();
		links.add(new Link(sucBuilder.path("/api/v1/").build().toString(), "api"));
		links.add(new Link(sucBuilder.path("directory/").build().toString(), "self"));
		Path reqDir = Paths.get(dir); // Path to requested directory
		List<Path> paths = new ArrayList<Path>();
		if (!Files.exists(reqDir)) {
			throw new ResourceNotFoundException(dir + " not found");
		}
		List<File> files = new ArrayList<File>();
		paths = fileService.walkDir(reqDir, paths);
		for (Path p : paths) {
			String fileName = p.getFileName().toString();
			String path = reqDir.toAbsolutePath().toString().replaceAll("\\\\", "/");
			String fullPath = p.toAbsolutePath().toString().replaceAll("\\\\", "/");
			String href = ServletUriComponentsBuilder.fromCurrentServletMapping()
					.path("/api/v1/file/?path=" + p.toAbsolutePath()).build().toString().replaceAll("\\\\", "/");
			files.add(new File(fileName, path, fullPath, String.valueOf(p.toFile().length()),
					new Link(href, "download")));
		}

		FileLinkListResource res = new FileLinkListResource();
		res.setFiles(files);
		res.setLinks(links);
		return res;
	}
}