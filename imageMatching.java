import java.io.*;
import hpkg.fund.pnm.*;

public class prog3A
{
   public static void main(String[] args)
   {
      try
      {
         if(args.length != 2)
         {
            System.err.println("java prog3A 入力画像(全体).pgm 入力画像(テンプレート).pgm");
            System.exit(0);
         }

         HPnm inImg = new HPnm(); // 全体画像
         inImg.readVoxels(args[0]);

         HPnm inImgTMPL = new HPnm(); // テンプレート画像
         inImgTMPL.readVoxels(args[1]);

         int ysize = inImg.ysize();
         int xsize = inImg.xsize();

         int ysizeTMPL = inImgTMPL.ysize();
         int xsizeTMPL = inImgTMPL.xsize();

	 int xMin=0, yMin=0;
	 int valueDiffMin = Integer.MAX_VALUE;
	 
	 // 全体画像に部分画像を重ね，対応する画素の差の絶対値和を計算
	 // する．絶対値和が最小になる位置を対応する位置とする．
         for(int y=ysizeTMPL/2; y<ysize-ysizeTMPL/2; y++)
         for(int x=xsizeTMPL/2; x<xsize-xsizeTMPL/2; x++)
         {
	    int valueDiff = 0;
            for(int yt=-ysizeTMPL/2; yt<ysizeTMPL/2; yt++)
            for(int xt=-xsizeTMPL/2; xt<xsizeTMPL/2; xt++)
            {
	       int value     = inImg.getUnsignedValue(x+xt, y+yt);
	       int valueTMPL = inImgTMPL.getUnsignedValue(xt+xsizeTMPL/2, yt+ysizeTMPL/2);
	       valueDiff += Math.abs(value - valueTMPL); // 絶対値
	    }
	    
	    if(valueDiff < valueDiffMin)
	    {
	       valueDiffMin = valueDiff;
	       xMin = x;
	       yMin = y;
	    }
	 }
	 
	 System.out.println("(x, y) = (" + xMin + ", " + yMin + ")");
      }
      catch(Exception e)
      {
         e.printStackTrace();
      }
   }
}
