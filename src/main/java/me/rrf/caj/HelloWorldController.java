package me.rrf.caj;

import java.util.Collections;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloWorldController {
	
	@RequestMapping("hello")
	public Map<String, String> hello() {
		return Collections.singletonMap("data", "Hello World!");
	}
}