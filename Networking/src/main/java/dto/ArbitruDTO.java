package dto;

import java.io.Serializable;

public class ArbitruDTO implements Serializable {
    private String id;
    private String passwd;

    public void setId(String id) {
        this.id = id;
    }

    public ArbitruDTO(String id, String passwd) {
        this.id = id;
        this.passwd = passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getId() {
        return id;
    }

    public String getPasswd() {
        return passwd;
    }
}
