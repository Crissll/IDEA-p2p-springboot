package com.bjpowernode.p2p;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

import java.lang.annotation.Documented;
import java.util.Date;

public class Demo {
    public static void main(String[] args) throws DocumentException {
//        String xml="<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
//                "\n" +
//                "<bookstore>\n" +
//                "\n" +
//                "<book>\n" +
//                "  <title lang=\"eng\">Harry Potter</title>\n" +
//                "  <price>29.99</price>\n" +
//                "</book>\n" +
//                "\n" +
//                "<book>\n" +
//                "  <title lang=\"eng\">Learning XML</title>\n" +
//                "  <price>39.95</price>\n" +
//                "</book>\n" +
//                "\n" +
//                "</bookstore>";
//
//        Document document = DocumentHelper.parseText(xml);
//        Node node = document.selectSingleNode("/bookstore/book/price");
//        String text = node.getText();
//        System.out.println(text);
        System.out.println(new Date());
    }
}
