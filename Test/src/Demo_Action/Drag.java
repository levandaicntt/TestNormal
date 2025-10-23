package Demo_Action;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import Initialization.Init;

public class Drag extends Init {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		SetUp("edge");
		driver.get("https://jqueryui.com/droppable/");
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		
		//Cuon trang 200px
		js.executeScript("window.scrollBy(0,200)");
		
		//Chuyen den iFrame
		driver.switchTo().frame(driver.findElement(By.className("demo-frame")));
		
		//Chon Element nguon
		WebElement drap = driver.findElement(By.id("draggable"));
		
		//
		WebElement drop = driver.findElement(By.id("droppable"));
		
		//Tao action tren doi tuong
		Actions act = new Actions(driver);
		act.dragAndDrop(drap, drop).perform();
		
		System.out.print("Chạy thành công");
//		Teardown();
	}

}
