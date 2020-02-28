import java.io.*;
import hpkg.fund.pnm.*;

public class prog0A
{
   public static void main(String[] args)
   {
      try
      {
         // コマンドライン引数を解析する．
	 int threshold = 0;
         if(args.length != 3)
         {
            System.err.println("java prog0A しきい値 入力画像.pgm 出力画像.pgm");
            System.exit(0);
         }
         else
         {
            threshold = Integer.parseInt(args[0]);
         }
	 
         // PGM 形式の濃淡画像を読み込む．
         HPnm inImg = new HPnm();
         inImg.readVoxels(args[1]);

         // 入力した画像のサイズ xsize (横)と ysize (縦)を取得する．
         int ysize = inImg.ysize();
         int xsize = inImg.xsize();
	 
         // 出力用の画像を生成する．画像サイズは(xsize,ysize)で，
         // 画素サイズは 8 Bit Per Pixel.
         HPnm outImg = new HPnm(xsize, ysize, 8);

         // y を 0 から ysize 未満まで，x を 0 から xsize 未満までそれぞれ
         // １ずつ変化させて，以下の処理を繰り返す．なお，この順番に画素に
         // アクセスすることを「ラスタスキャン」という．
         for(int y=0; y<ysize; y++) for(int x=0; x<xsize; x++)
         {
            // 入力画像の位置 (x,y) における画素値(符号なし)を取得する．
            int value = inImg.getUnsignedValue(x, y);

            // その画素値が「しきい値」以上のときだけ，出力画像の同じ位置の
            // 画素に１をセットする(outImg の画素値のデフォルトは０である
            // ことに注意する)．
            if(value >= threshold)
            {
               outImg.setValue(x, y, 255);
            }
         }

         // 出力用の画像を書き出す．
         outImg.writeVoxels(args[2]);
      }
      catch(Exception e)
      {
         e.printStackTrace();
      }
   }
}
