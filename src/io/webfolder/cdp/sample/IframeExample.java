package io.webfolder.cdp.sample;

import static io.webfolder.cdp.logger.CdpConsoleLogggerLevel.Info;
import static io.webfolder.cdp.logger.CdpLoggerType.Console;
import static java.lang.Boolean.TRUE;

import java.net.URL;
import java.util.List;

import io.webfolder.cdp.JsFunction;
import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.Options;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;
import io.webfolder.cdp.session.SessionSettings;
import io.webfolder.cdp.type.page.Frame;

public class IframeExample {

    public static interface JsBridge {

        @JsFunction("return palindrome(msg)")
        String getPalindrome(String msg);
    }

    public static void main(String[] args) {
        URL url = IframeExample.class.getResource("/iframe-example.html");

        Launcher launcher = new Launcher(Options.builder()
                                                .loggerType(Console)
                                                .consoleLoggerLevel(Info)
                                            .build());

        try (SessionFactory factory = launcher.launch();
                //
                // Important!
                //
                // Please create the session with new SessionSettings(TRUE) option.
                // Otherwise, the session does not support Iframe features.
                //
                Session session = factory.create(new SessionSettings(TRUE))) {

            // Register the JsFunction before the navigate method
            JsBridge bridge = session.registerJsFunction(JsBridge.class);

            session.navigate(url.toString());
            session.waitDocumentReady();

            session.enableConsoleLog();

            // getFrames() returns flat list
            // if you need a structured tree list, use: session.getCommand().getPage().getFrameTree()
            List<Frame> frames = session.getFrames();

            // ----------------------------------------------------------------
            // switch to iframe 1 (child of root/main frame)
            // ----------------------------------------------------------------
            Frame iframe1 = frames.stream()
                                    .filter(p -> p.getUrl().endsWith("iframe1.html"))
                                    .findFirst()
                                .get();
            session.switchFrame(iframe1);
            String titleIframe1 = session.getText("title");
            // prints "child iframe 1"
            System.out.println(titleIframe1);
            // prints "child iframe 1 - btn clicked" to Console
            session.click("#btn");

            String attrValue = session.getAttribute("#dummy-txt", "data-key");
            // prints "foo"
            System.out.println(attrValue);
            session.setAttribute("#dummy-txt", "data-key", "bar");
            attrValue = session.getAttribute("#dummy-txt", "data-key");
            // prints "bar"
            System.out.println(attrValue);

            // ----------------------------------------------------------------
            // switch to iframe 2 (child of iframe 1)
            // ----------------------------------------------------------------
            Frame iframe2 = frames.get(1);
            session.switchFrame(iframe2);
            String titleIframe2 = session.getText("title");
            // prints "child iframe 2"
            System.out.println(titleIframe2);
            String iframe2Response = (String) session.evaluate("dummyJsFunction()");
            // prints "hi!"
            System.out.println(iframe2Response);
            // prints false
            System.out.println(session.isRootFrame());
            // prints the frameId
            System.out.println(session.getChildFrameId());
            String msg = bridge.getPalindrome("kayak");
            // prints "kayak"
            System.out.println(msg);
            String id = (String) session.getProperty("#dummy-txt-iframe-2", "id");
            // prints "dummy-txt-iframe-2"
            System.out.println(id);

            session.setAttribute("#dummy-txt-iframe-2", "dummy-attribute", "foobar");
            String outerHtml = session.getOuterHtml("#dummy-txt-iframe-2");
            System.out.println("outerHtml: " + outerHtml);

            String dummyAttribute = session.getAttribute("#dummy-txt-iframe-2", "dummy-attribute");
            System.out.println("dummyAttribute: " + dummyAttribute);

            // ----------------------------------------------------------------
            // switch to Root/Main frame
            // ----------------------------------------------------------------
            session.switchToRootFrame();
            // prints "iframe example root"
            System.out.println(session.getTitle());
            // prints true
            System.out.println(session.isRootFrame());

            session.wait(1000);
        } finally {
            launcher.kill();
        }
    }
}