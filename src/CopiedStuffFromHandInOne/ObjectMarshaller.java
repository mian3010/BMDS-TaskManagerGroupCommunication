package CopiedStuffFromHandInOne;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Note the poor use of exception handling and error checking, as well as the code duplication.
 * This is intentional as it's not an open system, but just a JAXB exercise. This class was ment for testing.
 */
public class ObjectMarshaller {
	
	public static Marshaller getMarshaller(Object obj) {
		try {
			JAXBContext jc = JAXBContext.newInstance(obj.getClass());
			return jc.createMarshaller();
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Close the stream yourself.
	 */
	public static void marshall(Object obj, OutputStream os) {
		Marshaller marshaller = getMarshaller(obj);
		try {
			marshaller.marshal(obj, os);
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}
	}
	
	public static void marshall(Object obj, String filePath) {
		File file = new File(filePath);
		marshall(obj, file);
	}
	
	public static void marshall(Object obj, File file) {
    Marshaller marshaller = getMarshaller(obj);
    try {
      FileWriter fw = new FileWriter(file);
      marshaller.marshal(obj, fw);
      fw.close();
    } catch (Exception e) {
      System.out.println("Exception: " + e.getMessage());
    }
  }
	
	@SuppressWarnings("rawtypes")
  public static Unmarshaller getUnmarshaller(Class objClass) {
		try {
			JAXBContext jc = JAXBContext.newInstance(objClass);
			return jc.createUnmarshaller();
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			return null;
		}
	}
			
}
