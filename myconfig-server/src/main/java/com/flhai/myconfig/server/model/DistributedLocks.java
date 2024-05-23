package com.flhai.myconfig.server.model;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
public class DistributedLocks {

    @Autowired
    DataSource dataSource;

    Connection connection;

    @Getter
    private AtomicBoolean locked = new AtomicBoolean(false);

    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void init() {
        try {
            connection = dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        executorService.scheduleAtFixedRate(this::tryLock, 1, 5, java.util.concurrent.TimeUnit.SECONDS);
    }

    private void tryLock() {
        try {
            lock();
            locked.set(true);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("Failed to lock...");
            locked.set(false);
        }
    }

    public boolean lock() throws SQLException {
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        // only to the current connection.
        connection.createStatement().execute("set innodb_lock_wait_timeout=5");
        connection.createStatement().execute("use myconfig");
        // wait lock timeout 5s will result in a lock wait timeout exception
        connection.prepareStatement("select app from locks where id=1 for update").executeQuery();

        if (locked.get()) {
            log.info("reenter dist lock");
        } else {
            log.info("get a dist lock");
        }
        return true;
    }


    @PreDestroy
    public void close() {
        try {
            if (connection != null || !connection.isClosed()) {
                connection.rollback();
                connection.close();
            }
        } catch (SQLException e) {
            log.warn("Failed to close connection");
        }
    }
}
