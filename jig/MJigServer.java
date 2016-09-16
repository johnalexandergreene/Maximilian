package org.fleen.maximilian.jig;

import org.fleen.maximilian.MShape;

/*
 * A jig server serves jigs to a maximilian composition composer
 */
public interface MJigServer{
  
  /*
   * return a jig compatible with the specified shape and with the specified tag/s
   */
  MJig getJig(MShape target,String[] tags);
  
}
