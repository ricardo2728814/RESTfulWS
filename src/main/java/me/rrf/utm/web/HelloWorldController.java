package me.rrf.utm.web;

import java.util.Collections;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class HelloWorldController {
	@RequestMapping(produces = "application/json")
	public Map<String, String> hello() {
		return Collections.singletonMap("data", "Hello World!");
	}
}