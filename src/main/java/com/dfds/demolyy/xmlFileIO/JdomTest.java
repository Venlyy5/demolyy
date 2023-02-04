package com.dfds.demolyy.xmlFileIO;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * XML文件 读写测试 - Jdom
 */
public class JdomTest {
    public static void main(String[] args) throws Exception {
        writeXMLTest();
        //readXMLTest();
    }

    /**
     * 写XML
     */
    public static void writeXMLTest() throws IOException {
        // Element people, person, name, address, street, street_name, number;
        //创建文档
        Document document = new Document();
        //创建根元素
        Element people = new Element("people");
        //把根元素加入到document中
        document.addContent(people);

        //创建注释 comment 评论
        Comment rootComment = new Comment("将数据从程序输出到XML中！");
        Comment rootComment1 = new Comment("Hello XML");

        //把注释加上去
        people.addContent(rootComment);
        people.addContent(rootComment1);

        /*
             第一个人
         */
        Element person1 = new Element("person");

        //把 person 元素加入到根元素中
        people.addContent(person1);

        //设置person1元素属性
        person1.setAttribute("id", "001");

        person1.setAttribute("hello", "world");

        //设置另外一个属性 进行添加
        Attribute person1_gender = new Attribute("gender", "male");
        person1.setAttribute(person1_gender);

        //自主添加属性
        Attribute person1_age = new Attribute("age", "13");
        person1.setAttribute(person1_age);

        Element person1_name = new Element("name");
        person1_name.setText("Jack");
        person1.addContent(person1_name);

        Element person1_address = new Element("address");
        person1_address.setText("Anywhere");
        person1.addContent(person1_address);

        Element person1_street = new Element("street");
        person1.addContent(person1_street);

        Element person1_street_name = new Element("street_name");
        person1_street_name.setText("xue yuan lu");
        person1_street.addContent(person1_street_name);

        Element person1_no = new Element("number");
        person1_no.setText("29");
        person1_street.addContent(person1_no);



        /*
            第二个人
         */
        Element person2 = new Element("person");
        people.addContent(person2);

        //添加属性，可以一次添加多个属性
        person2.setAttribute("id", "002").setAttribute("gender","male");

        Element person2_name = new Element("name");
        person2_name.setText("Tom");
        person2.addContent(person2_name);

        Element person2_address = new Element("address");
        person2_address.setText("OtherWhere");
        person2.addContent(person2_address);

        /*
            第三个人
         */
        Element person3 = new Element("person");   // 新建第三个人
        people.addContent(person3);                      // 第三个人加到 根元素 people 中，与前两个人平级
        person3.setAttribute("age","12");    // 设置第三个人的属性

        Element person3_name = new Element("name");// 新建第三个人的名字标签 标签的名字叫做 name
        person3_name.setText("Jerry");                   // 第三个人的名字叫什么写好
        person3.addContent(person3_name);                // 将建好的第三个人的名字标签加上去


        //设置XML输出格式
        Format format = Format.getPrettyFormat();//PrettyFormat标准格式, RawFormat一行格式
        format.setEncoding("utf-8");//设置编码
        format.setIndent("    ");//设置缩进为4格


        //得到xml输出流
        XMLOutputter out = new XMLOutputter(format);
        //指定路径,写出到XML文件
        out.output(document, new FileOutputStream("src/main/resources/JdomTest.xml"));//或者FileWriter
    }

    /**
     * 读XML
     */
    public static void readXMLTest() throws Exception {
        SAXBuilder builder = new SAXBuilder();	//jdom和dom4j都是用SAX来遍历xml的
        Document document = builder.build("src/main/resources/JdomTest.xml");
        Element people = document.getRootElement();		//获取根节点

        List<Element> childrenList = people.getChildren();
        for(int i=0; i<childrenList.size(); i++) {
            Element person = childrenList.get(i);
            //获取所有people属性
            System.out.println(person.getAttributes());
            //获取单个people属性
            String id = person.getAttributeValue("id");
            //获取people的子节点
            String name = person.getChildText("name");
            System.out.println(id +" "+ name);
        }
    }
}


