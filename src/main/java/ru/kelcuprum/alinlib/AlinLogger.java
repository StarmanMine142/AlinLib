package ru.kelcuprum.alinlib;

import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AlinLogger {
    public final String name;
    public final String logName;
    public final Logger logger;
    public AlinLogger(String name){
        this.name = name;
        this.logName = String.format("[%s] ", this.name);
        this.logger = LogManager.getLogger(this.name);
    }
    public void log(Level level, String msg){
        logger.log(level, this.logName+msg);
    }

    public void log(String msg){
        log(Level.INFO, msg);
    }

    public void log(Level level, String msg, Object ...objects){
        log(level, String.format(msg, objects));
    }

    public void log(String msg, Object ...objects){
        log(Level.INFO, String.format(msg, objects));
    }

    public void log(Level level, Component msg){
        log(level, msg.getString());
    }

    public void log(Component msg){
        log(Level.INFO, msg.getString());
    }

    public void log(Level level, Component msg, Object ...objects){
        log(level, String.format(msg.getString(), objects));
    }

    public void log(Component msg, Object ...objects){
        log(Level.INFO, String.format(msg.getString(), objects));
    }

    public void error(String msg, Object ...objects){
        log(Level.ERROR, String.format(msg, objects));
    }
    public void error(String msg){
        log(Level.ERROR, msg);
    }
    public void error(Component msg, Object ...objects){
        log(Level.ERROR, String.format(msg.getString(), objects));
    }
    public void error(Component msg){
        log(Level.ERROR, msg.getString());
    }

    public void debug(String msg, Object ...objects){
        log(Level.DEBUG, String.format(msg, objects));
    }
    public void debug(String msg){
        log(Level.DEBUG, msg);
    }

    public void debug(Component msg, Object ...objects){
        log(Level.DEBUG, String.format(msg.getString(), objects));
    }
    public void debug(Component msg){
        log(Level.DEBUG, msg.getString());
    }

    public void warn(String msg, Object ...objects){
        log(Level.WARN, String.format(msg, objects));
    }
    public void warn(String msg){
        log(Level.WARN, msg);
    }
    public void warn(Component msg, Object ...objects){
        log(Level.WARN, String.format(msg.getString(), objects));
    }
    public void warn(Component msg){
        log(Level.WARN, msg.getString());
    }

    public void fatal(String msg, Object ...objects){
        log(Level.FATAL, String.format(msg, objects));
    }
    public void fatal(String msg){
        log(Level.FATAL, msg);
    }
    public void fatal(Component msg, Object ...objects){
        log(Level.FATAL, String.format(msg.getString(), objects));
    }
    public void fatal(Component msg){
        log(Level.FATAL, msg.getString());
    }
}
