package org.fleen.maximilian.app.test;

import java.util.List;

import org.fleen.forsythia.grammar.Jig;
import org.fleen.maximilian.MJig;
import org.fleen.maximilian.MShape;

public class MSplitter implements MJig{
  
  //param is forsythia jig
  MSplitter(Jig fjig,BoundedDeformableKGrid grid){
    init(fjig,grid);
  }

  private void init(Jig jig,BoundedDeformableKGrid grid){
    //init grid
  }
  
  @Override
  public List<MShape> createShapes(MShape target){
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public double getDetailSizePreview(MShape target){
    // TODO Auto-generated method stub
    return 0;
  }

}
