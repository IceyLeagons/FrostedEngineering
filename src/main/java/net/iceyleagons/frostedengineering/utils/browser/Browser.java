/*
 *  Copyright (C) IceyLeagons(https://iceyleagons.net/)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.iceyleagons.frostedengineering.utils.browser;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.net.ssl.SSLHandshakeException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowEvent;
import com.gargoylesoftware.htmlunit.WebWindowListener;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlFileInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.Keyboard;
import com.gargoylesoftware.htmlunit.html.parser.HTMLParserListener;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Location;
import com.gargoylesoftware.htmlunit.javascript.host.html.DocumentProxy;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.google.common.collect.Maps;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.IdScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.NativeObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * From Selenium in order to save space
 */
public class Browser {

    private Lock conditionLock = new ReentrantLock();
    private Condition mainCondition = conditionLock.newCondition();
    private WebClient client;
    private WebWindow currentWindow;
    private boolean runAsyncRunning;
    private RuntimeException exception;
    private final ExecutorService defaultExecutor;
    private Executor executor;
    private Page lastPage;

    private Map<SgmlPage, Map<DomElement, HtmlUnitWebElement>> elementsMap = new WeakHashMap<>();
    @SuppressWarnings("unused")
    private boolean gotPage = false;

    public Browser() {
        this.defaultExecutor = Executors.newCachedThreadPool();
        this.executor = this.defaultExecutor;
        createWebClient(BrowserVersion.CHROME);
    }

    private void createWebClient(BrowserVersion version) {
        WebClient client = new WebClient(version);

        final WebClientOptions options = client.getOptions();
        options.setHomePage(WebClient.URL_ABOUT_BLANK.toString());
        options.setThrowExceptionOnFailingStatusCode(false);
        options.setPrintContentOnFailingStatusCode(false);
        options.setJavaScriptEnabled(true);
        options.setRedirectEnabled(true);
        options.setUseInsecureSSL(true);
        options.setDownloadImages(false);
        options.setCssEnabled(false);
        client.setRefreshHandler(new ThreadedRefreshHandler());

        client.setIncorrectnessListener(new IncorrectnessListener() {

            @Override
            public void notify(String arg0, Object arg1) {
                // Ignored.
            }
        });
        client.setJavaScriptErrorListener(new JavaScriptErrorListener() {

            @Override
            public void timeoutError(HtmlPage arg0, long arg1, long arg2) {
                // Ignored.
            }

            @Override
            public void scriptException(HtmlPage arg0, ScriptException arg1) {
                // Ignored.
            }

            @Override
            public void malformedScriptURL(HtmlPage arg0, String arg1, MalformedURLException arg2) {
                // Ignored.
            }

            @Override
            public void loadScriptError(HtmlPage arg0, URL arg1, Exception arg2) {
                // Ignored.
            }

            @Override
            public void warn(String arg0, String arg1, int arg2, String arg3, int arg4) {
                // Ignored.
            }
        });
        client.setHTMLParserListener(new HTMLParserListener() {

            @Override
            public void error(String arg0, URL arg1, String arg2, int arg3, int arg4, String arg5) {
                // Ignored.
            }

            @Override
            public void warning(String arg0, URL arg1, String arg2, int arg3, int arg4, String arg5) {
                // Ignored.
            }
        });

        client.addWebWindowListener(new WebWindowListener() {
            @Override
            public void webWindowContentChanged(WebWindowEvent event) {
                elementsMap.remove(event.getOldPage());
                if (event.getWebWindow() != currentWindow) {
                    return;
                }

                switchToDefaultContentOfWindow(currentWindow);
            }

            @Override
            public void webWindowClosed(WebWindowEvent event) {
                elementsMap.remove(event.getOldPage());
                WebWindow curr = currentWindow;
                do {
                    if (curr == event.getWebWindow()) {
                        currentWindow = currentWindow.getTopWindow();
                        return;
                    }
                    curr = curr.getParentWindow();
                } while (curr != currentWindow.getTopWindow());
            }

            @Override
            public void webWindowOpened(WebWindowEvent arg0) {
            }
        });

        this.client = client;

        get(client.getOptions().getHomePage());
        gotPage = false;
    }

    public void close() {
        client.close();
        client = null;

        defaultExecutor.shutdown();

        currentWindow = null;
    }

