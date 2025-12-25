package TestNG;

import org.testng.annotations.Test;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import Initialization.Init;

public class TestLogin extends Init{
  @Test
  public void validLogin() {
	  driver.get("https://practice.bpbonline.com/");
	  
	  driver.findElement(By.linkText("My Account")).click();
		driver.findElement(By.name("email_address")).sendKeys("bpb@bpb.com");
		driver.findElement(By.name("password")).sendKeys("bpb@123");
		
		driver.findElement(By.id("tdb5")).click();
		
		if (driver.getPageSource().contains("My Account Information")) {			
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement log_off = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Log Off")));
			log_off.click();
			
			WebElement btnContinue = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Continue")));
			btnContinue.click();
			Reporter.log("Dang nhap thanh cong");
		} else {
			Reporter.log("Dang nhap that bai");
		}
  }
  
  @BeforeMethod
  @Parameters({"browser"})
  public void beforeMethod(String brow) {
	  SetUp(brow);
  }

  @AfterMethod
  public void afterMethod() throws InterruptedException {
	  Teardown();
  }

}
