package com.cucumber.stepdefs;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

public class CharacterImplementation {

    private static final String BASE_URI = "https://rickandmortyapi.com/api/";
    private static final String CHARACTER_ENDPOINT = "character";

    private RequestSpecification request;
    private Response response;
    private String partialName;

    private JsonPath jsonPathChars;
    List<String> jsonCharsList;
    private List<Map<String, Object>> characterResults;

    @Before
    public void before() {
        RestAssured.baseURI = BASE_URI;
    }
    @Given("the user hit the url of get characters api endpoint")
    public void theUserHitTheUrlOfGetCharactersApiEndpoint() {
        request = given();
    }
    @When("the user pass the name of a character {string}")
    public void theUserPassTheNameOfACharacter(String name) {
        response = request.param("name", name).get(CHARACTER_ENDPOINT);
    }



    @Then("the user receive the characters list")
    public void theUserReceiveTheCharactersList() {
        // Extraccion del JSON
        jsonPathChars = response.jsonPath();
        // obtiene la lista de personajes
        List<Map<String, Object>> results = jsonPathChars.getList("results");

        for (Map<String, Object> character : results) {
            printCharacterDetails(character);
        }
    }
    @Then("the user receive the response code as {int}")
    public void theUserReceiveTheResponseCodeAs(int statusCode) {
        response.then().statusCode(statusCode);
    }
    @When("the user pass the partial-name of a character {string}")
    public void theUserPassThePartialNameOfACharacter(String name) {
        partialName = name;
        response = request.param("name", partialName).get(CHARACTER_ENDPOINT);
    }

    @Then("the user receive the character suggestions")
    public void theUserReceiveCharacterSuggestions() {
        jsonPathChars = response.jsonPath();
        characterResults = jsonPathChars.getList("results");

        if (characterResults == null || characterResults.isEmpty()) {
            System.out.println("No character suggestions found.");
            return;
        }

        System.out.println("Character Suggestions:");
        List<Map<String, Object>> suggestions = filterCharacterSuggestionsByPartialName();

        if (suggestions.isEmpty()) {
            System.out.println("No character suggestions found.");
            System.out.println("Try another name.");
        } else {
            suggestions.forEach(character ->
                    System.out.println("- " + character.get("name"))
            );
        }
    }

    private List<Map<String, Object>> filterCharacterSuggestionsByPartialName() {
        // Imprimir el partialName para depuración
        System.out.println("Partial Name: " + partialName);

        Pattern pattern = Pattern.compile("^" + Pattern.quote(partialName), Pattern.CASE_INSENSITIVE);
        return characterResults.stream()
                .filter(character -> pattern.matcher((String) character.get("name")).find())
                .collect(Collectors.toList());
    }
    private void printCharacterDetails(Map<String, Object> character) {
        String name = (String) character.get("name");
        String gender = (String) character.get("gender");
        String origin = ((Map<String, String>) character.get("origin")).get("name");
        String status = (String) character.get("status");
        String species = (String) character.get("species");
        String location = ((Map<String, String>) character.get("location")).get("name");
        String image = (String) character.get("image");
        List<String> episodes = ((List<String>) character.get("episode")).stream()
                .map(url -> url.substring(url.lastIndexOf('/') + 1))
                .collect(Collectors.toList());

        // Definir e imprimir los episodios correctamente
        String episodeNumbers = String.join(", ", episodes);

        System.out.println("Name: " + name);
        System.out.println("Status: " + status);
        System.out.println("Species: " + species);
        System.out.println("Gender: " + gender);
        System.out.println("Origin: " + origin);
        System.out.println("Location: " + location);
        System.out.println("Image: " + image);
        System.out.println("Episodes: " + episodeNumbers);
        System.out.println();
    }

    @When("the user pass the id of a character {int}")
    public void theUserPassTheIdOfACharacter(int id) {
        response = request.get(CHARACTER_ENDPOINT + "/" + id);

    }

    @Then("the response bring a result")
    public void theResponseBringResult() {

        jsonPathChars = response.jsonPath();
        // Obtiene el mapa que representa el objeto JSON del personaje
        Map<String, Object> character = jsonPathChars.getMap("");
        // Verifica que se haya encontrado un personaje
        assertNotNull("No character found in the response.", character);

    }

    @Then("the response bring the details with the name {string}")
    public void theResponseBringTheDetailsWithTheName(String expectedName) {
        response.then().statusCode(200); // Verifica que el código de estado sea 200 OK

        String actualName = response.jsonPath().getString("name");
        assertEquals("El nombre del personaje no coincide", expectedName, actualName);
    }

    @Then("the response bring all the details")
    public void theResponseBringAllTheDetails() {
        response.then().statusCode(200); // Verifica que el código de estado sea 200 OK

        // Obtiene los detalles del personaje de la respuesta
        jsonPathChars = response.jsonPath();
        Map<String, Object> characterDetails = jsonPathChars.getMap("");
        printCharacterDetails(characterDetails);
    }
}
