package org.vaadin.artur.icepush;

public class JavascriptLocations {

    static final String ICEPUSH_JS_NAME = "code.icepush";

    private String code;
    private String createPushId;
    private String notify;
    private String addGroupMember;
    private String removeGroupMember;
    private String listen;

    public JavascriptLocations(String baseUrl) {
        code = baseUrl + "/" + ICEPUSH_JS_NAME;
        createPushId = baseUrl + "/create-push-id.icepush";
        notify = baseUrl + "/notify.icepush";
        addGroupMember = baseUrl + "/add-group-member.icepush";
        removeGroupMember = baseUrl + "/remove-group-member.icepush";
        listen = baseUrl + "/listen.icepush";
    }

    public JavascriptLocations(String codeURL, String createPushIdURL,
            String addGroupMemberURL, String removeGroupMemberURL,
            String listenURL, String notifyURL) {
        this.code = codeURL;
        this.createPushId = createPushIdURL;
        this.notify = notifyURL;
        this.addGroupMember = addGroupMemberURL;
        this.removeGroupMember = removeGroupMemberURL;
        this.listen = listenURL;
    }

    public String getCreatePushId() {
        return createPushId;
    }

    public String getNotify() {
        return notify;
    }

    public String getAddGroupMember() {
        return addGroupMember;
    }

    public String getRemoveGroupMember() {
        return removeGroupMember;
    }

    public String getListen() {
        return listen;
    }

    public String getCode() {
        return code;
    }

}