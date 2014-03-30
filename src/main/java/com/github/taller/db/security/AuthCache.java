package com.github.taller.db.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: Ivan A. Ivanchikov (taller@github.com)
 * Date: 06.02.14
 */
public class AuthCache {
    private final static String SQL =
            "SELECT \"auth\" AS \"AUTH\", \"key\" AS \"KEY\" " +
            "FROM SECURITY.CREDENTIALS;";

    private final static String AUTH = "AUTH";
    private final static String KEY = "KEY";

    private final static Logger log = LoggerFactory.getLogger(AuthCache.class);
    private DataSource dataSource;
    private Map<String, String> cache = null;

    public AuthCache(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void loadAuth() {
        cache = new ConcurrentHashMap<>();

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(SQL);
            while (rs.next()) {
                String auth = rs.getString(AUTH);
                String key = rs.getString(KEY);

                if (auth == null || auth.isEmpty()
                        || key == null || key.isEmpty()) {
                    continue;
                }

                cache.put(auth, key);
            }
        } catch (SQLException e) {
            log.error("SQL-error loading authorize cache.\n{}", e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Closing result-set exception, {}", e.getMessage());
            }
            try {
                if (st != null && !st.isClosed()) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Closing statement exception, {}", e.getMessage());
            }
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                log.error("Closing connection exception, {}", e.getMessage());
            }
        }
    }

    public boolean allowed(String auth, String key) {
        if (!cache.containsKey(auth)) {
            return false;
        }

        return cache.get(auth).equals(key);
    }
}
