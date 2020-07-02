package para;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import para.graphic.target.*;
import para.graphic.shape.*;
import para.graphic.parser.MainParser;
import java.util.*;
import java.util.concurrent.*;

/** クライアントからの通信を受けて描画するサーバプログラム。
 * 監視ポートは30000番
 */
public class Main07{
  final public int PORTNO=30000;
  final int MAXCONNECTION=3;
  final Target target;
  final ShapeManager[] sms;
  final ServerSocket ss;
  final Executor exec;
  boolean [] keep = new boolean[3];

  /** 受け付け用ソケットを開くこと、受信データの格納場所を用意すること
   * を行う
   */
  public Main07(){
    keep[0] = false;
    keep[1] = false;
    keep[2] = false;
    Executor exec = Executors.newFixedThreadPool(MAXCONNECTION);

    this.exec = exec;
    target = new JavaFXTarget("Server", 320*MAXCONNECTION, 240);
    //target = new TextTarget(System.out);
    ServerSocket tmp=null;

    try{
      tmp = new ServerSocket(PORTNO);
    }catch(IOException ex){
      System.err.println(ex);
      System.exit(1);
    }
    ss = tmp;

    sms = new ShapeManager[MAXCONNECTION];
    for(int i=0;i<MAXCONNECTION;i++){
      sms[i] = new OrderedShapeManager();
    }
  }

  /** 受け付けたデータを表示するウィンドウの初期化とそこに受信データを表示するスレッドの開始
   */
  public void init(){
    System.err.println(Thread.currentThread().getName());//
    target.init();
    target.clear();
    target.flush();
    new Thread(()->{
      System.err.println(Thread.currentThread().getName());
      while(true){
        for(ShapeManager sm: sms){
          synchronized(sm){
            target.draw(sm);
          }
        }
        target.flush();
        try{
          Thread.sleep(100);
        }catch(InterruptedException ex){

        }
      }
    }).start();
  }

  /** 受信の処理をする
   */
  public void start(){
    /*
    while(true){
      Thread t[]=new Thread[3];
      t[0]=new Thread( () ->{
        while(true){
          try(Socket s = ss.accept()){
            BufferedReader r =
              new BufferedReader(new InputStreamReader(s.getInputStream()));
            ShapeManager dummy = new ShapeManager();
            MainParser parser
              = new MainParser(new TranslateTarget(sms[0],
                                new TranslationRule(10000*0, new Vec2(320*0,0))),
                               dummy);
            parser.parse(new Scanner(r));
          }catch(IOException ex){
            System.err.print(ex);
          }
        }
      });
      t[1]=new Thread( () ->{
        while(true){
          try(Socket s = ss.accept()){
            BufferedReader r =
              new BufferedReader(new InputStreamReader(s.getInputStream()));
            ShapeManager dummy = new ShapeManager();
            MainParser parser
              = new MainParser(new TranslateTarget(sms[1],
                                new TranslationRule(10000*1, new Vec2(320*1,0))),
                               dummy);
            parser.parse(new Scanner(r));
          }catch(IOException ex){
            System.err.print(ex);
          }
        }
      });
      t[2]=new Thread( () ->{
        while(true){
          try(Socket s = ss.accept()){
            BufferedReader r =
              new BufferedReader(new InputStreamReader(s.getInputStream()));
            ShapeManager dummy = new ShapeManager();
            MainParser parser
              = new MainParser(new TranslateTarget(sms[2],
                                new TranslationRule(10000*2, new Vec2(320*2,0))),
                               dummy);
            parser.parse(new Scanner(r));
          }catch(IOException ex){
            System.err.print(ex);
          }
        }
      });

        t[0].start();
        t[1].start();
        t[2].start();

        try {
            while(true){
              Thread.sleep(100000000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
*/


    int i=0;
    while(true){
      try{
        Socket s = ss.accept();
        int n;
        if(keep[0] == false){
          n = 0;
        }else if(keep[1] == false){
          n = 1;
        }else{
          n = 2;
        }
        Runnable r = new Runnable(){
          public void run(){
            communicate(s,n);
          }
        };
        exec.execute(r);
      }catch(IOException ex){
        System.err.print(ex);
      }
      i=(i+1)%MAXCONNECTION;
    }
  }

  private void communicate(Socket s,final int n){
    try{
      keep[n] = true;
      BufferedReader r =
              new BufferedReader(new InputStreamReader(s.getInputStream()));
      ShapeManager dummy = new ShapeManager();
      MainParser parser
              = new MainParser(new TranslateTarget(sms[n],
              new TranslationRule(10000*n, new Vec2(320*n,0))),
              dummy);
      parser.parse(new Scanner(r));
    }catch(IOException ex){
      System.err.print(ex);
    }
    keep[n] = false;
  }

  public static void main(String[] args){
    Main07 m = new Main07();
    m.init();
    m.start();
  }
}
