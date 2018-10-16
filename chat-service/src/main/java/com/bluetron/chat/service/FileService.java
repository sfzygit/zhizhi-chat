package com.bluetron.chat.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bluetron.chat.utils.Base64;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

@Service
@Slf4j
public class FileService {

	@Value("${windows.image.save.path}")
	String windowsSaveImagePath;
	
	@Value("${linux.image.save.path}")
	String linuxSaveImagePath;
	
	@Value("${resource.image.path}")
	String resourcePath;
	
	/**
	 * Save file to local server
	 * @param toSavefile
	 * @return return file load path
	 */
	public String saveToLoalServer(MultipartFile toSavefile) {
		
		String fileName = toSavefile.getOriginalFilename();
		String saveImagePath = "";
		String os = System.getProperty("os.name");
		if(os.toLowerCase().contains("win")) {
			
			saveImagePath = windowsSaveImagePath;
		}else {
			
			saveImagePath = linuxSaveImagePath;
		}
		String fileFullPath = saveImagePath+File.separator+fileName;
		log.info("save file path: "+fileFullPath);
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		try {
			File dirfile = new File(saveImagePath);
			if(!dirfile.exists()) {
				dirfile.mkdirs();
			}
			File file = new File(fileFullPath);
			if(!file.exists()) {
				file.createNewFile();
			}
			fileOutputStream = new FileOutputStream(file);
			inputStream = toSavefile.getInputStream();
			byte[] tempData = new byte[1024];
			int len = -1;
			while((len=inputStream.read(tempData))!=-1) {
				fileOutputStream.write(tempData);
			}
			
			log.info("save file seccess: "+fileFullPath);
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			
			if(fileOutputStream!=null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(inputStream!=null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	
		return resourcePath+fileName;
	}
	
	/**
	 * Save file to remote server
	 * @param toUploadFile
	 * @return
	 */
	public String uploadFileToRemoteServer(MultipartFile toUploadFile) {
		String filePath = "";
		//TO-DO
		return filePath;
		
	}
	
	
	public String  imageBase64code(MultipartFile inputFile, String saveFileType) {
		String imageBase64 = "";
		// get thumnail image (240*240)
		int width = 240;
		int height = 240;
		FileInputStream  fileInputStream = null;
		String  tmpFileFullName = "c:\\data\\chat-service\\tmp\\"+Calendar.getInstance().getTimeInMillis()+saveFileType;
		try {
			Thumbnails.of(inputFile.getInputStream()).size(width, height).toFile(tmpFileFullName);
			File file = new File(tmpFileFullName);
			fileInputStream = new FileInputStream(file);
			byte[] data = new byte[fileInputStream.available()];
			fileInputStream.read(data);
			imageBase64 = imageBase64 = Base64.encode(data);
			
			//delete temp image
			//file.delete();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(fileInputStream!=null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		return imageBase64;
	} 
	
}
