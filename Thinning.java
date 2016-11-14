/* 
   --画像処理演習課題--
   所属:
   学籍番号:
   氏名:
*/
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

class Thinning {

	public static void main(String args[]){
		try{

			/* 入力画像の読み込み */
			BufferedImage readImage = ImageIO.read(new File(args[0])); //第一引数をファイル名とする
			int w = readImage.getWidth(); //横幅
			int h = readImage.getHeight(); //縦幅

			/* 出力画像の準備 */
			BufferedImage writeImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

		    /*
		     * 細線化処理を記述してください。
		     * 必要に応じて変数・メソッドの定義を行ってかまいません。
		     * 例として下記にコピーを行うだけのメソッドを記述します。
		     */
		    /* 例:コピーを行う処理 */
		    copy(readImage, writeImage, w,h);

		    /* output.pngへの書き込み */
		    ImageIO.write(writeImage, "png", new File("output.png"));

		}catch (IOException e){
			/* 例外処理 */
			throw new RuntimeException(e.toString());
		}
		System.out.println("画像処理が完了しました");
	}

	/*
	 * メソッドの定義
	 */
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
}