package api.utils;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;


public class RestAssuredSetup {

    public static final RequestSpecification requestSpec;
    public static final ResponseSpecification responseSpec;

    static {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(ConfigManager.get("BASE_URL"))
                .setContentType(ContentType.JSON)
                .addHeader("Accept", "application/json")
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectResponseTime(org.hamcrest.Matchers.lessThan(5000L))
                .log(LogDetail.ALL)
                .build();

    }
}
