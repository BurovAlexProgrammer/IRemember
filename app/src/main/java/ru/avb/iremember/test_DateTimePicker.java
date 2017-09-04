package ru.avb.iremember;

        import android.os.Bundle;
        import android.support.v7.app.ActionBarActivity;
        import android.util.Log;

        import ru.avb.iremember.datetimepicker.DateTime;
        import ru.avb.iremember.datetimepicker.DateTimePicker;
        import ru.avb.iremember.datetimepicker.SimpleDateTimePicker;

        import java.util.Date;

public class test_DateTimePicker extends ActionBarActivity implements DateTimePicker.OnDateTimeSetListener {


    //TODO  допилить библиотеку https://github.com/jjobes/SlideDateTimePicker/blob/master/slideDateTimePicker/src/main/java/com/github/jjobes/slidedatetimepicker/SlideDateTimeDialogFragment.java
    //TODO  http://armanpagilagan.blogspot.ru/2014/05/creating-custom-date-and-time-picker-in.html
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a SimpleDateTimePicker and Show it
        SimpleDateTimePicker simpleDateTimePicker = SimpleDateTimePicker.make(
                "Set Date & Time Title",
                new Date(),
                this,
                getSupportFragmentManager()
        );
        // Show It baby!
        simpleDateTimePicker.show();

        // Or we can chain it to simplify
        SimpleDateTimePicker.make(
                "Set Date & Time Title",
                new Date(),
                this,
                getSupportFragmentManager()
        ).show();
    }

    @Override
    public void DateTimeSet(Date date) {

        // This is the DateTime class we created earlier to handle the conversion
        // of Date to String Format of Date String Format to Date object
        DateTime mDateTime = new DateTime(date);
        // Show in the LOGCAT the selected Date and Time
        Log.v("TEST_TAG","Date and Time selected: " + mDateTime.getDateString());

    }
}
