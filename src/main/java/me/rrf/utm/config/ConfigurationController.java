package me.rrf.utm.config;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class ConfigurationController extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		// TODO Auto-generated method stub
		return new Class[] {
				MVCAdapter.class,
				JmllAdapter.class
		};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] {
				"/"
		};
	}

	@Override
    protected void customizeRegistration(Dynamic registration) {
        registration.setInitParameter("dispatchOptionsRequest", "true");
        registration.setMultipartConfig(new MultipartConfigElement(
                null, 20_971_520L, 41_943_040L, 512_000
        ));
    }
}
