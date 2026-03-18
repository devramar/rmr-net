package dev.ramar.net;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Paginator
{

    @FunctionalInterface
    public interface OffsetPage
    {
        void load(PaginationState state) throws Exception;
    }


    @FunctionalInterface
    public interface OffsetItemsPage<T>
    {
        Iterable<T> load(PaginationState state) throws Exception;
    }


    public static void Paginate(OffsetPage page) throws Exception
    { Paginate(0, page); }

    public static void Paginate(Integer startOffset, OffsetPage page) throws Exception
    {
        Objects.requireNonNull(page);

        Integer currentOffset = startOffset;
        int retryCount = 0;

        while( currentOffset != null )
        {
            PaginationState state = new PaginationState(currentOffset, retryCount);
            page.load(state);

            if( state.doRetry() )
            {
                retryCount++;
                currentOffset = state.currentOffset();
                continue;
            }

            ValidateNextOffset(currentOffset, state.nextOffset());
            currentOffset = state.nextOffset();
            retryCount = 0;
        }
    }


    public static <T> List<T> Paginate(OffsetItemsPage<T> page) throws Exception
    {
        return Paginate(0, page);
    }
    public static <T> List<T> Paginate(Integer startOffset, OffsetItemsPage<T> page) throws Exception
    {
        try
        {
            Objects.requireNonNull(page);

            List<T> out = new ArrayList<>();
            Integer currentOffset = startOffset;
            int retryCount = 0;

            while( currentOffset != null )
            {
                try
                {
                    PaginationState state = new PaginationState(currentOffset, retryCount);
                    Iterable<T> items = page.load(state);
                    
                    if( items != null )
                        for( T item : items )
                            out.add(item);

                    if( state.doRetry() )
                    {
                        retryCount++;
                        currentOffset = state.currentOffset();
                        continue;
                    }

                    ValidateNextOffset(currentOffset, state.nextOffset());
                    currentOffset = state.nextOffset();
                    retryCount = 0;
                }
                catch(Exception ex)
                {
                    throw new Exception("failed to paginate @ " + currentOffset + ": " + ex.getMessage(), ex);
                }
            }

            return out;
        }
        catch(Exception ex)
        {
            throw new Exception("failed to paginate. " + ex.getMessage(), ex);
        }

    }


    private static void ValidateNextOffset(Integer currentOffset, Integer nextOffset)
    {
        if( nextOffset != null && nextOffset.equals(currentOffset) )
            throw new IllegalStateException("Paginator returned the same offset twice: " + currentOffset);
    }
}