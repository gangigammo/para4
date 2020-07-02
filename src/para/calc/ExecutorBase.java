package para.calc;

import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * 逆ポーランド記法の数式文字列に対して数値計算をするための基本処理が定義されているクラス
 */
abstract public class ExecutorBase{ 
  protected Scanner s;
  private Stack<Float> stack;
  protected String result;
  private String state;

  protected ExecutorBase(){
  }

  /**
   * 処理対象文字列を設定し、処理のための初期化を行う
   * @param data 処理対象文字列
   */
  protected void init(String data){
    s = new Scanner(data);
    s.useDelimiter(",");
    stack = new Stack<Float>();
    state = "";
  }

  /**
   * 処理の1ステップ分を行う
   * @return 1ステップが処理できれば true 処理対象の文字列の文法誤りで処理できなればfalse
   */
  protected boolean onestep(){// throws InterruptedException{
    if(s.hasNext("\\-?+\\d++\\.?+\\d*")){
      float ret = Float.parseFloat(s.next("\\-?+\\d++\\.?+\\d*"));
      stack.push(ret);
      result = Float.toString(ret);
      showProgress();
      return true;
    }else if(s.hasNext("[\\+\\-\\*/]")){
      String op = s.next("[\\+\\-\\*/]");
      showProgress(op);
      if(stack.empty()){
        return false;
      }
      float b = stack.pop();
      if(stack.empty()){
        return false;
      }
      float a = stack.pop();
      float res=0;
      switch(op){
      case "+":
        res = a+b;
        break;
      case "-":
        res = a-b;
        break;
      case "*":
        res = a*b;
        break;
      case "/":
        res = a/b;
        break;
      }
      stack.push(res);
      result = Float.toString(res);
      showProgress();
      return true;
    }else{
      s.next();
      return false;
    }
  }

  public void showProgress(String op){
    Stream<Float> st = stack.stream();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(bos); 
    ps.print("(");
    st.forEach(f->{ps.print(f+"|");});
    ps.print(op+")");
    state = bos.toString();
    writeState(state);
    try{
      Thread.sleep(2000);
    }catch(InterruptedException ex){
    }
    //rec(30); //no meaning work to waste time.
  }

  /*
  private double rec(double l){
    if(0<l){
      return rec(l-1)*rec(l-1);
    }else{
      return 1;
    }
  }
  */  

  public void showProgress(){// throws InterruptedException{
    showProgress("");
  }


  /**
   *　状態を出力する
   * @param state 状態
   */
  abstract void writeState(String state);

  /*
   * 処理対象文字列を設定し、処理のための初期化を行う
   * @param data 処理対象文字列
   */
  abstract String operation(String data);
}
