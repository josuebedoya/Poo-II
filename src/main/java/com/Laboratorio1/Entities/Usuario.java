package com.Laboratorio1.Entities;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity(name = "UsrANDPer")
@Table(schema = "LABPOOII", name = "usuario")
public class Usuario implements Serializable{
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private UsuarioPK id;
    @Column(name = "password")
    private String password;
    @Column(name = "apikey")
    private String apikey;

    public Usuario() {}
    public Usuario(String login, String password) {
        this.id.setLogin(login);
        this.password = password;
    }

    public UsuarioPK getId() {
        return id;
    }
    public void setId(UsuarioPK id) {
        this.id = id;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getApikey() {
        return apikey;
    }
    public void setApikey(String apikey) {
        this.apikey = apikey;
    }
}
