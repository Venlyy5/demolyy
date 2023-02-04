package com.dfds.demolyy.xmlFileIO;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class W3CDomTest {
	public static void main(String[] args) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();

		// 解决SAXParseException,文件提前结束问题。
		db.setErrorHandler(new ErrorHandler() {
			@Override
			public void warning(SAXParseException exception) throws SAXException {
				System.err.println("warning: caught exception");
				exception.printStackTrace(System.err);
			}

			@Override
			public void error(SAXParseException exception) throws SAXException {
				System.err.println("fatalError: caught exception");
				exception.printStackTrace(System.err);
			}

			@Override
			public void fatalError(SAXParseException exception) throws SAXException {
				System.err.println("error: caught exception");
				exception.printStackTrace(System.err);
			}
		});

		Document doc = db.parse("src/main/resources/Dom4jTest.xml");

		// 获取方式2
		//FileInputStream fr = new FileInputStream(new File("src/main/resources/Dom4jTest.xml"));
		//Document doc = db.parse(fr);
		//fr.close();
		//doc.getDocumentElement().normalize();

		// 读取XML
		NodeList nodelist = doc.getElementsByTagName("student");
		for(int i = 0; i < nodelist.getLength(); i++) {
			Node node = nodelist.item(i);
			System.out.println("====" + (node.getNodeType() == Node.ELEMENT_NODE));
			Element element = (Element) node;
			System.out.println("=" + element.getNodeName() + ",id=" + element.getAttribute("id"));
			NodeList chs = node.getChildNodes();

			//for(int j = 0; j < chs.getLength(); j++) {
			//	Node chnode = chs.item(j);
			//	if(chnode.getNodeType() == Node.TEXT_NODE) {
			//		System.out.println("textnode:" + chnode.getNodeValue());
			//	}else if(chnode.getNodeType() == Node.ELEMENT_NODE) {
			//		System.out.println("elementnode:" + chnode.getNodeName());
			//	}
			//}

			for(Node next = node.getFirstChild(); next != null; next = next.getNextSibling()) {
				if(next.getNodeType() == Node.TEXT_NODE) {
					//两个标签之间的内容（上一个结束跟下一个开始标签之间，包括空白和回车）
					System.out.println("textnode:" + next.getNodeValue());
				}else if(next.getNodeType() == Node.ELEMENT_NODE) {
					System.out.println("elementnode:" + next.getNodeName());
					if("name".equals(next.getNodeName())) {
						Element nameElement = (Element) next;
						System.out.println(String.format("<type:%s;val:%s>", nameElement.getAttribute("type"), nameElement.getTextContent()));
					}else if("sex".equals(next.getNodeName())) {
						Element sizeElement = (Element) next;
						System.out.println("_sex=" + sizeElement.getTextContent());
					}
				}
			}
		}


		/*//借助Transformer生成xml
		Document newDoc = db.newDocument();
		Element rootEle = newDoc.createElement("ElementRoot");
		newDoc.appendChild(rootEle);

		Element ele1 = newDoc.createElement("Element");
		ele1.setAttribute("ID", "xxxx");
		Element ele1_str = newDoc.createElement("hello");
		ele1_str.setTextContent("HELLO内容");
		ele1.appendChild(ele1_str);
		rootEle.appendChild(ele1);

		Element ele2 = newDoc.createElement("Element");
		ele2.setAttribute("ID", "yyyy");
		ele2.appendChild(newDoc.createTextNode("This is Text Node"));
		rootEle.appendChild(ele2);

		TransformerFactory factory = TransformerFactory.newInstance();
		try {
			Transformer transform = factory.newTransformer();
			transform.setOutputProperty(OutputKeys.INDENT, "yes");//设置添加空白缩进排列
			//transform.setOutputProperty(OutputKeys.ENCODING, "ASCII");
			transform.transform(new DOMSource(newDoc), new StreamResult(new File("src/testw3cdom/testw3cdomnew.xml")));
		} catch (TransformerException e) {
			e.printStackTrace();
		}*/
	}
}
/**===================================================================
 * DOM是Document Object Model的缩写，即文档对象模型。前面说过，XML将数据组织为一颗树，所以DOM就是对这颗树的一个对象描叙。通俗的说，就是通过解析XML文档，为XML文档在逻辑上建立一个树模型，树的节点是一个个对象。我们通过存取这些对象就能够存取XML文档的内容。
 * 　　下面我们来看一个简单的例子，看看在DOM中，我们是如何来操作一个XML文档的。
 * 　　这是一个XML文档，也是我们要操作的对象：
 * 　　 xml version="1.0" encoding="UTF-8"?>
 *
 * 　　 Good-bye serialization, hello Java!
 *
 * 　　下面，我们需要把这个文档的内容解析到一个个的Java对象中去供程序使用，利用JAXP，我们只需几行代码就能做到这一点。首先，我们需要建立一个解析器工厂，以利用这个工厂来获得一个具体的解析器对象：
 * 　　DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
 * 　　我们在这里使用DocumentBuilderFacotry的目的是为了创建与具体解析器无关的程序，当DocumentBuilderFactory类的静态方法newInstance()被调用时，它根据一个系统变量来决定具体使用哪一个解析器。又因为所有的解析器都服从于JAXP所定义的接口，所以无论具体使用哪一个解析器，代码都是一样的。所以当在不同的解析器之间进行切换时，只需要更改系统变量的值，而不用更改任何代码。这就是工厂所带来的好处。这个工厂模式的具体实现，可以参看下面的类图。
 * 　　DocumentBuilder db = dbf.newDocumentBuilder();
 * 　　当获得一个工厂对象后，使用它的静态方法newDocumentBuilder()方法可以获得一个DocumentBuilder对象，这个对象代表了具体的DOM解析器。但具体是哪一种解析器，微软的或者IBM的，对于程序而言并不重要。
 * 　　然后，我们就可以利用这个解析器来对XML文档进行解析了：
 * 　　Document doc = db.parse("c:/ xml/message. xml");
 * 　　DocumentBuilder的parse()方法接受一个XML文档名作为输入参数，返回一个Document对象，这个Document对象就代表了一个XML文档的树模型。以后所有的对XML文档的操作，都与解析器无关，直接在这个Document对象上进行操作就可以了。而具体对Document操作的方法，就是由DOM所定义的了。
 *
 * 　　Jaxp支持W3C所推荐的DOM 2。如果你对DOM很熟悉，那么下面的内容就很简单了：只需要按照DOM的规范来进行方法调用就可以。当然，如果你对DOM不清楚，也不用着急，后面我们会有详细的介绍。在这儿，你所要知道并牢记的是：DOM是用来描叙XML文档中的数据的模型，引入DOM的全部原因就是为了用这个模型来操作XML文档的中的数据。DOM规范中定义有节点（即对象）、属性和方法，我们通过这些节点的存取来存取XML的数据。
 * 　　从上面得到的Document对象开始，我们就可以开始我们的DOM之旅了。使用Document对象的getElementsByTagName()方法，我们可以得到一个NodeList对象，一个Node对象代表了一个XML文档中的一个标签元素，而NodeList对象，观其名而知其意，所代表的是一个Node对象的列表：
 * 　　NodeList nl = doc.getElementsByTagName("message");
 * 　　我们通过这样一条语句所得到的是XML文档中所有 标签对应的Node对象的一个列表。然后，我们可以使用NodeList对象的item()方法来得到列表中的每一个Node对象：
 * 　　Node my_node = nl.item(0);
 * 　　当一个Node对象被建立之后，保存在XML文档中的数据就被提取出来并封装在这个Node中了。在这个例子中，要提取Message标签内的内容，我们通常会使用Node对象的getNodeValue()方法：
 * 　　String message = my_node.getFirstChild().getNodeValue();
 * 　　请注意，这里还使用了一个getFirstChild()方法来获得message下面的第一个子Node对象。虽然在message标签下面除了文本外并没有其它子标签或者属性，但是我们坚持在这里使用getFirseChild()方法，这主要和W3C对DOM的定义有关。W3C把标签内的文本部分也定义成一个Node，所以先要得到代表文本的那个Node，我们才能够使用getNodeValue()来获取文本的内容。
 * 　　现在，既然我们已经能够从XML文件中提取出数据了，我们就可以把这些数据用在合适的地方，来构筑应用程序。
 * 　　下面的内容，我们将更多的关注DOM，为DOM作一个较为详细的解析，使我们使用起来更为得心应手。
 * 　　DOM详解
 * 　　1．基本的DOM对象
 * 　　DOM的基本对象有5个：Document，Node，NodeList，Element和Attr。下面就这些对象的功能和实现的方法作一个大致的介绍。
 * 　　[[The No.5 Picture.]]
 * 　　Document对象代表了整个XML的文档，所有其它的Node，都以一定的顺序包含在Document对象之内，排列成一个树形的结构，程序员可以通过遍历这颗树来得到XML文档的所有的内容，这也是对XML文档操作的起点。我们总是先通过解析XML源文件而得到一个Document对象，然后再来执行后续的操作。此外，Document还包含了创建其它节点的方法，比如createAttribut()用来创建一个Attr对象。它所包含的主要的方法有：
 *
 * 　　createAttribute(String)：用给定的属性名创建一个Attr对象，并可在其后使用setAttributeNode方法来放置在某一个Element对象上面。
 *
 * 　　createElement(String)：用给定的标签名创建一个Element对象，代表XML文档中的一个标签，然后就可以在这个Element对象上添加属性或进行其它的操作。
 *
 * 　　createTextNode(String)：用给定的字符串创建一个Text对象，Text对象代表了标签或者属性中所包含的纯文本字符串。如果在一个标签内没有其它的标签，那么标签内的文本所代表的Text对象是这个Element对象的唯一子对象。
 *
 * 　　getElementsByTagName(String)：返回一个NodeList对象，它包含了所有给定标签名字的标签。
 *
 * 　　getDocumentElement()：返回一个代表这个DOM树的根节点的Element对象，也就是代表XML文档根元素的那个对象。
 *
 * 　　Node对象是DOM结构中最为基本的对象，代表了文档树中的一个抽象的节点。在实际使用的时候，很少会真正的用到Node这个对象，而是用到诸如Element、Attr、Text等Node对象的子对象来操作文档。Node对象为这些对象提供了一个抽象的、公共的根。虽然在Node对象中定义了对其子节点进行存取的方法，但是有一些Node子对象，比如Text对象，它并不存在子节点，这一点是要注意的。Node对象所包含的主要的方法有：
 *
 * 　　appendChild(org.w3c.dom.Node)：为这个节点添加一个子节点，并放在所有子节点的最后，如果这个子节点已经存在，则先把它删掉再添加进去。
 *
 * 　　getFirstChild()：如果节点存在子节点，则返回第一个子节点，对等的，还有getLastChild()方法返回最后一个子节点。
 *
 * 　　getNextSibling()：返回在DOM树中这个节点的下一个兄弟节点，对等的，还有getPreviousSibling()方法返回其前一个兄弟节点。
 *
 * 　　getNodeName()：根据节点的类型返回节点的名称。
 *
 * 　　getNodeType()：返回节点的类型。
 *
 * 　　getNodeValue()：返回节点的值。
 *
 * 　　hasChildNodes()：判断是不是存在有子节点。
 *
 * 　　hasAttributes()：判断这个节点是否存在有属性。
 *
 * 　　getOwnerDocument()：返回节点所处的Document对象。
 *
 * 　　insertBefore(org.w3c.dom.Node new，org.w3c.dom.Node ref)：在给定的一个子对象前再插入一个子对象。
 * 　　removeChild(org.w3c.dom.Node)：删除给定的子节点对象。
 *
 * 　　replaceChild(org.w3c.dom.Node new，org.w3c.dom.Node old)：用一个新的Node对象代替给定的子节点对象。
 *
 * 　　NodeList对象，顾名思义，就是代表了一个包含了一个或者多个Node的列表。可以简单的把它看成一个Node的数组，我们可以通过方法来获得列表中的元素：
 *
 * 　　GetLength()：返回列表的长度。
 *
 * 　　Item(int)：返回指定位置的Node对象。
 *
 * 　　Element对象代表的是XML文档中的标签元素，继承于Node，亦是Node的最主要的子对象。在标签中可以包含有属性，因而Element对象中有存取其属性的方法，而任何Node中定义的方法，也可以用在Element对象上面。
 * 　　getElementsByTagName(String)：返回一个NodeList对象，它包含了在这个标签中其下的子孙节点中具有给定标签名字的标签。
 *
 * 　　getTagName()：返回一个代表这个标签名字的字符串。
 *
 * 　　getAttribute(String)：返回标签中给定属性名称的属性的值。在这儿需要主要的是，应为XML文档中允许有实体属性出现，而这个方法对这些实体属性并不适用。这时候需要用到getAttributeNodes()方法来得到一个Attr对象来进行进一步的操作。
 *
 * 　　getAttributeNode(String)：返回一个代表给定属性名称的Attr对象。
 *
 * 　　Attr对象代表了某个标签中的属性。Attr继承于Node，但是因为Attr实际上是包含在Element中的，它并不能被看作是Element的子对象，因而在DOM中Attr并不是DOM树的一部分，所以Node中的getparentNode()，getpreviousSibling()和getnextSibling()返回的都将是null。也就是说，Attr其实是被看作包含它的Element对象的一部分，它并不作为DOM树中单独的一个节点出现。这一点在使用的时候要同其它的Node子对象相区别。
 * 　　需要说明的是，上面所说的DOM对象在DOM中都是用接口定义的，在定义的时候使用的是与具体语言无关的IDL语言来定义的。因而，DOM其实可以在任何面向对象的语言中实现，只要它实现了DOM所定义的接口和功能就可以了。同时，有些方法在DOM中并没有定义，是用IDL的属性来表达的，当被映射到具体的语言时，这些属性被映射
 */

