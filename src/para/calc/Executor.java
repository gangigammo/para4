package para.calc;

import javafx.scene.control.*;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * 電卓内の数値計算実装部のインタフェース
 */
public interface Executor{
  public String operation(String data);
}
