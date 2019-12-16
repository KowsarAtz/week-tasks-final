package com.atzandroid.weektasks;

class DbConstants {

    static final String DB_NAME = "week-tasks-db";
    static final int DB_VERSION = 1;

    // Param Table and its Columns
    static final String PARAM_TABLE = "params";
    static final String PARAM_TABLE_ITEM_PK = "pk";
    static final String PARAM_TABLE_ITEM_VALUE = "status";

    // Week Tasks Table and its Columns
    static final String WEEK_TASKS_TABLE = "week_tasks";

    static final short NOT_PASS_PROTECTED = -1;
    static final short NOT_SET = 0;
    static final short PASS_PROTECTED = 1;

    // Param Table all possible values for Primary Key Column
    static final short PASS_PROTECTED_STATUS = 1;
    static final short CURRENT_WEEK_NUM = 2;
    static final short PASSWORD_HASH = 3;

    static final String CREATE_TABLE_PARAMS =
            "CREATE TABLE "+ PARAM_TABLE + "(" +
                    PARAM_TABLE_ITEM_PK +" INTEGER PRIMARY KEY," +
                    PARAM_TABLE_ITEM_VALUE +" INTEGER NOT NULL" + ");";

    static final String INIT_TABLE_PARAMS_1 =
        "INSERT INTO " + PARAM_TABLE
                + " ("+ PARAM_TABLE_ITEM_PK + " , " + PARAM_TABLE_ITEM_VALUE +") "
                + "VALUES " + "(" + String.valueOf(PASS_PROTECTED_STATUS)
                + " , " + String.valueOf(NOT_SET) + ")";

    static final String INIT_TABLE_PARAMS_2 =
            "INSERT INTO " + PARAM_TABLE
                    + " ("+ PARAM_TABLE_ITEM_PK + " , " + PARAM_TABLE_ITEM_VALUE +") "
                    + "VALUES " + "(" + String.valueOf(CURRENT_WEEK_NUM)
                    + " , " + String.valueOf(NOT_SET) + ")";

    static final String INIT_TABLE_PARAMS_3 =
            "INSERT INTO " + PARAM_TABLE
                    + " ("+ PARAM_TABLE_ITEM_PK + " , " + PARAM_TABLE_ITEM_VALUE +") "
                    + "VALUES " + "(" + String.valueOf(PASSWORD_HASH)
                    + " , " + String.valueOf(NOT_SET) + ")";

    //static final String INIT_TABLE_PARAMS = INIT_TABLE_PARAMS_1 + "; " + INIT_TABLE_PARAMS_2 + "; " + INIT_TABLE_PARAMS_3 + ";";

    static final String DROP_TABLES =
            "DROP TABLE IF EXISTS "+ PARAM_TABLE +"; " +
            "DROP TABLE IF EXISTS "+ WEEK_TASKS_TABLE +";";


}
