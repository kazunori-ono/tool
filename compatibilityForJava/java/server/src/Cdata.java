import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class Cdata {
    private static final String FILE_PATH = "cdata.xml";
    private boolean isOutputNewLine = true;

    public static void main(String... args){
        try {
            setCdata();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void setCdata() throws Exception{

        File file = new File(FILE_PATH);
		file.createNewFile();
		file.setExecutable(true);
		file.setReadable(true);
		file.setWritable(true);

		OutputStream out = new FileOutputStream(file);
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = factory.createXMLStreamWriter(out, "UTF-8");
        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeCharacters("\n\t");
        writer.writeStartElement("text");
        writer.writeCData(getTestData());
        writer.writeEndElement();
    }

    private static String getTestData(){
        char[] array = { 't','e','s','t','<','b','r','>','"','d','a','t','a','"',']',']','>','t','e','s','t'};
        return new String(array);
    }
}