package com.Laboratorio1.Services.Interfaces;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.Laboratorio1.Entities.Usuario;
import com.Laboratorio1.Entities.UsuarioPK;

public interface IUsuarioService {

    //METODOS CRUD
    boolean guardar(Usuario usuario);

    boolean actualizar(Usuario usuario);

    boolean eliminar(UsuarioPK id);

    List<Usuario> consultarUsuario(Pageable pageable);

    Usuario getUsuarioById(UsuarioPK id);
}
