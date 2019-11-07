package Data;

import java.util.Date;


/**
 * Holds data for a record.
 */
public final class Record
{
    private final int ID;
    private double amount;
    private Category category;
    private Date date;
    private String comment;


    /**
     * Creates a new record object.
     * @param id - ID number
     * @param amount - Dollar amount
     * @param cat - Category of record
     * @param date - Transaction date
     * @param comment - Record comment
     */
    public Record(int id, double amount, Category cat, Date date, String comment)
    {
        this.ID = id;
        this.amount = amount;
        this.category = cat;
        this.setDate(date);
        this.setComment(comment);
    }


    /**
     * Returns the ID number.
     * @return ID number
     */
    public int getID()
    {
        return this.ID;
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
     * Calls copy constructor for Category on c.
     * @param c New category
     */
    public void setCategory(Category c)
    {
        this.category = new Category(c);
    }


    /**
     * Returns category for record.
     * @return Category object
     */
    public Category getCategory()
    {
        return this.category;
    }


    /**
     * Returns the comment on record.
     * @return User comment
     */
    public String getComment()
    {
        return this.comment;
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
