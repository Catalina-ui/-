import java.io.*;
import hpkg.fund.pnm.*;

public class prog1_4 {
    final static int L = 65536;

    static double[] calculate_area(boolean a[], HPnm b) {
        int figureNumber = 1;
        double figureArea[];
        figureArea = new double[L];
        for(int i = 0;i < L;i++) figureArea[i] = 0;

        while(a[figureNumber] == true){
            for(int y= 0;y < b.ysize();y++) for(int x = 0;x <b.xsize();x++){
                int value = b.getUnsignedValue(x,y);
                if(figureNumber == value)
                    figureArea[figureNumber] += 1.0;
            }
            System.out.println(figureNumber+"番目の面積は"+figureArea[figureNumber]);
            figureNumber++;
        }
        return figureArea;
    }

    static double[][] calculate_moment(boolean a[], double b[], HPnm c){
        int figureNumber = 1;
        double figureMoment[][];
        figureMoment = new double[L][2];
        for(int i=0;i < L;i++) for(int j =0;j<2;j++) figureMoment[i][j] = 0;

        while(a[figureNumber] == true){
            int total_x = 0,total_y = 0;
            for(int y = 0;y<c.ysize();y++){
                for(int x = 0;x<c.xsize();x++){
                    int value = c.getUnsignedValue(x,y);
                    if(figureNumber == value)
                       total_x += x;
                }
            }
            for (int x = 0; x < c.xsize(); x++) {
                for (int y = 0; y < c.ysize(); y++) {
                    int value = c.getUnsignedValue(x, y);
                    if (figureNumber == value)
                        total_y += y;
                }
            }
        figureMoment[figureNumber][0] = total_x/b[figureNumber];
        figureMoment[figureNumber][1] = total_y/b[figureNumber];
        System.out.println(figureNumber+"番目の重心は("+figureMoment[figureNumber][0]+","+figureMoment[figureNumber][1]+")");
        figureNumber++;
        }
        return figureMoment;
    }

    static double[] calculate_length(boolean a[],HPnm b){
        int figureNumber = 1;
        double figureLength[];
        figureLength = new double[L];
        for(int i = 0;i<L;i++) figureLength[i] = 0;

        while(a[figureNumber] == true){
            for(int y=0;y<b.ysize();y++) for(int x=0;x<b.xsize();x++){
                int value_xa = 0,value_xb = 0,value_ya=0,value_yb=0;
                int value = b.getUnsignedValue(x,y);
                if(x>0) value_xb = b.getUnsignedValue(x-1,y);
                if(y>0) value_yb = b.getUnsignedValue(x,y-1);
                if(x < b.xsize()-1) value_xa = b.getUnsignedValue(x+1,y);
                if(y < b.ysize()-1) value_ya = b.getUnsignedValue(x,y+1);

                if(value == figureNumber && (value_xa == 0 || value_xb == 0 || value_ya == 0|| value_yb == 0)){
                    figureLength[figureNumber] += 1.0;
                }
            }
            System.out.println(figureNumber+"番目の周囲長は"+figureLength[figureNumber]);
            figureNumber++;
        }
        return figureLength;
    }

    static double[] calculate_circularity(boolean a[],double area[],double length[]){
        int figureNumber = 1;
        double figureCricularity[];
        figureCricularity = new double[L];

        while(a[figureNumber] == true){
            figureCricularity[figureNumber] = 4.0 * 3.14159 * area[figureNumber] / (length[figureNumber]*length[figureNumber]);
            System.out.println(figureNumber+"番目の円形度は"+figureCricularity[figureNumber]);
            figureNumber++;
        }

        return figureCricularity;
    }

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

            int LUT[];
            boolean LutCheck[], figureCount[];
            double figureArea[],figureMoment[][],figureLength[],figureCricularity[];

            LUT = new int[L];
            figureArea = new double[L];
            figureCount = new boolean[L];
            LutCheck = new boolean[L];
            figureMoment = new double[L][2];
            figureLength = new double[L];
            figureCricularity = new double[L];

            for (int i = 0; i < L; i++) {
                LUT[i] = i;
                LutCheck[i] = false;
                figureCount[i] = false;
                figureArea[i] = 0;
                figureLength[i] = 0;
                for(int j=0;j<2;j++) figureMoment[i][j] = 0;
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
                    if (LUT[value] != 0) {
                        int check = LUT[value];

                        if (LutCheck[check] != true && figureCount[check] != true) {
                            LutCheck[check] = true;
                            figureCount[f_l] = true;
                            for (int i = 1; i < L; i++) {
                                if (check == LUT[i])
                                    LUT[i] = f_l;
                            }
                            f_l++;
                        }
                    }
                    outImg.setValue(x, y, LUT[value]);
                }
            
            figureArea = calculate_area(figureCount,outImg);
            figureMoment = calculate_moment(figureCount,figureArea,outImg);
            figureLength = calculate_length(figureCount,outImg);
            figureCricularity = calculate_circularity(figureCount, figureArea, figureLength);

            outImg.writeVoxels(args[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
