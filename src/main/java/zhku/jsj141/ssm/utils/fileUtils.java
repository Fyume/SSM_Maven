package zhku.jsj141.ssm.utils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class fileUtils {
	private static String image = "D:\\SSM_Maven\\image\\";
	public static void moveImage(MultipartFile fileName){
		if(fileName!=null){
			String originalFileName = fileName.getOriginalFilename();
			String newFileName = UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf("."));
			/*File newFile = new File(image+newFileName);
			try {
				fileName.transferTo(newFile);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}	
	}
	
}
