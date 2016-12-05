import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

/**
 * 将xml的数据转为json数据
 * 
 * @author zhengxr 上午9:55:10
 */
public class Xml2Json {

	/**
	 * xml转为json
	 * 
	 * @param xmlFile
	 * @param jsonFile
	 */
	@SuppressWarnings({ "resource", "static-access" })
	public static void trans(File xmlFile, File jsonFile) {
		if (!xmlFile.exists()) {
			System.out.println("xml文件不存在！");
			return;
		}

		if (!jsonFile.exists()) {
			try {
				jsonFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(xmlFile));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		StringBuilder tmp = new StringBuilder();
		String str = null;
		try {
			while ((str = bufferedReader.readLine()) != null) {
				tmp.append(str);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		XML xml = new XML();
		FileOutputStream fileOutputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		try {
			JSONObject j = xml.toJSONObject(tmp.toString());
			String jsonStr = j.toString();
			byte[] mByte = jsonStr.getBytes();

			fileOutputStream = new FileOutputStream(jsonFile);
			// fileOutputStream.write(mByte);
			bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			bufferedOutputStream.write(mByte);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileOutputStream.close();
				bufferedOutputStream.flush();
				bufferedOutputStream.close();
				System.out.println("--->json转换完成，刷新项目即可看到！");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
