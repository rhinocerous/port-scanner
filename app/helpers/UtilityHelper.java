package helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Aaron on 1/1/2015.
 */
public class UtilityHelper
{
    public static List<Integer> extractIntegers(String value)
    {
        List<Integer> list = new ArrayList<>();
        Pattern p = Pattern.compile("-?\\d+");
        Matcher m = p.matcher(value);
        while (m.find()) {
            list.add(Integer.valueOf(m.group()));
        }

        return list;
    }

    public static String extractIp(String value)
    {
        String IPADDRESS_PATTERN =
                "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

        Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            return matcher.group();
        }
        else{
            return "0.0.0.0";
    }

}

    public static List<String> extractDomains(String value)
    {
        List<String> result = new ArrayList<String>();
        String domainPattern = "[a-z0-9\\-\\.]+\\.(com|org|net|mil|edu|(co\\.[a-z].))";
        Pattern p = Pattern.compile(domainPattern,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(value);
        while (m.find()) {
            result.add(value.substring(m.start(0),m.end(0)));
        }
        return result;
    }
}
