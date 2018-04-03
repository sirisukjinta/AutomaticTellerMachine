package main.atm.service;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

public class DozerMapperService{
	private static Logger log = Logger.getLogger(DozerMapperService.class);
	@Autowired
	private DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean;
	
	DozerBeanMapper mapper = null;

	public <Source, Dest> Dest mapObject(Source sourceObject,
			Class<Dest> clazz) {
		Dest dest = null;

		try {
			dest = (Dest) getMapper().map(sourceObject, clazz);
		} catch (Exception e) {			
			log.error("Exception: " + e.getMessage(), e);
			throw new RuntimeException("DozerMapperService:Collection: Error while perfrming Dozer mapping");
		}
		return dest;
	}


	public <Source, Dest> Collection<Dest> mapCollection(
			Collection<Source> source, Class<Source> sourceClass,
			Class<Dest> destClass) {
		Collection<Dest> destCollection = new ArrayList<Dest>();
		try {
			for (Source element : source) {
				destCollection.add(getMapper().map(element, destClass));
			}
		} catch (Exception e) {			
			log.error("Exception: " + e.getMessage(), e);
			throw new RuntimeException("DozerMapperImpl:Collection: Error while perfrming Dozer mapping");
		}
		return destCollection;
	}

	public DozerBeanMapper getMapper() throws Exception {

		if (mapper == null && dozerBeanMapperFactoryBean != null) {
			mapper = (DozerBeanMapper) dozerBeanMapperFactoryBean.getObject();
		}

		return mapper;
	}

}
