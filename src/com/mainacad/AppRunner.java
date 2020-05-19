package com.mainacad;

import com.mainacad.helper.ConnectionInfoHelper;
import com.mainacad.model.ConnectionInfo;
import com.mainacad.multithreads.MultithreadWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class AppRunner {

    private static final Logger LOGGER = Logger.getLogger(AppRunner.class.getName());

    public static void main(String[] args) {

        List<String> connectionIpList = Collections.synchronizedList(new ArrayList<>());
        List<Thread> threads = new ArrayList<>();

        for (int i = 1; i <= 100 ; i++) {
            ConnectionInfo connectionInfo = ConnectionInfoHelper.getRandomConnectionInfo();
            MultithreadWrapper multithreadWrapper = new MultithreadWrapper("thread " + i, connectionInfo, connectionIpList);
            threads.add(multithreadWrapper);
            multithreadWrapper.start();
        }

        while (threadsAlive(threads)) {
            try {
                LOGGER.info("\n--------------------\nThreads still alive!");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        LOGGER.info(connectionIpList.size() + " connections were written to file");
    }

    private static boolean threadsAlive(List<Thread> threads) {
        for (Thread thread: threads) {
            if(thread.isAlive() || thread.getState().equals(Thread.State.NEW)) {
                return true;
            }
        }
        return false;
    }
}
