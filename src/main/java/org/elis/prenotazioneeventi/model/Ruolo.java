package org.elis.prenotazioneeventi.model;

public enum Ruolo {
/*    CLIENTE("cliente"),
    VENDITORE("venditore"),
    ADMIN("admin"),
    SUPERADMIN("superadmin");
    private String ruolo;

    Ruolo(String ruolo) {
        this.ruolo = ruolo;
    }

    @Override
    public String toString() {
        return ruolo;
    }

    public static Ruolo getRuoloByNome(String nome){
        return switch (nome.toLowerCase()){
            case "cliente"->CLIENTE;
            case "venditore"->VENDITORE;
            case "admin"->ADMIN;
            case "superadmin"->SUPERADMIN;
            default -> null;
        };
    }


 */

    CLIENTE,
    VENDITORE,
    ADMIN,
    SUPERADMIN;
}
