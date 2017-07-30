package com.example.mahmoudheshmat.mahmoudheshmattask;


public class RegularExpression {

    public static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]{1,30}+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]{3,10}+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,4})$";

    public static final String PASSWORD_PATTERN =
            "((?=.*[a-zA-Z0-9]).{8,40})";

    //public static final String NAME_PATTERN = "^[a-zA-Z \\-\\.\']*$";
    public static final String NAME_PATTERN = "^[a-zA-Z \\-\\.\']{3,20}$";

}
