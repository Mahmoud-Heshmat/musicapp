package com.example.mahmoudheshmat.musicapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegularExpression {

    public static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]{1,30}+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]{3,10}+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,4})$";

    public static final String PASSWORD_PATTERN =
            "((?=.*[a-zA-Z0-9]).{8,40})";

    public static final String NAME_PATTERN = "^[a-zA-Z \\-\\.\']{3,20}$";

    private Pattern pattern;
    private Matcher matcher;

    public Boolean check(String data, String key){
        int  x = 0;
        Boolean check=false;
        if(key == "email"){
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(data);
            if(matcher.matches()){
                x++;
            }
        }if(key == "password"){
            pattern = Pattern.compile(PASSWORD_PATTERN);
            matcher = pattern.matcher(data);
            if(matcher.matches()){
                x++;
            }
        }if(key == "name"){
            pattern = Pattern.compile(NAME_PATTERN);
            matcher = pattern.matcher(data);
            if(matcher.matches()){
                x++;
            }
        }

        if(x > 0){check = true;}
        return check;
    }

}
