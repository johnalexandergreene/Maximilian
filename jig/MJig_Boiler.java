package org.fleen.maximilian.jig;

import java.util.ArrayList;
import java.util.Arrays;

import org.fleen.geom_2D.DPolygon;
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
public class MJig_Boiler extends MJig_Abstract{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  MJig_Boiler(double boilgap){
    this.boilgap=boilgap;
    setTags(TAG);}
  
  /*
   * ################################
   * TAG
   * ################################
   */
  
  private static final String TAG="boiler";
  
  /*
   * ################################
   * GAP
   * ################################
   */

  private double boilgap;
  
  /*
   * ################################
   * CREATE SHAPES
   * ################################
   */
  
  public CreatedShapes createShapes(MShape target){
    if(target instanceof MYard)return null;
    //
    MPolygon mptarget=(MPolygon)target;
    //create egg
    MMetagon eggmetagon=mptarget.mmetagon;//metagons are immutable
    DPolygon eggdpolygon=(DPolygon)mptarget.dpolygon.clone();
    MPolygon egg=new MPolygon(eggdpolygon,eggmetagon,0,new ArrayList<String>());
    Util.shrink(eggdpolygon,boilgap);
    egg.addTags("egg");
    //create yard
    MYard yard=new MYard(Arrays.asList(new MPolygon[]{(MPolygon)target,egg}),0,new ArrayList<String>());
    yard.addTags("donut");
    //
    return new CreatedShapes(egg,yard);}

}
