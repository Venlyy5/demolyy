package com.dfds.demolyy.xmlFileIO;

import java.io.*;
import java.util.*;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Dom4jTest {

	public static void main(String[] args) throws Exception {
		//============ Demo1
		//writeXMLTest();
		readXMLTest();

		//============ Demo2
		//Set<String> values = new LinkedHashSet<>();
		//values.add("洋洋");
		//values.add("AKA");
		//values.add("Class2");
		//values.add("男");
		//values.add("32");
		//setXml(values);  //写

		//getXml(); //读
	}

	/**
	 * 写XML
	 * 重新创建根节点 覆盖写
	 */
	public static void writeXMLTest() throws Exception {
		//建立document对象，用来操作xml文件
		Document document = DocumentHelper.createDocument();
		//添加根节点students
		Element students = document.addElement("students");
		//添加注释
		students.addComment("这是注释");

		//students添加子节点student
		Element student = students.addElement("student");
		//设置student信息
		student.addAttribute("id", "001");
		//添加student的三个子节点
		Element name = student.addElement("name");
		//给各子节点添加文本内容
		name.setText("张三");
		Element sex = student.addElement("sex");
		sex.setText("男");
		Element age = student.addElement("age");
		age.setText("22");


		//CDATA节点: 可以在CDATA中使用非转义字符。例如可以使用 > 代替 &gt;。CDATA 节标记无需使用字符实体来指示 >、< 和其他字符，从而便于在 XML 文档中包含计算机代码和数学表达式。
		//对普通的节点，值就是值，直接就value()函数就取出了。而CDATA类型的节点，却认为这个CDATA是一个child，需要继续向下一层才能取出值
		//<![CDATA[x > 1 | x < 2]]>
		student.addElement("CDATA_Test").addCDATA("x > 1 | x < 2");


		//使用OutputFormat格式对象指定写入的风格和编码方法
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");

		// 写出到XML文件
		XMLWriter writer = new XMLWriter(new FileOutputStream("src/main/resources/Dom4jTest.xml"),format);
		//XMLWriter writer = new XMLWriter(System.out, format);
		writer.write(document);
		writer.close();
	}

	/**
	 * 读XML
	 */
	public static void readXMLTest() throws Exception {
		SAXReader saxReader = new SAXReader();
		//读取指定的xml文件之后返回一个Document对象，这个对象代表了整个XML文档，用于各种Dom运算。按照XML文件头所定义的编码来转换。
		Document document = saxReader.read("src/main/resources/Dom4jTest.xml");

		// Document/Element 与 String 互转
		String document2Str = document.asXML();
		System.out.println("====== 将文档或节点的XML转为字符串 ======\r\n"+ document2Str);
		Document str2Document = DocumentHelper.parseText(document2Str);
		System.out.println("====== 将字符串转为文档或节点的XML ======\r\n"+ str2Document);

		//获取根节点
		Element students = document.getRootElement();

		Iterator<?> iterator = students.elementIterator();
		while(iterator.hasNext()) {
			Element student = (Element)iterator.next();
			System.out.println("====== 将文档或节点的XML转化为字符串 ======\r\n"+ student.asXML());
			System.out.println("Element的XPath表达式: "+ student.getPath());

			System.out.print("学号："+student.attributeValue("id")+" ");
			System.out.print("姓名："+student.elementText("name")+" ");
			System.out.print("性别："+student.elementText("sex")+" "); //直接获取节点的text
			System.out.println("年龄："+student.element("age").getText()); //获取节点后再取text
			System.out.println("============================================");
		}

		// 使用XPath表达式获取所有student节点的"id"
		List<Node> nodeList = document.selectNodes("/students/student/@id");
		for (int i = 0; i < nodeList.size(); i++) {
			System.out.println("AttributeName="+nodeList.get(i).getName() + ", Value="+nodeList.get(i).getText());
		}
	}



	//============================================== Demo2
	//文件夹路径
	public static final String path = "D://XMLFile//";
	//文件名
	public static final String fileName="xmlTest.xml";
	//文件路径
	public static final String filePath = path+fileName;

	/**
	 * Demo2-向xml写入数据
	 * 获取已存在的XML根节点后,设置OutputFormat实现 追加写
	 */
	public static void setXml(Set<String> set) {
		try {
			/*
			 * 1. （创建）得到Document
			 * 2. （创建）得到root元素
			 * 3. 要把数据对象转换成Element元素
			 * 4. 把对象的属性插入到root元素中
			 * 5. 回写document
			 */
			//创建文件夹
			File Path = new File(path);
			if (!Path.exists()) {
				//把路径中不存在的文件夹也能创建出来
				Path.mkdirs();
			}
			//创建文件
			File file = new File(filePath);
			Element root = null;
			Document doc = null;
			//如果不存在则新建文件和根节点
			if (!file.exists()) {
				file.createNewFile();
				//创建一个xml文档
				doc = DocumentHelper.createDocument();
				//创建一个根节点
				root = doc.addElement("root");
				//添加注释
				root.addComment("这是注释");
			}else {
				// 否则读取已存在的XML,获取根节点后追加写
				//SAX解析
				SAXReader read = new SAXReader();
				//获取文件输入流
				FileReader fr = new FileReader(file);
				//获取文档结构
				doc = read.read(fr);
				//获得根节点
				root = doc.getRootElement();
			}

			//创建root的子节点
			Element userInfo = root.addElement("userInfo");
			//自定义的属性数组
			String[] str = {"name","file","classes","sex","age"};
			int i = 0;
			for(String s:set) {
				//创建userInfo的子节点
				userInfo.addElement(str[i++]).addText(s);
			}
			//获取文件输出流
			FileWriter fw = new FileWriter(file);
			//格式化,碰到\t换行
			OutputFormat format = new OutputFormat("\t",true);
			//这句话是给追加的准备的,碰到回车不换行,防止追加的没空行，之前的有空行
			format.setTrimText(true);
			//获取xml文件输出流
			XMLWriter writer = new XMLWriter(fw, format);
			//向writer写入doc
			writer.write(doc);
			//刷新
			fw.flush();
			//关闭流
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Demo2-从xml读出数据
	 */
	public static void getXml() {
		try {
			//创建文件夹
			File Path = new File(path);
			if (!Path.exists()) {
				//把路径中不存在的文件夹也能创建出来
				Path.mkdirs();
			}
			//创建文件
			File file = new File(filePath);
			//如果不存在则新建
			if (!file.exists()) {
				file.createNewFile();
			}
			//SAX解析
			SAXReader read = new SAXReader();
			//获取文件输入流
			FileReader fr = new FileReader(file);

			//获取文档结构
			Document doc = read.read(fr);
			//获取根节点
			Element root = doc.getRootElement();
			//获得根节点所有的子节点
			List<Element> list = root.elements();
			//循环子节点
			for(Element e:list) {
				//获得该节点的所有子节点
				List<Element> list1 = e.elements();
				//循环该节点的所有子节点
				for(Element e2:list1) {
					//输出节点名称和节点值
					System.out.println(e2.getName()+" : "+e2.getText());
				}
			}
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
