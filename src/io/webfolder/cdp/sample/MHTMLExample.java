package io.webfolder.cdp.sample;

import static io.webfolder.cdp.type.constant.SnapshotType.Mhtml;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;

public class MHTMLExample {

    public static void main(String[] args) throws IOException {
        Launcher launcher = new Launcher();

        try (SessionFactory factory = launcher.launch();
                            Session session = factory.create()) {
            session.navigate("https://wikipedia.org");
            session.waitDocumentReady();
            String page = session.getCommand().getPage().captureSnapshot(Mhtml);
            Path tempFile = Files.createTempFile("wikipedia", ".mhtml");
            Files.write(tempFile, page.getBytes(UTF_8));

            Session dummySession = factory.create();
            dummySession.navigate("file:///" + tempFile.toString());
            dummySession.wait(5_000);
            dummySession.close();
        } finally {
            launcher.kill();
        }
    }
}
