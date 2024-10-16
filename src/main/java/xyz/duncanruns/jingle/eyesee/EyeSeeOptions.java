package xyz.duncanruns.jingle.eyesee;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import xyz.duncanruns.jingle.Jingle;
import xyz.duncanruns.jingle.util.FileUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class EyeSeeOptions {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path OPTIONS_PATH = Jingle.FOLDER.resolve("eyesee.json");

    public boolean enabled = false;
    public boolean autoPos = true;
    public int x;
    public int y;
    public int w;
    public int h;
    public int fpsLimit = 30;

    public static Optional<EyeSeeOptions> load() {
        if (!Files.exists(OPTIONS_PATH)) return Optional.of(new EyeSeeOptions());
        try {
            EyeSeeOptions eyeSeeOptions = FileUtil.readJson(OPTIONS_PATH, EyeSeeOptions.class);
            return Optional.of(eyeSeeOptions);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean trySave() {
        try {
            FileUtil.writeString(OPTIONS_PATH, GSON.toJson(this));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
