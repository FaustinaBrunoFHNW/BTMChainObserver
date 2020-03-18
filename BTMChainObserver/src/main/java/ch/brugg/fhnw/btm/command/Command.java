package ch.brugg.fhnw.btm.command;

import java.sql.Timestamp;

/**
 * Interface für Commands für das Command Pattern
 * @Author Faustina Bruno, Serge Jurij Maikoff
 */
public interface Command {
    public Timestamp getTimestamp();
    public void execute();
}