/**===============================================================
 * 首先出场的是 DOM（JAXP Crimson 解析器）
 *
 * 　　DOM 是用与平台和语言无关的方式表示 XML 文档的官方 W3C 标准。DOM 是以层次结构组织的节点或信息片断的集合。这个层次结构允许开发人员在树中寻找特定信息。分析该结构通常需要加载整个文档和构造层次结构，然后才能做任何工作。由于它是基于信息层次的，因而 DOM 被认为是基于树或基于对象的。DOM 以及广义的基于树的处理具有几个优点。首先，由于树在内存中是持久的，因此可以修改它以便应用程序能对数据和结构作出更改。它还可以在任何时候在树中上下导航，而不是像 SAX 那样是一次性的处理。DOM 使用起来也要简单得多。
 *
 * 　　另一方面，对于特别大的文档，解析和加载整个文档可能很慢且很耗资源，因此使用其他手段来处理这样的数据会更好。这些基于事件的模型，比如 SAX。
 *
 * 　　Bean文件：
 *
 * 　　package com.test;
 *
 * 　　import java.io.*;
 * 　　import java.util.*;
 * 　　import org.w3c.dom.*;
 * 　　import javax.xml.parsers.*;
 *
 * 　　public class MyXMLReader{
 *
 * 　　public static void main(String arge[]){
 * 　　long lasting =System.currentTimeMillis();
 * 　　try{
 * 　　　File f=new File("data_10k.xml");
 * 　　　DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
 * 　　　DocumentBuilder builder=factory.newDocumentBuilder();
 * 　　　Document doc = builder.parse(f);
 * 　　　NodeList nl = doc.getElementsByTagName("VALUE");
 * 　　　for (int i=0;i＜nl.getLength();i++){
 * 　　　　System.out.print("车牌号码:" + doc.getElementsByTagName("NO").item(i).getFirstChild().getNodeValue());
 * 　　　　System.out.println(" 车主地址:" + doc.getElementsByTagName("ADDR").item(i).getFirstChild().getNodeValue());
 * 　　}
 * 　　}catch(Exception e){
 * 　　　e.printStackTrace();
 * 　　}
 * 　　System.out.println("运行时间："+(System.currentTimeMillis() - lasting)+" 毫秒");
 * 　　}
 * 　　}
 *
 *
 * 　　10k消耗时间：265 203 219 172
 * 　　100k消耗时间：9172 9016 8891 9000
 * 　　1000k消耗时间：691719 675407 708375 739656
 * 　　10000k消耗时间：OutOfMemoryError
 *
 * 　　接着是 SAX
 *
 * 　　这种处理的优点非常类似于流媒体的优点。分析能够立即开始，而不是等待所有的数据被处理。而且，由于应用程序只是在读取数据时检查数据，因此不需要将数据存储在内存中。这对于大型文档来说是个巨大的优点。事实上，应用程序甚至不必解析整个文档；它可以在某个条件得到满足时停止解析。一般来说，SAX 还比它的替代者 DOM 快许多。
 *
 * 　　 选择 DOM 还是选择 SAX ？
 *
 * 　　对于需要自己编写代码来处理 XML 文档的开发人员来说，选择 DOM 还是 SAX 解析模型是一个非常重要的设计决策。
 *
 * 　　DOM 采用建立树形结构的方式访问 XML 文档，而 SAX 采用的事件模型。
 *
 * 　　DOM 解析器把 XML 文档转化为一个包含其内容的树，并可以对树进行遍历。用 DOM 解析模型的优点是编程容易，开发人员只需要调用建树的指令，然后利用navigation APIs访问所需的树节点来完成任务。可以很容易的添加和修改树中的元素。然而由于使用 DOM 解析器的时候需要处理整个 XML 文档，所以对性能和内存的要求比较高，尤其是遇到很大的 XML 文件的时候。由于它的遍历能力，DOM 解析器常用于 XML 文档需要频繁的改变的服务中。
 *
 * 　　SAX 解析器采用了基于事件的模型，它在解析 XML 文档的时候可以触发一系列的事件，当发现给定的tag的时候，它可以激活一个回调方法，告诉该方法制定的标签已经找到。SAX 对内存的要求通常会比较低，因为它让开发人员自己来决定所要处理的tag。特别是当开发人员只需要处理文档中所包含的部分数据时，SAX 这种扩展能力得到了更好的体现。但用 SAX 解析器的时候编码工作会比较困难，而且很难同时访问同一个文档中的多处不同数据。
 *
 * 　　Bean文件：
 *
 * 　　package com.test;
 * 　　import org.xml.sax.*;
 * 　　import org.xml.sax.helpers.*;
 * 　　import javax.xml.parsers.*;
 *
 * 　　public class MyXMLReader extends DefaultHandler {
 *
 * 　　java.util.Stack tags = new java.util.Stack();
 *
 * 　　public MyXMLReader() {
 * 　　super();
 * 　　}
 *
 * 　　public static void main(String args[]) {
 * 　　long lasting = System.currentTimeMillis();
 * 　　try {
 * 　　　SAXParserFactory sf = SAXParserFactory.newInstance();
 * 　　　SAXParser sp = sf.newSAXParser();
 * 　　　MyXMLReader reader = new MyXMLReader();
 * 　　　sp.parse(new InputSource("data_10k.xml"), reader);
 * 　　} catch (Exception e) {
 * 　　　e.printStackTrace();
 * 　　}
 * 　　System.out.println("运行时间：" + (System.currentTimeMillis() - lasting) + " 毫秒");
 * 　　}
 *
 * 　　public void characters(char ch[], int start, int length) throws SAXException {
 * 　　String tag = (String) tags.peek();
 * 　　if (tag.equals("NO")) {
 * 　　　System.out.print("车牌号码：" + new String(ch, start, length));
 * 　　}
 * 　　if (tag.equals("ADDR")) {
 * 　　System.out.println(" 地址:" + new String(ch, start, length));
 * 　　}
 * 　　}
 *
 * 　　public void startElement(
 * 　　String uri,
 * 　　String localName,
 * 　　String qName,
 * 　　Attributes attrs) {
 * 　　tags.push(qName);
 * 　　}
 * 　　}
 *
 * 　　10k消耗时间：110 47 109 78
 * 　　100k消耗时间：344 406 375 422
 * 　　1000k消耗时间：3234 3281 3688 3312
 * 　　10000k消耗时间：32578 34313 31797 31890 30328
 *
 * 　　然后是 JDOM http://www.jdom.org/
 *
 * 　　JDOM 的目的是成为 Java 特定文档模型，它简化与 XML 的交互并且比使用 DOM 实现更快。由于是第一个 Java 特定模型，JDOM 一直得到大力推广和促进。正在考虑通过“Java 规范请求 JSR-102”将它最终用作“Java 标准扩展”。从 2000 年初就已经开始了 JDOM 开发。
 *
 * 　　JDOM 与 DOM 主要有两方面不同。首先，JDOM 仅使用具体类而不使用接口。这在某些方面简化了 API，但是也限制了灵活性。第二，API 大量使用了 Collections 类，简化了那些已经熟悉这些类的 Java 开发者的使用。
 *
 * 　　JDOM 文档声明其目的是“使用 20%（或更少）的精力解决 80%（或更多）Java/XML 问题”（根据学习曲线假定为 20%）。JDOM 对于大多数 Java/XML 应用程序来说当然是有用的，并且大多数开发者发现 API 比 DOM 容易理解得多。JDOM 还包括对程序行为的相当广泛检查以防止用户做任何在 XML 中无意义的事。然而，它仍需要您充分理解 XML 以便做一些超出基本的工作（或者甚至理解某些情况下的错误）。这也许是比学习 DOM 或 JDOM 接口都更有意义的工作。
 *
 * 　　JDOM 自身不包含解析器。它通常使用 SAX2 解析器来解析和验证输入 XML 文档（尽管它还可以将以前构造的 DOM 表示作为输入）。它包含一些转换器以将 JDOM 表示输出成 SAX2 事件流、DOM 模型或 XML 文本文档。JDOM 是在 Apache 许可证变体下发布的开放源码。
 *
 * 　　Bean文件：
 *
 * 　　package com.test;
 *
 * 　　import java.io.*;
 * 　　import java.util.*;
 * 　　import org.jdom.*;
 * 　　import org.jdom.input.*;
 *
 * 　　public class MyXMLReader {
 *
 * 　　public static void main(String arge[]) {
 * 　　long lasting = System.currentTimeMillis();
 * 　　try {
 * 　　　SAXBuilder builder = new SAXBuilder();
 * 　　　Document doc = builder.build(new File("data_10k.xml"));
 * 　　　Element foo = doc.getRootElement();
 * 　　　List allChildren = foo.getChildren();
 * 　　　for(int i=0;i＜allChildren.size();i++) {
 * 　　　　System.out.print("车牌号码:" + ((Element)allChildren.get(i)).getChild("NO").getText());
 * 　　　　System.out.println(" 车主地址:" + ((Element)allChildren.get(i)).getChild("ADDR").getText());
 * 　　　}
 * 　　} catch (Exception e) {
 * 　　　e.printStackTrace();
 * 　　}
 * 　　System.out.println("运行时间：" + (System.currentTimeMillis() - lasting) + " 毫秒");
 * 　　}
 * 　　}
 *
 * 　　10k消耗时间：125 62 187 94
 * 　　100k消耗时间：704 625 640 766
 * 　　1000k消耗时间：27984 30750 27859 30656
 * 　　10000k消耗时间：OutOfMemoryError
 *
 * 　　最后是 DOM4J http://dom4j.sourceforge.net/
 *
 * 　　虽然 DOM4J 代表了完全独立的开发结果，但最初，它是 JDOM 的一种智能分支。它合并了许多超出基本 XML 文档表示的功能，包括集成的 XPath 支持、XML Schema 支持以及用于大文档或流化文档的基于事件的处理。它还提供了构建文档表示的选项，它通过 DOM4J API 和标准 DOM 接口具有并行访问功能。从 2000 下半年开始，它就一直处于开发之中。
 *
 * 　　为支持所有这些功能，DOM4J 使用接口和抽象基本类方法。DOM4J 大量使用了 API 中的 Collections 类，但是在许多情况下，它还提供一些替代方法以允许更好的性能或更直接的编码方法。直接好处是，虽然 DOM4J 付出了更复杂的 API 的代价，但是它提供了比 JDOM 大得多的灵活性。
 *
 * 　　在添加灵活性、XPath 集成和对大文档处理的目标时，DOM4J 的目标与 JDOM 是一样的：针对 Java 开发者的易用性和直观操作。它还致力于成为比 JDOM 更完整的解决方案，实现在本质上处理所有 Java/XML 问题的目标。在完成该目标时，它比 JDOM 更少强调防止不正确的应用程序行为。
 *
 * 　　DOM4J 是一个非常非常优秀的Java XML API，具有性能优异、功能强大和极端易用使用的特点，同时它也是一个开放源代码的软件。如今你可以看到越来越多的 Java 软件都在使用 DOM4J 来读写 XML，特别值得一提的是连 Sun 的 JAXM 也在用 DOM4J。
 *
 * 　　Bean文件：
 *
 * 　　package com.test;
 *
 * 　　import java.io.*;
 * 　　import java.util.*;
 * 　　import org.dom4j.*;
 * 　　import org.dom4j.io.*;
 *
 * 　　public class MyXMLReader {
 *
 * 　　public static void main(String arge[]) {
 * 　　long lasting = System.currentTimeMillis();
 * 　　try {
 * 　　　File f = new File("data_10k.xml");
 * 　　　SAXReader reader = new SAXReader();
 * 　　　Document doc = reader.read(f);
 * 　　　Element root = doc.getRootElement();
 * 　　　Element foo;
 * 　　　for (Iterator i = root.elementIterator("VALUE"); i.hasNext();) {
 * 　　　　foo = (Element) i.next();
 * 　　　　System.out.print("车牌号码:" + foo.elementText("NO"));
 * 　　　　System.out.println(" 车主地址:" + foo.elementText("ADDR"));
 * 　　　}
 * 　　} catch (Exception e) {
 * 　　　e.printStackTrace();
 * 　　}
 * 　　System.out.println("运行时间：" + (System.currentTimeMillis() - lasting) + " 毫秒");
 * 　　}
 * 　　}
 *
 * 　　10k消耗时间：109 78 109 31
 * 　　100k消耗时间：297 359 172 312
 * 　　1000k消耗时间：2281 2359 2344 2469
 * 　　10000k消耗时间：20938 19922 20031 21078
 *
 * 　　JDOM 和 DOM 在性能测试时表现不佳，在测试 10M 文档时内存溢出。在小文档情况下还值得考虑使用 DOM 和 JDOM。虽然 JDOM 的开发者已经说明他们期望在正式发行版前专注性能问题，但是从性能观点来看，它确实没有值得推荐之处。另外，DOM 仍是一个非常好的选择。DOM 实现广泛应用于多种编程语言。它还是许多其它与 XML 相关的标准的基础，因为它正式获得 W3C 推荐（与基于非标准的 Java 模型相对），所以在某些类型的项目中可能也需要它（如在 JavaScript 中使用 DOM）。
 *
 * 　　SAX表现较好，这要依赖于它特定的解析方式。一个 SAX 检测即将到来的XML流，但并没有载入到内存（当然当XML流被读入时，会有部分文档暂时隐藏在内存中）。
 *
 * 　　无疑，DOM4J是这场测试的获胜者，目前许多开源项目中大量采用 DOM4J，例如大名鼎鼎的 Hibernate 也用 DOM4J 来读取 XML 配置文件。如果不考虑可移植性，那就采用DOM4J吧！
 */