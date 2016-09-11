package org.fleen.maximilian.jig;

import java.util.List;

import org.fleen.maximilian.MShape;

/*
 * A jig server serves jigs to a maximilian composition composer
 */
public interface MJigServer{
  
  /*
   * return jigs compatible with the specified shape
   */
  List<MJig> getJigs(MShape shape);
  
  /*
   * return jigs compatible with the specified shape and with the specified tag/s
   */
  List<MJig> getJigs(MShape shape,String... tag);
  
}
