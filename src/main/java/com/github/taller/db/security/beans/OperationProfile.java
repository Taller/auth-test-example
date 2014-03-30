package com.github.taller.db.security.beans;

/**
 * Author: Ivan A. Ivanchikov (taller@github.com)
 * Date: 16.02.14
 */
public enum OperationProfile {
    Op_01( 1, true,  true,  true,  true ),

    Op_02( 2, true,  true,  true,  false),
    Op_03( 3, true,  true,  false, true ),
    Op_04( 4, true,  false, true,  true ),
    Op_05( 5, false, true,  true,  true ),

    Op_06( 6, true,  true,  false, false),
    Op_07( 7, true,  false, false, true ),
    Op_08( 8, false, false, true,  true ),
    Op_09( 9, false, true,  true,  false),
    Op_10(10, false, true,  false, true ),
    Op_11(11, true,  false, true,  false),

    Op_12(12, true,  false, false, false),
    Op_13(13, false, false, false, true ),
    Op_14(14, false, false, true,  false),
    Op_15(15, false, true,  false, false),

    Op_16(16, false, false, false, false);

    private int id;
    private boolean createAllowed;
    private boolean readAllowed;
    private boolean updateAllowed;
    private boolean deleteAllowed;

    private OperationProfile(int id,
                            boolean createAllowed,
                            boolean readAllowed,
                            boolean updateAllowed,
                            boolean deleteAllowed) {
        this.id = id;
        this.createAllowed = createAllowed;
        this.readAllowed = readAllowed;
        this.updateAllowed = updateAllowed;
        this.deleteAllowed = deleteAllowed;
    }

    public static OperationProfile getById(int id) {
        for (OperationProfile opPr : values()) {
            if (opPr.id == id) {
                return opPr;
            }
        }
        return null;
    }

    public boolean canExecute(Operation o) {
        boolean result = false;
        switch (o) {
            case CREATE:
                result = createAllowed;
                break;
            case READ:
                result = readAllowed;
                break;
            case UPDATE:
                result = updateAllowed;
                break;
            case DELETE:
                result = deleteAllowed;
                break;
        }

        return result;
    }
}
