package com.modelink.common.excel;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/08/14 0014.
 */
public class ExcelEventModel {
    public static Logger logger = LoggerFactory.getLogger(ExcelEventModel.class);
    public void processOneSheet(String filename) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();

        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        SheetHandler handler = new SheetHandler(sst);
        parser.setContentHandler(handler);

        InputStream sheet2 = r.getSheet("rId1");
        InputSource sheetSource = new InputSource(sheet2);
        parser.parse(sheetSource);
        sheet2.close();
        List<List<String>> dataList = handler.getDataList();
        System.out.println(dataList);
    }



    private class SheetHandler extends DefaultHandler {
        private SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;
        private List<String> valueList = new ArrayList<>();
        private List<List<String>> dataList = new ArrayList<>();

        private SheetHandler(SharedStringsTable sst) {
            this.sst = sst;
        }

        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            if ("row".equals(name)) {
                valueList = new ArrayList<>();
            } else if (name.equals("c")) {
                System.out.print(attributes.getValue("r") + " - ");
                String cellType = attributes.getValue("t");
                if (cellType != null && cellType.equals("s")) {
                    nextIsString = true;
                } else {
                    nextIsString = false;
                }
            }
            lastContents = "";
        }

        public void endElement(String uri, String localName, String name) throws SAXException {
            if (nextIsString) {
                int idx = Integer.parseInt(lastContents);
                lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                nextIsString = false;
            }

            if (name.equals("v")) {
//                logger.info(lastContents);
                System.out.println(lastContents);
                valueList.add(lastContents);
            } else if("row".equals(name)) {
                dataList.add(valueList);
            }
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            lastContents += new String(ch, start, length);
        }

        public List<List<String>> getDataList() {
            return dataList;
        }

        public void setDataList(List<List<String>> dataList) {
            this.dataList = dataList;
        }
    }

    public static void main(String[] args) throws Exception {
        logger.info("start read");
        System.out.println("start read");
        ExcelEventModel example = new ExcelEventModel();
        example.processOneSheet("E:/modelink/document/系统导入数据模板-最终版6.30.xlsx");
        Thread.sleep(1000);
    }

}
