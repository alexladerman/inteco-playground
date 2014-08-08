package xbrl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class Instance {

	public static void main(String[] args) throws DatatypeConfigurationException, ParseException {
		// TODO Auto-generated method stub
		ObjectFactory objectFactory = new ObjectFactory();

		Report report = objectFactory.createReport();
		report.setId("pgc07abreviado"); // pgc07abreviado | pgc07normal | pgc07pymes | pgc07mixto
		report.setDate(xbrlDate());
		
		Entity entity = objectFactory.createEntity();
		entity.setId("00000000T");
		entity.setUri("http://www.xbrlfilingcompany.com");
		report.entity = entity;

		Module module = objectFactory.createModule();
		module.setId("bal"); // bal | pyg | patnetA | patnetB (see global.xml for more)
		module.setReportingDateStart(xbrlDate("2012-01-01"));
		module.setReportingDateEnd(xbrlDate("2012-12-31"));
		module.setBaseUnit(Unittype.EURO);
		module.setBaseDecimals("0");
		
		report.module = new ArrayList<Module>();
		report.module.add(module);
		
		module.item = new ArrayList<Item>();
		
		Random rand = new Random();
		int max = 10000;
		int min = 0;
		int sum = 0;
		
		int[] accounts = {
		11000,
		11100,
		11200,
		11300,
		11400,
		11500,
		11600,
		11700,
		12000,
		12100,
		12200,
		12300,
		12380,
		12381,
		12382,
		12370,
		12390,
		12400,
		12500,
		12600,
		12700
		};
		
		int i = 0;
		for (; i < accounts.length - 1; i++) {
			Item item = new Item();
			item.setSign("+");
			item.setId(Integer.toString(accounts[i]));
			int value = rand.nextInt((max - min) + 1) + min;
			item.setValue(Integer.toString(value));
			module.item.add(item);
			sum += value;
		}

		//total
		Item item = new Item();
		item.setSign("+");
		item.setId(Integer.toString(accounts[i]));
		int value = sum;
		item.setValue(Integer.toString(value));
		module.item.add(item);
		sum = 0;
		
		jaxbObjectToXML(report, "intermediate_xbrl.xml");
	}
	
	private static XMLGregorianCalendar xbrlDate() throws DatatypeConfigurationException { //today
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());	
	}

	private static XMLGregorianCalendar xbrlDate(String date) throws DatatypeConfigurationException, ParseException {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime((new SimpleDateFormat("yyyy-MM-dd")).parse(date));
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
	}

    private static void jaxbObjectToXML(Object object, String filename) {  	 
        try {    	
        	Marshaller m = JAXBContext.newInstance(object.getClass()).createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            FileOutputStream fileStream = new FileOutputStream(filename);
            m.marshal(object, System.out);
            m.marshal(object, fileStream);
            fileStream.close();          
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
    }
	
}
