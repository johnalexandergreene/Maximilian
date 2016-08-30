package org.fleen.maximilian.app.test;

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

public class MSplitter implements MJig{
  
  //param is forsythia jig
  MSplitter(Jig fjig){
    init(fjig);
  }

  private void init(Jig fjig){
    this.fjig=fjig;
  }
  
  Jig fjig;
  
  
  public List<MShape> createShapes(MShape target){
    BoundedDeformableKGrid grid=new BoundedDeformableKGrid(target,fjig.getGridDensity());
    List<MShape> shapes=new ArrayList<MShape>();
    MPolygon mp;
    DPolygon dp;
    MMetagon mm;
    for(JigSection s:fjig.sections){
      mm=new MMetagon(s.productmetagon);
      dp=getDPolygon(mm,s.productanchor,grid);
      mp=new MPolygon(dp,mm,s.productchorusindex,Arrays.asList(s.tags.getTags()));
      mp.setParent(target);
      shapes.add(mp);}
    target.setChildren(shapes);
    return shapes;}
  
  DPolygon getDPolygon(MMetagon mm,KAnchor anchor,BoundedDeformableKGrid grid){
    KPolygon p=mm.getPolygon(anchor.v0,anchor.v1,anchor.twist);
    DPolygon dp=new DPolygon();
    for(KVertex v:p)
      dp.add(grid.getPoint(v));
    return dp;
  }

  @Override
  public double getDetailSizePreview(MShape target){
    // TODO Auto-generated method stub
    return 0;
  }

}
