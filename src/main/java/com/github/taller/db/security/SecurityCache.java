package com.github.taller.db.security;

import com.github.taller.db.security.beans.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: Ivan A. Ivanchikov (taller@github.com)
 * Date: 06.02.14
 */
public class SecurityCache {
    private static final String S_NAME = "SNAME";
    private static final String O_NAME = "OBNAME";
    private static final String O_TYPE = "OTYPE";
    private static final String OPS = "OPS";

    private static final String AUTH = "AUTH";
    private static final String KEY = "KEY";

    private static final String UNAME = "UNAME";
    private static final String GNAME = "GNAME";

    private static final Logger log = LoggerFactory.getLogger(SecurityCache.class);

    private DataSource dataSource;
    private String adminGroup;

    public SecurityCache(DataSource dataSource, String magicGroup) {
        this.dataSource = dataSource;
        this.adminGroup = magicGroup;
    }

    private Map<String, Map<Objects, OperationProfile>> userSecurity = null;
    private Map<String, List<String>> groups4users = null;
    private Map<Credentials, String> userByCredential = null;

    public void loadAuth() {
        userSecurity = loadUserSecurity();
        groups4users = loadGroups4Users();
        userByCredential = loadUserByCredential();
    }

    private Map<Credentials, String> loadUserByCredential() {
        final String SQL =
                "SELECT "
                    + "c.\"auth\" AS \"AUTH\", "
                    + "c.\"key\" AS \"KEY\", "
                    + "s.\"name\" AS \"UNAME\" "
                + "FROM SECURITY.CREDENTIALS c "
                    + "LEFT JOIN SECURITY.SUBJECTS s ON c.\"subj_id\" = s.\"subj_id\";";

        Map<Credentials, String> result = new ConcurrentHashMap<>();

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(SQL);
            while (rs.next()) {
                String user = rs.getString(UNAME);
                String auth = rs.getString(AUTH);
                String key = rs.getString(KEY);

                if (user == null || user.isEmpty()
                        || auth == null || auth.isEmpty()
                        || key == null || key.isEmpty()) {
                    continue;
                }

                Credentials c = new Credentials(auth, key);
                result.put(c, user);
            }
        } catch (SQLException e) {
            log.error("SQL-error loading security cache.\n{}", e.getMessage());
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


        return result;
    }

    private Map<String, List<String>> loadGroups4Users() {
        final String SQL =
                "SELECT "
                    + "su.\"name\" AS \"UNAME\", "
                    + "sg.\"name\" AS \"GNAME\" "
                + "FROM SECURITY.GROUPS4USERS g "
                    + "LEFT JOIN SECURITY.SUBJECTS su ON g.\"user_id\" = su.\"subj_id\" "
                    + "LEFT JOIN SECURITY.SUBJECTS sg ON g.\"group_id\" = sg.\"subj_id\" "
                + "ORDER BY "
                    + "su.\"name\";";

        Map<String, List<String>> result = new ConcurrentHashMap<>();

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(SQL);
            while (rs.next()) {
                String user = rs.getString(UNAME);
                String group = rs.getString(GNAME);

                if (user == null || user.isEmpty()
                        || group == null || group.isEmpty()) {
                    continue;
                }

                List<String> groups = result.get(user);
                if (groups == null || groups.isEmpty()) {
                    groups = new ArrayList<>();
                    groups.add(group);
                    result.put(user, groups);
                } else {
                    groups.add(group);
                }
            }
        } catch (SQLException e) {
            log.error("SQL-error loading security cache.\n{}", e.getMessage());
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

        return result;
    }

    private Map<String, Map<Objects, OperationProfile>> loadUserSecurity() {
        final String SQL =
                "SELECT "
                    + "s.\"name\" AS \"SNAME\", "
                    + "o.\"name\" AS \"OBNAME\", o.\"type\" AS \"OTYPE\", "
                    + "p.\"ops_id\" AS \"OPS\" "
                + "FROM SECURITY.PERMISSION p "
                    + "LEFT JOIN SECURITY.SUBJECTS s ON p.\"subj_id\" = s.\"subj_id\" "
                    + "LEFT JOIN SECURITY.OBJECTS o ON p.\"obj_id\" = o.\"obj_id\" "
                + "WHERE s.\"type\" =  'USER';";

        Map<String, Map<Objects, OperationProfile>> result = new ConcurrentHashMap<>();

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(SQL);
            while (rs.next()) {
                String obj_name = rs.getString(O_NAME);
                String obj_type = rs.getString(O_TYPE);

                ObjectType type = ObjectType.getObjectTypeByCode(obj_type);
                Objects object = new Objects(obj_name, type);

                int ops = rs.getInt(OPS);
                OperationProfile profile = OperationProfile.getById(ops);

                String uname = rs.getString(S_NAME);

                Map<Objects, OperationProfile> cachedObject = result.get(uname);
                if (cachedObject == null) {
                    cachedObject = new HashMap<>();
                    cachedObject.put(object, profile);
                    result.put(uname, cachedObject);
                } else {
                    cachedObject.put(object, profile);
                }
            }
        } catch (SQLException e) {
            log.error("SQL-error loading security cache.\n{}", e.getMessage());
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

        return result;
    }

    public boolean allowed(String auth, String key, String objName, ObjectType type, Operation operation) {

        if (auth == null || auth.isEmpty()) {
            log.warn("Auth is not provided.");
            return false;
        }

        if (key == null || key.isEmpty()) {
            log.warn("Key is not provided.");
            return false;
        }

        if (objName == null || objName.isEmpty()) {
            log.warn("Object for checking security is not provided.");
            return false;
        }

        if (operation == null) {
            log.warn("Operation for checking security is not provided.");
            return false;
        }

        Credentials c = new Credentials(auth, key);
        String user = userByCredential.get(c);

        if (user == null || user.isEmpty()) {
            log.warn("Can't find user for {}/{}", auth, key);
            return false;
        }

        List<String> groups = groups4users.get(user);

        if (groups != null) {
            for (String group : groups) {
                if (group == null || group.isEmpty()) {
                    continue;
                }

                if (group.equals(adminGroup)) {
                    log.warn("Admin group was found.");
                    return true;
                }
            }
        }

        Map<Objects, OperationProfile> cachedObject = userSecurity.get(user);

        if (cachedObject == null || cachedObject.isEmpty()) {
            log.warn("Can't find security objects for user {}", user);
            return false;
        }

        Objects objects = new Objects(objName, type);
        OperationProfile operations = cachedObject.get(objects);

        log.debug("Checking security for user {} on {}", user, objName);

        if (operations == null) {
            log.warn("Any operations are forbidden for {} and {}", user, objName);
            return false;
        }

        return operations.canExecute(operation);
    }

}
