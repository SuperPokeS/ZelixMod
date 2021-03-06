package zelix.cc.client.inGameGui.altLogin.source.account;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import zelix.cc.client.Zelix;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AltSaving {
    private static File altFile;
    private static File lastAltFile;
    private static File alteningFile;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public AltSaving(File dir) {
        altFile = new File(dir + File.separator + "alts.json");
        lastAltFile = new File(dir + File.separator + "lastalt.json");
        alteningFile = new File(dir + File.separator + "alteningkey.json");
    }

    public void setup() {
        // Tries to create the module's file.
        try {
            // Creates the module file if it doesn't exist.
            if (!altFile.exists()) {

                // Creates the module's file.
                altFile.createNewFile();
                // Returns because there is no need to load.
                return;
            }

            // Loads the file.
            loadFile();
        } catch (IOException exception) {
        }
    }

    public void loadLastAltFile() {
        if (!lastAltFile.exists()) {
            try {
                lastAltFile.createNewFile();
            } catch (IOException e) {
            }
            return;
        }
        try (FileReader inFile = new FileReader(lastAltFile)) {
            Zelix.getInstance().getAltManager().setLastAlt(GSON.fromJson(inFile, new TypeToken<Account>() {
            }.getType()));
        } catch (Exception e) {
        }
    }

    public void loadAlteningTokenFile() {
        if (!alteningFile.exists()) {
            try {
                alteningFile.createNewFile();
            } catch (IOException e) {
            }
            return;
        }
        try (FileReader inFile = new FileReader(alteningFile)) {
            Zelix.getInstance().getAltManager().setAlteningToken(GSON.fromJson(inFile, new TypeToken<String>() {
            }.getType()));
        } catch (Exception e) {
        }
    }

    public void loadFile() {
        if (!altFile.exists()) {
            return;
        }
        try (FileReader inFile = new FileReader(altFile)) {
            Zelix.getInstance().getAltManager().setAccounts(GSON.fromJson(inFile, new TypeToken<ArrayList<Account>>() {
            }.getType()));

            if (Zelix.getInstance().getAltManager().getAccounts() == null) {
                Zelix.getInstance().getAltManager().setAccounts(new ArrayList<Account>());
            }

        } catch (Exception e) {
        }
    }

    public void saveFile() {
        if (altFile.exists()) {
            try (PrintWriter writer = new PrintWriter(altFile)) {
                writer.print(GSON.toJson(Zelix.getInstance().getAltManager().getAccounts()));
            } catch (Exception e) {
            }
        }
    }

    public void saveLastAltFile() {
        if (Zelix.getInstance().getAltManager().getLastAlt() != null) {
            try (PrintWriter writer = new PrintWriter(lastAltFile)) {
                writer.print(GSON.toJson(Zelix.getInstance().getAltManager().getLastAlt()));
            } catch (Exception e) {
            }
        }
    }
    public void saveAlteningTokenFile() {
        if (Zelix.getInstance().getAltManager().getAlteningToken() != null) {
            try (PrintWriter writer = new PrintWriter(alteningFile)) {
                writer.print(GSON.toJson(Zelix.getInstance().getAltManager().getAlteningToken()));
            } catch (Exception e) {
            }
        }
    }
}
