package api.tests;

import api.utils.RestAssuredSetup;
import org.testng.annotations.BeforeSuite;


public class BaseTest {

    @BeforeSuite
    public void initFramework() {
        RestAssuredSetup.requestSpec.toString();
    }
}
