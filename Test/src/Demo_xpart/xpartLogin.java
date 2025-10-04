package Demo_xpart;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Initialization.Init;

public class xpartLogin extends Init {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SetUp("edge");
		driver.get("https://www.facebook.com/");
		WebElement login = driver.findElement(By.xpath("//*[contains(@id, 'u_0_5')]"));
		login.click();
//		System.out.println(login.getText());
	}

}
