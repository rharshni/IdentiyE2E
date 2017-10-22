package com.identity.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileService {
	
	//PART 1 - service method to get filename, filesize, file extension and mime type for all the files within a folder or directory
	public  HashMap <String,FileMetadataVO> scanConfiguredDirectoryForFiles(String directoryPath) throws IOException {
		 File parentDirectory = new File(directoryPath);
		 HashMap <String,FileMetadataVO> fileDetailsMap = new HashMap<String,FileMetadataVO>();
		 
		 //go through every entery in the given directory and collect the details
		 for (final File fileEntry : parentDirectory.listFiles()) {
			 
			 //enter the if condition only if the current entry is a file
		        if (!fileEntry.isDirectory()) {
		        	
		              String fileName = fileEntry.getName();
		              long fileLength = fileEntry.length();
		              String fileType = Files.probeContentType(fileEntry.toPath());
		              String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
		              System.out.println("filename, fileLength, fileType, Externsion and path are - " + fileName+ "," +fileLength + ","+ fileType +  ","+ fileExtension + "," + fileEntry.toPath());
		              
		              //setting the values into the FileMetadataVOObject
		              FileMetadataVO FileMetadataVO = new FileMetadataVO();
		              FileMetadataVO.setFileName(fileName);
		              FileMetadataVO.setFileLength(fileLength);
		              FileMetadataVO.setFileType(fileType);
		              FileMetadataVO.setFileExtension(fileExtension);
		              FileMetadataVO.setFilePath(fileEntry.toPath());
		              
		              //setting the FileMetadataVO object into the HASHMap
		              fileDetailsMap.put(fileName, FileMetadataVO);
		              
		        } else {
		        	//if the entry is a directory, just ignore it.
		        	System.out.println("ignoring since it is a directory");
		        }
		        
		    }
		 return fileDetailsMap;
	}
	
	//PART1 -service to retrieve files based on the fileExtension in a given directory
	public HashMap<String, File> retrieveFilesByMimeTypeFromTheDirectory(String directoryPath) {
		HashMap<String, File> fileMap = new HashMap<String, File>();
		try {
			HashMap <String,FileMetadataVO> fileDetailsMap = this.scanConfiguredDirectoryForFiles(directoryPath);
			//loop through every file metadata to retrieve excel or CSV
			for(FileMetadataVO FileMetadataVO : fileDetailsMap.values()){
				if (FileMetadataVO.getFileExtension().equals("xlsx") || FileMetadataVO.getFileExtension().equals("xls") || FileMetadataVO.getFileExtension().equals("csv")){
					File file = new File(FileMetadataVO.getFilePath().toString());
					fileMap.put(FileMetadataVO.getFileName(), file);
				}
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fileMap;
		
	}
	
	public static void main(String[] args) {
		try {
		FileService fileService = new FileService();
		fileService.readExcel("C:\\identityTestData","vehicleTestdata.xlsx");
		
		
		}catch (Exception e){
		e.printStackTrace();
		}
	}
	
	
	//PART2 - read the excel file for the vehicle registration details
	
	public String[][] readExcel(String directoryPath, String inputExcelFileName) throws IOException {
		
		String[][] tabArray = null;
		
		try{
	
			HashMap<String, File> fileMap = this.retrieveFilesByMimeTypeFromTheDirectory(directoryPath);
	
			File file = null;
	
			for (String filename : fileMap.keySet()) {
				if (filename.equals(inputExcelFileName)) {
					file = fileMap.get(filename);
				}
			}
	
			// Create an object of FileInputStream class to read excel file
	
			FileInputStream inputStream = new FileInputStream(file);
	
			Workbook inputWorkbook = new XSSFWorkbook(inputStream);
	
			// Read sheet inside the workbook by its name
	
			Sheet inputSheet = inputWorkbook.getSheet("VehicleInfo");
	
			// Find number of rows in excel file
	
			int startRow = 1;
	
			int startCol = 0;
	
			int totalRows = inputSheet.getLastRowNum();
			System.out.println("total rows are -" + totalRows);
	
			// you can write a function as well to get Column count
	
			int totalCols = 3;
	
			tabArray = new String[totalRows][totalCols];
	

			for (int i = startRow; i <=totalRows; i++) {
	
				
	
				for (int j = startCol; j < totalCols; j++) {
	
					tabArray[i-1][j] = getCellData(i, j, inputSheet);
	
					System.out.println(tabArray[i-1][j]);
	
				}
	
			}
	
		} catch(FileNotFoundException e){

			System.out.println("Could not read the Excel sheet");
	
			e.printStackTrace();

		} catch(IOException e){

			System.out.println("Could not read the Excel sheet");
	
			e.printStackTrace();

	    } catch(Exception e){

			System.out.println("Could not read the Excel sheet");
		
			e.printStackTrace();

	   }

	return(tabArray);

	}
	 
	 
	public static String getCellData(int RowNum, int ColNum, Sheet inputSheet) throws Exception {

		try{

			Cell cell = inputSheet.getRow(RowNum).getCell(ColNum);

			int dataType = cell.getCellType();

			if  (dataType == 3) {

				return "";

			}else{

				String CellData = cell.getStringCellValue();

				return CellData;
			}

		}catch (Exception e){

			System.out.println(e.getMessage());

			throw (e);

		}

	}

	

}
