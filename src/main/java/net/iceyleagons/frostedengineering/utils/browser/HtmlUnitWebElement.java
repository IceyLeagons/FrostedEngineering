package net.iceyleagons.frostedengineering.utils.browser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlFileInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLInputElement;

import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

public class HtmlUnitWebElement {
    protected final Browser parent;
    protected final int id;
    protected final DomElement element;
    private static final String[] booleanAttributes = {"async", "autofocus", "autoplay", "checked", "compact",
            "complete", "controls", "declare", "defaultchecked", "defaultselected", "defer", "disabled", "draggable",
            "ended", "formnovalidate", "hidden", "indeterminate", "iscontenteditable", "ismap", "itemscope", "loop",
            "multiple", "muted", "nohref", "noresize", "noshade", "novalidate", "nowrap", "open", "paused", "pubdate",
            "readonly", "required", "reversed", "scoped", "seamless", "seeking", "selected", "spellcheck", "truespeed",
            "willvalidate"};

    public HtmlUnitWebElement(Browser parent, int id, DomElement element) {
        this.parent = parent;
        this.id = id;
        this.element = element;
    }

    public void submit() {
        parent.submit(this);
    }

    public void sendKeys(CharSequence... charSequences) {
        parent.sendKeys(this, charSequences);
    }

    void submitImpl() {
        try {
            if (element instanceof HtmlForm) {
                submitForm((HtmlForm) element);
            } else if ((element instanceof HtmlSubmitInput) || (element instanceof HtmlImageInput)) {
                element.click();
            } else if (element instanceof HtmlInput) {
                HtmlForm form = ((HtmlElement) element).getEnclosingForm();
                if (form == null) {
                    throw new NoSuchElementException("Unable to find the containing form");
                }
                submitForm(form);
            } else {
                HtmlUnitWebElement form = findParentForm();
                if (form == null) {
                    throw new NoSuchElementException("Unable to find the containing form");
                }
                form.submitImpl();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HtmlUnitWebElement findParentForm() {
        DomNode current = element;
        while (!(current == null || current instanceof HtmlForm)) {
            current = current.getParentNode();
        }
        return parent.toWebElement((HtmlForm) current);
    }

    private static boolean isSubmitElement(HtmlElement element) {
        HtmlElement candidate = null;

        if (element instanceof HtmlSubmitInput && !((HtmlSubmitInput) element).isDisabled()) {
            candidate = element;
        } else if (element instanceof HtmlImageInput && !((HtmlImageInput) element).isDisabled()) {
            candidate = element;
        } else if (element instanceof HtmlButton) {
            HtmlButton button = (HtmlButton) element;
            if ("submit".equalsIgnoreCase(button.getTypeAttribute()) && !button.isDisabled()) {
                candidate = element;
            }
        }

        return candidate != null;
    }

    private void submitForm(HtmlForm form) {
        List<HtmlElement> allElements = new ArrayList<>();
        allElements.addAll(form.getElementsByTagName("input"));
        allElements.addAll(form.getElementsByTagName("button"));

        HtmlElement submit = null;
        for (HtmlElement e : allElements) {
            if (!isSubmitElement(e)) {
                continue;
            }

            if (submit == null) {
                submit = e;
            }
        }

        if (submit == null) {
            ScriptResult eventResult = form.fireEvent("submit");
            if (!ScriptResult.isFalse(eventResult)) {
                parent.executeScript("arguments[0].submit()", form);
            }
            return;

        }
        try {
            submit.click(false, false, false, true, true, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAttribute(String name) {
        final String lowerName = name.toLowerCase();

        String value = element.getAttribute(name);

        if (element instanceof HtmlInput && ("selected".equals(lowerName) || "checked".equals(lowerName))) {
            return trueOrNull(((HtmlInput) element).isChecked());
        }

        if ("href".equals(lowerName) || "src".equals(lowerName)) {
            String link = element.getAttribute(name);
            if (DomElement.ATTRIBUTE_NOT_DEFINED == link) {
                return null;
            }
            HtmlPage page = (HtmlPage) element.getPage();
            try {
                return page.getFullyQualifiedUrl(link.trim()).toString();
            } catch (MalformedURLException e) {
                return null;
            }
        }
        if ("disabled".equals(lowerName)) {
            return trueOrNull(!isEnabled());
        }

        if ("multiple".equals(lowerName) && element instanceof HtmlSelect) {
            String multipleAttribute = ((HtmlSelect) element).getMultipleAttribute();
            if ("".equals(multipleAttribute)) {
                return trueOrNull(element.hasAttribute("multiple"));
            }
            return "true";
        }

        for (String booleanAttribute : booleanAttributes) {
            if (booleanAttribute.equals(lowerName)) {
                return trueOrNull(element.hasAttribute(lowerName));
            }
        }
        if ("index".equals(lowerName) && element instanceof HtmlOption) {
            HtmlSelect select = ((HtmlOption) element).getEnclosingSelect();
            List<HtmlOption> allOptions = select.getOptions();
            for (int i = 0; i < allOptions.size(); i++) {
                HtmlOption option = select.getOption(i);
                if (element.equals(option)) {
                    return String.valueOf(i);
                }
            }

            return null;
        }

        if ("value".equals(lowerName)) {
            if (element instanceof HtmlFileInput) {
                return ((HTMLInputElement) element.getScriptableObject()).getValue();
            }
            if (element instanceof HtmlTextArea) {
                return ((HtmlTextArea) element).getText();
            }

            if (element instanceof HtmlOption && !element.hasAttribute("value")) {
                return element.getTextContent();
            }

            return value == null ? "" : value;
        }

        if (!value.isEmpty()) {
            return value;
        }

        if (element.hasAttribute(name)) {
            return "";
        }

        final Object scriptable = element.getScriptableObject();
        if (scriptable instanceof Scriptable) {
            final Object slotVal = ScriptableObject.getProperty((Scriptable) scriptable, name);
            if (slotVal instanceof String) {
                return (String) slotVal;
            }
        }

        return null;
    }

    public boolean isEnabled() {
        return !element.hasAttribute("disabled");
    }

    private static String trueOrNull(boolean condition) {
        return condition ? "true" : null;
    }
}
