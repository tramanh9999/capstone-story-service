package com.storyart.storyservice.common.constants;

import java.util.ArrayList;
import java.util.List;

public class ConstantsList {
    public static List<String>  getNumberConditionList(){
        List<String> number_conditions = new ArrayList<>();
        number_conditions.add(NUMBER_CONDITIONS.EQUAL);
        number_conditions.add(NUMBER_CONDITIONS.GREATER);
        number_conditions.add(NUMBER_CONDITIONS.GREATER_OR_EQUAL);
        number_conditions.add(NUMBER_CONDITIONS.LESS);
        number_conditions.add(NUMBER_CONDITIONS.LESS_OR_EQUAL);
        return number_conditions;
    }

    public static List<String> getStringConditionList(){
        List<String> string_conditions = new ArrayList<>();
        string_conditions.add(NUMBER_CONDITIONS.EQUAL);
        return string_conditions;
    }

    public static List<String> getStringOperationList(){
        List<String> string_ops = new ArrayList<>();
        string_ops.add(STRING_OPERATIONS.APPEND);
        string_ops.add(STRING_OPERATIONS.PREPEND);
        string_ops.add(STRING_OPERATIONS.REPLACE);
        return string_ops;
    }

    public static List<String> getNumberOperationList(){
        List<String> number_ops = new ArrayList<>();
        number_ops.add(NUMBER_OPERATIONS.PLUS);
        number_ops.add(NUMBER_OPERATIONS.MINUS);
        number_ops.add(NUMBER_OPERATIONS.MULTIPLY);
        number_ops.add(NUMBER_OPERATIONS.DIVIDE);
        number_ops.add(NUMBER_OPERATIONS.REPLACE);
        return number_ops;
    }
}
