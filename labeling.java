import java.io.*;
import hpkg.fund.pnm.*;

public class prog1_3 {
    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                System.err.print("java prog1_2 1 2");
                System.exit(1);
            }
            HPnm inImg = new HPnm();
            inImg.readVoxels(args[0]);

            int xsize = inImg.xsize();
            int ysize = inImg.ysize();

            HPnm labelImg = new HPnm(xsize, ysize, 32);
            HPnm outImg = new HPnm(xsize, ysize, 8);

            int L = 65536;
            int LUT[];
            boolean LutCheck[],figureCount[];
            LUT = new int[L];
            figureCount = new boolean[L];
            LutCheck = new boolean[L];

            for (int i = 0; i < L; i++) {
                LUT[i] = i;
                LutCheck[i] = false;
                figureCount[i] = false;
            }
            ;
            int lnew = 1;

            for (int y = 0; y < ysize; y++)
                for (int x = 0; x < xsize; x++)
                    labelImg.setValue(x, y, 0);

            for (int y = 0; y < ysize; y++)
                for (int x = 0; x < xsize; x++) {

                    int value = inImg.getUnsignedValue(x, y);
                    int vx = 0, vy = 0;
                    if (value != 0) {
                        if (x > 0)
                            vx = labelImg.getUnsignedValue(x - 1, y);
                        if (y > 0)
                            vy = labelImg.getUnsignedValue(x, y - 1);

                        if (vx == 0 && vy == 0) {
                            labelImg.setValue(x, y, lnew);
                            lnew++;
                            // System.out.println(lnew);
                        }
                        if (vx != 0 && vy != 0 && vx == vy) {

                            labelImg.setValue(x, y, vx);
                        }
                        if (vx == 0 && vy != 0) {

                            labelImg.setValue(x, y, vy);
                        }
                        if (vx != 0 && vy == 0) {
                            labelImg.setValue(x, y, vx);
                        }
                        if ((vx != 0 && vy != 0) && vx != vy) {
                            if (vx > vy) {
                                labelImg.setValue(x, y, vy);
                                for (int i = 1; i < lnew; i++) {
                                    if (LUT[i] == LUT[vx])
                                        LUT[i] = LUT[vy];
                                }
                            } else {
                                labelImg.setValue(x, y, vx);
                                for (int i = 1; i < lnew; i++) {
                                    if (LUT[i] == LUT[vy]) {
                                        LUT[i] = LUT[vx];
                                    }
                                }
                            }
                        }
                    }
                }
                int f_l = 1;
            for (int y = 0; y < ysize; y++)
                for (int x = 0; x < xsize; x++) {
                    int value = labelImg.getUnsignedValue(x, y);
                    if(LUT[value] != 0){
                        int check = LUT[value];
                        
                        if(LutCheck[check] != true && figureCount[check] != true){
                            LutCheck[check] = true;
                            figureCount[f_l] = true;
                            for(int i = 1;i < L;i++){
                                if(check == LUT[i])
                                    LUT[i] = f_l;
                            }
                            //System.out.println(f_l);
                            f_l++;
                        }
                    }
                    outImg.setValue(x, y, LUT[value]);
                }
            outImg.writeVoxels(args[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
