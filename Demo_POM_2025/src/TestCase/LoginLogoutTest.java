package TestCase;

import org.testng.annotations.Test;

import Base.BaseFile;
import Page.LoginPage;
import Page.LogoutPage;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;

public class LoginLogoutTest {
	private WebDriver driver;
  @Test
  public void testLogin() {
	  driver.get("https://practice.bpbonline.com/");
	  LoginPage login = new LoginPage(driver);
	  LogoutPage logout = new LogoutPage(driver);
	  
	  login.clickMyAccount();
	  login.loginAs("bpb@gmail.com", "bpb@123");
	  assertTrue(login.validateLogin("My Account Information"));
	  logout.clickLogOff();
  }
  @BeforeMethod
  @Parameters({"browser"})
  public void setUpBrowser(String brow) {
	  BaseFile.SetUp(brow);
	  driver = BaseFile.driver;
  }

  @AfterMethod
  public void Teardown() {
	  driver.close();
  }

}
