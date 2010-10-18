package org.vaadin.artur.icepush;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.icepush.CodeServer;

public class JavascriptProvider {

    private String javaScript;
    private JavascriptLocations jsLocations;

    /**
     * Constructor for servlet based push where a base url can be used.
     * 
     * @param baseUrl
     * @throws IOException
     */
    public JavascriptProvider(String baseUrl) throws IOException {
        jsLocations = new JavascriptLocations(baseUrl);
        init();
    }

    public JavascriptProvider(String codeURL, String createPushIdURL,
            String addGroupMemberURL, String removeGroupMemberURL,
            String listenURL, String notifyURL) throws IOException {
        jsLocations = new JavascriptLocations(codeURL, createPushIdURL,
                addGroupMemberURL, removeGroupMemberURL, listenURL, notifyURL);
        init();
    }

    public String getJavaScript() {
        return javaScript;
    }

    private void init() throws IOException {
        InputStream code = CodeServer.class
                .getResourceAsStream("/icepush-modified.js");

        javaScript = read(code).toString();
        javaScript = javaScript.replace(
                "calculateURI('create-push-id.icepush')", quote(jsLocations
                        .getCreatePushId()));
        javaScript = javaScript.replace(
                "calculateURI('add-group-member.icepush')", quote(jsLocations
                        .getAddGroupMember()));
        javaScript = javaScript.replace("calculateURI('listen.icepush')",
                quote(jsLocations.getListen()));
        javaScript = javaScript.replace("calculateURI('notify.icepush')",
                quote(jsLocations.getNotify()));
        javaScript = javaScript.replace(
                "calculateURI('remove-group-member.icepush')",
                quote(jsLocations.getRemoveGroupMember()));
    }

    private static String quote(String string) {
        return "'" + string + "'";
    }

    private static StringBuilder read(InputStream input) throws IOException {
        StringBuilder sb = new StringBuilder(input.available());
        InputStreamReader isr = new InputStreamReader(input);

        char[] buf = new char[4096];
        int len = 0;
        while ((len = isr.read(buf)) > -1) {
            sb.append(buf, 0, len);
        }

        return sb;
    }

    public String getCodeLocation() {
        return jsLocations.getCode();
    }

    public String getCodeName() {
        return JavascriptLocations.ICEPUSH_JS_NAME;
    }

}
