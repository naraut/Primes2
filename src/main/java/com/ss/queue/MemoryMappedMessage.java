package com.ss.queue;

import sun.misc.Unsafe;

public interface MemoryMappedMessage {

    Integer getValue();

    int messageLength();

    void writeMessage(Unsafe unsafe, long mem, long offset);
}
