package Page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
	private WebDriver driver;
	
	public LoginPage(WebDriver driver) {
		this.driver = driver;
	}
	
	By myaccount = By.linkText("My Account");
	By usernameLocator = By.name("email_address");
	By passwordLocator = By.name("password");
	By loginButtonLocator = By.xpath("//*[@if='tfb5'");
	
	//click chuột vào nút My Account
	public LoginPage clickMyAccount() {
		driver.findElement(myaccount).click();
		return this;
	}
	
	//nhập UserName
	public LoginPage typeUsername(String username) {
		driver.findElement(usernameLocator).sendKeys(username);
		return this;
	}
	
	//nhập Password
	public LoginPage typePasword(String password) {
		driver.findElement(passwordLocator).sendKeys(password);
		return this;
	}
	
	//nhấn nút Submit
	public LoginPage submitLogin() {
		driver.findElement(loginButtonLocator).submit();
		return this;
	}
	
	//xác thực
	public boolean validateLogin(String text) {
		if (driver.getPageSource().contains(text)) {
			return true;
		} else {
			return false;
		}
	}
	
	//thực hiện thao tác Login
	public LoginPage loginAs(String username, String password) {
		typeUsername(username);
		typePasword(password);
		return submitLogin();
	}
}
