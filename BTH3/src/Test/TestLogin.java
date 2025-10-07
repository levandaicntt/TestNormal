package Test;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Initialization.Init;

public class TestLogin extends Init {
	public static void main(String[] args) throws InterruptedException {
		SetUp("edge");
		driver.get("https://practicetestautomation.com/practice-test-login/");
		driver.manage().window().maximize();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys("student");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).sendKeys("Password123");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("submit"))).click();
		
		String urlSuccess = driver.getCurrentUrl();
		if (urlSuccess.contains("practicetestautomation.com/logged-in-successfully/"))
			System.out.println("Dang nhap thanh cong");
		else 
			System.out.println("Dang nhap that bai");
		
		String pageSourceSuccess = driver.getPageSource();
		if (pageSourceSuccess.contains("Congratulations") || pageSourceSuccess.contains("successfully logged in"))
			System.out.println("Co chua tu 'Congratulations' hoac 'successfully logged in'");
		else
			System.out.println("Ko chua tu 'Congratulations' hoac 'successfully logged in'");
		
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Log out"))).click();
		
		driver.navigate().to("https://practicetestautomation.com/practice-test-login/");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys("incorrectUser");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).sendKeys("Password123");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("submit"))).click();
		
		WebElement errorMessagUsername = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));

		if (errorMessagUsername.isDisplayed())
			System.out.println("Da hien thi thong bao loi");
		else
			System.out.println("Ko hien thi thong bao loi");
		if (errorMessagUsername.getText().contains("Your username is invalid!"))
			System.out.println("Hien thi dung message 'Your username is invalid!'");
		else
			System.out.println(errorMessagUsername.getText());
		
		driver.navigate().to("https://practicetestautomation.com/practice-test-login/");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys("student");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).sendKeys("incorrectPassword");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("submit"))).click();
		
		WebElement errorMessagePassword = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));

		if (errorMessagePassword.isDisplayed())
			System.out.println("Da hien thi thong bao loi");
		else
			System.out.println("Ko hien thi thong bao loi");
		if (errorMessagePassword.getText().contains("Your password is invalid!"))
			System.out.println("Hien thi dung message 'Your username is invalid!'");
		else
			System.out.println(errorMessagePassword.getText());
		Teardown();
	}
}
