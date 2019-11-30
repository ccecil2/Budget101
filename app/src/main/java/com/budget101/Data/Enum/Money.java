package com.budget101.Data.Enum;

public enum Money
{
    INCOME, EXPENSE;

    /**
     * Returns a Money object from corresponding int.
     * @param m int to translate
     * @return Money object or null
     */
    public static Money toMoney(int m)
    {
        switch(m)
        {
            case 0:
                return INCOME;
            case 1:
                return EXPENSE;
            default:
                return null;
        }
    }


    /**
     * Returns the int equivalent of given Money m.
     * @param m Money object
     * @return Int of m
     */
    public static int toInt(Money m)
    {
        if(m == Money.INCOME)
            return 0;
        else // EXPENSE
            return 1;
    }
}
