package para.calc;

import javafx.scene.control.*;
import javafx.application.Platform;

public class Executor2 extends ExecutorBase implements Executor{
  Label label;
  public Executor2(Label label){
    super();
    this.label = label;
  }

  public void writeState(String state){

    System.err.println(Thread.currentThread().getName());//hint
    System.out.print(state);
    Platform.runLater(()->{
        label.setText(state);
        System.err.println(Thread.currentThread().getName());//hint 手続き全てが終わってから全て出力される。
      });
  }

  synchronized public String operation(String data){
    //buttoncalと同時に出力
    System.err.println(Thread.currentThread().getName());

    init(data);
    result = null;
    boolean isSuccess=true;

    while(isSuccess && s.hasNext()){
      isSuccess = onestep();
    }

    //isSuccess = onestep();
    return result;
  }
}
