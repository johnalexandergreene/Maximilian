package org.fleen.maximilian.jig;

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
 * 
 * Create a donut of space along the perimeter of a polygon
 * 
 * the yard is tagged "donut"
 * the inner polygon is tagged "egg"
 * 
 * copy the target 
 *   Must be MPolygon
 * squeeze it, return it
 * use the same metagon
 */
public class MJig_Boiler implements MJig{

  
  private static final double BOILSPAN=0.04;//this will be one of 2 or 3 (think small, med, large)
  //selected at random at jig creation time, specified in composition constructor, or jigserver if we are doing that
  
  public List<MShape> createShapes(MShape target){
    if(target.hasBadGeometry())return null;//DEBUG
    //
    if(target instanceof MYard)return null;
    //
    MPolygon mptarget=(MPolygon)target;
    //create egg
    MMetagon eggmetagon=mptarget.mmetagon;//metagons are immutable
    DPolygon eggdpolygon=(DPolygon)mptarget.dpolygon.clone();
    
//    for(DPoint a:eggdpolygon){
//      if(a==null){
//        System.out.println("=== NULL POINT DETECTED ===+");  
//        Thread.currentThread().getStackTrace();}}
    
    
    MPolygon egg=new MPolygon(eggdpolygon,eggmetagon,0,new ArrayList<String>());
    Util.shrink(eggdpolygon,BOILSPAN);
    egg.setParent(target);
    egg.addTags(Arrays.asList(new String[]{"egg"}));
    //create yard
    MYard yard=new MYard(Arrays.asList(new MPolygon[]{(MPolygon)target,egg}),0,new ArrayList<String>());
    yard.setParent(target);
    //
    List<MShape> newshapes=new ArrayList<MShape>(2);
    newshapes.add(yard);
    newshapes.add(egg);
    target.setChildren(newshapes);
    //
    return newshapes;}


  //TODO
  //also check and constrain distortion
  public double getDetailSizePreview(MShape target){
    // TODO Auto-generated method stub
    return 0;
  }

}
