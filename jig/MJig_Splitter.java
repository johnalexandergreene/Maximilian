package org.fleen.maximilian.jig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fleen.forsythia.grammar.Jig;
import org.fleen.forsythia.grammar.JigSection;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_Kisrhombille.KAnchor;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;
import org.fleen.maximilian.MJig;
import org.fleen.maximilian.MMetagon;
import org.fleen.maximilian.MPolygon;
import org.fleen.maximilian.MShape;
import org.fleen.maximilian.boundedDeformableKGrid.BoundedDeformableKGrid;

public class MJig_Splitter implements MJig{
  
  //param is forsythia jig
  public MJig_Splitter(Jig fjig){
    init(fjig);
  }

  private void init(Jig fjig){
    this.fjig=fjig;
  }
  
  Jig fjig;
  
  public List<MShape> createShapes(MShape target){
    List<MShape> shapes=new ArrayList<MShape>();
    List<MPolygon> shards=Util.split((MPolygon)target,fjig);
    shapes.addAll(shards);
    target.setChildren(shapes);
    for(MShape shape:shapes)
      shape.setParent(target);
    return shapes;}

  @Override
  public double getDetailSizePreview(MShape target){
    // TODO Auto-generated method stub
    return 0;
  }

}
