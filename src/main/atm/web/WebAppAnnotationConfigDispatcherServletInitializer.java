package main.atm.web;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import main.atm.configuration.AppConfig;
import main.atm.configuration.WebAppConfig;
import main.atm.initialize.web.PropertyLoaderServletContextListener;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.opensymphony.sitemesh.webapp.SiteMeshFilter;
import com.trgr.cobalt.infrastructure.logging.LoggingInitializerServletContextListener;

public class WebAppAnnotationConfigDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	/*
	* (non-Javadoc)
	* @see
	* org.springframework.web.servlet.support.AbstractDispatcherServletInitializer#onStartup(javax.servlet.ServletContext
	* )
	*/
	@Override
	public void onStartup(ServletContext container) throws ServletException {
		//add property loader context listener to the container
		container.addListener(PropertyLoaderServletContextListener.class);

		//add logging initializer context listener to the container
		container.addListener(LoggingInitializerServletContextListener.class);

		//add SiteMesh filter, note that it doesn't support ASYNC dispatcher type at the time this was added
		FilterRegistration.Dynamic sitemeshFilterReg = container.addFilter("sitemeshFilter", new SiteMeshFilter());
		sitemeshFilterReg.addMappingForUrlPatterns(null, true, "/*");

		//this call needs to be here at the end of this method otherwise the property loader will fire after the
		//container starts up!
		super.onStartup(container);
	}

	/*
	* (non-Javadoc)
	* @see
	* org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer#getRootConfigClasses
	* ()
	*/
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { AppConfig.class };
	}

	/*
	* (non-Javadoc)
	* @see
	* org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer#getServletConfigClasses
	* ()
	*/
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebAppConfig.class };
	}

	/*
	* (non-Javadoc)
	* @see org.springframework.web.servlet.support.AbstractDispatcherServletInitializer#getServletMappings()
	*/
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
}
