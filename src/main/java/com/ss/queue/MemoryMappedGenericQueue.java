package com.ss.queue;

import sun.misc.Unsafe;
import sun.nio.ch.FileChannelImpl;

import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.util.AbstractQueue;
import java.util.Iterator;

public class MemoryMappedGenericQueue<T extends MemoryMappedMessage> extends AbstractQueue<MemoryMappedMessage>
        implements AutoCloseable
{
    private final String loc;
    private final long fileSize;
    private final Class<T> clazz;
    private long memAddress;

    private static Unsafe unsafe;
    private static Method mmap;
    private static Method unmmap;
    private static final byte Commit = 1;
    private static final int commitFlagLen = 1;
    private static final int MetaData = 4;
    private static final int messageLength = 4 + MetaData;
    private long limit, writeLimit = 0;

    public MemoryMappedGenericQueue(String loc, long fileSizeInMB, Class<T> clazz) throws Exception {
        this.loc = loc;
        this.fileSize = fileSizeInMB*1024*1024;
        this.clazz = clazz;
        loadFileIntoMemory();
    }

    private void loadFileIntoMemory() throws Exception
    {
        final RandomAccessFile file = new RandomAccessFile(this.loc, "rw");
        file.setLength(this.fileSize);
        final FileChannel ch = file.getChannel();
        this.memAddress = (long)mmap.invoke(ch, 1, 0L, this.fileSize);
        ch.close();
        file.close();
    }

    static {
        unsafe();
    }

    private static final void unsafe()
    {
        try
        {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe)f.get(null);
            mmap = getMethod(FileChannelImpl.class, "map0", int.class, long.class, long.class);
            unmmap = getMethod(FileChannelImpl.class, "unmap0", long.class, long.class);

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private static Method getMethod(Class<?> clazz, String name, Class<?>... params ) throws Exception {
        Method m = clazz.getDeclaredMethod(name, params);
        m.setAccessible(true);
        return m;
    }


    @Override
    public void close() throws Exception
    {
        unmmap.invoke(null, memAddress, fileSize);
    }

    @Override
    public boolean offer(MemoryMappedMessage msg) {
        long commitPos = writeLimit;
        writeLimit+=commitFlagLen;
        if (writeLimit+messageLength > fileSize) {
            throw new RuntimeException("End of file was reached.");
        }
        putInt(writeLimit, msg.getValue());
        writeLimit+=messageLength;
        commit(commitPos);
        return true;
    }

    private void putInt(long pos, int value)
    {
        unsafe.putInt(pos+memAddress, value);
    }

    private void commit(long commitPos)
    {
        unsafe.putByteVolatile(null, commitPos+ memAddress, Commit);
    }

    @Override
    public MemoryMappedMessage poll() {
        if (isRecordCommitted(limit))
        {
            long recordPos = limit+commitFlagLen;
            limit=recordPos+messageLength;
            if(clazz == ResultMessage.class){
                return new ResultMessage(unsafe.getInt(recordPos+memAddress), unsafe.getBoolean(null, recordPos+1+memAddress));
            }else{
                return new PrimeMessage(unsafe.getInt(recordPos+memAddress));
            }

        }
        return null;
    }

    private boolean isRecordCommitted(long pos)
    {
        return Commit == unsafe.getByteVolatile(null, pos+memAddress);
    }

    @Override
    public T peek() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<MemoryMappedMessage> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException();
    }
}