package ch.brugg.fhnw.btm.command;

import java.sql.Timestamp;

public interface Command {
    public Timestamp getTimestamp();
    public void execute();
}
