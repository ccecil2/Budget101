package budget101.Data;

import budget101.Enum.Money;


/**
 * Contains a category object.
 */
public final class Category
{
    private final int ID;
    private boolean alarm;
    private String name;
    private double limit;
    private Money money;

    public static final double alarmPercent = 0.25; // Percentage for when to sound alarm


    /**
     * Creates a category object.
     * @param id - ID of category
     * @param name - Name of category
     * @param alarm - Alarm on/off
     * @param limit - Limit for alarm
     * @param money - Money type
     */
    public Category(int id, String name, int alarm, double limit, int money)
    {
        this.ID = id;
        this.name = name;
        this.setAlarm(alarm);
        this.limit = limit;
        this.money = Money.toMoney(money);
    }


    /**
     * Creates a category object.
     * @param id - ID of category
     * @param name - Name of category
     * @param alarm - Alarm on/off
     * @param limit - Limit for alarm
     * @param money - Money type
     */
    public Category(int id, String name, boolean alarm, double limit, Money money)
    {
        this.ID = id;
        this.name = name;
        this.alarm = alarm;
        this.limit = limit;
        this.money = money;
    }


    /**
     * Copy constructor
     * @param cat Object to copy
     */
    public Category(Category cat)
    {
        this.ID = cat.getID();
        this.name = cat.getName();
        this.alarm = cat.isAlarm();
        this.setLimit(cat.getLimit());
        this.money = cat.getMoney();
    }


    /**
     * Translates int to boolean for alarm status.
     * @param alarm 1 for true, 0 for false
     */
    private void setAlarm(int alarm)
    {
        if (alarm == 1)
            this.alarm = true;
        else
            this.alarm = false;
    }


    /**
     * Returns the category's ID.
     * @return ID for database
     */
    public int getID()
    {
        return this.ID;
    }


    /**
     * Returns the category's name.
     * @return name
     */
    public String getName()
    {
        return this.name;
    }


    /**
     * Returns the alarm state.
     * @return boolean of alarm
     */
    public boolean isAlarm()
    {
        return this.alarm;
    }


    /**
     * Returns the alarm as an int.
     * @return 1 for true, 0 for false
     */
    public static int getAlarmInt(boolean alarm)
    {
        if(alarm)
            return 1;
        else
            return 0;
    }


    /**
     * Sets the limit for alarm.
     * Defaults to 0.0 if limit < 0.0.
     * @param limit - Max for category
     */
    public void setLimit(double limit)
    {
        if(limit < 0.0)
            this.limit = 0.0;
        else
            this.limit = limit;
    }


    /**
     * Returns limit for alarm.
     * @return Limit for category
     */
    public double getLimit()
    {
        return this.limit;
    }


    /**
     * Returns the type of money. INCOME/EXPENSE
     * @return Money enum
     */
    public Money getMoney()
    {
        return this.money;
    }


    /**
     * Returns true if total is within spending
     * limit percentage and spending alarm should sound.
     * @param total - Total spending for category
     * @return If alarm should sound
     */
    public boolean isSoundAlarm(double total)
    {
        double p = this.alarmPercent * this.limit; // Dollar amount for percent of max
        double remain = this.limit - total; // What's left of budget

        if(p >= remain) // Threshold for alarm is greater than remaining budget
            return true;
        else
            return false;
    }
}
