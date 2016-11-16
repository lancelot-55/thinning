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
	    byte[][] binary = new byte[w][h];
	    binarization(writeImage, binary, w, h);
	    /* 細線化を行う処理 */
	    thinning(writeImage, binary, w, h);	    
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
    public static void binarization(BufferedImage readImage, byte[][] binary, int w, int h) {
	for (int y = 0; y < h; y++) { // 1ピクセルづつ処理
	    for (int x = 0; x < w; x++) {
		int color = readImage.getRGB(x, y); // 色を取得
		int mono = (int)(0.299 * r(color) + 0.587 * g(color) + 0.114 * b(color)); // モノクロに変換
		if(mono > 230) { // monoが閾値以上なら白，以下なら黒
		    readImage.setRGB(x, y, 0xFFFFFF);
		    binary[x][y] = 1;
		} else {
		    readImage.setRGB(x, y, 0x000000);
		    binary[x][y] = 0;
		}
	    }
	}
    }
    /* 色情報をR，G，B成分に分解 */
    public static int r(int color) {
	return color>>16 & 0xff;
    }
    public static int g(int color) {
	return color>>8 & 0xff;
    }
    public static int b(int color) {
	return color & 0xff;
     } 
    /* 田村のアルゴリズムによる細線化メソッド */
    public static void thinning(BufferedImage readImage, byte[][] binary, int w, int h) {
        byte[][] delete_pix = new byte[w][h];
	while(true) {
	    //int count = 0;
	    //thin(binary, delete_pix, count, w, h, 1);
	    //if(count == 0) break;
	    //count = 0;
	    //thin(binary, delete_pix, count, w, h, 2);
	    //if(count == 0) break;
	    int count = 0;
	    for(int y = 0; y < h-2; y++) {
		for(int x = 0; x < w-2; x++) {
		    byte[] p = new byte[9];
		    p[0] = binary[x][y];
		    p[1] = binary[x+1][y];
		    p[2] = binary[x+2][y];
		    p[3] = binary[x][y+1];
		    p[4] = binary[x+1][y+1];
		    p[5] = binary[x+2][y+1];
		    p[6] = binary[x][y+2];
		    p[7] = binary[x+1][y+2];
		    p[8] = binary[x+2][y+2];
		    if(!delete(p, 1)) continue;
		    if(nodelete(p, 1)) continue;
		    if(con_nodelete(p)) continue;
		    delete_pix[x+1][y+1] = 1;
		    count++;
		}
	    }
	    for(int y = 0; y < h; y++) {
		for(int x = 0; x < w; x++) {
		    if(delete_pix[x][y] == 1) binary[x][y] = 0;
		}
	    }
	    if(count == 0) break;
	    count = 0;
	    for(int y = 0; y < h-2; y++) {
		for(int x = 0; x < w-2; x++) {
		    byte[] p = new byte[9];
		    p[0] = binary[x][y];
		    p[1] = binary[x+1][y];
		    p[2] = binary[x+2][y];
		    p[3] = binary[x][y+1];
		    p[4] = binary[x+1][y+1];
		    p[5] = binary[x+2][y+1];
		    p[6] = binary[x][y+2];
		    p[7] = binary[x+1][y+2];
		    p[8] = binary[x+2][y+2];
		    if(!delete(p, 2)) continue;
		    if(nodelete(p, 2)) continue;
		    if(con_nodelete(p)) continue;
		    delete_pix[x+1][y+1] = 1;
		    count++;
		}
	    }
	    for(int y = 0; y < h; y++) {
		for(int x = 0; x < w; x++) {
		    if(delete_pix[x][y] == 1) binary[x][y] = 0;
		}
	    }
	    if(count == 0) break;
	}
	for(int y = 0; y < h; y++) {
	    for(int x = 0; x < w; x++) {
		if(binary[x][y] == 1) {
		    readImage.setRGB(x, y, 0xffffff);
		} else {
		    readImage.setRGB(x, y, 0);
		}
	    }
	}
    }
    public static void thin(byte[][] binary, byte[][] delete_pix, int count, int w, int h, int petern) {
	for(int y = 0; y < h-2; y++) {
	    for(int x = 0; x < w-2; x++) {
		byte[] p = new byte[9];
		p[0] = binary[x][y];
		p[1] = binary[x+1][y];
		p[2] = binary[x+2][y];
		p[3] = binary[x][y+1];
		p[4] = binary[x+1][y+1];
		p[5] = binary[x+2][y+1];
		p[6] = binary[x][y+2];
		p[7] = binary[x+1][y+2];
		p[8] = binary[x+2][y+2];
		if(!delete(p, petern)) continue;
		if(nodelete(p, petern)) continue;
		if(con_nodelete(p)) continue;
		delete_pix[x+1][y+1] = 1;
		count++;
	    }
	}
	for(int y = 0; y < h; y++) {
	    for(int x = 0; x < w; x++) {
		if(delete_pix[x][y] == 1) binary[x][y] = 0;
	    }
	}
    }
    public static boolean delete(byte[] p, int petern) {
	if(petern == 1) {
	    if((p[1]==0 && p[4]==1) || (p[4]==1 && p[5]==0)) return true;
	} else {
	    if((p[4]==1 && p[7]==0) || (p[3]==0 && p[4]==1)) return true;
	}
	return false;
    }
    public static boolean nodelete(byte[] p, int petern) {
	if(petern == 1) {
	    if((p[1]==0 && p[4]==1 && p[5]==1 && p[7]==1 && p[8]==0) ||
	       (p[0]==0 && p[1]==1 && p[3]==1 && p[4]==1 && p[5]==0)) return true;
	} else {
	    if((p[0]==0 && p[1]==1 && p[3]==1 && p[4]==1 && p[7]==0) ||
	       (p[3]==0 && p[4]==1 && p[5]==1 && p[7]==1 && p[8]==0)) return true;
	}
	return false;
    }
    public static boolean con_nodelete(byte[] p) {
	if((p[3]==0 && p[4]==1 && p[5]==0 && p[7]==1) ||
	   (p[1]==0 && p[3]==1 && p[4]==1 && p[7]==0) ||
	   (p[1]==1 && p[3]==0 && p[4]==1 && p[5]==0) ||
	   (p[1]==0 && p[4]==1 && p[5]==1 && p[7]==0) ||
	   (p[3]==0 && p[4]==1 && p[6]==1 && p[7]==0) ||
	   (p[0]==1 && p[1]==0 && p[3]==0 && p[4]==1) ||
	   (p[1]==0 && p[2]==1 && p[4]==1 && p[5]==0) ||
	   (p[4]==1 && p[5]==0 && p[7]==0 && p[8]==1) ||
	   (p[0]==0 && p[1]==1 && p[2]==0 && p[3]==1 && p[4]==1 && p[5]==1 && p[6]==0 && p[8]==0) ||
	   (p[0]==0 && p[1]==1 && p[2]==0 && p[4]==1 && p[5]==1 && p[6]==0 && p[7]==1 && p[8]==0) ||
	   (p[0]==0 && p[2]==0 && p[3]==1 && p[4]==1 && p[5]==1 && p[6]==0 && p[7]==1 && p[8]==0) ||
	   (p[0]==0 && p[1]==1 && p[2]==0 && p[3]==1 && p[4]==1 && p[6]==0 && p[7]==1 && p[8]==0)) return true;
	return false;
    }
}
/* 参考サイト
   http://treewoods.net/kugelblitz/?p=564, 11月16日
   http://www.hundredsoft.jp/win7blog/log/eid119.html, 11月16日
   http://zattonaka.blogspot.jp/2013/09/java.html, 11月16日
*/
   
