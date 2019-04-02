package controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import utility.EmployeeFactory;

// https://stackoverflow.com/a/42274245/3026886

@SpringBootTest
@ContextConfiguration(classes = { app.Application.class })
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class TestController {

	@Autowired
	private WebApplicationContext context;

	@Test
	public void getEmployee() {
		RestAssuredMockMvc
		.given()
		.webAppContextSetup(context)
		.postProcessors(httpBasic("user", "user"))
		.when()
	      .get("/employee")
	    .then()
	      .log().body()
	      .statusCode(200)
	      .assertThat()
	        .body("size()", equalTo(3))
	          .and()
	        .body("[0].fullName", equalTo("John A Doe"));
	}
	
	@Test
	public void deleteEmployeeWithoutPermission() {
		RestAssuredMockMvc
		.given()
		.webAppContextSetup(context)
		.postProcessors(httpBasic("user", "user"))
		.when()
	      .delete("/employee/{id}", 100)
	    .then()
	      .statusCode(403);
	}
	
	@Test
	public void deleteEmployeeWithPermission() {
		RestAssuredMockMvc
		.given()
		.webAppContextSetup(context)
		.postProcessors(httpBasic("admin", "admin"))
		.when()
	      .delete("/employee/{id}", 100)
	    .then()
	      .statusCode(204);
	}
	
	// https://stackoverflow.com/a/39193077/3026886
	class LocalDateAdapter implements JsonSerializer<LocalDate> {
		@Override
		public JsonElement serialize(LocalDate date, Type arg1, JsonSerializationContext arg2) {
			return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
		}
	}
	
	@Test
	public void registerEmployeeWithPermission() {
		
		Gson gson = new GsonBuilder()
		        .setPrettyPrinting()
		        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
		        .create();
		
		String json = gson.toJson(EmployeeFactory.employeeResquest());
		
		RestAssuredMockMvc
		.given()
		.webAppContextSetup(context)
		.postProcessors(httpBasic("admin", "admin"))
		.contentType(ContentType.JSON)
		.body(json)
		.log().body()
		.when()
	      .post("/employee")
	    .then()
	      .log().body()
	      .statusCode(201);
		
		
		RestAssuredMockMvc
		.given()
		.webAppContextSetup(context)
		.postProcessors(httpBasic("admin", "admin"))
		.when()
	      .delete("/employee/{id}", 4)
	    .then()
	      .statusCode(204);
	}
	
	
}