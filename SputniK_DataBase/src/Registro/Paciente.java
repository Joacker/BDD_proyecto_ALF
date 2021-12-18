/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Registro;

/**
 *
 * @author Fernando
 */
public class Paciente 
{
    private String nombre_r;
    private int edad_r;
    private String rut_r;
    private String sexo_r;
    private String estadoPais_r;
    private String patologia_r;
    private String tratam_r;
    private String estadoPaciente_r;

    public Paciente() {
        nombre_r = null;
        edad_r = 0;
        rut_r = null;
        sexo_r = null;
        estadoPais_r = null;
        patologia_r = null;
        tratam_r = null;
        estadoPaciente_r = null;
    }
    public Paciente(String nombre_e, int edad_e, String rut_e, String sexo_e,
            String estadoPais_e, String patologia_e, String tratam_e, String estadoPaciente_e)
    {
        nombre_r = nombre_e;
        edad_r = edad_e;
        rut_r = rut_e;
        sexo_r = sexo_e;
        estadoPais_r = estadoPais_e;
        patologia_r = patologia_e;
        tratam_r = tratam_e;
        estadoPaciente_r = estadoPaciente_e;
    }

    public String getNombre_r() {
        return nombre_r;
    }

    public void setNombre_r(String nombre_r) {
        this.nombre_r = nombre_r;
    }

    public int getEdad_r() {
        return edad_r;
    }

    public void setEdad_r(int edad_r) {
        this.edad_r = edad_r;
    }

    public String getRut_r() {
        return rut_r;
    }

    public void setRut_r(String rut_r) {
        this.rut_r = rut_r;
    }

    public String getSexo_r() {
        return sexo_r;
    }

    public void setSexo_r(String sexo_r) {
        this.sexo_r = sexo_r;
    }

    public String getEstadoPais_r() {
        return estadoPais_r;
    }

    public void setEstadoPais_r(String estadoPais_r) {
        this.estadoPais_r = estadoPais_r;
    }

    public String getPatologia_r() {
        return patologia_r;
    }

    public void setPatologia_r(String patologia_r) {
        this.patologia_r = patologia_r;
    }

    public String getTratam_r() {
        return tratam_r;
    }

    public void setTratam_r(String tratam_r) {
        this.tratam_r = tratam_r;
    }

    public String getEstadoPaciente_r() {
        return estadoPaciente_r;
    }

    public void setEstadoPaciente_r(String estadoPaciente_r) {
        this.estadoPaciente_r = estadoPaciente_r;
    }
    
}
