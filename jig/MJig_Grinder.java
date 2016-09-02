package org.fleen.maximilian.jig;

import java.util.List;

import org.fleen.maximilian.MJig;
import org.fleen.maximilian.MShape;

/*
 * boil, split inner polygon, split children, shrink children's children
 * 
 * grinds the children finer than the crusher
 */
public class MJig_Grinder implements MJig{

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
