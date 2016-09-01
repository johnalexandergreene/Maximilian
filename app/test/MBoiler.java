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

  
  private static final double BOILSPAN=0.07;
  
  public List<MShape> createShapes(MShape target){
    if(target instanceof MYard)return null;
    MPolygon mptarget=(MPolygon)target;
    //create inner polygon
    MMetagon innermpolygonmetagon=mptarget.mmetagon;//metagons are immutable
    DPolygon innermpolygondpolygon=(DPolygon)mptarget.dpolygon.clone();
    MPolygon innermpolygon=new MPolygon(innermpolygondpolygon,innermpolygonmetagon,0,new ArrayList<String>());
    shrink(innermpolygondpolygon);
    innermpolygon.setParent(target);
    //create yard
    MYard yard=new MYard(Arrays.asList(new MPolygon[]{(MPolygon)target,innermpolygon}),0,new ArrayList<String>());
    yard.setParent(target);
    //
    List<MShape> newshapes=new ArrayList<MShape>(2);
    newshapes.add(yard);
    newshapes.add(innermpolygon);
    target.setChildren(newshapes);
    //
    return newshapes;}
  
  //TODO clean up
  private void shrink(DPolygon dp){
    boolean polygonisclockwise=dp.getTwist();
    int s=dp.size(),iprior,inext;
    List<DVector> vectors=new ArrayList<DVector>(s);
    DPoint p;
    DVector v;
    for(int i=0;i<s;i++){
      iprior=i-1;
      if(iprior==-1)iprior=s-1;
      inext=i+1;
      if(inext==s)inext=0;
      vectors.add(getVector(dp.get(iprior),dp.get(i),dp.get(inext),BOILSPAN,polygonisclockwise));}
    for(int i=0;i<s;i++){
      p=dp.get(i);
      v=vectors.get(i);
      p.applyVector(v);}}
  
  private DVector getVector(DPoint p0,DPoint p1,DPoint p2,double span,boolean polygonisclockwise){
    double  
      dir=GD.getDirection_3Points(p0.x,p0.y,p1.x,p1.y,p2.x,p2.y),
      angle,mag;
    if(polygonisclockwise){
      angle=GD.getAngle_3Points(p0.x,p0.y,p1.x,p1.y,p2.x,p2.y);
      mag=BOILSPAN/(GD.sin(angle/2));
    }else{
      angle=GD.getAngle_3Points(p2.x,p2.y,p1.x,p1.y,p0.x,p0.y);
      mag=-BOILSPAN/(GD.sin(angle/2));//TODO this is weird. I dunno why we gotta do neg. Leave it for now.
    }
    
    return new DVector(dir,mag);}

  @Override
  public double getDetailSizePreview(MShape target){
    // TODO Auto-generated method stub
    return 0;
  }

}
