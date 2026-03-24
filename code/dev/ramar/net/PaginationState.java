package dev.ramar.net;

public class PaginationState
{

    public PaginationState(Integer currentOffset, int retryCount)
    {
        this.currentOffset = currentOffset;
        this.nextOffset = currentOffset;
        this.retry = false;
        this.retryCount = retryCount;
    }

    @Override
    public String toString()
    {
        return this.offset() + ": " + this.retries();
    }


    /* Public API
    --===-----------
        all the things you should need to use
    */
    public Integer offset()
    { return this.currentOffset; }

    /// call to retry this page
    public void retry()
    {
        this.retry = true;
        this.nextOffset = currentOffset;
    }


    /// set the next page offset
    public void next(Integer nextOffset)
    {
        this.retry = false;
        this.nextOffset = nextOffset;
    }

    /// stop early
    public void stop()
    {
        this.retry = false;
        this.nextOffset = null;
    }


    private final Integer currentOffset;

    public Integer currentOffset()
    { return currentOffset; }


    private Integer nextOffset;
    public Integer nextOffset()
    { return nextOffset; }


    private boolean retry;
    public boolean doRetry()
    { return retry; }


    private int retryCount;
    public int retries()
    { return retryCount; }
    
    public int retryCount()
    { return retryCount; }

}