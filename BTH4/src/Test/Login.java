package Test;

import java.awt.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import Initialization.Init;

public class Login extends Init{

	public static void main(String[] args) throws InterruptedException {
		SetUp("edge");
		driver.get("https://automationexercise.com/");
        driver.manage().window().maximize();
		
        WebElement btnSignUpLogin = driver.findElement(By.xpath("//a[normalize-space()='Signup / Login']"));
        btnSignUpLogin.click();
        
        WebElement isNewUserSignup = driver.findElement(By.xpath("//section[@id='form']//div[@class='col-sm-4']"));
        if (isNewUserSignup.isDisplayed())
			System.out.println("'New User Signup!' is visible"); 
        else
        	System.out.println("'New User Signup!' is invisible");
        
        WebElement userName = driver.findElement(By.xpath("//input[@placeholder='Name']"));
        userName.clear();      
        userName.sendKeys("blabla");
        
        WebElement email = driver.findElement(By.xpath("//input[@data-qa='signup-email']"));
        email.clear();      
        email.sendKeys("blabla@bla.bla");
        
        driver.findElement(By.xpath("//button[normalize-space()='Signup']")).submit();
        
        WebElement isAddressInformation = driver.findElement(By.cssSelector(".col-sm-4.col-sm-offset-1"));
        if (isAddressInformation.isDisplayed())
			System.out.println("'ENTER ACCOUNT INFORMATION' is visible"); 
        else
        	System.out.println("'ENTER ACCOUNT INFORMATION' is invisible");
        
        WebElement checkboxGenderMan = driver.findElement(By.xpath("//*[@id='id_gender1']"));
        boolean resultGenderMan = checkboxGenderMan.isSelected();
        System.out.println(resultGenderMan);
        if (!resultGenderMan)
        	checkboxGenderMan.click();
        System.out.println(resultGenderMan);
        
        WebElement password = driver.findElement(By.xpath("//*[@id='password']"));
        password.clear();      
        password.sendKeys("blabla");
        
        WebElement selectDays = driver.findElement(By.xpath("//*[@id='days']"));
        Select select = new Select(selectDays);
        select.selectByIndex(1);
//		Teardown();
//		
//		driver.quit();
	}

}
