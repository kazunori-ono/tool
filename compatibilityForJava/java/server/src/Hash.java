
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import common.ConvertHash;

public class Hash {
    private static final String FILE_PATH = "testData.jpg";

    public static void main(String... args) {
        System.out.println(ConvertHash.getFileHash(FILE_PATH));
    }
}