    public void get(String url) {
        if (WebClient.URL_ABOUT_BLANK.toString().equals(url)) {
            get(WebClient.URL_ABOUT_BLANK);
            return;
        }

        runAsync(() -> {
            try {
                get(new URL(url));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
    }

    private void get(URL url) {
        if (currentWindow == null)
            currentWindow = client.getCurrentWindow();

        try {
            final BrowserVersion browser = client.getBrowserVersion();
            final WebRequest request = new WebRequest(url, browser.getHtmlAcceptHeader(),
                    browser.getAcceptEncodingHeader());
            request.setCharset(StandardCharsets.UTF_8);
            lastPage = client.getPage(currentWindow.getTopWindow(), request);

            currentWindow = currentWindow.getTopWindow();
        } catch (UnknownHostException e) {
            currentWindow.getTopWindow().setEnclosedPage(
                    new UnexpectedPage(new StringWebResponse("Unknown host", url), currentWindow.getTopWindow()));
        } catch (SSLHandshakeException e) {
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        gotPage = true;
    }

    void runAsync(Runnable r) {
        while (runAsyncRunning) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        conditionLock.lock();
        runAsyncRunning = true;

        exception = null;
        Runnable wrapped = () -> {
            try {
                r.run();
            } catch (RuntimeException e) {
                exception = e;
            } finally {
                conditionLock.lock();
                runAsyncRunning = false;
                mainCondition.signal();
                conditionLock.unlock();
            }
        };
        executor.execute(wrapped);

        if (this.runAsyncRunning) {
            mainCondition.awaitUninterruptibly();
            conditionLock.unlock();
        }

        if (exception != null) {
            throw exception;
        }
    }

    private int elementsCounter = 0;

    protected HtmlUnitWebElement toWebElement(DomElement element) {
        Map<DomElement, HtmlUnitWebElement> pageMap = elementsMap.get(element.getPage());
        if (pageMap == null) {
            pageMap = new HashMap<>();
            elementsMap.put(element.getPage(), pageMap);
        }

        HtmlUnitWebElement e = pageMap.get(element);
        if (e == null) {
            e = new HtmlUnitWebElement(this, ++elementsCounter, element);
            pageMap.put(element, e);
        }
        return e;
    }

    public HtmlUnitWebElement getElementById(int id) {
        for (Map<DomElement, HtmlUnitWebElement> pageMap : elementsMap.values()) {
            for (HtmlUnitWebElement e : pageMap.values()) {
                if (e.id == id) {
                    return e;
                }
            }
        }
        return null;
    }

    void sendKeys(HtmlUnitWebElement element, CharSequence... value) {
        runAsync(() -> sendKeys(element, true, value[0]));
    }

    void submit(HtmlUnitWebElement element) {
        runAsync(() -> element.submitImpl());
    }

    public List<HtmlUnitWebElement> findElementsByName(String name) {
        if (!(lastPage instanceof HtmlPage)) {
            throw new IllegalStateException("Unable to locate element by name for " + lastPage);
        }

        List<DomElement> allElements = ((HtmlPage) lastPage).getElementsByName(name);
        List<HtmlUnitWebElement> elements = new ArrayList<>();

        allElements.forEach((element) -> {
            elements.add(toWebElement(element));
        });

        return elements;
    }

    public HtmlUnitWebElement findElementByName(String name) {
        return findElementsByName(name).get(0);
    }

    void sendKeys(HtmlUnitWebElement htmlElem, boolean releaseAllAtEnd, CharSequence value) {

        final HtmlElement element = (HtmlElement) htmlElem.element;
        final boolean inputElementInsideForm = element instanceof HtmlInput
                && ((HtmlInput) element).getEnclosingForm() != null;

        if (element instanceof HtmlFileInput) {
            HtmlFileInput fileInput = (HtmlFileInput) element;
            fileInput.setValueAttribute(value.toString());
            return;
        }

        Keyboard keyboard = asHtmlUnitKeyboard(false, value, true);
        try {
            element.type(keyboard);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (inputElementInsideForm) {
            htmlElem.submitImpl();
        }
    }

    private Object[] convertScriptArgs(HtmlPage page, final Object[] args) {
        final Object scope = page.getEnclosingWindow().getScriptableObject();

        if (!(scope instanceof Scriptable)) {
            return args;
        }

        final Object[] parameters = new Object[args.length];
        Context.enter();
        try {
            for (int i = 0; i < args.length; i++) {
                parameters[i] = parseArgumentIntoJavascriptParameter((Scriptable) scope, args[i]);
            }
        } finally {
            Context.exit();
        }
        return parameters;
    }

    private Object parseArgumentIntoJavascriptParameter(Scriptable scope, Object arg) {

        if (!(arg instanceof HtmlUnitWebElement || arg instanceof HtmlElement || // special case the underlying type
                arg instanceof Number || arg instanceof String || arg instanceof Boolean || arg.getClass().isArray()
                || arg instanceof Collection<?> || arg instanceof Map<?, ?>)) {
            throw new IllegalArgumentException(
                    "Argument must be a string, number, boolean or WebElement: " + arg + " (" + arg.getClass() + ")");
        }

        if (arg instanceof HtmlUnitWebElement) {
            HtmlUnitWebElement webElement = (HtmlUnitWebElement) arg;
            return webElement.element.getScriptableObject();

        } else if (arg instanceof HtmlElement) {
            HtmlElement element = (HtmlElement) arg;
            return element.getScriptableObject();

        } else if (arg instanceof Collection<?>) {
            List<Object> list = new ArrayList<>();
            for (Object o : (Collection<?>) arg) {
                list.add(parseArgumentIntoJavascriptParameter(scope, o));
            }
            return Context.getCurrentContext().newArray(scope, list.toArray());

        } else if (arg.getClass().isArray()) {
            List<Object> list = new ArrayList<>();
            for (Object o : (Object[]) arg) {
                list.add(parseArgumentIntoJavascriptParameter(scope, o));
            }
            return Context.getCurrentContext().newArray(scope, list.toArray());

        } else if (arg instanceof Map<?, ?>) {
            Map<?, ?> argmap = (Map<?, ?>) arg;
            Scriptable map = Context.getCurrentContext().newObject(scope);
            for (Object key : argmap.keySet()) {
                map.put((String) key, map, parseArgumentIntoJavascriptParameter(scope, argmap.get(key)));
            }
            return map;

        } else {
            return arg;
        }
    }

    public Object executeScript(String script, final Object... args) {
        HtmlPage page = (HtmlPage) lastPage;

        script = "function() {" + script + "\n};";
        ScriptResult result = page.executeJavaScript(script);
        Object function = result.getJavaScriptResult();

        Object[] parameters = convertScriptArgs(page, args);

        try {
            result = page.executeJavaScriptFunction(function, currentWindow.getScriptableObject(), parameters,
                    page.getDocumentElement());

            return parseNativeJavascriptResult(result);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        return null;
    }

    protected interface JavaScriptResultsCollection {
        int getLength();

        Object item(int index);
    }

    private Object parseNativeJavascriptResult(Object result) {
        Object value;
        if (result instanceof ScriptResult) {
            value = ((ScriptResult) result).getJavaScriptResult();
        } else {
            value = result;
        }
        if (value instanceof HTMLElement) {
            return toWebElement(((HTMLElement) value).getDomNodeOrDie());
        }

        if (value instanceof DocumentProxy) {
            Element element = ((DocumentProxy) value).getDelegee().getDocumentElement();
            if (element instanceof HTMLElement) {
                return toWebElement(((HTMLElement) element).getDomNodeOrDie());
            }
        }

        if (value instanceof Number) {
            final Number n = (Number) value;
            final String s = n.toString();
            if (!s.contains(".") || s.endsWith(".0")) { // how safe it is? enough for the unit tests!
                return n.longValue();
            }
            return n.doubleValue();
        }

        if (value instanceof NativeObject) {
            @SuppressWarnings("unchecked") final Map<String, Object> map = Maps.newHashMap(((NativeObject) value));
            for (final Entry<String, Object> e : map.entrySet()) {
                e.setValue(parseNativeJavascriptResult(e.getValue()));
            }
            return map;
        }

        if (value instanceof Location) {
            return convertLocationToMap((Location) value);
        }

        if (value instanceof NativeArray) {
            final NativeArray array = (NativeArray) value;

            JavaScriptResultsCollection collection = new JavaScriptResultsCollection() {
                @Override
                public int getLength() {
                    return (int) array.getLength();
                }

                @Override
                public Object item(int index) {
                    return array.get(index);
                }
            };

            return parseJavascriptResultsList(collection);
        }

        if (value instanceof HTMLCollection) {
            final HTMLCollection array = (HTMLCollection) value;

            JavaScriptResultsCollection collection = new JavaScriptResultsCollection() {
                @Override
                public int getLength() {
                    return array.getLength();
                }

                @Override
                public Object item(int index) {
                    return array.get(index);
                }
            };

            return parseJavascriptResultsList(collection);
        }

        if (value instanceof IdScriptableObject && value.getClass().getSimpleName().equals("NativeDate")) {
            long l = ((Number) getPrivateField(value, "date")).longValue();
            return Instant.ofEpochMilli(l).toString();
        }

        if (value instanceof Undefined) {
            return null;
        }

        return value;
    }

    private static Object getPrivateField(Object o, String fieldName) {
        try {
            final Field field = o.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, Object> convertLocationToMap(Location location) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("href", location.getHref());
        map.put("protocol", location.getProtocol());
        map.put("host", location.getHost());
        map.put("hostname", location.getHostname());
        map.put("port", location.getPort());
        map.put("pathname", location.getPathname());
        map.put("search", location.getSearch());
        map.put("hash", location.getHash());
        map.put("href", location.getHref());
        return map;
    }

    private List<Object> parseJavascriptResultsList(JavaScriptResultsCollection array) {
        List<Object> list = new ArrayList<>(array.getLength());
        for (int i = 0; i < array.getLength(); ++i) {
            list.add(parseNativeJavascriptResult(array.item(i)));
        }
        return list;
    }

    private Keyboard asHtmlUnitKeyboard(final boolean startAtEnd, final CharSequence keysSequence,
                                        final boolean isPress) {
        Keyboard keyboard = new Keyboard(startAtEnd);
        for (int i = 0; i < keysSequence.length(); i++) {
            char ch = keysSequence.charAt(i);
            addToKeyboard(keyboard, ch, isPress);
        }
        return keyboard;
    }

    private void addToKeyboard(final Keyboard keyboard, char ch, final boolean isPress) {
        keyboard.type(ch);
    }

    private void switchToDefaultContentOfWindow(WebWindow window) {
        Page page = window.getEnclosedPage();
        if (page instanceof HtmlPage) {
            currentWindow = window;
        }
    }

}
