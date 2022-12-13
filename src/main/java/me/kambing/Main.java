package me.kambing;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.examples.command.PingCommand;
import com.jagrosh.jdautilities.examples.command.ShutdownCommand;
import me.kambing.commands.Verification;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Main {

    public static CommandClientBuilder client;
    public static JDA jda;

    //TODO latest jda
    public static void main(String[] args) throws IOException, LoginException, IllegalArgumentException, RateLimitedException {
        String token = "MTA1MTgyMjExNDY2NTE0ODQ1Ng.G01lAj.-pGvS4v173cSqaKZynfgp_fQESXFq2H7guCD_0"; //insert ur token

        String ownerId = "806897032337817610";

        EventWaiter waiter = new EventWaiter();
        client = new CommandClientBuilder();
        client.useDefaultGame();
        client.setOwnerId(ownerId);
        client.setActivity(Activity.listening("verifications"));
        client.setStatus(OnlineStatus.ONLINE);
        client.setEmojis("\u2705", "\u26A0", "\uD83D\uDEAB");
        client.setPrefix(";");
        client.addCommands( //register all da commands
                new PingCommand(),
                new ShutdownCommand(),
                new Verification()

     );
        jda = JDABuilder.createDefault(token)
                .addEventListeners(waiter, client.build())
                .build();
        }
    }
