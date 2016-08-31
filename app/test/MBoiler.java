package org.fleen.maximilian.app.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.DVector;
import org.fleen.geom_2D.GD;
import org.fleen.maximilian.MJig;
import org.fleen.maximilian.MMetagon;
import org.fleen.maximilian.MPolygon;
import org.fleen.maximilian.MShape;
import org.fleen.maximilian.MYard;

/*
 * copy the target 
 *   Must be MPolygon
 * squeeze it, return it
 * use the same metagon
 */
public class MBoiler implements MJig{

  
  private static final double BOILSPAN=0.2;
  
  public List<MShape> createShapes(MShape target){
    System.out.println("create shapes boiler");
    if(target instanceof MYard)return null;
    MPolygon mptarget=(MPolygon)target;
    MMetagon mm=mptarget.mmetagon;//metagons are immutable
    DPolygon dp=(DPolygon)mptarget.dpolygon.clone();
    MPolygon newmpolygon=new MPolygon(dp,mm,0,new ArrayList<String>());
    shrink(dp);
    MYard newyard=new MYard(Arrays.asList(new MPolygon[]{(MPolygon)target,newmpolygon}),0,new ArrayList<String>());
    return Arrays.asList(new MShape[]{newyard,newmpolygon});}
  
  private void shrink(DPolygon dp){
    //crude TODO properly
    int s=dp.size(),iprior,inext;
    List<Double> inwards=new ArrayList<Double>(s);
    double d;
    DPoint p;
    for(int i=0;i<s;i++){
      iprior=i-1;
      if(iprior==-1)iprior=s-1;
      inext=i+1;
      if(inext==s)inext=0;
      inwards.add(getInwards(dp.get(iprior),dp.get(i),dp.get(inext)));}
    for(int i=0;i<s;i++){
      p=dp.get(i);
      d=inwards.get(i);
      p.applyVector(new DVector(d,BOILSPAN));}}
  
  private double getInwards(DPoint p0,DPoint p1,DPoint p2){
    double d=GD.getDirection_3Points(p0.x,p0.y,p1.x,p1.y,p2.x,p2.y);
    return d;}

  @Override
  public double getDetailSizePreview(MShape target){
    // TODO Auto-generated method stub
    return 0;
  }

}
