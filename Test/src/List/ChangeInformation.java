package List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Initialization.Init;

public final class ChangeInformation extends Init {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		/*
		 * B1: Mo trang web https://practice.bpbonline.com/index.php
		 * B2: Click vao "My Account"
		 * B3: Nhap Email, Password
		 * B4: Click vao vut "Sign in"
		 * B5: Click vao "View or change my account information."
		 * B6: Thay doi thong tin ca nhan
		 * B7: Click "Continue"
		 * B8: Xac thuc thay doi thong tin thanh cong: "Your account has been successfully updated."
		 * B9: Click vao nut "Log Off"
		 * B10: Click vao nut "Continue"
		 */
		SetUp("edge");
		driver.get("https://practice.bpbonline.com/index.php");
		
		driver.findElement(By.linkText("My Account")).click();
		driver.findElement(By.name("email_address")).sendKeys("bpb@bpb.com");
		driver.findElement(By.name("password")).sendKeys("bpb@123");
		
		driver.findElement(By.id("tdb5")).click();
		
		driver.findElement(By.partialLinkText("my account information.")).click();
		
		WebElement rdNam = driver.findElement(By.xpath("@name = 'gender' & @value = 'm'"));
		WebElement rdNu = driver.findElement(By.xpath("@name = 'gender' & @value = 'f'"));
		
		if (rdNam.isSelected())
			rdNu.click();
		else
			rdNam.click();
		
		WebElement telephone = driver.findElement(By.name("telephone"));
		telephone.clear();
		telephone.sendKeys("duoi 100% deu la 50 50");
		
		driver.findElement(By.xpath("//button[@id='tdb5']")).click();
		
		String st = driver.findElement(By.xpath("//td[@class='messageStackSuccess']")).getText();
		if (st.contains("Your account has been successfully updated."))
			System.out.println("Thay doi thong tin thanh cong!");
		else
			System.out.println("Thay doi thong tin that bai!");
		Teardown();
	}

}
