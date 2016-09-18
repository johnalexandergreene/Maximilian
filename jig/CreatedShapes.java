package org.fleen.maximilian.jig;

import java.util.ArrayList;
import java.util.List;

import org.fleen.maximilian.MPolygon;
import org.fleen.maximilian.MShape;
import org.fleen.maximilian.MYard;

class CreatedShapes{
  
  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  CreatedShapes(List<MPolygon> polygons,MYard yard){
    this.polygons=polygons;
    this.yard=yard;}
  
  CreatedShapes(List<MPolygon> polygons){
    this.polygons=polygons;}
  
  CreatedShapes(MPolygon polygon,MYard yard){
    polygons=new ArrayList<MPolygon>(1);
    polygons.add(polygon);
    this.yard=yard;}
  
  /*
   * ################################
   * SHAPES
   * ################################
   */
  
  List<MPolygon> polygons=null;
  MYard yard=null;
  
  /*
   * ################################
   * UTIL
   * ################################
   */
  
  List<MShape> getList(){
    List<MShape> a=new ArrayList<MShape>();
    if(yard!=null)a.add(yard);
    a.addAll(polygons);
    return a;}
  
}
