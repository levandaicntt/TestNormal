package Demo_xpart;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Initialization.Init;

public class xpartCheckbox extends Init{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SetUp("edge");
		driver.get("https://web.facebook.com/r.php?locale=en_US&_rdc=1&_rdr#");
		WebElement rdNam = driver.findElement(By.xpath("//*[@id='sex' and @value='2']"));
		System.out.println(rdNam.isSelected());
		rdNam.click();
		System.out.println(rdNam.isSelected());
	}

}
