package cn.edu.zju.isst.party.service;

import javax.servlet.http.HttpSession;

public class UserIdentityService<T> {
    private static String SESSION_KEY = "user-party";
    private HttpSession session;
    
    public UserIdentityService(HttpSession session) {
        this.session = session;
    }
    
    public void login(T user) {
        session.setAttribute(SESSION_KEY, user);
    }
    
    public boolean isLogged() {
        return session.getAttribute(SESSION_KEY) != null;
    }
    
    public void logout() {
        session.removeAttribute(SESSION_KEY);
    }
    
    @SuppressWarnings("unchecked")
    public T getIdentity() {
        return (T) session.getAttribute(SESSION_KEY);
    }
}
