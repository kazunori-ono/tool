import java.math.BigDecimal;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.awt.color.ColorSpace;
import java.awt.image.ColorConvertOp;
import java.awt.RenderingHints;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Color;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.io.File;
import java.io.FileOutputStream;
import common.ConvertHash;

public class ImageConvert{
    private static final String FILE_PATH = "testData.jpg";
    private static final String OUTPUT_FILE_PATH = "testData2.jpg";

    public static void main(String... args){
        System.out.println("Start");
        try{
            execute();
            System.out.println("End");
        }catch(Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }
    }

    private static void execute() throws Exception {
        final File imageFile = new File(FILE_PATH);
        final Path imageFilePath = imageFile.toPath();
		final BufferedImage image = ImageIO.read(imageFile);
		int width = image.getWidth();
		int height = image.getHeight();
		if(width * 4 == height * 3){
			//何もしない
		}else if(width * 4 > height * 3){
			//縦幅を補完
			height = (width * 4) / 3;
		}else{
			//横幅を補完
			width = (height * 3) / 4;
		}
        byte[] bytes = convertImage(imageFilePath.toString(), Color.WHITE, width, height, false, false);
        FileOutputStream fileOuputStream = new FileOutputStream(OUTPUT_FILE_PATH);
        fileOuputStream.write(bytes);
        System.out.println(ConvertHash.getFileHash(OUTPUT_FILE_PATH));
    }

    public static byte[] convertImage(String fileName, Color background, int width, int height, boolean grayScale, boolean proportional) throws IOException{
        FileInputStream input = null;
        try{
            input = new FileInputStream(fileName);
            return convertImage(input, background, width, height, grayScale, proportional);
        }finally{
            if(input != null){
                input.close();
                input = null;
            }
        }
    }

    private static byte[] convertImage(InputStream data, Color background, int width, int height, boolean grayScale, boolean proportional) throws IOException{
		ByteArrayOutputStream out = null;
		BufferedImage image = null;
		BufferedImage shrinkImage = null;
		Graphics2D graphics = null;
		ColorConvertOp op = null;
		double magH = 0;
		double magW = 0;
		int magHeight = height;
		int magWidth = width;

		try{
			image = ImageIO.read(data);

			//縮尺判定
			if(width <= 0 && height <= 0){
				return null;

			}else if(width == 0){
				magH = new BigDecimal(height).divide(new BigDecimal(image.getHeight()), 3, BigDecimal.ROUND_HALF_UP).doubleValue();
				magW = magH;
				magWidth = (int)(image.getWidth() * magW);

			}else if(height == 0){
				magW = new BigDecimal(width).divide(new BigDecimal(image.getWidth()), 3, BigDecimal.ROUND_HALF_UP).doubleValue();
				magH = magW;
				magHeight = (int)(image.getHeight() * magH);

			}else{
				magH = new BigDecimal(height).divide(new BigDecimal(image.getHeight()), 3, BigDecimal.ROUND_HALF_UP).doubleValue();
				magW = new BigDecimal(width).divide(new BigDecimal(image.getWidth()), 3, BigDecimal.ROUND_HALF_UP).doubleValue();

				//縦横小さい縮尺に合わせて縮小する
				if(proportional){
					if(magH > magW){
						magH = magW;
						magHeight = (int)(image.getHeight() * magH);
					}else if(magH < magW){
						magW = magH;
						magWidth = (int)(image.getWidth() * magW);
					}
				}
			}

			shrinkImage = new BufferedImage(magWidth, magHeight, image.getType());
			graphics = shrinkImage.createGraphics();
			graphics.setColor(background);
			graphics.fillRect(0, 0, magWidth, magHeight);

			//アルファ補完 - クオリティ優先
			graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
					RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

			//補完 - クオリティ優先
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			//描画 - クオリティ優先
			graphics.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);

			//カラー描画 - クオリティ優先
			graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
					RenderingHints.VALUE_COLOR_RENDER_QUALITY);

			//ディザリング - 有効
			graphics.setRenderingHint(RenderingHints.KEY_DITHERING,
					RenderingHints.VALUE_DITHER_ENABLE);

			//アンチエイリアス - 有効
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			//テキストアンチエイリアス - 有効
			graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

			//フォント部分メトリックス
			graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
					RenderingHints.VALUE_FRACTIONALMETRICS_ON);

			//ストローク正規化コントロール
			graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
					RenderingHints.VALUE_STROKE_NORMALIZE);

			if(magW == magH){
				if(grayScale){
					op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
					graphics.drawImage(op.filter(image, null), 0, 0, magWidth, magHeight, null);
				}else{
					graphics.drawImage(image, 0, 0, magWidth, magHeight, null);
				}

			}else if(magW > magH){
				magHeight = height;
				magWidth = (int)(image.getWidth() * magH);
				if(grayScale){
					op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
					graphics.drawImage(op.filter(image, null), (width - magWidth) / 2, 0, magWidth, magHeight, background, null);
				}else{
					graphics.drawImage(image, (width - magWidth) / 2, 0, magWidth, magHeight, background, null);
				}

			}else{
				magHeight = (int)(image.getHeight() * magW);
				magWidth = width;
				if(grayScale){
					op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
					graphics.drawImage(op.filter(image, null), 0, (height - magHeight) / 2, magWidth, magHeight, background, null);
				}else{
					graphics.drawImage(image, 0, (height - magHeight) / 2, magWidth, magHeight, background, null);
				}
			}

			out = new ByteArrayOutputStream();
			ImageIO.write(shrinkImage, "jpg", out);
			return out.toByteArray();
		}finally{
			if(image != null){
				image.flush();
				image = null;
			}
			if(shrinkImage != null){
				shrinkImage.flush();
				shrinkImage = null;
			}
			graphics = null;
		}
	}


}