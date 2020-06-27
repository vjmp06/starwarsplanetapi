package com.starwarsplanetapi.resource;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.starwarsplanetapi.config.RestClient;
import com.starwarsplanetapi.dal.PlanetDAL;
import com.starwarsplanetapi.exception.PlanetNotFoundException;
import com.starwarsplanetapi.model.Planet;
import com.starwarsplanetapi.model.SWAPI;
import com.starwarsplanetapi.repository.PlanetRepository;

@RestController
@RequestMapping(value = "/planet")
public class PlanetResource {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	private final PlanetRepository planetRepository;

	private final PlanetDAL planetDAL;
	
	public PlanetResource(PlanetRepository planetRepository, PlanetDAL planetDAL) {
		this.planetRepository = planetRepository;
		this.planetDAL = planetDAL;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<Planet> addNewPlanet(@RequestBody Planet planet) {
		LOG.info("Saving planet.");
		Integer countFilms  = this.consumoServiceRest(planet.getName());
		planet.setCountFilms(countFilms);
		Planet result = planetRepository.save(planet);
		if(result == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/findAll", method = RequestMethod.GET)
	public List<Planet> getAllPlanets() {
		LOG.info("Getting all planets.");
		return planetRepository.findAll();
	}

	
	@RequestMapping(value = "/{planetId}", method = RequestMethod.GET)
	public ResponseEntity<Planet> getPlanet(@PathVariable String planetId) {
			Planet result = planetRepository.findById(planetId).get();
			if(result == null) {
				return ResponseEntity.notFound().build();
			}
			LOG.info("Getting planet with ID: {}.", planetId);
			return ResponseEntity.ok(result);
		
	}
	
	@RequestMapping(value = "/remove/{planetId}", method = RequestMethod.DELETE)
	public void removePlanet(@PathVariable String planetId) throws PlanetNotFoundException {
				ResponseEntity<Planet> existPlanet = this.getPlanet(planetId);
				if (!existPlanet.hasBody()) {
					throw new PlanetNotFoundException();
				}
				LOG.info("Remove planet with ID: {}.", planetId);
				planetRepository.deleteById(planetId);
				
			
			
	}
	
	@GetMapping()
	public ResponseEntity<Planet> findPlanetByName(@RequestParam(name = "planetName") String planetName) {
		Planet result = planetDAL.getPlanetByName(planetName);
		if(result == null) {
			return ResponseEntity.notFound().build();
		}
		LOG.info("Getting planet with name: {}.", planetName);
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/consumoRest", method = RequestMethod.GET)
	public Integer consumoServiceRest(String planetName) {
		 RestClient restClient = new RestClient();
		 //A https://swapi.dev/ está com um issue aberto no github sobre um problema com redirecionamento https://github.com/Juriy/swapi/issues/6, a chamada a api seria conforme abaixo
		 //return restClient.get("/planets?search=" + planetName).getResults().get(0).getFilms().size();
		 
		 return restClient.get("/planets").getResults().get(0).getFilms().size(); 
	}

}