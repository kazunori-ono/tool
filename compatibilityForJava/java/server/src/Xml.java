import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Xml {
    private static final String FILE_PATH = "xml.xml";

    public static void main(String... args){
        Path filePath = Paths.get(FILE_PATH);
        getXml(filePath);
    }

    public static void getXml(Path filePath){

        System.out.println(filePath.toString());
        System.out.println(getTestData());
        DataSet dataSet = new DataSet(getTestData());
        try {
            final OutputStream os = Files.newOutputStream(filePath);
            final BufferedOutputStream bs = new BufferedOutputStream(os);
            final XMLEncoder encoder = new XMLEncoder(bs);
            encoder.writeObject(dataSet);
            encoder.close();

            File file = new File(filePath.toString());
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String str = br.readLine();
            while(str != null){
            System.out.println(str);
            str = br.readLine();
            }

            br.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String getTestData(){
        char[] array = { 't','e','s','t','<','b','r','>','"','d','a','t','a','"', '\'', '\t', '\r', '\n', '&', ' '};
        return new String(array);
    }
}