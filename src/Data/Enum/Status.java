package com.budget101.Data.Enum;

public enum Status
{
    PENDING, CANCELED, CLOSED;


    /**
     * Returns a Status object from corresponding int.
     * @param t int to translate
     * @return Status object or null
     */
    public static Status toStatus(int t)
    {
        switch(t)
        {
            case 0:
                return PENDING;
            case 1:
                return CANCELED;
            case 2:
                return CLOSED;
            default:
                return null;
        }
    }


    /**
     * Returns the int equivalent of given Status t.
     * @param t Status object
     * @return Int of t
     */
    public static int toInt(Status t)
    {
        if(t == Status.PENDING)
            return 0;
        else if(t == Status.CANCELED)
            return 1;
        else // CLOSED
            return 2;
    }
}
