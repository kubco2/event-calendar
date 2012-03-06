package cz.muni.fi.pv.projekt;

/**
 * Created by IntelliJ IDEA.
 * User: xjanco
 * Date: 28.2.2012
 * Time: 8:08:31
 * To change this template use File | Settings | File Templates.
 */
public class User  {

    private Long id;
    private String name;
    private String nick;
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
