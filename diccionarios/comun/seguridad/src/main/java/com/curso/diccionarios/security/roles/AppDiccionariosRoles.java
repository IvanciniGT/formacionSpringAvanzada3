package com.curso.diccionarios.security.roles;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AppDiccionariosRoles {
    ROLE_ADMIN(AppDiccionariosRoles.ROLE_ADMIN_NAME),
    ROLE_EDITOR(AppDiccionariosRoles.ROLE_EDITOR_NAME);

    @Getter
    private final String nombre;

    @Override
    public String toString(){
        return getNombre();
    }

    public static String getNombre(AppDiccionariosRoles role){
        return role.getNombre();
    }

    public static final String ROLE_ADMIN_NAME = "Admin";
    public static final String ROLE_EDITOR_NAME = "Editor";

}
