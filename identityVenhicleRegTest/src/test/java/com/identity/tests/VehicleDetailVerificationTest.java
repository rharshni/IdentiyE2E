package com.identity.tests;

import java.io.IOException;
import java.util.HashMap;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.identity.pages.HomePage;
import com.identity.pages.VehicleResultsPage;
import com.identity.pages.VehicleServicesPage;
import com.identity.tests.BaseTests.BaseTest;
import com.identity.utils.FileService;

import junit.framework.Assert;



public class VehicleDetailVerificationTest extends BaseTest {
	private static String browser;
	HomePage homePage = null;

	

	
	@BeforeClass
	public void setUp() {
		try {
			parent();
					
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	 @DataProvider(name = "vehicleData")
	  
	  public static Object[][] credentials() {
	 
	     // For the number of rows of data present in the excel, test will be executed the same no. of times
		 FileService fileService = new FileService();
		
		 Object[][] testObjArray = null;
		
		 try {
			testObjArray = fileService.readExcel("C:\\identityTestData","vehicleTestdata.xlsx");
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
         return testObjArray;
	      
	  }
	
	@Test(dataProvider = "vehicleData")
	
	public void assertVehicleInfo(String vehicleRegistrationNum, String make, String colour){
		try {
			
			homePage = navigateToHomePage();
			//clicking start button
			VehicleServicesPage vehicleServicesPage = homePage.clickStartNowButton();
		 
			//enter the vehicle Registration Number to get the details
			VehicleResultsPage vehicleResultsPage = vehicleServicesPage.enterVehicleRegistrationAndSubmit(vehicleRegistrationNum);
			
			HashMap<String, String>vehicleDetailsMap = vehicleResultsPage.returnVehicleDetails();
			
			//asserting the vehicle make 
			Assert.assertEquals(make, vehicleDetailsMap.get("make"));
			
			//asserting the vehicle colour 
			Assert.assertEquals(colour, vehicleDetailsMap.get("colour"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	

	@AfterClass
	public void tearDown() {
		driver.quit();
	}

}
