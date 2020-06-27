package com.starwarsplanetapi;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.starwarsplanetapi.exception.PlanetNotFoundException;
import com.starwarsplanetapi.model.Planet;
import com.starwarsplanetapi.resource.PlanetResource;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class PlanetResourceTest {
	@Autowired
	PlanetResource resource;
	
	//Como são testes de integração necessito que os mesmo sejam executados em uma ordem especifica por isso o nome não está seguindo o padrão createPlanetTest por exemplo
	
	@Test
	public void test1CreatePlanet() {
		Planet planetAux = new Planet("Yavin IV", "temperate, tropical",  "jungle, rainforests");
		ResponseEntity<Planet> response = resource.addNewPlanet(planetAux);
		assertNotNull(response.getBody().getPlanetId());
	}
	
	@Test
	public void test2ListAllPlanets() {
		List<Planet> planets = resource.getAllPlanets();
		assertNotNull(planets);
	}
	
	
	@Test
	public void test3GetPlanetId() {
		ResponseEntity<Planet> response = resource.findPlanetByName("Yavin IV");
		assertNotNull(resource.getPlanet(response.getBody().getPlanetId()).getBody());
	}
	
	@Test
	public void teste4GetPlanetByName() {
		ResponseEntity<Planet> response = resource.findPlanetByName("Yavin IV");
		assertNotNull(response.getBody());
	}
	
	@Test
	public void teste5DeletePlanetById() throws PlanetNotFoundException {
		ResponseEntity<Planet> resp = resource.findPlanetByName("Yavin IV");
		resource.removePlanet(resp.getBody().getPlanetId());
		resp = resource.findPlanetByName("Yavin IV");
		assertNull(resp.getBody());
		
	}

}
