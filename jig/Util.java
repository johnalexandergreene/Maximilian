package org.fleen.maximilian.jig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fleen.forsythia.grammar.Jig;
import org.fleen.forsythia.grammar.JigSection;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.DVector;
import org.fleen.geom_2D.GD;
import org.fleen.geom_Kisrhombille.KAnchor;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;
import org.fleen.maximilian.MMetagon;
import org.fleen.maximilian.MPolygon;
import org.fleen.maximilian.boundedDeformableKGrid.BoundedDeformableKGrid;

/*
 * Maximillian jigging utilities
 * shared utilities used by Jigs
 * geometry and stuff
 */
public class Util{
  
//  public static final List<MPolygon> split(MPolygon target,MJig jig){
//    return split(target,jig);}
  
  /*
   * split an MPolygon with a Forsythia Operator (presently called Jig, soon to be called ForsythiaOperator or something)
   * Used by MJig_Splitter, MJig_Crusher, MJig_Grinder
   */
  public static final List<MPolygon> split(MPolygon target,Jig jig){
    BoundedDeformableKGrid grid=new BoundedDeformableKGrid(target,jig.getGridDensity());
    List<MPolygon> shards=new ArrayList<MPolygon>();
    MPolygon mpolygon;
    DPolygon dpolygon;
    MMetagon mmetagon;
    for(JigSection s:jig.sections){
      mmetagon=new MMetagon(s.productmetagon);
      dpolygon=getDPolygon(mmetagon,s.productanchor,grid);
      mpolygon=new MPolygon(dpolygon,mmetagon,s.productchorusindex,Arrays.asList(s.tags.getTags()));
      shards.add(mpolygon);}
    return shards;}
  
  private static final DPolygon getDPolygon(MMetagon mmetagon,KAnchor anchor,BoundedDeformableKGrid grid){
    
    boolean twist=anchor.twist;
    
    KPolygon kpolygon=mmetagon.getPolygon(anchor.v0,anchor.v1,twist);
    DPolygon dpolygon=new DPolygon();
    DPoint dpoint;
    int vertexindex=0;
    for(KVertex v:kpolygon){
      dpoint=grid.getPoint(v);
      
      //DEBUG
      if(dpoint==null){
        System.out.println("@@@--@@@ NULL DPOINT DETECTED @@@--@@@");
        System.out.println("kvertex that returned null dpoint : "+v);
        System.out.println("VERTEX INDEX : "+vertexindex);
        System.out.println("kpolygon : "+kpolygon);
        System.out.println("kpolygon size : "+kpolygon.size());
        System.out.println("kanchor : "+anchor);
        System.out.println("---------------------------------------------");
//        throw new IllegalArgumentException();
      }
      
      dpolygon.add(dpoint);
      
      vertexindex++;}//DEBUG
    
    return dpolygon;}
  
  /*
   * shrink a polygon by an even span all around
   * used by MJig_Boiler, MJig_Crusher
   */
  public static final void shrink(DPolygon dp,double span){
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
      vectors.add(getVector(dp.get(iprior),dp.get(i),dp.get(inext),span,polygonisclockwise));}
    for(int i=0;i<s;i++){
      p=dp.get(i);
      v=vectors.get(i);
      p.applyVector(v);}}
  
  private static final DVector getVector(DPoint p0,DPoint p1,DPoint p2,double span,boolean polygonisclockwise){
    double  
      dir=GD.getDirection_3Points(p0.x,p0.y,p1.x,p1.y,p2.x,p2.y),
      angle,mag;
    if(polygonisclockwise){
      angle=GD.getAngle_3Points(p0.x,p0.y,p1.x,p1.y,p2.x,p2.y);
      mag=span/(GD.sin(angle/2));
    }else{
      angle=GD.getAngle_3Points(p2.x,p2.y,p1.x,p1.y,p0.x,p0.y);
      mag=-span/(GD.sin(angle/2));}
    return new DVector(dir,mag);}

}
