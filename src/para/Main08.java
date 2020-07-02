package para;

import java.io.*;
import java.net.*;
import para.graphic.shape.*;
import para.graphic.target.*;

/** カメラを起動し、サーバに送るデモ
 */
public class Main08{
  /** メインメソッド
   * @param args args[0]は相手先のホスト
   */
  public static void main(String[] args){
    final int PORTNO = 30000;
    ShapeManager sm = new ShapeManager();
    sm.put(new Camera(0,0,0));
    try(Socket s = new Socket(args[0],PORTNO)){
      PrintStream ps = new PrintStream(s.getOutputStream());
      Target target = new TextTarget(ps);
      //Target target = new TextTarget(System.out);
      target.init();
      target.clear();
      while(true){
        target.draw(sm);
        target.flush();
        try{
          Thread.sleep(100);
        }catch(InterruptedException ex){
        }
        if(ps.checkError()){
          break;
        }
      }
    }catch(IOException ex){
      System.err.println(ex);
    }
  }
}
