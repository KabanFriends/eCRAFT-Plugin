package io.github.kabanfriends.ecraft.handlers;

import io.github.kabanfriends.ecraft.ECraft;
import io.github.kabanfriends.ecraft.commands.AbstractCommand;
import io.github.kabanfriends.ecraft.events.listeners.CommandEvents;

import java.util.HashMap;

public class CommandHandler {

    private final CommandEvents events;
    private HashMap<String, AbstractCommand> commands = new HashMap<>();

    public CommandHandler() {
        events = new CommandEvents();
    }

    public void register(String name, AbstractCommand command) {
        commands.put(name, command);
        ECraft.getInstance().getCommand(name).setTabCompleter(events);
    }

    public HashMap<String, AbstractCommand> getCommands() {
        return commands;
    }

}
