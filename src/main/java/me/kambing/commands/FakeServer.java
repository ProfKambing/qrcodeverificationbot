package me.kambing.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

//TODO
public class FakeServer extends Command {
    public FakeServer() {
        this.name = "fakeserver";
        this.hidden = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getMember().equals(event.getGuild().getOwner())) {

        }
    }
}
