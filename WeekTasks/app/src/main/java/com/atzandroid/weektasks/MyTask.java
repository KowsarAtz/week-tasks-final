package com.atzandroid.weektasks;

public class MyTask {
    private String title;
    private String text;
    private String time;

    private static String[] dummyTitles = {"Android HW3", "Compiler HW", "OR Assignment", "Math Practice", "Homework", "Class"};
    private static String[] dummyTexts = {"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut"
            , "eiusmod tempor incididunt ut labore et dolore magna aliqua. Purus sit amet luctus venenatis"
            , "Tortor consequat id porta nibh venenatis sit amet luctus venenatis lectus magna fringilla."
            , "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut"
            , "eiusmod tempor incididunt ut labore et dolore magna aliqua. Purus sit amet luctus venenatis"
            , "Tortor consequat id porta nibh venenatis sit amet luctus venenatis lectus magna fringilla."};
    private static String[] dummyTimes = {"10:00", "08:20", "06:45", "20:00", "18:00", "16:15"};


    private MyTask(String title, String text, String time){
        this.title = title;
        this.text = text;
        this.time = time;
    }
    public String getTitle(){
        return this.title;
    }
    public String getText(){
        return this.text;
    }
    public String getTime(){
        return this.time;
    }

    private static int getRandomIntegerBetweenRange(int min, int max){
        return (int) ((Math.random()*((max-min)+1))+min);
    }

    public static MyTask createRandomTask(){
        return new MyTask(dummyTitles[getRandomIntegerBetweenRange(0,5)]
                , dummyTexts[getRandomIntegerBetweenRange(0,5)] + dummyTexts[getRandomIntegerBetweenRange(0,5)],
                dummyTimes[getRandomIntegerBetweenRange(0,5)]);
    }

}
