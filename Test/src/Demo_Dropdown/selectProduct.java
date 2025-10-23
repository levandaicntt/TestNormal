package Demo_Dropdown;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import Initialization.Init;

public class selectProduct extends Init {

	public static void main(String[] args) throws InterruptedException {
		/*
		 * Lấy các sản phẩm theo thương hiệu
		 */
		SetUp("edge");
        driver.get("https://practice.bpbonline.com/");
        driver.manage().window().maximize();
        
        //B1: chọn hãng sản xuát
        WebElement manufacturers = driver.findElement(By.name("manufacturers_id"));
        Select select = new Select(manufacturers);
        
        //Lấy tên của các hãng sx
        List<WebElement> manus = select.getOptions();
//      for (WebElement manu:manus)
//        	System.out.println(manu.getText());
        
        //Bỏ phần tử đầu tiên
        manus.remove(0);
        
        ArrayList<String> list_manus = new ArrayList<String>();
        for (WebElement manu:manus) {
        	list_manus.add(manu.getText());
        }	
        
        for (String item:list_manus) {
        	try {
        		//Chọn từng hãng sản xuất
        		select.selectByVisibleText(null);       	
        		
        		//Xử lý trạng thái thay đổi - các sản phẩm load theo hãng sản xuất
        		manufacturers = driver.findElement(By.name("manufacturers_id"));
        		String st = "There are no products available in this category.";
        		if (driver.getPageSource().contains(st))
        			System.out.println("Hang san xuat " + item +  " Khong co san pham");
        		else
        		{
        			System.out.println("-------------------------------------------");
        			System.out.println("Hang san xuat " + item +  " co cac san pham");
        		}
        	} catch (StaleElementReferenceException e) {
        		e.toString();
        		System.out.println("Loi: " + e.getMessage());
        	}
        }
        Teardown();
	}

}
