import java.io.*;
import hpkg.fund.pnm.*;
import hpkg.tk.ImageFilter.*;

public class prog3_2 {

    public static HPnm changeSize(HPnm inImg,int count) {
        try {
            int xsize = inImg.xsize(),  ysize = inImg.ysize();
            int size = xsize;
            if(ysize > xsize) size = ysize;
            HPnm otwImg = inImg; 
            int a = otwImg.xsize(), b = otwImg.ysize();
            HPnm fImg = inImg;
            for(int i = size; i > count;i--){
                a -= 1; b -= 1;
                HPnm Img = new HPnm(a,b,8);

                for(int x = 0;x < a; x++){
                    for(int y = 0;y < b; y++){
                        double dx = (double)(otwImg.xsize()-1)/(a-1)*(x);
                        double dy = (double)(otwImg.ysize()-1)/(b-1)*(y);

                        int nx = (int)dx,   ny = (int)dy;
                        
                        double value = (1-dx+nx)*(1-dy+ny)*otwImg.getUnsignedValue(nx,ny);
                            
                        if(nx+1 < otwImg.xsize() - 1)
                        {value +=(dx-nx)*(1-dy+ny)*otwImg.getUnsignedValue(nx+1,ny);}
                        else{value += (dx - nx) * (1 - dy + ny) * otwImg.getUnsignedValue(nx, ny);}

                        if(ny+1 < otwImg.ysize() - 1)
                        {value +=(1-dx+nx)*(dy-ny)*otwImg.getUnsignedValue(nx,ny+1);}
                        else{value += (1 - dx + nx) * (dy - ny) * otwImg.getUnsignedValue(nx, ny);}

                        if(nx+1 < otwImg.xsize() - 1 && ny+1 < otwImg.ysize() - 1)
                        {value +=(dx-nx)*(dy-ny)*otwImg.getUnsignedValue(nx+1,ny+1);}
                        else{value += (dx - nx) * (dy - ny) * otwImg.getUnsignedValue(nx, ny);}
                        
                        Img.setValue(x,y,(int)value);
                    }
                }
                fImg = Img;
            }
            return fImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                System.err.println("java prog3A 入力画像(全体).pgm 入力画像(テンプレート).pgm");
                System.exit(0);
            }
            
            HPnm inImg = new HPnm(); // 全体画像
            inImg.readVoxels(args[0]);
            
            HPnm inImgTMPL = new HPnm(); // テンプレート画像
            inImgTMPL.readVoxels(args[1]);
            inImgTMPL = changeSize(inImgTMPL,54);
            int ysize = inImg.ysize();
            int xsize = inImg.xsize();

            int ysizeTMPL = inImgTMPL.ysize();
            int xsizeTMPL = inImgTMPL.xsize();

            int xMin = 0, yMin = 0;
            int valueDiffMin = Integer.MAX_VALUE;
            int po_theta = 0;

            for(int theta = 0; theta < 10; theta += 15){
                System.out.println(theta);
                HPnm rotatedImg = HPixelFilter.rotate(inImgTMPL, -(double)theta / 180. * Math.PI);
                for (int y = ysizeTMPL / 2; y < ysize - ysizeTMPL / 2; y++)
                    for (int x = xsizeTMPL / 2; x < xsize - xsizeTMPL / 2; x++) {
                        int valueDiff = 0;
                        for (int yt = -ysizeTMPL / 2; yt < ysizeTMPL / 2; yt++)
                            for (int xt = -xsizeTMPL / 2; xt < xsizeTMPL / 2; xt++) {
                                int value = inImg.getUnsignedValue(x + xt, y + yt);
                                int valueTMPL = rotatedImg.getUnsignedValue(xt + xsizeTMPL / 2, yt + ysizeTMPL / 2);
                                if(valueTMPL != 0)
                                valueDiff += Math.abs(value - valueTMPL); // 絶対値
                            }

                        if (valueDiff < valueDiffMin) {
                            valueDiffMin = valueDiff;
                            po_theta = theta;
                            xMin = x;
                            yMin = y;
                        }
                    }
                }
            System.out.println("photo xsize: "+inImgTMPL.xsize()+" ysize: "+inImgTMPL.ysize()+" politic_theta: "+po_theta+"(x, y) = (" + xMin + ", " + yMin + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
