package com.Laboratorio1.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Laboratorio1.Entities.Usuario;
import com.Laboratorio1.Entities.UsuarioPK;

@Repository("IUsuarioRepository")
public interface UsuarioRepository extends JpaRepository<Usuario, UsuarioPK>, CrudRepository<Usuario, UsuarioPK>{

    //Hay Métodos que JPA ya los tiene desarrollados, se pueden crear para tener
    //una manipulación más específica a la hora de usarlos en el service

    @Query("SELECT usr FROM UsrANDPer usr WHERE usr.id.login = :login AND usr.id.persona = :persona")
    public abstract Usuario getUsuarioANDPersona(@Param("login") String login, @Param("persona") int persona);

    //usr.id.login, usr.id.persona, usr.apikey, usr.password
    @Query("SELECT usr FROM UsrANDPer usr WHERE usr.id.login = :login")
    public abstract Usuario findByUsername(@Param("login") String login);
}
