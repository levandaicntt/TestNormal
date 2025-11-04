package Demo_ScreenShot;

import Initialization.Init;
import java.io.*;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ScreenShot extends Init{
	public static void main(String[] args) throws InterruptedException, IOException {
		/*
		 * Kiểm tra chức năng đăng nhập. Nếu đăng nhập thành công hay thất bại thì chụp màn hình
		 */
		SetUp("edge");
		driver.get("http://practice.bpbonline.com/");
		//Đọc dữ liệu từ file
		FileReader readerObj = new FileReader("DataFiles\\Login.csv");
		BufferedReader reader = new BufferedReader(readerObj);
		String line = reader.readLine();
		
		//Kiểm tra từng username và password
		while (line != null) {
			//Tách username và password
			String []st = line.split(",");
			
			driver.findElement(By.linkText("My Account")).click();
			driver.findElement(By.name("email_address")).sendKeys(st[0]);
			driver.findElement(By.name("password")).sendKeys(st[1]);
			
			driver.findElement(By.id("tdb5")).click();
			
			if (driver.getPageSource().contains("My Account Information")) {
				//Đăng nhập thành công
				//Chụp lại màn hình
				File screen = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				//Chuyển đổi sang dạng file .jpg và lưu vào thư mục ScreenShots
				String name = "ScreenShots\\" + st[0] + ".jpg";
				FileUtils.copyFile(screen, new File(name));
				
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
				WebElement log_off = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Log Off")));
				log_off.click();
				
				WebElement btnContinue = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Continue")));
				btnContinue.click();
			} else {
				//Đăng nhập không thành công
				//Chụp lại màn hình
				File screen = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				//Chuyển đổi sang dạng file .jpg và lưu vào thư mục ScreenShots
				String name = "ScreenShots\\" + st[0] + ".jpg";
				FileUtils.copyFile(screen, new File(name));
			}
				
			
			line = reader.readLine();
		}
		
		Teardown();
	}
}
