package LoginLogoutTest_XLSX;

import org.testng.annotations.Test;

import DataHanding.DataReader;

import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;

public class LoginLogoutUsing_XLSX {
	WebDriver driver;
	@Test(dataProvider = "loginExcel")
	public void f(String user, String pw) {
		driver.get("http://practice.bpbonline.com/");
		driver.findElement(By.linkText("My Account")).click();
		driver.findElement(By.name("email_address")).sendKeys(user);
		driver.findElement(By.name("password")).sendKeys(pw);
		driver.findElement(By.xpath("//*[@id='tdb5']")).click();
		driver.findElement(By.linkText("Log Off")).click();
		driver.findElement(By.linkText("Continue")).click();
	}

	@BeforeMethod
	public void beforeMethod() {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}

	@AfterMethod
	public void afterMethod() {
		driver.close();
	}

	//Lưu ý giữa các thành phần trong csv phải ngăn cách bằng dấu phẩy thì mới đ�?c được
	@DataProvider (name ="loginExcel") 
	public Object[][] dp() throws IOException {
		String[][] data = DataReader.getExcelDataUsingPoi("Data\\login.xlsx", "sheet1"); //0 là vì file không có tiêu đ�?
		return data;
	
	}
}
