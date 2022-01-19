cdp4j Release Notes
-------------------------------------------------------------------------------

### 6.0.0 - TBA

* :new: Added **async** support to cdp4j. All devtools command could be invoked asynchronously (e.g. session.getAsyncCommand().getNetwork().getCookies().thenApply(cookies -> { });).
* :new: Added FFmpeg video recorder, uses FFmpeg [slideshow](https://trac.ffmpeg.org/wiki/Slideshow) feature
* :new: Added H264 realtime video recorder, uses Cisco [openh264](https://github.com/cisco/openh264) & [IHMC Java Video Codecs](https://github.com/ihmcrobotics/ihmc-video-codecs)
* :new: Updated devtools protocol to r953906 (Dec 23, 2021).
* :new: Added toString method to type, event and result classes (auto generated).
* :new: Added setRegisteredEventListeners to Session class. cdp4j parse and process all event registered if related domain (Network, Runtime....) is enabled.
  With this new method, cdp4j process and parse only listened events. This new feature minimize CPU and memory usage. 
* Updated Playwright [selectors](https://playwright.dev/docs/selectors) version to 1.17.2
* Fixed, SemaphoreContext throws CdpReadTimeoutException if tryAcquire method fails. Before 6.0.0 cdp4j throws NullPointerException instead of CdpReadTimeoutException.
  Thank you Marc P. for the bug report.
* Updated external dependencies to latest versions.
* Removed PipeChannelFactory (libuv). Most of the users/customers don't want to use JNI based integratation that's why we removed libuv library from cdp4j.
* Changed Options.workerThreadPool(ExecutorService) and Options.eventHandlerThreadPool(ExecutorService) signatures to workerThreadPool(Executor) and eventHandlerThreadPool(Executor).
* Removed ApplicationCache command and related types which is no longer supported by devtools protocol.
* Formatted generated codes (type, event, result, command) with Java Google style.
* Improved Jackson json parse performance (see JacksonPerfTest)
* Removed hideScrollbar param from Session.captureScreenshot() method **BREAKING CHANGE**
* Removed Domain, EventName and Returns annotations
* Added more example code snippets. ScreenshotDomElement, VideoRecorderExample, VideoRecorderExampleLibx264, VideoRecorderRealtimeExample

### 5.4.1 - December 24, 2021

* Updated log4j-core to 2.17.0
* [new feature] - :new: Added reuseEmptyTab option to SessionSettings. This new option helps to disable reusing empty tabs.

3 New features and 3 bug-fixes contributed by Pascal B., Thanks!

* [new feature] - :new: "loggerNamePrefix" option to separate logging from multiple cdp4j connections (use Options.loggerNamePrefix(String))
* [new feature] - :new: Support CDP connections using the simplified V8 inspector protocol (use Options.protocolVersion(ProtocolVersion))
* [new feature] - :new: Do not drop session upon target crash Because a target can be recreated and a Target.targetDestroyed event is trigged, when a crashed target is finally closed
* [bug-fix] - Fix wrong Target.TargetCrashed value type (see Events.java)
* [bug-fix] - Added missing enum value to PrivateNetworkRequestPolicy (fixed jackson FormatException: Cannot deserialize value of...)
* [bug-fix] - Support id-after-error pattern in jackson handler (see JacksonMessageHandler.java)

### 5.4.0 - August 24, 2021

* Improved libuv-java performance
* Updated Playwright [selectors](https://playwright.dev/docs/selectors) version to 1.9.2
* Deprecated TaskKillProcessManager.java (Child processes still runs after the parent dies if chrome is executed with headless mode. This problem does not exist for headful mode.)
* Added a new Launcher class that will support POSIX spawn ([yeshup](https://github.com/webfolderio/yeshup)).
* Updated DevTools protocol to version r882921.
* WfExecLauncher is used as a default ProcessManger for Windows enviorment.

### 5.3.1 - March 26, 2021

* Pass all environment variables to WfExecLauncher otherwise, Chrome fails to get TEMP variable.
* Improved SessionFactory.close(), getPage().close() method closes target internally, no need to invoke getTarget().close()
* Fixed race condition while using blank pages. Do not reuse the blank page if the headless mode is enabled or chrome started externally
  If more than one clients try to reuse the same blank page, unexpected race conditions might occur.
* Double check if session disposed. On under heavy load concurrent usage cdp4j throws "Session was disposed" exception.
* Improved navigateAndWait, tried to get frame Id from different ways.
* Fixed concurrency bug for JreWebSocketChannel. If multiple threads try to send websocket message
  JRE WebSocket throws IllegalStateException("Send pending") exception. This simple lock mechanism helps us to send WebSocket messages one by one in order.
* Fixed, commandTypeReferences must cleared by SessionFactory.close()

**cdp4j 5.3.1 is a bug-fix release, with no new features with respect to cdp4j 5.3.0.**

### 5.2.1 - March 5, 2021

* :new: Added iframe supports (Thanks to AYCD for sponsorship)
* Fixed NoSuchMethodException while using log4j
 
### 5.2.0 - February 15, 2021

* :new: Added CloseListener feature to Session and SessionFactory classes.
* :new: Added UserProfileDirectoryCleaner which clears the user profile directory when browser is terminated.
* :new: Dispose the Session and remove it from SessionFactory when the target (window/tab) crashed.
* :new: Launcher.kill() method closes the SessionFactory.
* :new: Use nv-websocket-client as a default WebSocket library if the Java version is equal to 8.
* Update wf-exec library to version 1.1.0

List of contributions by Pascal B., Thank you.

* Removed wait for termination for ps process in LinuxProcessManager & MacOsProcessManager.kill() - since it will never terminate before reading starts.
* Allow override of launchWithProcessBuilder
* Remove context from HashMap once consumed to avoid Memory Leak
* Enable runtime domain during connect after attaching the eventListeners Otherwise we might miss existing runtime execution contexts.
* :new: Support connecting sessions (and removing them) without issuing page commands This is required to connect to non-page sessions to call commands like session.getCommand().getRuntime().runIfWaitingForDebugger()
* :new: Allow synchronous message handler
* :new: Make SessionFactory.getSessions available on public API
* :new: Make SessionFactory.getBrowserSession available on public API
* :new: Added option to create session for already attached target. This is required to properly use Target.setAutoAttach
* :new: Added missing Strings list to CaptureSnapshotResult

### 5.1.0 - January 15, 2021

* Fixed "Target.closeTarget Specified target doesn't support closing" error. Check if browser session is already closed before closing it.
* Connect existing devtools server [new feature]
* Added JUL (Java Logging) support
* Added DevToolsConnection and DevToolsProfileConnection examples

### 5.0.0 - September 11, 2020

* Added libuv support

* Updated devtools protocol to r784747

* Introduced JavaScript/[QuickJS](https://bellard.org/quickjs/) support. Example: [bing.js](https://github.com/webfolderio/cdp4j/blob/master/src/test/resources/quickjs/sample/bing.js), [TestQuickJs.java](https://github.com/webfolderio/cdp4j/blob/master/src/test/java/io/webfolder/cdp/test/TestQuickJs.java)

* Added [Playwright](https://playwright.dev/#version=v1.2.1&path=docs%2Fselectors.md) selector engine. Playwright supports multiple selector engines (css, text, xpath and Shadow DOM) used to query elements in the web page.
Example: [PlaywrightSelector.java](https://github.com/webfolderio/cdp4j/blob/master/src/test/java/io/webfolder/cdp/sample/PlaywrightSelector.java)

* Added Microsoft Edge support

### 4.2.2 - February 17, 2020

* __Fixed__ - Revert read timeout exception to previous logic (v3.x)

* __Fixed__ - invalid json mapping (Expected BEGIN_OBJECT but was STRING at...)

### 4.2.1 - January 27, 2020

* __FIXED__ Launcher throws ClassFoundException when wf-exec library is missing.

### 4.2.0 - January 22, 2020

* :new: Added Vert.x [WebSocket client support](https://github.com/webfolderio/cdp4j/blob/master/src/test/java/io/webfolder/cdp/sample/VertxWebSocketConnection.java).
* :new: Added a new process executor ([wf-exec](https://github.com/webfolderio/wf-exec)). This feature available only for Windows platform.
wf-exec help to kill all child processes when parent dies normally or abnormally. Sample usage: [WfExecSample.java](https://github.com/webfolderio/cdp4j/blob/master/src/test/java/io/webfolder/cdp/sample/WfExecSample.java)
* Update devtools protocol [r726364](https://github.com/webfolderio/cdp4j/commit/9d726822cb869bae5895589247b655df559ffa27)
* :new: Released cdp4j.jar to GitHub package. [#118](https://github.com/webfolderio/cdp4j/issues/118)
* Dropped graalvm8 supports. cdp4j supports only graalvm11.

### 4.1.0 - November 26, 2019

* :new: Add [GraalVM](https://www.graalvm.org/) support [#116](https://github.com/webfolderio/cdp4j/issues/116)
* Add [SessionSettings](https://github.com/webfolderio/cdp4j/commit/2354a8cb2c1ad82847968d0a28a686ea5f82c550) class to configure width and height of tab page.

### 4.0.1 - October 10, 2019

:new: Added Session.getDOMSnapshot() method (_Returns a document snapshot, including the full DOM tree of the root node_).

:new: Added initial GraalVM support (_this feature is not ready to use, wait until version 4.1.0_)

__Fixed__ Fixed buggy [TaskKillProcessManager](https://github.com/webfolderio/cdp4j/blob/master/src/main/java/io/webfolder/cdp/TaskKillProcessManager.java) implementation.

### 4.0.0 - October 1, 2019

:new: Added __non-blocking__ WebSocket support

List of supported WebSocket clients:

1. [async-http-client](https://github.com/AsyncHttpClient/async-http-client) (Netty based)
2. [Eclipse Jetty](https://github.com/eclipse/jetty.project)
3. [JRE WebSocket Client](https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/WebSocket.html) (requires Java 11, **default WebSocket client** of cdp4j 4.x)
4. [JSR-356](https://www.oracle.com/technetwork/articles/java/jsr356-1937161.html) WebSocket Client ([Tomcat WebSocket Client](https://tomcat.apache.org/tomcat-8.5-doc/web-socket-howto.html) & [Tyrus](https://tyrus-project.github.io/) is supported)
5. [TooTallNate](https://github.com/TooTallNate/Java-WebSocket) WebSocket client
6. [Undertow](http://undertow.io/) WebSocket client

> Beside these WebSocket clients implementations we still supports [nv-websocket-client](https://github.com/TakahikoKawasaki/nv-websocket-client) which is default WebSocket client of cdp4j 3.x. We highly recommended to use a non-blocking WebSocket client implemntation instead of nv-websocket-client. nv-websocket-client use _java.net.Socket_ which blocks reading & writing threads.

non-blocking WebSocket client examples: [Netty](https://github.com/webfolderio/cdp4j/blob/master/src/test/java/io/webfolder/cdp/sample/NettyWebSocketConnection.java), [Jetty](https://github.com/webfolderio/cdp4j/blob/master/src/main/java/io/webfolder/cdp/channel/JettyWebSocketFactory.java), [JRE WebSocket Client](https://github.com/webfolderio/cdp4j/blob/master/src/main/java/io/webfolder/cdp/channel/JreWebSocketFactory.java), [JSR-356 WebSocketClient](https://github.com/webfolderio/cdp4j/blob/master/src/main/java/io/webfolder/cdp/channel/StandardWebSocketFactory.java), [TooTallNateWebSocketFactory](https://github.com/webfolderio/cdp4j/blob/master/src/main/java/io/webfolder/cdp/channel/TooTallNateWebSocketFactory.java), [Undertow WebSocket Client](https://github.com/webfolderio/cdp4j/blob/master/src/main/java/io/webfolder/cdp/channel/UndertowWebSocketFactory.java)

:new: Replaced [dynamic proxy](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/reflect/Proxy.html) based `io.webfolder.cdp.command` classes with [concrete class](https://github.com/webfolderio/cdp4j/blob/master/src/main/java/io/webfolder/cdp/command/PageImpl.java) implementations.

:new: Added custom gson type adapter to improve json serialization & deserialization performance. cdp4j 4.x use [stag-java](https://medium.com/vimeo-engineering-blog/boosting-app-performance-with-reflectionless-de-serialization-486179edeb29) to generate [TypeAdapter](https://static.javadoc.io/com.google.code.gson/gson/2.8.5/com/google/gson/TypeAdapter.html) classes. Default option still use reflection based TypeAdapter. Invoke `Options.useCustomTypeAdapter(true)` to use __reflectionless__ (de)serialization.

:new: Move to flatten cdp protocol. DevTools protocol is dropping nested targets and switching to flatten protocol. cdp4j 4.x will be incompatible with Chromium below version 72

### 3.0.15 - December 12, 2019

* Added connectionTimeout paramater to  AbstractLauncher.launch()

### 3.0.14 - November 11, 2019

* Fixed [Cancel close timer after task execution](https://github.com/TakahikoKawasaki/nv-websocket-client/pull/191)
* Updated nv-websocket-client to 2.9

### 3.0.13 - October 11, 2019

* Removed WINP dependency from TaskKillProcessManager

### 3.0.12 - June 25, 2019

* Added Launcher.isChromeInstalled() method

### 3.0.11 - June 14, 2019

* Fixed xpath selector [bug](https://github.com/webfolderio/cdp4j/commit/c612087c9b8f7d491b8c6f4b88edbff060d3a4f5)

### 3.0.10 - June 14, 2019

* Added [TaskKillProcessManager](https://github.com/webfolderio/cdp4j/blob/master/src/main/java/io/webfolder/cdp/TaskKillProcessManager.java), Alternative implmentation of [WindowsProcessManager](https://github.com/webfolderio/cdp4j/blob/master/src/main/java/io/webfolder/cdp/WindowsProcessManager.java)
* Fixed xpath selector [bug](https://github.com/webfolderio/cdp4j/commit/f173ca373163ee56a3c98df9f92fe04b50b1606d)
* Added missing value field to [DataCollected](https://github.com/webfolderio/cdp4j/commit/1c9eae3bfa054af2ce0568c0a7971c493b446f36) class
* Added Profiling [sample](https://github.com/webfolderio/cdp4j/blob/master/src/test/java/io/webfolder/cdp/sample/Profiling.java).

### 3.0.9 - June 8, 2019

* Added proxy connection [sample](https://github.com/webfolderio/cdp4j/blob/master/src/test/java/io/webfolder/cdp/sample/ProxyConnection.java)
* Added [proxy support] to [ws connection](https://github.com/webfolderio/cdp4j/commit/a448b63ed1fcd0c736ebce90d043eaa0bbe6ca56)
* Properly [disable](https://github.com/webfolderio/cdp4j/commit/50ca0ad4e42e319d0f20ff04e61267c1fc88827a) translate UI
* Added [missing try catch](https://github.com/webfolderio/cdp4j/commit/1d4fa4b2948dd9b88bb7996da920d3a091d14ba4) block to close process stream

### 3.0.8 - January 14, 2019

* Updated copyright date
* Added missing [getters](https://github.com/webfolderio/cdp4j/commit/d38444833d272500463cf3471885d45ebebb3b53)

### 3.0.7 - November 29, 2018

* Update websocket client library (nv-websocket-client) to 2.6
* Roll devtools protocol to r608591
* retry to get cdp4jId more than [once](https://github.com/webfolderio/cdp4j/commit/467a0a2ac9e47be8011f7eb189ee1cec2fbeaff6)
* Selector.matches() return false instead of throwing [exception](https://github.com/webfolderio/cdp4j/commit/147ced7a60c7e753414daac09265d39cf7dd87fa)

### 3.0.6 - August 15, 2018

* Added @JsFunction annotation

### 3.0.5 - July 31, 2018

* Fixed NullPointerException

### 3.0.4 - July 31, 2018

* Added Added a new Constructor to Launcher class
* Fixed ClassNotFoundException (org.jvnet.winp.WinProcess)

### 3.0.3 - July 26, 2018

* Added MacOsProcessManager
* Roll devtools protocol to r574367
* Added Dom.getClickablePoint() and Dom.scrollIntoViewIfNeeded() method
* Fixed Mouse.click() does not trigger click event if the element is not visible.

### 3.0.2 - July 9, 2018

* Fixed ElementNotFoundException for xpath expressions
* Fixed the wrong OS detection code and added human-readable error in case OS cannot be detected (ProcessManager)

### 3.0.1 - June 16, 2018

* Removed sizzle support
* Fixed concurrency bug (incognito mode)
* Added Session.navigateAndWait() method
* Added guard for Launcher.launch #95
* Removed NullProcessManager (AdaptiveProcessManager is the new default ProcessManager)
* Added Session.captureScreenshot(boolean hideScrollbar) method
