package com.budget101.Data;

import java.util.Date;

public class Income {

/*
   Contains an income object.
 */

    private Category category;
    private String comment; //leave a note for the income category
    private double amount;
    private Date date;


    /*
     * Creates an income object.
     * @param category - Name of category
     * @param comment - note for income category
     * @param amount - amount of income
     * @param date - date when transaction occurred
     */
    public Income(Category cat, String comment, double amount, Date date)
    {
        this.category = cat;
        this.setComment(comment);
        this.amount = amount;
        this.setDate(date);
    }


    /**
     * Calls copy constructor for Category on c.
     * @param c New category
     */
    public void setCategory(Category c)
    {
        this.category = new Category(c);
    }
    /**
     * Returns the category's name.
     * @return name
     */
    public Category getCategory()
    {
        return this.category;
    }



    /**
     * Sets the comment from c.
     * If c is null, comment is set to "".
     * @param c New comment
     */
    public void setComment(String c)
    {
        if(c == null)
            this.comment = "";
        else
            this.comment = c;
    }

    public String getComment()
    {
        return this.comment;
    }

    /**
     * Sets the amount for transaction.
     * @param amount New amount
     */
    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    /**
     * Returns the amount for transaction.
     * @return Amount as double
     */
    public double getAmount()
    {
        return this.amount;
    }

    /**
     * Returns the date of record.
     * @return Date object
     */
    public Date getDate()
    {
        return this.date;
    }


    /**
     * Calls copy constructor for Date on d.
     * @param d New date
     */
    public void setDate(Date d)
    {
        this.date = new Date(d.getTime());
    }

}
