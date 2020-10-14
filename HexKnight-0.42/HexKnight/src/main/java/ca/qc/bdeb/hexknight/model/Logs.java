/*
 * 
 * Copyright (C) 2015 Éric Wenaas
 * 
 */
package ca.qc.bdeb.hexknight.model;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author Éric Wenaas
 * @version 0.40
 */
public class Logs {
    public static final Logger ACTION_LOGGER = Logger.getLogger("actions");
    public static final Logger STATE_LOGGER = Logger.getLogger("main");
    public static final Logger AI_LOGGER = Logger.getLogger("ai");
    
    public static void initializeLoggers() {

        addHandler(STATE_LOGGER,  "ETAT  -> ");
        addHandler(AI_LOGGER,     "AI    -> ");
        addHandler(ACTION_LOGGER, "ACTION-> ");
    }
    
    private static void addHandler(Logger newLogger, String prefix) {        
        newLogger.setUseParentHandlers(false);
        Formatter formatter = new Formatter() {
            @Override
            public String format(LogRecord lr) {
                StringBuilder chaine = new StringBuilder();
                chaine.append(prefix);
                chaine.append(lr.getMessage());
                chaine.append("\n");
                return chaine.toString();
            }
        };
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(formatter);
        newLogger.addHandler(handler);
    };
}
