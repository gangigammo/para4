package para.calc;

/**
 * 逆ポーランド記法によリ書かれた四則演算の文字列に対して数値計算をするクラス
 */
public class Executor1 extends ExecutorBase implements Executor{
  
  public Executor1(){
  }

  @Override
  public void writeState(String state){
    System.out.print(state);
  }

  /**
   * 文字列を受け取り、処理を行う
   * @return 計算結果　文字列の文法が不完全のときは 不定
   */
  public String operation(String data){
    init(data);
    result = null;
    boolean isSuccess=true;
    while(isSuccess && s.hasNext()){
      isSuccess = onestep();
    }
    return result;
  }
}
