package Page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LogoutPage {
	private WebDriver driver;

	public LogoutPage(WebDriver driver) {
        this.driver = driver;
       }

	By logOff = By.linkText("Log Off");
	By continueButton = By.linkText("Continue");

	public LogoutPage clickLogOff() {
		// This is the only place that "knows" how to enter a username
		driver.findElement(logOff).click();
		return this;
	}

	public LogoutPage clickContinue() {
		// This is the only place that "knows" how to enter a username
		driver.findElement(continueButton).click();
		return this;
	}

	public LogoutPage logOff() {
		clickLogOff();
		return clickContinue();
	}
}
