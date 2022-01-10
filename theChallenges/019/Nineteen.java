// Unlike normal java programs, this has no package


//import java.time;

/**
 * Euler task task nineteen



 */
public class Nineteen {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        int sundays = 0;
        int dayOfWeek = 2;// day of week is zero based, to make the math easier
        int month = 0;  // Jan is 0
        int year = 1901;     // D'oh!  The preliminary text mentions 1900, but the problem starts at 1901
        final int daysInMonths[] = {31,28,31,30,31,30,31,31,30,31,30,31}; // months are 0 based

        for(;;) {

            // calculate NEXT month
            int daysThisMonth = daysInMonths[month];
            if (month==1) { // February and its wacky leap year calc
                if (year%4!=0
                || (year%100==0 && year%400!=0)) { // the mod 400 part wont happen in this run, but what the heck
                    ; // for some reason its easier (cleaner?) to figure what ISNT leap year
                } else {
                    daysThisMonth += 1;
                }
            }

            // now, move the dayOfWeek forward
            dayOfWeek = (dayOfWeek+daysThisMonth) % 7;


            // Finally, move to the next month
            month += 1;
            if (month==12) {
                month=0;
                year += 1;
            }


            // count Sunday for this month
            if (dayOfWeek==0)
                sundays += 1;

            // checking vs javaa.time
            //LocalDate jdate = LocalDate.of(year,month,1);
            //DayOfWeek jdow = jdate.getDayOfWeek();

            if (dayOfWeek==0 || year==2000)
                System.out.println(String.format("%d %d %d %d",year,month,dayOfWeek,sundays));

            // stop when we counted Dec 2000
            if (year==2000 && month==11)
                break;
        }


        long end = System.currentTimeMillis();

        System.out.println(String.valueOf(sundays));

        System.out.println(String.format("took %f seconds",(end-start)/1000.0));
    }
}
