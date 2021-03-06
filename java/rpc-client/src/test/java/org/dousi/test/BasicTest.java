package org.dousi.test;


import io.netty.buffer.ByteBuf;
import org.dousi.client.DousiRpcClient;
import org.dousi.client.DousiService;
import org.dousi.client.NativeRpcClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


// A POJO class.
class PersonInfo implements java.io.Serializable {

    private static final long serialVersionUID = 8010472539851795705L;

    private String name;

    private int age;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}


public class BasicTest {

    @Test
    public void testBasicCall() throws InterruptedException, ExecutionException {
        DousiRpcClient rpcClient = new NativeRpcClient("127.0.0.1:10001");
        DousiService service = rpcClient.getService("Adder");
        CompletableFuture<Object> future = service.asyncFunction("add").invoke(Integer.class, 2, 3);
        CompletableFuture<Object> future2 = service.asyncFunction("add").invoke(Integer.class, 2, 6);
        Assert.assertEquals(future.get(), 5);
        Assert.assertEquals(future2.get(), 8);
    }

    @Test
    public void testPojoObjectAsArgumentAndReturn() throws ExecutionException, InterruptedException {
        DousiRpcClient rpcClient = new NativeRpcClient("127.0.0.1:10002");
        DousiService service = rpcClient.getService("UserDefinedClass");
        PersonInfo p = new PersonInfo();
        p.setName("Allen");
        p.setAge(19);
        CompletableFuture<Object> future = service.asyncFunction("IncrAge").invoke(PersonInfo.class, p, 2);
        PersonInfo received = (PersonInfo) future.get();
        Assert.assertEquals(received.getName(), "Allen");
        Assert.assertEquals(received.getAge(), 21);
    }

    @Test
    public void f() {
        byte[] data = new byte[8];
        ByteBuffer buf = ByteBuffer.wrap(data);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putLong(7);

        int x = 3;
        int ux = x >> 8;
    }
}
