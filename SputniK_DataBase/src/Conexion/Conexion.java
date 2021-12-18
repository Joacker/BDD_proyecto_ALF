/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

import Registro.Paciente;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Jaime, Fernando
 */
public class Conexion {
    private String DB = null;
    private String usuario =null;
    private String pw =null;
    private Connection conn= null;
    private Statement st = null;
    private ResultSet rs = null;
    private PreparedStatement ps;
    

    public Conexion() {
        
        try{
            DB = "jdbc:postgresql://database.c3ztydko2a0k.sa-east-1.rds.amazonaws.com:5432/sputnikdatabase";
            usuario ="sputnikdatabase";
            pw ="postgres";
            this.conn = DriverManager.getConnection(DB,usuario,pw);
            System.out.println("conectado");
        
        }catch(Exception e){
            
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error al conectar");
        }
    }
    public Paciente Busqueda(String nombre, String rut)
    {
        String result = null;
        Paciente pa = new Paciente();
        try
        {
            ps = this.conn.prepareStatement("select p.nombre,p.edad,p.rut,p.genero,p.nombre_estado,\n" +
                                            "p.patologia, p.tratamiento, p.estadopaciente from paciente as p\n" +
                                            "inner join atiende as a on a.rut_paciente = p.rut\n" +
                                            "inner join centro_de_salud as c on c.id = a.id_centro_de_salud\n" +
                                            "where c.nombre like ? and p.rut like ?;");
           
            ps.setString(1, nombre);
            ps.setString(2, rut);
            rs = ps.executeQuery(); 
            while(rs.next())
            {
                pa.setNombre_r(rs.getString("nombre"));
                pa.setEdad_r(rs.getInt("edad"));
                pa.setRut_r(rs.getString("rut"));
                pa.setSexo_r(rs.getString("genero"));
                pa.setEstadoPais_r(rs.getString("nombre_estado"));
                System.out.println(pa.getEstadoPais_r());
                pa.setPatologia_r(rs.getString("patologia"));
                pa.setTratam_r(rs.getString("tratamiento"));
                pa.setEstadoPaciente_r(rs.getString("estadopaciente"));
                
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return pa;
    }   
    
    public void insertarPaciente(String centro_s, String nombre, String rut,int edad, 
            String genero ,String estado_pais, String patologia, String tratamiento, String estado){
        try{
                LocalDate date = LocalDate.now();
                ps = this.conn.prepareStatement("call Insertar(?,?,?,?,?,?,?,?,?,?);");
                ps.setString(1, rut); 
                ps.setString(2, nombre);
                ps.setInt(3, edad);
                ps.setString(4,genero);
                ps.setString(5, estado_pais);
                ps.setString(6, patologia);
                ps.setString(7, tratamiento);
                ps.setString(8, estado);
                ps.setObject(9, date);
                ps.setString(10, centro_s);
                ps.executeUpdate();
           
        }catch(Exception e){
            e.printStackTrace();
        }  
        
    }
    
    public List<String []> listadoPacientePorRut(String rut){
        List<String []> salida = new ArrayList<String []>();
        try {
            
            ps = this.conn.prepareStatement("select id,rut, nombre, edad, genero, estado_pais,patologia, tratamiento, estadopaciente from persona as p\n" +
                                            "inner join paciente as pa on pa.rut_persona = p.rut\n" +
                                            "where p.rut = ? order by pa.id desc;");
                ps.setString(1, rut); 
                rs=ps.executeQuery();
                
                while(rs.next()){
                     String datos[] = {String.valueOf(rs.getInt(1)), rs.getString(2), rs.getString(3), String.valueOf(rs.getInt(4)), rs.getString(5), 
                         rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)};
                     salida.add(datos);
                }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return salida;
    }
    
    public Paciente BusquedaPorId(int id){
        Paciente pa = new Paciente();
        try{
             ps = this.conn.prepareStatement("select p.nombre, p.edad, p.rut, p.genero, p.estado_pais, pa.patologia, \n" +
                                                "pa.tratamiento, pa.estadopaciente from persona as p\n" +
                                                "inner join paciente as pa on p.rut = pa.rut_persona \n" +
                                                "where pa.id = ?;");
                ps.setInt(1, id); 
                
                rs=ps.executeQuery();
                if(rs.next()){
                    pa.setNombre_r(rs.getString(1));
                    pa.setEdad_r(rs.getInt(2));
                    pa.setRut_r(rs.getString(3));
                    pa.setSexo_r(rs.getString(4));
                    pa.setEstadoPais_r(rs.getString(5));
                    pa.setPatologia_r(rs.getString(6));
                    pa.setTratam_r(rs.getString(7));
                    pa.setEstadoPaciente_r(rs.getString(8));
                }else{
                     JOptionPane.showMessageDialog(null, "Id Invalido");     
                }
              
        }catch(Exception e){
            e.printStackTrace();
        }             
        return pa;
    }
    public void borrarPorId(int id){
        try{
                LocalDate date = LocalDate.now();
                ps = this.conn.prepareStatement("call Borrar(?);");
                ps.setInt(1, id); 
                ps.executeUpdate();
           
        }catch(Exception e){
            e.printStackTrace();
        }  
    }
    public List<String []> consultas(int index){
         List<String []> salida = new ArrayList<String []>();
         
        try{
            switch(index){
               case 1:
                       st= conn.createStatement();
                       rs = st.executeQuery("select e.nombre as Estado, count(rut) as pacientes\n" +
                                            "from paciente p\n" +
                                            "	inner join Persona per\n" +
                                            "		on p.rut_persona = per.rut\n" +
                                            "	inner join Estado e\n" +
                                            "		on per.estado_pais = e.nombre\n" +
                                            "    where p.tratamiento like '%sputnik1%' and estadopaciente like '%mutado%'\n" +
                                            "    group by e.nombre\n" +
                                            "    order by e.nombre;");
                    
                       
                       while(rs.next()){
                           String datos[] = {rs.getString(1), String.valueOf(rs.getInt(2))};
                           salida.add(datos);
                       }
                   break;
               case 2:
                    st= conn.createStatement();
                    rs = st.executeQuery("select (max(p.edad) - min(p.edad))/2 as rango \n" +
                                                "from persona p\n" +
                                                "	inner join Paciente pa\n" +
                                                "		on pa.rut_persona = p.rut\n" +
                                                "    where pa.estadopaciente like '%mutado%';");
                    
                       
                    while(rs.next()){
                        String datos[] = {String.valueOf(rs.getInt(1))};
                        salida.add(datos);
                    }
                   break;
               case 3:
                   st= conn.createStatement();
                       rs = st.executeQuery("select p.estado_pais, a.fecha,  count(p.rut)\n" +
                                                "from persona p\n" +
                                                "	inner join Paciente pa\n" +
                                                "		on pa.rut_persona = p.rut\n" +
                                                "    inner join atiende a\n" +
                                                "        on pa.id = a.id_paciente       \n" +
                                                "where pa.patologia like '%infectado%'\n" +
                                                "group by  p.estado_pais, a.fecha\n" +
                                                "order by a.fecha desc;");
                    
                       
                       while(rs.next()){
                           String datos[] = {rs.getString(1), String.valueOf(rs.getDate(2)),String.valueOf(rs.getInt(3))};
                           salida.add(datos);
                       }
                   break;
               case 4:
                   st= conn.createStatement();
                       rs = st.executeQuery("select e.nombre, mutados(e.nombre) as \"mutados es a\", recuperados(e.nombre) as \"recuperados\" from estado e;");
                    
                       
                       while(rs.next()){
                           String datos[] = {rs.getString(1), String.valueOf(rs.getInt(2))+" : "+String.valueOf(rs.getInt(3))};
                           salida.add(datos);
                       }
                   break;
               case 5:
                   st= conn.createStatement();
                       rs = st.executeQuery("select estado_pais as estado, count(rut) as \"cantidad de muertos\" from persona p\n" +
                                            "	inner join paciente pa\n" +
                                            "		on pa.rut_persona = p.rut \n" +
                                            "    where pa.estadopaciente like '%muerto%'\n" +
                                            "    group by p.estado_pais;");
                       while(rs.next()){
                           String datos[] = {rs.getString(1), String.valueOf(rs.getInt(2))};
                           salida.add(datos);
                       }
                   break;
               case 6:
                   st= conn.createStatement();
                       rs = st.executeQuery("select \n" +
                                            "(select count(genero) as \"Hombres es a\"   \n" +
                                            "    from persona p\n" +
                                            " 		inner join paciente pa\n" +
                                            " 			on p.rut = pa.rut_persona\n" +
                                            "    where pa.estadopaciente like '%mutado%' and p.genero like '%MASCULINO%'), \n" +
                                            "count(genero) as Mujeres  \n" +
                                            "from persona p\n" +
                                            " 		inner join paciente pa\n" +
                                            " 			on p.rut = pa.rut_persona\n" +
                                            "    where pa.estadopaciente like '%mutado%' and p.genero like '%FEMENINO%';");
                       while(rs.next()){
                           String datos[] = {String.valueOf(rs.getInt(1))+" : "+ String.valueOf(rs.getInt(2))};
                           salida.add(datos);
                       }
                   break;    
            }
        }catch(Exception e){
             e.printStackTrace();
        }
        
        return salida;
    }
    public String rutDeUnaId(int id){
        String salida=null;
        try{
            ps = this.conn.prepareStatement("select rut_persona from paciente where id=?;");
            ps.setInt(1,id);
            rs=ps.executeQuery();
            while(rs.next()){
                salida = rs.getString(1);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return salida;
    }
    public void actualizar(String nombre, int edad, String rut, String genero, String estado_pais, String patologia, String tratamiento, String estado_paciente,int id){
        try{
                String rutViejo=rutDeUnaId(id);
                ps = this.conn.prepareStatement("update Persona set\n" +
                                                "        rut = ?,\n" +
                                                "        nombre = ?,\n" +
                                                "        edad = ?,\n" +
                                                "        genero = ?,\n" +
                                                "        estado_pais = ?\n" +
                                                "where rut = ?;");
                ps.setString(1, rut);
                ps.setString(2, nombre);
                ps.setInt(3, edad);
                ps.setString(4, genero);
                ps.setString(5, estado_pais);
                ps.setString(6, rutViejo);
                 
                ps.executeUpdate();
           
        }catch(Exception e){
            e.printStackTrace();
        }  
         try{
                ps = this.conn.prepareStatement("update Paciente set\n" +
                                                "            patologia = ?,\n" +
                                                "            tratamiento = ?,\n" +
                                                "            estadopaciente = ?\n" +
                                                "        where id = ?;");
                
                ps.setString(1, patologia);
                ps.setString(2, tratamiento);
                ps.setString(3, estado_paciente);
                ps.setInt(4, id);
                 
                ps.executeUpdate();
           
        }catch(Exception e){
            e.printStackTrace();
        }  
        
        
    }
    
    
    
}
