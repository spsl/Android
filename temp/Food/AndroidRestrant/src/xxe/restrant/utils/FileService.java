package xxe.restrant.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import xxe.restrant.show.DownloadInfo;

import android.content.Context;

/**
 * 
 * @author �ļ���д������
 *
 */
public class FileService {
	private static final String FILE_NAME = "ip.txt"; //�ļ���
	private static Context context;
	
	public FileService(Context context) {
		super();
		this.context = context;
	}
	
	public FileService() {
		super();
	}

	/**
	 * �ļ�������
	 * @return
	 */
	public static String read() {
		try {
			FileInputStream fis = context.openFileInput(FILE_NAME);
     
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			return new String(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
 
/**
    * �ļ�д����
    * @param content
    */
	public static void write(String content) {
		try {

			FileOutputStream fos = context.openFileOutput(FILE_NAME,context.MODE_PRIVATE);

			fos.write(content.getBytes());
			fos.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
