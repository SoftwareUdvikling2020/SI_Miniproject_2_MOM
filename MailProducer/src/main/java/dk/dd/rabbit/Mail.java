package dk.dd.rabbit;

public class Mail {
    String[] recipients;
    String body;
    String subject;

    public Mail(String[] recipients, String body, String subject) {
        this.recipients = recipients;
        this.body = body;
        this.subject = subject;
    }

    public String[] getRecipients() {
        return recipients;
    }

    public void setRecipients(String[] recipients) {
        this.recipients = recipients;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}