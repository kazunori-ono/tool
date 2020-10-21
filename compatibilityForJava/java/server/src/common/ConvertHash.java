package common;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ConvertHash {
    public static String getFileHash(String filePath) {

        Path path = Paths.get(filePath);

        byte[] hash = null;

        // アルゴリズム取得
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try (
            // 入力ストリームの生成
            DigestInputStream dis = new DigestInputStream(new BufferedInputStream(Files.newInputStream(path)), md)) {

            // ファイルの読み込み
            while (dis.read() != -1) {
            }

            // ハッシュ値の計算
            hash = md.digest();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // ハッシュ値（byte）を文字列に変換し返却
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            String hex = String.format("%02x", b);
            sb.append(hex);
        }
        return sb.toString();
    }

}