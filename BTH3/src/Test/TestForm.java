package Test;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import Initialization.Init;

public class TestForm extends Init {

	public static void main(String[] args) {
		SetUp("edge");
		driver.get("https://www.techlistic.com/p/selenium-practice-form.html");
		driver.manage().window().maximize();
		
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("firstname"))).sendKeys("blabla");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("lastname"))).sendKeys("blabla");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("sex-0"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("exp-0"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("datepicker"))).sendKeys("01/01/2000");
//		wait.until(ExpectedConditions.elementToBeClickable(By.id("profession-1"))).click();
		
		try {
			WebElement element = driver.findElement(By.id("profession-1"));
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(true);", element);
			element.click();
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		
		wait.until(ExpectedConditions.elementToBeClickable(By.id("tool-0"))).click();
		WebElement continentsDropdown = driver.findElement(By.id("continents"));
		Select continentSelect = new Select(continentsDropdown);
		continentSelect.selectByVisibleText("Africa");
		
		WebElement CommandsListbox = driver.findElement(By.id("selenium_commands"));
		Select CommandsSelect = new Select(CommandsListbox);
		CommandsSelect.selectByVisibleText("Browser Commands");
		CommandsSelect.selectByVisibleText("WebElement Commands");
		
		System.out.println("Chạy thành công");
	}

}
