package budget101.Data;

import budget101.Enum.Status;

public final class Split
{
    final int ID;
    final String otherUser;
    Status status;
    final Record record;


    /**
     * Creates a split object.
     * @param id - ID of split
     * @param otherUser - Name of user money is owed to/from
     * @param status - Current status of split
     * @param record - Record for split
     */
    public Split(int id, String otherUser, Status status, Record record)
    {
        this.ID = id;
        this.otherUser = otherUser;
        this.status = status;
        this.record = record;
    }


    /**
     * Returns ID of split record.
     * @return record ID
     */
    public int getID()
    {
        return this.ID;
    }


    /**
     * Returns other user in split.
     * @return Other person
     */
    public String getUser()
    {
        return this.otherUser;
    }


    /**
     * Set new status to split record.
     * @param status - New status
     */
    public void setStatus(Status status)
    {
        this.status = status;
    }

    /**
     * Return status of split.
     * @return Current status
     */
    public Status getStatus()
    {
        return this.status;
    }

    /**
     * Returns the record tied to split.
     * @return Returns record for split
     */
    public Record getRecord()
    {
        return this.record;
    }
}
