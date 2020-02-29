import java.io.*;
import hpkg.fund.pnm.*;

public class prog1_1{
    public static void main(String[] args){
        try{
            if(args.length != 2){
                System.err.println("java prog1_1 1 2");
                System.exit(0);
            }

            HPnm inImg = new HPnm();
            inImg.readVoxels(args[0]);

            int xsize = inImg.xsize();
            int ysize = inImg.ysize();

            HPnm outImg = new HPnm(xsize,ysize,8);

            double qmax = 0;
            int tmax = 0;

            for(int t=0; t<=255; t++){

                double n0 = 0,n1 = 0,sum0 = 0,sum1 = 0;
                double u1 = 0,u0 = 0;

                for(int y=0; y<ysize;y++) for(int x=0;x<xsize;x++){
                    double value = inImg.getUnsignedValue(x,y);

                    if(value >= (double)t){
                        n1++;
                        sum1 += value;
                    }else{
                        n0++;
                        sum0 += value;
                    }
                }
                    if(n0 > 0) u0 = sum0/n0;
                    if(n1 > 0) u1 = sum1/n1;
                    double q = n0*n1*(u0 - u1) * (u0 - u1) /((n0 + n1)*(n0 + n1));
                    //System.out.println("q: "+q+" t: "+ t);
                    if(q >= qmax){
                        qmax = q;
                        tmax = t;
                }
            }
            System.out.println(tmax);

            for(int y=0; y<ysize; y++) for(int x=0; x<xsize; x++){
                int value = inImg.getUnsignedValue(x, y);

                if(value >= tmax){
                    outImg.setValue(x, y, 255);
                }
            }

            outImg.writeVoxels(args[1]);
        }catch(Exception e){
                e.printStackTrace();
        }
    }
}
