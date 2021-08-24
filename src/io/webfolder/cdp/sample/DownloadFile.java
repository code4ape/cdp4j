package io.webfolder.cdp.sample;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.command.Browser;
import io.webfolder.cdp.command.Page;
import io.webfolder.cdp.event.Events;
import io.webfolder.cdp.event.page.DownloadProgress;
import io.webfolder.cdp.event.page.DownloadWillBegin;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;
import io.webfolder.cdp.type.constant.DownloadBehavior;
import io.webfolder.cdp.type.constant.DownloadState;

@SuppressWarnings("deprecation")
public class DownloadFile {

    public static void main(String[] args) throws IOException, InterruptedException {
        Launcher launcher = new Launcher();

        try (SessionFactory factory = launcher.launch();
                            Session session = factory.create()) {
            session.navigate("https://www.chiark.greenend.org.uk/~sgtatham/putty/latest.html");
            session.waitDocumentReady();

            Page page = session.getCommand().getPage();
            // this is required to capture download events
            page.enable();
            
            Browser browser = session.getCommand().getBrowser();

            Path tempDir = Files.createTempDirectory("download").toAbsolutePath();
            System.out.println(tempDir);

            browser.setDownloadBehavior(DownloadBehavior.Allow,
                                        null,
                                        tempDir.toString(),
                                        Boolean.TRUE);

            // link must be visible before downloading the file
            session.evaluate("document.querySelector(\"code\").scrollIntoView()");
            // click the download link
            session.click("code");
            CountDownLatch latch = new CountDownLatch(1);
            session.addEventListener((event, value) -> {
                // important!
                //
                // if chrome version is equal less than 90, chrome triggers PageDownloadProgress
                // otherwise chrome triggers BrowserDownloadProgress (chrome version >= 92)
                // based on your chrome version, you can simplify this logic
                //
                if (Events.PageDownloadProgress == event) {
                    DownloadProgress dp1 = (DownloadProgress) value;
                    System.out.println("download state: " + dp1.getState() + ", rceivedBytes: " + dp1.getReceivedBytes());
                    if (DownloadState.Completed == dp1.getState()) {
                        latch.countDown();
                    }
                } else if (Events.BrowserDownloadProgress == event) {
                    io.webfolder.cdp.event.browser.DownloadProgress dp2 = (io.webfolder.cdp.event.browser.DownloadProgress) value;
                    System.out.println("download state: " + dp2.getState() + ", rceivedBytes: " + dp2.getReceivedBytes());
                    if (DownloadState.Completed == dp2.getState()) {
                        latch.countDown();
                    }
                } else if (Events.PageDownloadWillBegin == event) {
                    DownloadWillBegin begin1 = (DownloadWillBegin) value;
                    System.out.println("download started: " + begin1.getSuggestedFilename()
                            + ", url: " + begin1.getUrl());
                } else if (Events.BrowserDownloadWillBegin == event) {
                    io.webfolder.cdp.event.browser.DownloadWillBegin begin2 = (io.webfolder.cdp.event.browser.DownloadWillBegin) value;
                    System.out.println("download started: " + begin2.getSuggestedFilename()
                            + ", url: " + begin2.getUrl());
                }
            });
            latch.await();
        } finally {
            launcher.kill();
        }
    }
}
