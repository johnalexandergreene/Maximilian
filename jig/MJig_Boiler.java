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
import org.fleen.maximilian.app.test.Util;

/*
 * copy the target 
 *   Must be MPolygon
 * squeeze it, return it
 * use the same metagon
 */
public class MJig_Boiler implements MJig{

  
  private static final double BOILSPAN=0.07;
  
  public List<MShape> createShapes(MShape target){
    if(target instanceof MYard)return null;
    MPolygon mptarget=(MPolygon)target;
    //create inner polygon
    MMetagon innermpolygonmetagon=mptarget.mmetagon;//metagons are immutable
    DPolygon innermpolygondpolygon=(DPolygon)mptarget.dpolygon.clone();
    MPolygon innermpolygon=new MPolygon(innermpolygondpolygon,innermpolygonmetagon,0,new ArrayList<String>());
    Util.shrink(innermpolygondpolygon,BOILSPAN);
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


  //TODO
  //also check and constrain distortion
  public double getDetailSizePreview(MShape target){
    // TODO Auto-generated method stub
    return 0;
  }

}
