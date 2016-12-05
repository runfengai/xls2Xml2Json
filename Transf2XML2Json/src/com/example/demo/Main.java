package com.example.demo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * 将xls的数据转为xml、json数据,master中修改
 * 
 * @author runfengai
 */
public class Main {
	// 实体装载
	static ArrayList<Area> areas = new ArrayList<Area>();
	static ArrayList<Area> areas2 = new ArrayList<Area>();

	/**
	 * 执行之后，f5刷新即可看到xml以及json文件
	 * @param args
	 */

	public static void main(String[] args) {

		File xlsFile = new File("area.xls");
		File xmlFile = new File("area.xml");
		File jsonFile = new File("area.txt");

		xls2Xml(xlsFile, xmlFile);// xls文档转为xml
		Xml2Json.trans(xmlFile, jsonFile);// xml转为json

	}

	/**
	 * 将xls文件转化为xmlFile
	 * 
	 * @param xlsFile
	 * @param xmlFile
	 */
	@SuppressWarnings("unchecked")
	private static void xls2Xml(File xlsFile, File xmlFile) {

		if (!xlsFile.exists()) {
			System.out.println("xls文件不存在");
			return;
		}
		if (!xmlFile.exists()) {
			try {
				xmlFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		FileWriter fileWriterUse = null;

		Sheet sheet;
		Workbook workbook = null;
		Cell cell1, cell2, cell3;
		try {

			workbook = Workbook.getWorkbook(xlsFile);
			sheet = workbook.getSheet(0);// 工作簿
			int i = 1;
			boolean isContinue = true;
			while (isContinue) {
				try {

					cell1 = sheet.getCell(0, i);
					cell2 = sheet.getCell(1, i);
					cell3 = sheet.getCell(2, i);
					if (cell1.getContents() == null
							|| "".equals(cell1.getContents())) {
						break;
					}

					String areaId = cell1.getContents().trim();
					String areaName = cell2.getContents().trim();
					String parentId = cell3.getContents().trim();

					Area area = new Area(areaId, areaName, parentId);
					areas.add(area);
					i++;

				} catch (IndexOutOfBoundsException e) {
					isContinue = false;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workbook.close();
		}

		areas2 = (ArrayList<Area>) areas.clone();

		/**
		 * 排序,省市区，格式为： <Province> <City> <County/> </City> </Province>
		 * 
		 */
		try {
			fileWriterUse = new FileWriter(xmlFile);
			fileWriterUse.write("<AreaList>\n");

			for (Area area : areas2) {

				if (!area.parentId.equals("0")) {// 先获取一级
					continue;
				}

				String areaId = area.areaId;
				String areaName = area.areaName;
				ArrayList<Area> childs = getChilds(areaId);

				if (childs.size() > 0) {// 必须大于0
					fileWriterUse.write("  <Province areaId=\"" + areaId
							+ "\" areaName=\"" + areaName + "\">\n");// 一级

					for (Area childArea : childs) {

						String areaId2 = childArea.areaId;
						fileWriterUse.write("    <City areaId=\""
								+ childArea.areaId + "\" areaName=\""
								+ childArea.areaName + "\">\n");// 二级
						/**
						 * 第三层的孩子
						 */
						ArrayList<Area> childs2 = getChilds(areaId2);
						if (childs2.size() > 0) {
							for (Area child2Area : childs2) {
								fileWriterUse.write("      <County areaId=\""
										+ child2Area.areaId + "\" areaName=\""
										+ child2Area.areaName + "\" />\n");// 三级

							}
						}
						fileWriterUse.write("    </City>\n");// 二级
					}

					fileWriterUse.write("  </Province>\n");// 一级
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileWriterUse.write("</AreaList>");
				fileWriterUse.flush();
				fileWriterUse.close();
				System.out.println("--->转换成xml完成！");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据parentId，找到孩子
	 * 
	 * @param parentId
	 * @return
	 */
	private static ArrayList<Area> getChilds(String parentId) {
		ArrayList<Area> arrayList = new ArrayList<Area>();
		for (int i = 0, length = areas.size(); i < length; i++) {
			Area area = areas.get(i);
			if (area.parentId.equals(parentId)) {
				arrayList.add(area);
				areas.remove(area);
				i--;
				length--;
			}
		}

		return arrayList;
	}
}
