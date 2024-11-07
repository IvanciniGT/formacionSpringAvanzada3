package com.curso.diccionarios.security.roles;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AppDiccionariosRoles {
    ROLE_ADMIN("Admin"),
    ROLE_EDITOR("Editor");

    @Getter
    private final String nombre;

    @Override
    public String toString(){
        return getNombre();
    }
}
