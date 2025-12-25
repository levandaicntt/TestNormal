package TestNG;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

public class Demo_Annotation {
  @Test (priority = 1)
  public void f1() {
	  System.out.println("Method Test 1");
  }
  @Test (priority = 2)
  public void f2() {
	  System.out.println("Method Test 2");
  }
  @BeforeMethod
  public void beforeMethod() {
	  System.out.println("Method beforeMethod");
  }

  @AfterMethod
  public void afterMethod() {
	  System.out.println("Method afterMethod");
  }

  @BeforeClass
  public void beforeClass() {
	  System.out.println("Method beforeClass");
  }

  @AfterClass
  public void afterClass() {
	  System.out.println("Method afterClass");
  }

  @BeforeTest
  public void beforeTest() {
	  System.out.println("Method beforeTest");
  }

  @AfterTest
  public void afterTest() {
	  System.out.println("Method afterTest");
  }

  @BeforeSuite
  public void beforeSuite() {
	  System.out.println("Method beforeSuite");
  }

  @AfterSuite
  public void afterSuite() {
	  System.out.println("Method afterSuite");
  }

}
