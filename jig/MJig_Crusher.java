package org.fleen.maximilian.jig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fleen.forsythia.grammar.Jig;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.maximilian.MPolygon;
import org.fleen.maximilian.MShape;
import org.fleen.maximilian.MYard;

/*
 * boil, split inner polygon, shrink children
 */
public class MJig_Crusher implements MJig{
  
  //param is forsythia jig
  public MJig_Crusher(Jig fjig){
    init(fjig);
  }

  private void init(Jig fjig){
    this.fjig=fjig;
  }
  
  Jig fjig;
  
  private static final double 
    BOILSPAN=0.04,
    CRUSHSPAN=0.04;

  public List<MShape> createShapes(MShape target){
    if(target.hasBadGeometry())return null;//DEBUG
    //
    List<MShape> newshapes=new ArrayList<MShape>();
    MPolygon targetpolygon=(MPolygon)target;
    //get an inner polygon, squeezed in like boiling
    DPolygon dp0=(DPolygon)targetpolygon.dpolygon.clone();
    Util.shrink(dp0,BOILSPAN);
    MPolygon inner0=new MPolygon(dp0,targetpolygon.mmetagon);
    //split up the inner
    List<MPolygon> shards=Util.split(inner0,fjig);
    newshapes.addAll(shards);
    //shrink the shards
    for(MPolygon shard:shards){
      Util.shrink(shard.dpolygon,CRUSHSPAN);
      shard.addTags(Arrays.asList(new String[]{"egg"}));
      shard.setParent(target);}
    //do the yard
    MYard yard=new MYard((MPolygon)target,shards,0,Arrays.asList(new String[]{"foam"}));
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
