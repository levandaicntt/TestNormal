package Demo_Elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;

public class Element_Method_2 {

	public static void main(String[] args) {
		WebDriver driver = new EdgeDriver();
        driver.get("https://the-internet.herokuapp.com/checkboxes");
        driver.manage().window().maximize();
        
        WebElement checkbox1 = driver.findElement(By.xpath("//input[1]"));
        boolean result1 = checkbox1.isSelected();
        System.out.print(result1);
        if (result1==false)
        	checkbox1.click();
        
        boolean result2 = checkbox1.isSelected();
        System.out.print(result2);
        
        boolean result3 = checkbox1.isDisplayed();
        System.out.print(result3);
        
        boolean result4 = checkbox1.isEnabled();
        System.out.print(result4);
	}

}
