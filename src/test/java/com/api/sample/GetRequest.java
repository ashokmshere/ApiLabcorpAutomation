package com.api.sample;

import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GetRequest {
	ObjectMapper objectMapper;
	Map<String, Object> testData, expectedGetResponse, postPayload, expectedPostResponse;

	@BeforeTest
	public void getdata() throws StreamReadException, DatabindException, IOException {
		objectMapper = new ObjectMapper();
		testData = objectMapper.readValue(
				new File(System.getProperty("user.dir") + "\\src\\test\\resources\\testData.json"), Map.class);
		expectedGetResponse = (Map<String, Object>) testData.get("expectedGetResponse");
		postPayload = (Map<String, Object>) testData.get("postRequest");
		expectedPostResponse = (Map<String, Object>) testData.get("expectedPostResponse");
	}

	@Test
	public void getRequest() {

		RestAssured.baseURI = "https://echo.free.beeceptor.com";
		given().log().all() // Logs the request details
				.when().get("/sample-request?author=beeceptor").then().log().all().statusCode(200)
				.body("host", equalTo(expectedGetResponse.get("host")))
				.body("path", equalTo(expectedGetResponse.get("path")))
				.body("ip", containsString(expectedGetResponse.get("ip").toString()))
				.body("headers.Accept-Encoding", equalTo(expectedGetResponse.get("Accept-Encoding")))
				.body("headers.Host", equalTo(expectedGetResponse.get("Host")))
				.body("headers.Accept", equalTo(expectedGetResponse.get("Accept")))
				.body("headers.User-Agent", equalTo(expectedGetResponse.get("User-Agent")))
				.body("parsedQueryParams.author", equalTo(expectedGetResponse.get("author")));
	}

	@Test
	public void postRequest() {
		RestAssured.baseURI = "https://echo.free.beeceptor.com";
		Map<String, Object> customer = (Map<String, Object>) postPayload.get("customer");
		Map<String, Object> address = (Map<String, Object>) customer.get("address");
		Map<String, Object> payment = (Map<String, Object>) postPayload.get("payment");
		List<Map<String, Object>> items = (List<Map<String, Object>>) postPayload.get("items");
		given().log().all().contentType(ContentType.JSON).body(postPayload).when()
				.post("/sample-request?author=beeceptor").then().log().all().statusCode(200)
				.body("parsedBody.order_id", equalTo(postPayload.get("order_id")))
				.body("parsedBody.customer.name", equalTo(customer.get("name")))
				.body("parsedBody.customer.email", equalTo(customer.get("email")))
				.body("parsedBody.customer.phone", equalTo(customer.get("phone")))
				.body("parsedBody.customer.address.street", equalTo(address.get("street")))
				.body("parsedBody.customer.address.city", equalTo(address.get("city")))
				.body("parsedBody.customer.address.state", equalTo(address.get("state")))
				.body("parsedBody.customer.address.zipcode", equalTo(address.get("zipcode")))
				.body("parsedBody.payment.method", equalTo(payment.get("method")))
				.body("parsedBody.payment.transaction_id", equalTo(payment.get("transaction_id")))
				.body("parsedBody.payment.amount", equalTo(payment.get("amount")))
				.body("parsedBody.payment.currency", equalTo(payment.get("currency")))
				.body("parsedBody.items[0].product_id", equalTo(items.get(0).get("product_id")))
				.body("parsedBody.items[0].name", equalTo(items.get(0).get("name")))
				.body("parsedBody.items[0].quantity", equalTo(items.get(0).get("quantity")))
				.body("parsedBody.items[0].price", equalTo(items.get(0).get("price").toString()))
				.body("parsedBody.items[1].product_id", equalTo(items.get(1).get("product_id")))
				.body("parsedBody.items[1].name", equalTo(items.get(1).get("name")))
				.body("parsedBody.items[1].quantity", equalTo(items.get(1).get("quantity")))
				.body("parsedBody.items[1].price", equalTo(items.get(1).get("price").toString()));
	}

}
