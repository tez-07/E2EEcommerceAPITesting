package Tests;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

import POJO.LoginRequest;
import POJO.LoginResponse;
import POJO.OrderDetails;
import POJO.Orders;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

//Authorization should be passed in every request after the login request because
//it identifies whether wi



public class EcomTest {

	public static void main(String[] args) {
		
		//Login Request 
		RequestSpecification reqBase = 
			new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").setContentType(ContentType.JSON).build();
		
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUserEmail("gohaintz13@gmail.com");
		loginRequest.setUserPassword("Qwerty@123");
		
		RequestSpecification reqLogin = given().log().all().spec(reqBase).body(loginRequest);			//SpecBuilder Called
		
		LoginResponse loginResponse = 
				reqLogin.when().post("/api/ecom/auth/login")
				.then().log().all().extract().response().as(LoginResponse.class);						//De_serialization  
		
		String token = loginResponse.getToken();
		String userId = loginResponse.getUserId();
	
		System.out.println(loginResponse.getToken());
		System.out.println(loginResponse.getUserId());
	
		
		
		//Add Product
		RequestSpecification createProductBase = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token)
				.build();
		
		RequestSpecification createProduct = given().spec(createProductBase)
			.param("productName", "HelloWorld2")
			.param("productAddedBy", userId)
			.param("productCategory", "fashion")
			.param("productSubCategory", "shirts")
			.param("productPrice", "1720")
			.param("productDescription", "Addias Originals")
			.param("productFor", "women")
			.multiPart("productImage", new File("/Users/tezborgohain/Pictures/Free-Stock-Photos-01.jpg"));
			
			//importing image using multi_part: important interview question 
		
		
		String addProductResponse = createProduct.when().post("api/ecom/product/add-product")
				.then().log().all().extract().response().asString();
			
		JsonPath js = new JsonPath(addProductResponse);
		String productId = js.get("productId");
		System.out.println(productId);
		
		
		
		//Place an Order
		RequestSpecification createOrderBase = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token)
				.setContentType(ContentType.JSON)
				.build();
		
		//Set the items first and then Add to a list because the original setter method needs a list because of array
		OrderDetails details = new OrderDetails();
		details.setCountry("India");
		details.setProductOrderedId(productId);
		
		List<OrderDetails> list = new ArrayList<OrderDetails>();
		list.add(details);
		
		Orders orders = new Orders();
		orders.setOrders(list);
		
		RequestSpecification createOrderReq = given().spec(createOrderBase).body(orders);
		
		String createOrderResponse = createOrderReq.when().post("api/ecom/order/create-order")
		.then().log().all().extract().response().asString();
		
		
		System.out.println(createOrderResponse);
			
		
		//Delete Product created
		RequestSpecification deleteBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token)
				.build();
		
		
		RequestSpecification deleteReq = given().spec(deleteBaseReq).pathParam("productId", productId);
		
		String deleteResponse = deleteReq.when().delete("https://rahulshettyacademy.com/api/ecom/product/delete-product/{productId}")
		.then().log().all().extract().response().asString();
		
		JsonPath js1 = new JsonPath(deleteResponse);
		Assert.assertEquals("Product Deleted Successfully", js1.get("message"));
		System.out.println(deleteResponse);
		
	}

}
