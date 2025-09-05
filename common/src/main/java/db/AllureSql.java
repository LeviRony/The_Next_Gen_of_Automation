package db;

import io.qameta.allure.Attachment;

import java.util.Arrays;

public class AllureSql {
    private AllureSql() {}


    @Attachment(value = "SQL Query", type = "text/plain")
    public static String attachQuery(String sql, Object... params) {
        return sql + "\nparams=" + Arrays.toString(params);
    }


    @Attachment(value = "SQL Result", type = "text/plain")
    public static String attachResultCsv(String text) {
        return text;
    }
}
