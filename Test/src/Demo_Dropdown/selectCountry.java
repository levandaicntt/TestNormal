package Demo_Dropdown;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import Initialization.Init;

public class selectCountry extends Init {

	public static void main(String[] args) throws InterruptedException {
		SetUp("edge");
        driver.get("https://demo.guru99.com/test/newtours/register.php");
        driver.manage().window().maximize();
        
        WebElement element = driver.findElement(By.xpath("//select[@name='country']"));
        Select select = new Select(element);
        
//        select.selectByIndex(1);
//        System.out.println("Da chon ALGERIA");
        
//        select.selectByValue("ARGENTINA");
        
//        select.selectByVisibleText("VIETNAM");
        
        System.out.println(select.getOptions().size());
        
        String st = select.getFirstSelectedOption().getText();
        System.out.println(st);
        
        if (st.equals("ALBANIA"))
        	System.out.println("Dung ALBANIA");
        else
        	System.out.println("Sai ALBANIA");
        
        List<WebElement> getAllOption = select.getOptions();
        for (int i = 0; i<getAllOption.size(); i++)
        	System.out.println(getAllOption.get(i).getAttribute("value"));
	}

}
