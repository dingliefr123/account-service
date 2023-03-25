package account.Util;

public class SalaryConvertor {
  public static String serialize(Long salary) {
    // 1234 dollar(s) 56 cent(s)
    final int part = (int) (salary / 100);
    final int floatPart = (int) (salary % 100);
    return String.format("%d dollar(s) %d cent(s)", part, floatPart);
  }
}
