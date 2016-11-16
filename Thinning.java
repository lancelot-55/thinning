/* 
   --画像処理演習課題--
   所属: 情報学群2年
   学籍番号: 1190304
   氏名: 氏原友梨亜
*/
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

class Thinning {
    public static void main(String args[]){
	try{
            /* 入力画像の読み込み */
	    BufferedImage readImage = ImageIO.read(new File("sample.png"));
	    int w = readImage.getWidth(); //横幅
	    int h = readImage.getHeight(); //縦幅

	    /* 出力画像の準備 */
	    BufferedImage writeImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

	    /* コピーを行う処理 */
	    copy(readImage, writeImage, w,h);

	    /* グレイスケール化を行う処理 */
	    grayScale(writeImage, w, h);

	    /* 二値化を行う処理 */
	    binarization(writeImage, w, h);

	    /* 細線化を行う処理 */
	    
	    /* output.pngへの書き込み */
	    ImageIO.write(writeImage, "png", new File("output.png"));

	}catch (IOException e){
	    /* 例外処理 */
	    throw new RuntimeException(e.toString());
	}
        System.out.println("画像処理が完了しました");
    }

    /* コピーメソッド */
    public static void copy(BufferedImage readImage, BufferedImage writeImage, int w, int h){
	// 1ピクセルづつ処理を行う
	for (int y = 0; y < h; y++) {
	    for (int x = 0; x < w; x++) {
		int color = readImage.getRGB(x, y); // 入力画像の画素値を取得
		writeImage.setRGB(x, y, color); //出力画像に画素値をセット			
	    }
	}
    }
    /* グレイスケール化メソッド */
    public static void  grayScale(BufferedImage readImage, int w, int h) {
	// 出力画像をバイトグレースケールで表示
	BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
    	output.setRGB(0, 0, w, h, readImage.getRGB(0, 0, w, h, null, 0, w), 0, w);
        for (int y = 0; y < h; y++) {
	    for (int x = 0; x < w; x++) {
		int color = output.getRGB(x, y); // 入力画像の画素値を取得
		readImage.setRGB(x, y, color); //出力画像に画素値をセット			
	    }
	}
    }
    /* 二値化メソッド */
    public static void binarization(BufferedImage readImage, int w, int h) {
	BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
	for (int y = 0; y < h; y++) {
	    for (int x = 0; x < w; x++) {
		int color = readImage.getRGB(x, y);
		int mono = (int)(0.299 * r(color) + 0.587 * g(color) + 0.114 * b(color));
		if(mono > 125) {
		    output.setRGB(x, y, 0xFFFFFF);
		} else {
		    output.setRGB(x, y, 0x000000);
		}
	    }
	}
	for (int y = 0; y < h; y++) {
	    for (int x = 0; x < w; x++) {
		int color = output.getRGB(x, y); // 入力画像の画素値を取得
		readImage.setRGB(x, y, color); //出力画像に画素値をセット			
	    }
	}
    }
    public static int r(int c){
	    return c>>16&0xff;
    }
    public static int g(int c){
	    return c>>8&0xff;
    }
    public static int b(int c){
	    return c&0xff;
     }
    
    /* 細線化メソッド */
}
