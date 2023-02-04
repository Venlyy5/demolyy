package com.dfds.demolyy.xmlFileIO;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="students")
// 这里不自动映射了，默认的映射会去找public，注解在field上就冲突了，要报错的哟
@XmlAccessorType(XmlAccessType.NONE)
public class xml2Entity {

    @XmlElement(name="student")
    String student;

    @XmlElement(name="name")
    String name;

    @XmlElement(name="sex")
    String sex;

    @XmlElement(name="age")
    //@XmlCDATA
    String age;
}
