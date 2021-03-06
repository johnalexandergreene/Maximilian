package org.fleen.maximilian.jig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fleen.forsythia.grammar.Jig;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.maximilian.MPolygon;
import org.fleen.maximilian.MShape;
import org.fleen.maximilian.MYard;

/*
 * boil, split inner polygon, shrink children
 */
public class MJig_Grinder extends MJig_Abstract{
  
  //param is forsythia jig
  public MJig_Grinder(Jig fjig){
    init(fjig);
  }

  private void init(Jig fjig){
    this.fjig=fjig;
  }
  
  Jig fjig;
  
  private static final double 
    BOILSPAN=0.02,
    CRUSHSPAN=0.02;

  public List<MShape> createAndLinkShapes(MShape target){
    if(target.hasBadGeometry())return null;//DEBUG
    //
    List<MShape> newshapes=new ArrayList<MShape>();
    MPolygon targetpolygon=(MPolygon)target;
    //get an inner polygon, squeezed in like boiling
    DPolygon dp0=(DPolygon)targetpolygon.dpolygon.clone();
    Util.shrink(dp0,BOILSPAN);
    MPolygon inner0=new MPolygon(dp0,targetpolygon.mmetagon);
    //we're doing a double split
    //split up the inner
    //TODO we need some chorused jig selection here. cache it locally too
    List<MPolygon> shards0=Util.split(inner0,fjig);
//    //split up those shards
//    List<MPolygon> shards1=new ArrayList<MPolygon>();
//    for(MPolygon s:shards0){
//      sha
//    }
    newshapes.addAll(shards0);
    //shrink the shards
    for(MPolygon shard:shards0){
      Util.shrink(shard.dpolygon,CRUSHSPAN);
      shard.addTags(Arrays.asList(new String[]{"egg"}));
      shard.setParent(target);}
    //do the yard
    MYard yard=new MYard((MPolygon)target,shards0,0,Arrays.asList(new String[]{"foam"}));
    newshapes.add(yard);
    yard.setParent(target);
    //
    targetpolygon.setChildren(newshapes);
    return newshapes;}
    
    
  //testing for identical objects here, not equality  
  boolean shardsAreSharingPoints(List<MPolygon> shards){
    for(MPolygon polygon0:shards){
      for(MPolygon polygon1:shards){
        if(polygon0==polygon1)continue;
        for(DPoint p0:polygon0.dpolygon){
          for(DPoint p1:polygon1.dpolygon){
            if(p0==p1)
              return true;}}}}
    return false;}
  
  
  //------------------------------------------------------------
  
  
  @Override
  public double getDetailSizePreview(MShape target){
    // TODO Auto-generated method stub
    return 0;
  }

}
