package account.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
  private static final Pattern EmailNameMatch = Pattern.compile("\\b?(\\w+)@.*");
//  private static final Pattern UpperCharExistsMatch = Pattern.compile("\\b?(.*?[A-Z].*)@.*");

  public static String LowerUsernameInEmail(String str) {
    Matcher m = EmailNameMatch.matcher(str);
    if (!m.find()) return str;
    int idx = m.start();
    String matched = m.group(1);
    return new StringBuffer(str)
            .replace(idx, idx + matched.length(), matched.toLowerCase())
            .toString();
  }
}
