package org.fleen.maximilian;

/*
 * Given a shape, get a jig 
 * The selection logic depends on the implementation
 * 
 * we have several kinds of jigs; splitters, boilers, crushers... maybe some others
 * 
 * We have a default implementation, MJigServer_Basic. It does a chorused random selection thing.
 */
public interface MJigServer{
  
  /*
   * return a jig compatible with the specified shape
   */
  MJig getJig(MShape shape);
  
  /*
   * return a jigs compatible with the specified shape and with the specified tag/s
   */
  MJig getJigs(MShape shape,String... tag);
  
}
