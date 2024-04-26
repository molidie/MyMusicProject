package ar.edu.unnoba.pdyc.mymusic.dto;


public class PlaylistDTO {

    private Long id;
    private String name;
    private int cantidadDeCanciones;

    public PlaylistDTO() {
    }

    public PlaylistDTO(int cantidadDeCanciones, String name, Long id) {
        this.cantidadDeCanciones = cantidadDeCanciones;
        this.name = name;
        this.id = id;
    }

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

    public int getCantidadDeCanciones() {
        return cantidadDeCanciones;
    }

    public void setCantidadDeCanciones(int cantidadDeCanciones) {
        this.cantidadDeCanciones = cantidadDeCanciones;
    }

}
