package Demo_Linktext;

import org.openqa.selenium.By;
import Initialization.*;

public class linkText extends Init {

	public static void main(String[] args) {
		SetUp("edge");
		driver.get("https://www.tutorialspoint.com/selenium/practice/links.php");
		driver.findElement(By.linkText("Created")).click();
		
		driver.findElement(By.partialLinkText("Bad")).click();
	}

}
