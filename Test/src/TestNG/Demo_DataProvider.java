package TestNG;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import Initialization.Init;

public class Demo_DataProvider extends Init{
	  @Test(dataProvider = "dp")
	  public void validLogin(String User, String pwd) {
		  driver.get("https://practice.bpbonline.com/");
		  
		  //tìm và click My Account
		  driver.findElement(By.linkText("My Account")).click();
		  
		  //tìm và nhập Email, password
		  driver.findElement(By.name("email_address")).sendKeys("User");
		  driver.findElement(By.name("password")).sendKeys("pwd");
		  
		  //tìm và click Sign in
		  driver.findElement(By.xpath("//*[@id='tdb5']")).click();
	      if (driver.getPageSource().contains("My Account Information")) {
	          driver.findElement(By.linkText("Log Off")).click();
	          driver.findElement(By.linkText("Continue")).click();
	          Reporter.log("Dang nhap thanh cong");
	      } else {
	          Reporter.log("Dang nhap that bai");
	      }
	  }
	  
	  @BeforeMethod
	  @Parameters({"browser"})
	  public void beforeMethod(String brow) {
		  //SetUp("chrome");
		  SetUp(brow);
	  }

	  @AfterMethod
	  public void afterMethod() throws InterruptedException{
		  Teardown();
	  }
	  
	  @DataProvider
	  public Object[][] dp() {
		  String [][] data = new String [2][2];
		  //đọc dữ liệu từ file
		  int i = 0;
		  try {
			  FileReader readerObj = new FileReader("DataFiles\\login.csv");
			  BufferedReader reader = new BufferedReader(readerObj);
			  //đọc từng dòng
			  String line = reader.readLine();
			  while (line!=null) {
				  //tách username và password
				  String [] st = line.split(",");
				  data[i][0] = st[0]; //username
				  data[i][1] = st[1]; //password
				  
				  line = reader.readLine();
				  i++;
			  }
		  }catch (IOException e) {
			  //TODO: handle exception
			  e.printStackTrace();
		  }
		  
		  return data;
	  }
	}