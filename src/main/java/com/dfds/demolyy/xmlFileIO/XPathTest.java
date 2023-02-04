package com.dfds.demolyy.xmlFileIO;

import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

/**
 * XPath 是一门在 XML 文档中查找信息的语言。可用来在 XML 文档中对元素和属性进行遍历
 * JDK中自带包javax.xml.xpath包
 */
public class XPathTest {

    public static void main(String[] args) throws Exception {
        xPathTest();
    }
    public static void xPathTest() throws Exception {
        //将XML文档加载到DOM Document对象中
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true); // never forget this!
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document doc = builder.parse("src/books.xml");
        //创建 XPathFactory
        XPathFactory pathFactory = XPathFactory.newInstance();
        //使用XPathFactory工厂创建 XPath 对象
        XPath xpath = pathFactory.newXPath();
        //使用XPath对象编译XPath表达式
        XPathExpression pathExpression = xpath.compile("//book[author='TEST']/title/text()");
        //计算 XPath 表达式得到结果
        Object result = pathExpression.evaluate(doc, XPathConstants.NODESET);
        //节点集node-set转化为NodeList
        //将结果强制转化成 DOM NodeList
        org.w3c.dom.NodeList nodes = (NodeList) result;
        for (int i = 0; i < nodes.getLength(); i++) {
            System.out.println(nodes.item(i).getNodeValue());
        }
        //查找所有图书的 XPath 查询非常简单:
        //book[author="TEST"]
        //book代表节点的名称,author属性的名称,后面是要查询的值

        //为了找出这些图书的标题(title),只要增加一步,表达式就变成了:
        //book[author="TEST"]/title
        //title代表要取元素的名称

        //最后,真正需要的是 title 元素的文本节点内容.这就要求再增加一步,完整的表达式就是:
        //book[author="TEST"]/title/text()
        //text()该节点的内容
    }
}
