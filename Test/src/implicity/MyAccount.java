package implicity;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Initialization.*;

public class MyAccount extends Init {

	public static void main(String[] args) {
		SetUp("edge");
		driver.get("https://practice.bpbonline.com/");
		
		driver.findElement(By.linkText("My Account")).click();
		driver.findElement(By.name("email_address")).sendKeys("bpb@bpb.com");
		driver.findElement(By.name("password")).sendKeys("bpb@123");
		
		driver.findElement(By.id("tdb5")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement log_off = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Log Off")));
		
		log_off.click();
		
		WebElement btnContinue = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Continue")));
		btnContinue.click();
		System.out.println("Chạy thành công");
	}

}
