package org.daisycms.clientapp.adapter;

public class DaisyCredentials {

    private String login;
    private String password;
    
    public DaisyCredentials() {
    }
    
    public DaisyCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }
    
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getLogin() {
        return this.login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    
}
